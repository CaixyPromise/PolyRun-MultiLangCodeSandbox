package com.caixy.codesandbox.service.java.impl;

import com.caixy.codesandbox.models.ExecuteCodeRequest;
import com.caixy.codesandbox.models.ExecuteCodeResponse;
import com.caixy.codesandbox.service.java.JavaCodeSandBoxTemplate;

/**
 * Java代码沙箱具体实现
 *
 * @name: com.caixy.codesandbox.service.java.impl.JavaNativeCodeSandBox
 * @author: CAIXYPROMISE
 * @since: 2024-02-04 17:16
 **/
public class JavaNativeCodeSandBox extends JavaCodeSandBoxTemplate
{
    @Override
    public ExecuteCodeResponse preExecuteCode(ExecuteCodeRequest executeCodeRequest)
    {
        return super.preExecuteCode(executeCodeRequest);
    }
}
