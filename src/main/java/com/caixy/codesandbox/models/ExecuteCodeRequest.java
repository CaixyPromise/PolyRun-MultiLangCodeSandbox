package com.caixy.codesandbox.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 执行代码请求信息
 *
 * @name: com.caixy.codesandbox.models.ExecuteCodeRequest
 * @author: CAIXYPROMISE
 * @since: 2024-02-02 15:38
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ExecuteCodeRequest
{
    /**
     * 输入参数
     */
    private List<String> inputList;

    /**
     * 用户代码
     */
    private String code;

    /**
     * 语言类型
     */
    private String language;

    /**
     * 请求信息
     * */
    private HttpServletRequest request;
}
