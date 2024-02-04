package com.caixy.codesandbox.models;

import lombok.Data;

/**
 * 执行代码结果信息-用于收集信息并给返回体使用
 *
 * @name: com.caixy.codesandbox.models.ExecuteMessage
 * @author: CAIXYPROMISE
 * @since: 2024-02-02 15:39
 **/
@Data
public class ExecuteMessage
{

    /**
     * 执行结果
     * 0-正常
     * 1（非零）-异常
     */
    private Integer exitValue;
    /**
     * 输入参数对应的输出结果 key: 输入参数 -> value: 输出参数
     */
    private ExecutePair executePair = new ExecutePair();
    private Long time;
    private Long memoryUsage;
}