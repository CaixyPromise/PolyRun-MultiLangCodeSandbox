package com.caixy.codesandbox.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 执行代码结果返回信息
 *
 * @name: com.caixy.codesandbox.models.ExecuteCodeResponse
 * @author: CAIXYPROMISE
 * @since: 2024-02-02 15:38
 **/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExecuteCodeResponse
{

    /**
     * 运行结果输出信息
     * */
    private List<String> outputList;

    /**
     * 接口信息
     */
    private String message;

    /**
     * 执行状态
     */
    private Integer status;

    /**
     * 判题信息
     */
    private JudgeInfo judgeInfo;
}
