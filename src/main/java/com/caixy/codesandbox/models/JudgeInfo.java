package com.caixy.codesandbox.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 判题结果信息
 *
 * @name: com.caixy.codesandbox.models.JudgeInfo
 * @author: CAIXYPROMISE
 * @since: 2024-02-02 15:39
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class JudgeInfo
{

    /**
     * 程序执行信息
     */
    private String message;

    /**
     * 消耗内存(kb)
     */
    private Long memory;

    /**
     * 消耗时间(ms)
     */
    private Long time;

    public static JudgeInfo make(String message, Long memory, Long time)
    {
        return new JudgeInfo(message, memory, time);
    }
}