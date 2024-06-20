package com.yupi.lzloj.judge.codesandbox.model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 代码沙箱响应
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ExecuteCodeResponse {

    /**
     * 判题输出用例
     */
    private List<String> outputList;

    /**
     * 接口信息
     */
    private String message;

    /**
     * 判题时候代码沙箱传出的信息
     */
    private JudgeInfo judgeInfo;

    /**
     * 判题状态
     */
    private Integer status;

}
