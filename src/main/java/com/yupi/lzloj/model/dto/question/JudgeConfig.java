package com.yupi.lzloj.model.dto.question;

import lombok.Data;

/**
 * 判题信息封装实体类
 */
@Data
public class JudgeConfig {
    /**
     * 时间限制ms
     */
    private Long timeLimit;

    /**
     * 内存限制 KB
     */
    private Long memoryLimit;
    /**
     * 堆栈限制 KB
     */
    private Long stackLimit;
}
