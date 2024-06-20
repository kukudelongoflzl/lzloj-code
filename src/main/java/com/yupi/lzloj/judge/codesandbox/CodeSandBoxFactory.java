package com.yupi.lzloj.judge.codesandbox;

import com.yupi.lzloj.judge.codesandbox.impl.ExampleCodeSangBox;
import com.yupi.lzloj.judge.codesandbox.impl.RemoteSandBox;
import com.yupi.lzloj.judge.codesandbox.impl.ThirdPartySandBox;

/**
 * 工厂模式创建代码沙箱(根据传入的字符串创建不同的代码沙箱)
 */
public class CodeSandBoxFactory {

    /**
     * 工厂模式代理沙箱创建
     *
     * @param type
     * @return
     */
    public static CodeSandBox newInstance(String type) {
        switch (type) {
            case "example":
                return new ExampleCodeSangBox();
            case "remote":
                return new RemoteSandBox();
            case "thirdParty":
                return new ThirdPartySandBox();
            default:
                return new ExampleCodeSangBox();
        }
    }
}
