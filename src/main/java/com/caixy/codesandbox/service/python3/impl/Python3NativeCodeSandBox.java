package com.caixy.codesandbox.service.python3.impl;

import com.caixy.codesandbox.models.ExecuteCodeRequest;
import com.caixy.codesandbox.models.ExecuteCodeResponse;
import com.caixy.codesandbox.models.ExecuteMessage;
import com.caixy.codesandbox.service.python3.Python3CodeSandBoxTemplate;

import java.io.File;
import java.util.List;

/**
 * Python3 原生代码沙箱实现
 *
 * @version 1.0
 * @name: com.caixy.codesandbox.service.python.impl.Python3NativeCodeSandBox
 * @author: CAIXYPROMISE
 * @since: 2024-02-02 16:26
 **/
public class Python3NativeCodeSandBox extends Python3CodeSandBoxTemplate
{
    @Override
    public ExecuteCodeResponse preExecuteCode(ExecuteCodeRequest executeCodeRequest)
    {
        return super.preExecuteCode(executeCodeRequest);
    }

//    @Override
//    public List<ExecuteMessage> ExecuteCode(File file, List<String> inputList)
//    {
//        return super.ExecuteCode(file, inputList);
//    }
//
//    @Override
//    public ExecuteCodeResponse afterExecuteCode(List<ExecuteMessage> executeMessageList, File file)
//    {
//        return super.afterExecuteCode(executeMessageList, file);
//    }
}
