package com.caixy.codesandbox.models.enums;

import lombok.Getter;

/**
 * @name: com.caixy.codesandbox.models.enums.LanguageEnum
 * @description: 语言枚举
 * @author: CAIXYPROMISE
 * @date: 2024-02-02 16:39
 **/
@Getter
public enum LanguageEnum
{
    PYTHON_2("PYTHON_2", "Python_2"),
    PYTHON_3("PYTHON_3", "Python_3"),
    JAVA("JAVA", "Java"),
    CPP("CPP", "C++"),
    C_LANGUAGE("C_LANGUAGE", "C语言");

    private final String value;
    private final String text;

    LanguageEnum(String value, String text)
    {
        this.value = value;
        this.text = text;
    }
}
