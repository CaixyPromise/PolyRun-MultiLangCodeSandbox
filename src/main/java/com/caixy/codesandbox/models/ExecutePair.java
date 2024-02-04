package com.caixy.codesandbox.models;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Objects;

/**
 * 执行信息与结果键值对
 *
 * @name: com.caixy.codesandbox.models.ExecutePair
 * @author: CAIXYPROMISE
 * @since: 2024-02-02 22:50
 **/
@Data
@AllArgsConstructor

public class ExecutePair
{
    //  输入参数
    private String inputArgs;
    //  输出结果
    private String outputValue;

    public ExecutePair()
    {
        //  无参构造器
        this.inputArgs = "";
        this.outputValue = "";
    }

    public static ExecutePair make(String inputArgs, String output)
    {
        return new ExecutePair(inputArgs, output);
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o)
        {
            return true;
        }
        if (o == null || getClass() != o.getClass())
        {
            return false;
        }
        ExecutePair that = (ExecutePair) o;
        return inputArgs.equals(that.inputArgs) && outputValue.equals(that.outputValue);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(inputArgs, outputValue);
    }
}
