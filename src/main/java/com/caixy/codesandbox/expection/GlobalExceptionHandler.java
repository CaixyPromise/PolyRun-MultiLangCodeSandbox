package com.caixy.codesandbox.expection;

import com.caixy.codesandbox.common.BaseResponse;
import com.caixy.codesandbox.common.ErrorCode;
import com.caixy.codesandbox.common.ResultUtils;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局异常处理器
 *
 * @name: com.caixy.codesandbox.expection.GlobalExceptionHandler
 * @author: CAIXYPROMISE
 * @since: 2024-02-02 19:30
 **/
@RestControllerAdvice
public class GlobalExceptionHandler
{
    @ExceptionHandler(CodeSandBoxException.class)
    public BaseResponse<?> error(CodeSandBoxException e)
    {
        return ResultUtils.error(e.getCode(), e.getMessage());
    }

    @ExceptionHandler(RuntimeException.class)
    public BaseResponse<?> runtimeExceptionHandler(RuntimeException e)
    {
        return ResultUtils.error(ErrorCode.SYSTEM_ERROR, "系统错误");
    }
}
