package com.caixy.codesandbox.controller;

import com.caixy.codesandbox.common.BaseResponse;
import com.caixy.codesandbox.common.ResultUtils;
import com.caixy.codesandbox.expection.CodeSandBoxException;
import com.caixy.codesandbox.mannger.RedisLimiterManager;
import com.caixy.codesandbox.models.ExecuteCodeRequest;
import com.caixy.codesandbox.models.ExecuteCodeResponse;
import com.caixy.codesandbox.models.enums.CodeSandBoxCodeEnum;
import com.caixy.codesandbox.models.enums.LanguageProviderEnum;
import com.caixy.codesandbox.service.java.impl.JavaNativeCodeSandBox;
import com.caixy.codesandbox.service.python3.impl.Python3NativeCodeSandBox;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * 代码沙箱请求控制器
 *
 * @name: com.caixy.codesandbox.controller.MainController
 * @author: CAIXYPROMISE
 * @since: 2024-02-04 23:24
 **/
@RestController
@RequestMapping("/sand-box")
public class MainController
{
    @Value("${auth.request-header-text}")
    private String requestHeaderText;
    @Value("${auth.request-header-value}")
    private String requestHeaderValue;

    @Resource
    private JavaNativeCodeSandBox javaNativeCodeSandBox;

    @Resource
    private Python3NativeCodeSandBox python3NativeCodeSandBox;

    @Resource
    private RedisLimiterManager redisLimiterManager;

    @GetMapping("/hello")
    public String hello()
    {
        return "Hello, CodeSandbox!";
    }

    @PostMapping("/run/java")
    public BaseResponse<ExecuteCodeResponse> runJavaCode(@RequestBody ExecuteCodeRequest executeCodeRequest,
                                                         HttpServletRequest request)
    {
        redisLimiterManager.doRateLimiter(request.getSession().getId());
        String language = executeCodeRequest.getLanguage();
        checkLanguage(language, LanguageProviderEnum.JAVA);
        ExecuteCodeResponse response = javaNativeCodeSandBox.preExecuteCode(executeCodeRequest);
        return ResultUtils.success(response);
    }

    @PostMapping("/run/python3")
    public BaseResponse<ExecuteCodeResponse> runPythonCode(@RequestBody ExecuteCodeRequest executeCodeRequest,
                                                           HttpServletRequest request)
    {
        redisLimiterManager.doRateLimiter(request.getSession().getId());
        String language = executeCodeRequest.getLanguage();
        checkLanguage(language, LanguageProviderEnum.PYTHON_3);
        ExecuteCodeResponse response = python3NativeCodeSandBox.preExecuteCode(executeCodeRequest);
        return ResultUtils.success(response);
    }


    private void checkLanguage(String language, LanguageProviderEnum targetLanguage)
    {
        if (!language.equals(targetLanguage.getText()))
        {
            throw new CodeSandBoxException(CodeSandBoxCodeEnum.LANGUAGE_ERROR, "错误的编程语言");
        }
    }
}
