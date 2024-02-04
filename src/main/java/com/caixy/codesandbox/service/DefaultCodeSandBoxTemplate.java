package com.caixy.codesandbox.service;

import cn.hutool.core.date.StopWatch;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.RandomUtil;
import com.caixy.codesandbox.expection.CodeSandBoxException;
import com.caixy.codesandbox.models.ExecuteCodeResponse;
import com.caixy.codesandbox.models.ExecuteMessage;
import com.caixy.codesandbox.models.JudgeInfo;
import com.caixy.codesandbox.models.enums.CodeSandBoxCodeEnum;
import com.caixy.codesandbox.service.python3.Python3SandBoxConstants;
import com.caixy.codesandbox.utils.ProcessUtils;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Value;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * 默认代码沙箱方法
 *
 * @name: com.caixy.codesandbox.service.DefaultCodeSandBoxTemplate
 * @author: CAIXYPROMISE
 * @since: 2024-02-02 15:25
 **/
@AllArgsConstructor
public class DefaultCodeSandBoxTemplate
{
    @Value("${codesandbox.global-code-path}")
    private final static String GLOBAL_CODE_PATH = "codeStorage";

    public File dumpsCodeToFile(String code,
                                String language,
                                String fileName,
                                HttpServletRequest request)
    {
        String userDir = System.getProperty("user.dir");
        String filePath = userDir + File.separator + GLOBAL_CODE_PATH;
        if (!FileUtil.exist(filePath))
        {
            FileUtil.mkdir(filePath);
        }
        // 隔离用户代码，文件路径:
        // 项目根目录/codeStorage/sessionId-编程语言-随机数-uuid-文件名
        // 例如: /Users/caixypromise/Desktop/codeStorage/12345-uuid/main.java/
        StringBuilder buffer = new StringBuilder();
        buffer.append(filePath)
                .append("-")
                .append(request.getSession().getId())
                .append("-")
                .append(language)
                .append("-")
                .append(RandomUtil.randomString(5))
                .append("-")
                .append(UUID.randomUUID());
        // 创建文件夹
        FileUtil.mkdir(buffer.toString());
        // 完整的文件路径
        String fullFilePath = buffer + File.separator + fileName;
        // 写入文件
        return FileUtil.writeBytes(code.getBytes(StandardCharsets.UTF_8), fullFilePath);
    }

    public boolean deleteCodeFile(File file)
    {
        return FileUtil.del(file);
    }

    /**
     * 默认判题机并返回响应信息
     *
     * @author CAIXYPROMISE
     * @version 1.0
     * @since 2024/2/2 21:36
     */
    public ExecuteCodeResponse judgeOutputAndMakeResponse(List<ExecuteMessage> messagesList)
    {
        ExecuteCodeResponse executeCodeResponse = new ExecuteCodeResponse();
        List<String> outputList = new ArrayList<>(messagesList.size());
        long maxTime = 0L;
        long maxMemoryUsage = 0L;
        for (ExecuteMessage executeMessage : messagesList)
        {
            // 获取输出信息
            if (executeMessage.getExecutePair().getOutputValue() != null)
            {
                outputList.add(executeMessage.getExecutePair().getOutputValue());
            }
            else
            {
                throw new CodeSandBoxException(CodeSandBoxCodeEnum.RUNTIME_ERROR,
                        CodeSandBoxCodeEnum.RUNTIME_ERROR.getText() + ": 输出信息为null");
            }
            // 如果返回值 != 0
            if (executeMessage.getExitValue() != 0)
            {
                executeCodeResponse.setMessage(executeMessage.getExecutePair().getOutputValue());
                executeCodeResponse.setStatus(CodeSandBoxCodeEnum.FAILED.getCode());
                executeCodeResponse.setJudgeInfo(JudgeInfo.make(executeMessage.getExecutePair().getOutputValue(), maxMemoryUsage, maxTime));
                break;
            }
            // 获取运行时间
            Long executeTime = executeMessage.getTime();
            if (executeTime != null && executeTime > maxTime)
            {
                maxTime = executeTime;
            }
            // 获取最大内存使用量
            Long memoryUsage = executeMessage.getMemoryUsage();
            if (memoryUsage != null && memoryUsage > maxMemoryUsage)
            {
                maxMemoryUsage = memoryUsage;
            }
        }
        if (outputList.size() == messagesList.size())
        {
            executeCodeResponse.setStatus(CodeSandBoxCodeEnum.SUCCESS.getCode());
            executeCodeResponse.setMessage(CodeSandBoxCodeEnum.SUCCESS.getText());
        }
        // 设置输出结果
        executeCodeResponse.setOutputList(outputList);
        // 跑完全部样例，即为正常状态，交由判题机完成判断信息输出
        executeCodeResponse.setJudgeInfo(JudgeInfo.make(null, maxTime, maxMemoryUsage));
        return executeCodeResponse;
    }


    public static Thread starProcessMonitor(Process process, long timeOut)
    {
        Thread timeoutWatchDog = new Thread(() ->
        {
            try
            {
                Thread.sleep(timeOut);
                process.destroy();
            }
            catch (InterruptedException e)
            {
                Thread.currentThread().interrupt();
                throw new RuntimeException(e);
            }
        });
        timeoutWatchDog.start();
        return timeoutWatchDog;
    }


    /**
     * 启动运行程序并记录信息
     * @param runCmd  运行命令行信息
     * @param watchId 秒表id：文件名
     * @param inputArg   输入参数
     * @param timeout 运行时间
     *
     * @author CAIXYPROMISE
     * @version 1.0
     * @since 2024/2/4 22:14
     */
    public ExecuteMessage getRuntime(String runCmd,
                                     String watchId,
                                     String inputArg,
                                     Long timeout
                                     )
    {
        Process process = null;

        try {
            // 初始化计时器
            StopWatch stopWatch = new StopWatch(watchId);
            //  获取运行前的内存使用情况
            Long preMemoryUsage = ProcessUtils.getRuntimeMemory();
            // 启动计时器
            stopWatch.start();
            process = Runtime.getRuntime().exec(runCmd);
            // 观察超时线程，超时就终止程序
            Thread timeoutWatchDog = starProcessMonitor(process, timeout);
            // 等待进程退出
            process.waitFor();
            // 停止计时器
            stopWatch.stop();
            // 尝试回收线程
            timeoutWatchDog.interrupt();
            // 获取进程运行结果信息
            ExecuteMessage result = ProcessUtils.getProcessInfo(process);
            // 设置输入参数
            result.getExecutePair().setInputArgs(inputArg);
            // 设置运行时间
            result.setTime(stopWatch.getLastTaskTimeMillis());
            // 计算内存使用用量： 单位为字节(B)
            Long afterMemoryUsage = ProcessUtils.getRuntimeMemory();
            // 计算内存使用量: 转化为Mb (如果需要拿到Kb就不需要乘*1024L)
            result.setMemoryUsage((afterMemoryUsage - preMemoryUsage) / (1024L * 1024L));
            process.destroy();
            return result;
        }
        catch (IOException | InterruptedException e)
        {
            ExecuteMessage executeMessage = new ExecuteMessage();
            executeMessage.setExitValue(CodeSandBoxCodeEnum.SYSTEM_ERROR.getCode());
            executeMessage.getExecutePair().setInputArgs(inputArg);
            executeMessage.getExecutePair().setOutputValue(e.getMessage());
            throw new CodeSandBoxException(CodeSandBoxCodeEnum.RUNTIME_ERROR,
                    CodeSandBoxCodeEnum.RUNTIME_ERROR.getText() + ": " + e.getMessage());
        }
    }
}
