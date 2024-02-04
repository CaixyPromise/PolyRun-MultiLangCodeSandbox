package com.caixy.codesandbox.models.enums;

import lombok.Getter;
import org.springframework.util.ObjectUtils;

/**
 * 判题结果信息枚举
 *
 * @name: com.caixy.codesandbox.models.enums.SubmitResultMessageEnum
 * @author: CAIXYPROMISE
 * @since: 2024-02-02 21:12
 **/
@Getter
public enum SubmitResultMessageEnum
{
    ACCEPTED("成功", "Accepted"),

    WRONG_ANSWER("答案错误", "Wrong Answer"),

    COMPILE_ERROR("Compile Error", "编译错误"),

    MEMORY_LIMIT_EXCEEDED("Out of Memory", "内存溢出"),

    TIME_LIMIT_EXCEEDED("Time Limit Exceeded", "超时"),

    PRESENTATION_ERROR("Presentation Error", "展示错误"),

    WAITING("Waiting", "等待中"),

    OUTPUT_LIMIT_EXCEEDED("Output Limit Exceeded", "输出溢出"),

    DANGEROUS_OPERATION("Dangerous Operation", "危险操作"),

    RUNTIME_ERROR("Runtime Error", "运行错误"),

    RUNTIME_FAILED("Runtime Failed", "运行失败"),

    SYSTEM_ERROR("System Error","系统错误");

    private final String text;

    private final String value;

    SubmitResultMessageEnum(String text, String value)
    {
        this.text = text;
        this.value = value;
    }

    /**
     * 根据 value 获取枚举
     *
     * @param value
     * @return
     */
    public static SubmitResultMessageEnum getEnumByValue(String value)
    {
        if (ObjectUtils.isEmpty(value))
        {
            return null;
        }
        for (SubmitResultMessageEnum anEnum : SubmitResultMessageEnum.values())
        {
            if (anEnum.value.equals(value))
            {
                return anEnum;
            }
        }
        return null;
    }

    public String getValue()
    {
        return value;
    }

    public String getText()
    {
        return text;
    }
    }
