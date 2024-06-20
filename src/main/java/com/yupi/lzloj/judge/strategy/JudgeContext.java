package com.yupi.lzloj.judge.strategy;


import com.yupi.lzloj.judge.codesandbox.model.JudgeInfo;
import com.yupi.lzloj.model.dto.question.JudgeCase;
import com.yupi.lzloj.model.entity.Question;
import com.yupi.lzloj.model.entity.QuestionSubmit;
import lombok.Data;

import java.util.List;

/**
 * 上下文
 * 用于定义在策略中传递的参数（可以理解为一种DTO）
 */
@Data
public class JudgeContext {

    /**
     * 判题信息 来自代码沙箱的响应
     */
    private JudgeInfo judgeInfo;
    /**
     * 判题输入 来自题目
     */
    private List<String> inputList;


    /**
     * 判题输出 来自代码沙箱
     */
    private List<String> outputList;

    /**
     * 用例 来自问题信息然后转换
     */
    private List<JudgeCase> judgeCaseList;

    /**
     * 问题信息 那拿到输入用例以及对应输出
     */
    private Question question;

    /**
     * 提交信息
     */
    private QuestionSubmit questionSubmit;
}
