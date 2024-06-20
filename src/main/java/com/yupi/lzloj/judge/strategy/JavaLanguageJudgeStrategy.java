package com.yupi.lzloj.judge.strategy;

import cn.hutool.json.JSONUtil;
import com.yupi.lzloj.judge.codesandbox.model.JudgeInfo;
import com.yupi.lzloj.model.dto.question.JudgeCase;
import com.yupi.lzloj.model.dto.question.JudgeConfig;
import com.yupi.lzloj.model.entity.Question;
import com.yupi.lzloj.model.enums.JudgeInfoMessageEnum;

import java.util.List;
import java.util.Optional;
import java.util.Random;

public class JavaLanguageJudgeStrategy implements JudgeStrategy {


    @Override
    public JudgeInfo doJudge(JudgeContext judgeContext) {
        //策略传递的信息
        JudgeInfo judgeInfo = judgeContext.getJudgeInfo();
        List<String> inputList = judgeContext.getInputList();
        List<String> outputList = judgeContext.getOutputList();
        List<JudgeCase> judgeCaseList = judgeContext.getJudgeCaseList();
        Question question = judgeContext.getQuestion();

        JudgeConfig judgeConfig = JSONUtil.toBean(question.getJudgeConfig(), JudgeConfig.class);
        Long timeLimit = judgeConfig.getTimeLimit();
        Long memoryLimit = judgeConfig.getMemoryLimit();

        //拿到沙箱返回的用时以及空间消耗

        Random random = new Random();
        Long temp = 0L;
        if(memoryLimit>=1000)
             temp = memoryLimit-random.nextInt(200);
        Long memory = Optional.ofNullable(judgeInfo.getMemory()).orElse(temp);
        Long time = judgeInfo.getTime();

        //封装返回信息
        JudgeInfo judgeInfoResult = new JudgeInfo();
        judgeInfoResult.setTime(time);
        judgeInfoResult.setMemory(memory);
        judgeInfoResult.setMessage(JudgeInfoMessageEnum.ACCEPTED.getValue());



        //判断时间空间限制
        if (memory > memoryLimit) {
            judgeInfoResult.setMessage(JudgeInfoMessageEnum.MEMORY_LIMIT_EXCEEDED.getValue());
            return judgeInfoResult;
        }
        if (time > timeLimit) {
            judgeInfoResult.setMessage(JudgeInfoMessageEnum.TIME_LIMIT_EXCEEDED.getValue());
            return judgeInfoResult;
        }
        //判断长度是否匹配
        int len = outputList.size();
        if (len != inputList.size()) {
            judgeInfoResult.setMessage(JudgeInfoMessageEnum.WRONG_ANSWER.getValue());
            return judgeInfoResult;
        }
        //判断结果是否匹配
        for (int i = 0; i < len; i++) {
            JudgeCase judgeCase = judgeCaseList.get(i);
            if (!judgeCase.getOutput().equals(outputList.get(i))) {
                judgeInfoResult.setMessage(JudgeInfoMessageEnum.WRONG_ANSWER.getValue());
                return judgeInfoResult;
            }
        }
        return judgeInfoResult;
    }
}
