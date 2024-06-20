package com.yupi.lzloj.judge.codesandbox;


import com.yupi.lzloj.judge.codesandbox.model.ExecuteCodeRequest;
import com.yupi.lzloj.judge.codesandbox.model.ExecuteCodeResponse;
import lombok.extern.slf4j.Slf4j;

/**
 * 代理模式，增强接口的功能
 * 比如可以在沙箱执行前后分别打印log信息
 */
@Slf4j
public class CodeSandBoxProxy implements CodeSandBox {

    //成员变量
    public final CodeSandBox codeSandBox;

    //有参构造
    public CodeSandBoxProxy(CodeSandBox codeSandBox) {
        this.codeSandBox = codeSandBox;
    }

    @Override
    public ExecuteCodeResponse executeCode(ExecuteCodeRequest executeCodeRequest) {

        log.info("代码沙箱请求信息 " + executeCodeRequest.toString());
        ExecuteCodeResponse executeCodeResponse = codeSandBox.executeCode(executeCodeRequest);
        log.info("代码沙箱响应信息 " + executeCodeResponse.toString());
        return executeCodeResponse;
    }
}
