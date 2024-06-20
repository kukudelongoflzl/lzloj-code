package com.yupi.lzloj.judge;

import cn.hutool.json.JSONUtil;
import com.yupi.lzloj.common.ErrorCode;
import com.yupi.lzloj.exception.BusinessException;
import com.yupi.lzloj.judge.codesandbox.CodeSandBox;
import com.yupi.lzloj.judge.codesandbox.CodeSandBoxFactory;
import com.yupi.lzloj.judge.codesandbox.CodeSandBoxProxy;
import com.yupi.lzloj.judge.codesandbox.model.ExecuteCodeRequest;
import com.yupi.lzloj.judge.codesandbox.model.ExecuteCodeResponse;
import com.yupi.lzloj.judge.codesandbox.model.JudgeInfo;
import com.yupi.lzloj.judge.strategy.JudgeContext;
import com.yupi.lzloj.judge.strategy.JudgeManager;
import com.yupi.lzloj.model.dto.question.JudgeCase;
import com.yupi.lzloj.model.entity.Question;
import com.yupi.lzloj.model.entity.QuestionSubmit;
import com.yupi.lzloj.model.enums.JudgeInfoMessageEnum;
import com.yupi.lzloj.model.enums.QuestionSubmitStatusEnum;
import com.yupi.lzloj.service.QuestionService;
import com.yupi.lzloj.service.QuestionSubmitService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 判题服务实现类
 */
@Service
@Slf4j
public class JudgeServiceImpl implements JudgeService {


    @Resource
    private QuestionSubmitService questionSubmitService;

    @Resource
    private QuestionService questionService;

    @Value("${CodeSandBox.type:example}")
    private String type;

    @Override
    public QuestionSubmit doJudge(Long questionSubmitId) {
        //1参数验证 验证提交 验证题目 验证判题状态
        QuestionSubmit questionSubmit = questionSubmitService.getById(questionSubmitId);
        if (questionSubmit == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "提交信息不存在");
        }
        Integer status = questionSubmit.getStatus();
        Long questionId = questionSubmit.getQuestionId();
        Question question = questionService.getById(questionId);
        String language = questionSubmit.getLanguage();
        String code = questionSubmit.getCode();
        if (question == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "题目不存在");
        }
        if (!status.equals(QuestionSubmitStatusEnum.WAITING.getValue())) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "题目已判题");
        }
        //2更新判题状态
        QuestionSubmit questionSubmitUpdate = new QuestionSubmit();
        questionSubmitUpdate.setId(questionSubmitId);
        questionSubmitUpdate.setStatus(QuestionSubmitStatusEnum.RUNNING.getValue());//判题中
        boolean update = questionSubmitService.updateById(questionSubmitUpdate);
        if (!update) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "题目状态更新错误");
        }
        //3构建代码沙箱 工厂模式创建 代理模式增强功能
        CodeSandBox codeSandBox = CodeSandBoxFactory.newInstance(type);
        CodeSandBoxProxy codeSandBoxProxy = new CodeSandBoxProxy(codeSandBox);
        //4构建判题信息
        List<JudgeCase> judgeCaseList = JSONUtil.toList(question.getJudgeCase(), JudgeCase.class);
        List<String> inputList = judgeCaseList.stream()
                .map(JudgeCase::getInput).collect(Collectors.toList());
        //封装判题信息
        ExecuteCodeRequest executeCodeRequest = ExecuteCodeRequest.builder()
                .code(code)
                .language(language)
                .inputList(inputList)
                .build();
        //判题
        ExecuteCodeResponse executeCodeResponse = codeSandBoxProxy.executeCode(executeCodeRequest);
        if (executeCodeResponse == null) {
            //如果返回的响应信息是空的则代表判题失败
            questionSubmitUpdate.setId(questionSubmitId);
            questionSubmitUpdate.setStatus(QuestionSubmitStatusEnum.FAILED.getValue());
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "判题相应错误");
        }
        //封装判题结果判断策略信息
        JudgeContext judgeContext = new JudgeContext();
        judgeContext.setJudgeInfo(executeCodeResponse.getJudgeInfo());//判题沙箱返回的信息
        judgeContext.setInputList(inputList);//判题输入
        judgeContext.setOutputList(executeCodeResponse.getOutputList());//判题输出。来自沙箱
        judgeContext.setJudgeCaseList(judgeCaseList);//判题用例，来自问题信息之后转换
        judgeContext.setQuestion(question);//问题信息
        judgeContext.setQuestionSubmit(questionSubmit);//问题提交信息
        //验证信息获得返回
        JudgeInfo judgeInfo = new JudgeManager().doJudge(judgeContext);
        questionSubmitUpdate.setId(questionSubmitId);
        questionSubmitUpdate.setJudgeInfo(JSONUtil.toJsonStr(judgeInfo));
        questionSubmitUpdate.setStatus(QuestionSubmitStatusEnum.SUCCEED.getValue());
        update = questionSubmitService.updateById(questionSubmitUpdate);
        if (!update) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "题目提交状态更新错误");
        }
        QuestionSubmit questionSubmitResult = questionSubmitService.getById(questionSubmitId);
        //提交数通过数的增加
        JudgeInfo submitJudgeInfo = JSONUtil.toBean(questionSubmitResult.getJudgeInfo(), judgeInfo.getClass());
        question.setSubNum(question.getSubNum() + 1);
        if (submitJudgeInfo.getMessage().equals(JudgeInfoMessageEnum.ACCEPTED.getValue()))
            question.setAcceptedNum(question.getAcceptedNum() + 1);
        update = questionService.updateById(question);
        if (!update) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "题目状态更新错误");
        }
        return questionSubmitResult;
    }
}
