package com.yupi.lzloj.judge.codesandbox.impl;

import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import com.yupi.lzloj.common.ErrorCode;
import com.yupi.lzloj.exception.BusinessException;
import com.yupi.lzloj.judge.codesandbox.CodeSandBox;
import com.yupi.lzloj.judge.codesandbox.model.ExecuteCodeRequest;
import com.yupi.lzloj.judge.codesandbox.model.ExecuteCodeResponse;

/**
 * 调用远程代码沙箱
 */
public class RemoteSandBox implements CodeSandBox {
    // 定义鉴权请求头和密钥
    private static final String AUTH_REQUEST_HEADER = "auth";

    private static final String AUTH_REQUEST_SECRET = "secretKey";

    /**
     * 远程代码沙箱，调用远程api
     * @param executeCodeRequest
     * @return
     */
    @Override
    public ExecuteCodeResponse executeCode(ExecuteCodeRequest executeCodeRequest) {
        //String url = "http://localhost:8090/executeCode";
        String url = "http://123.56.107.202:8090/executeCode";
        String body = HttpUtil.createPost(url)//装配请求地址
                .header(AUTH_REQUEST_HEADER,AUTH_REQUEST_SECRET)//通过请求头加密
                .body(JSONUtil.toJsonStr(executeCodeRequest))//装配请求参数
                .execute()//发送请求
                .body();//得到返回体
        if(StrUtil.isBlank(body))
        {
            throw new BusinessException(ErrorCode.API_REQUEST_ERROR,"executeCode remoteSandbox error+"+body);
        }
        return JSONUtil.toBean(body,ExecuteCodeResponse.class);
    }
}
