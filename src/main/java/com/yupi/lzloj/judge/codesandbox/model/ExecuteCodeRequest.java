package com.yupi.lzloj.judge.codesandbox.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 代码沙箱请求
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ExecuteCodeRequest {

    /**
     * 输入代码
     */
    private String code;

    /**
     * 代码语言
     */
    private String language;

    /**
     * 输入用例列表
     */
    private List<String> inputList;
}
