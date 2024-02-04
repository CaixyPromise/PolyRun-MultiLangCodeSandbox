package com.caixy.codesandbox.expection;

import com.caixy.codesandbox.models.enums.CodeSandBoxCodeEnum;
import lombok.Getter;

/**
 * @name: com.caixy.codesandbox.expection.CodeSandBoxException
 * @author: CAIXYPROMISE
 * @since: 2024-02-02 19:20
 **/
@Getter
public class CodeSandBoxException extends RuntimeException
{
    private final int code;

    public CodeSandBoxException(int code, String message)
    {
        super(message);
        this.code = code;
    }

    public CodeSandBoxException(CodeSandBoxCodeEnum errorCode)
    {
        super(errorCode.getText());
        this.code = errorCode.getCode();
    }

    public CodeSandBoxException(CodeSandBoxCodeEnum errorCode, String message)
    {
        super(message);
        this.code = errorCode.getCode();
    }
}
