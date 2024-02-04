package com.caixy.codesandbox.models.enums;

import lombok.Getter;

/**
 * @name: com.caixy.codesandbox.models.enums.CodeSandBoxCodeEnum
 * @description: 代码沙箱错误码枚举
 * @author: CAIXYPROMISE
 * @date: 2024-02-02 19:24
 **/
@Getter
public enum CodeSandBoxCodeEnum
{
    /**
     * 错误的语言
     */
    LANGUAGE_ERROR(10001, "错误的语言"),
    /**
     * 危险操作
     */
    FORBIDDEN_CODE_ERROR(10002, "含有违禁代码"),
    /**
     * 运行中发生错误
     */
    RUNTIME_ERROR(10003, "运行中发生错误"),

    /**
     * 非0返回
     */
    NON_ZERO_RETURN_CODE(10005, "非0返回"),
    /**
     * 编译错误
     */
    COMPILE_ERROR(10004, "编译错误"),
    /**
     * 系统错误
     */
    SYSTEM_ERROR(-99999, "系统错误"),
    /**
     * 成功
     */
    SUCCESS(0, "运行成功"),
    /**
     * 失败
     */
    FAILED(-1, "运行失败");


    private final String text;
    private final int code;

    CodeSandBoxCodeEnum(int code, String text)
    {
        this.code = code;
        this.text = text;
    }
}
