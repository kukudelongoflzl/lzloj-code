package com.yupi.lzloj.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yupi.lzloj.common.ErrorCode;
import com.yupi.lzloj.constant.CommonConstant;
import com.yupi.lzloj.exception.BusinessException;
import com.yupi.lzloj.judge.JudgeService;
import com.yupi.lzloj.model.dto.questionsubmit.QuestionSubmitAddRequest;
import com.yupi.lzloj.model.dto.questionsubmit.QuestionSubmitQueryRequest;
import com.yupi.lzloj.model.entity.Question;
import com.yupi.lzloj.model.entity.QuestionSubmit;
import com.yupi.lzloj.model.entity.User;
import com.yupi.lzloj.model.enums.QuestionSubmitStatusEnum;
import com.yupi.lzloj.model.vo.QuestionSubmitVO;
import com.yupi.lzloj.model.vo.UserVO;
import com.yupi.lzloj.service.QuestionService;
import com.yupi.lzloj.service.QuestionSubmitService;
import com.yupi.lzloj.mapper.QuestionSubmitMapper;
import com.yupi.lzloj.service.UserService;
import com.yupi.lzloj.utils.SqlUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

/**
 * @author 酷酷的龙
 * @description 针对表【question_submit(题目提交)】的数据库操作Service实现
 * @createDate 2024-06-06 19:42:31
 */
@Service
public class QuestionSubmitServiceImpl extends ServiceImpl<QuestionSubmitMapper, QuestionSubmit>
        implements QuestionSubmitService {

    @Resource
    private QuestionService questionService;

    @Resource
    private UserService userService;

    @Lazy //有相互调用所有要用lazy注解
    @Resource
    private JudgeService judgeService;

    /*@Resource
    @Lazy //由于有相互调用所以要加上Lazy注解
    private JudgeServiceImpl judgeService;*/

    /**
     * 提交
     *
     * @param questionSubmitAddRequest
     * @param loginUser
     * @return
     */
    @Override
    public Long doQuestionSubmit(QuestionSubmitAddRequest questionSubmitAddRequest, User loginUser) {
        //获取题目提交信息
        String language = questionSubmitAddRequest.getLanguage();
        String code = questionSubmitAddRequest.getCode();
        Long questionId = questionSubmitAddRequest.getQuestionId();
        long userId = loginUser.getId();
        // 判断实体是否存在，根据类别获取实体
        Question question = questionService.getById(questionId);
        if (question == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        // 封装题目提交题目信息

        QuestionSubmit questionSubmit = new QuestionSubmit();
        questionSubmit.setLanguage(language);
        questionSubmit.setCode(code);
        questionSubmit.setJudgeInfo("{}");
        questionSubmit.setStatus(QuestionSubmitStatusEnum.WAITING.getValue());
        questionSubmit.setQuestionId(questionId);
        questionSubmit.setUserId(userId);
        boolean save = this.save(questionSubmit);
        if (!save) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "数据插入失败");
        }
        //拿到本次提交本次id
        Long questionSubmitId = questionSubmit.getId();
        //提交之后肯定提交判题请求，否则永远也不会判题 异步判题
        CompletableFuture.runAsync(() -> {
            judgeService.doJudge(questionSubmitId);
        });
        return questionSubmitId;
    }

    /**
     * 获取查询包装类
     *
     * @param questionSubmitQueryRequest
     * @return
     */
    @Override
    public QueryWrapper<QuestionSubmit> getQueryWrapper(QuestionSubmitQueryRequest questionSubmitQueryRequest) {
        QueryWrapper<QuestionSubmit> queryWrapper = new QueryWrapper<>();
        if (questionSubmitQueryRequest == null) {
            return queryWrapper;
        }
        //只保留题号和编程语言两个查询条件
        String language = questionSubmitQueryRequest.getLanguage();
        Long questionId = questionSubmitQueryRequest.getQuestionId();
        String sortField = questionSubmitQueryRequest.getSortField();
        String sortOrder = questionSubmitQueryRequest.getSortOrder();
        // 拼接查询条件
        queryWrapper.like(StringUtils.isNotBlank(language), "language", language);
        queryWrapper.eq(ObjectUtils.isNotEmpty(questionId), "questionId", questionId);
        queryWrapper.orderBy(SqlUtils.validSortField(sortField), sortOrder.equals(CommonConstant.SORT_ORDER_ASC),
                sortField); //分别是验证排序参数是否有效，检查排序顺序是降序还是升序，对字段进行排序
        return queryWrapper; //返回的是一个封装好的查询条件
    }

    /**
     * 分页查询
     *
     * @param questionSubmitPage
     * @param request
     * @return
     */
    @Override
    public Page<QuestionSubmitVO> getQuestionSubmitVOPage(Page<QuestionSubmit> questionSubmitPage, HttpServletRequest request) {
        List<QuestionSubmit> questionSubmitList = questionSubmitPage.getRecords(); //分页查询后封装的所有条信息列表
        //三个参数 当前页 每页条数 总数
        Page<QuestionSubmitVO> questionSubmitVOPage = new Page<>(questionSubmitPage.getCurrent(), questionSubmitPage.getSize(), questionSubmitPage.getTotal());
        if (CollUtil.isEmpty(questionSubmitList)) {
            return questionSubmitVOPage;
        }
        // 填充信息 主要要把Question转换成对应的QuestionVO
        List<QuestionSubmitVO> questionSubmitVOList = questionSubmitList.stream().map(questionSubmit -> {
            QuestionSubmitVO questionSubmitVO = QuestionSubmitVO.objToVo(questionSubmit);//question 转成对应VO
            //在question中存的是userId 到VO中要封装对应的userVO
            Long userId = questionSubmit.getUserId();
            User loginUser = userService.getLoginUser(request);
            Long loginUserId = loginUser.getId();
            //当前用户不为管理员也不是提交用户则没有权限查看提交题目的答案
            if (!loginUserId.equals(userId) && (!loginUser.getUserRole().equals("admin"))) {
                questionSubmitVO.setCode("非管理员或提交本人，不支持查看代码");
            }
            //用户也要脱敏 比如密码是不外露的
            UserVO userVO = userService.getUserVO(loginUser);
            questionSubmitVO.setUserVO(userVO);
            return questionSubmitVO;
        }).collect(Collectors.toList());
        questionSubmitVOPage.setRecords(questionSubmitVOList);
        return questionSubmitVOPage;
    }
}




