package com.yupi.lzloj.judge.codesandbox;

import com.yupi.lzloj.judge.codesandbox.model.ExecuteCodeRequest;
import com.yupi.lzloj.judge.codesandbox.model.ExecuteCodeResponse;

/**
 * 沙箱接口
 * 参数配置化
 * 把项目中一些需要配置的参数放到配置文件里面
 */
public interface CodeSandBox {

    /**
     * 代码沙箱执行
     * @param executeCodeRequest
     * @return
     */
    ExecuteCodeResponse executeCode(ExecuteCodeRequest executeCodeRequest);
}
