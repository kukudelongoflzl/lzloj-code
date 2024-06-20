package com.yupi.lzloj.judge.codesandbox.model;

import lombok.Data;

@Data
public class JudgeInfo {

    /**
     * 判题信息
     */
    private String message;

    /**
     * 判题消耗时间
     */
    private Long time;

    /**
     * 判题运行内存
     */
    private  Long memory;


}
