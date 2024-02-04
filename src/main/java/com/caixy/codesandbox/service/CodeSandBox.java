package com.caixy.codesandbox.service;

import com.caixy.codesandbox.models.ExecuteCodeRequest;
import com.caixy.codesandbox.models.ExecuteCodeResponse;
import com.caixy.codesandbox.models.ExecuteMessage;

import java.io.File;
import java.util.List;

/**
 * 代码沙箱接口类
 *
 * @name: com.caixy.codesandbox.service.CodeSandBox
 * @author: CAIXYPROMISE
 * @since: 2024-02-02 15:24
 **/
public interface CodeSandBox
{
    /**
     * 执行代码前的操作: 校验-写入文件
     *
     * @param executeCodeRequest 请求参数
     * @author CAIXYPROMISE
     * @version 1.0
     * @since 2024/2/4 17:35
     */
    ExecuteCodeResponse preExecuteCode(ExecuteCodeRequest executeCodeRequest);


//    /**
//     * 执行代码
//     *
//     * @param file      文件
//     * @param inputList 输入参数列表
//     * @return 执行结果列表
//     * @author CAIXYPROMISE
//     * @version 1.0
//     * @since 2024/2/4 17:36
//     */
//    List<ExecuteMessage> ExecuteCode(File file, List<String> inputList);
//
//    /**
//     * 执行代码后的操作: 删除文件, 保存信息
//     *
//     * @param executeMessageList 执行结果
//     * @param file               文件
//     * @return 代码执行结果报告 {@link ExecuteCodeResponse}
//     * @author CAIXYPROMISE
//     * @version 1.0
//     * @since 2024/2/4 17:36
//     */
//    ExecuteCodeResponse afterExecuteCode(List<ExecuteMessage> executeMessageList, File file);
}
