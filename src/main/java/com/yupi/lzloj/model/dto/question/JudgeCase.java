package com.yupi.lzloj.model.dto.question;

import lombok.Data;

/**
 * 判题用例输入输出的封装实体类
 */
@Data
public class JudgeCase {
    /**
     * 输入用例
     */
    private String input;

    /**
     * 输出用例
     */
    private String output;
}
