package com.caixy.codesandbox.service.python3;

import cn.hutool.dfa.FoundWord;
import cn.hutool.dfa.WordTree;
import com.caixy.codesandbox.expection.CodeSandBoxException;
import com.caixy.codesandbox.models.ExecuteCodeRequest;
import com.caixy.codesandbox.models.ExecuteCodeResponse;
import com.caixy.codesandbox.models.ExecuteMessage;
import com.caixy.codesandbox.models.enums.CodeSandBoxCodeEnum;
import com.caixy.codesandbox.models.enums.LanguageEnum;
import com.caixy.codesandbox.service.CodeSandBox;
import com.caixy.codesandbox.service.DefaultCodeSandBoxTemplate;
import com.caixy.codesandbox.service.java.JavaSandBoxConstants;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Python3 代码沙箱模板方法的实现
 *
 * @version 1.0
 * @name: com.caixy.codesandbox.service.python.Python3CodeSandBoxTemplate
 * @author: CAIXYPROMISE
 * @since: 2024-02-02 16:23
 **/
@Component
@Slf4j
public abstract class Python3CodeSandBoxTemplate
        extends DefaultCodeSandBoxTemplate implements CodeSandBox
{
    @Getter
    private static final String CODE_SANDBOX_NAME = LanguageEnum.PYTHON_3.getValue();


    /**
     * 代码黑名单字典树
     */
    private static final WordTree WORD_TREE;
    private static final String CMD_PREFIX;

    static
    {
        // 初始化黑名单字典树
        WORD_TREE = new WordTree();
        WORD_TREE.addWords(Python3SandBoxConstants.BLACK_LIST);
        CMD_PREFIX
                = System.getProperty("os.name").toLowerCase().contains("win")
                  ? "python"
                  : "python3";
    }

    /**
     * 代码运行前的预处理，处理完后运行代码
     * 操作：
     * <li>1. 检测代码是否包含黑名单词汇</li>
     * <li>2. 保存代码到文件中</li>
     * <li>3. 运行代码</li>
     * <li>4. 收集运行结果</li>
     * <li>5. 删除代码文件</li>
     *
     * @return 代码运行结果信息 {@link ExecuteCodeResponse}
     * @author CAIXYPROMISE
     * @version 1.0
     * @since 2024/2/2 20:25
     */
    @Override
    public ExecuteCodeResponse preExecuteCode(ExecuteCodeRequest executeCodeRequest)
    {
        // 1. 获取输入参数
        List<String> inputList = executeCodeRequest.getInputList();
        // 2. 获取代码
        String code = executeCodeRequest.getCode();
        // 3. 获取语言
        String language = executeCodeRequest.getLanguage();

        // 4. 检测代码是否包含黑名单词汇
        FoundWord foundWord = WORD_TREE.matchWord(code);
        if (foundWord != null)
        {
            throw new CodeSandBoxException(CodeSandBoxCodeEnum.FORBIDDEN_CODE_ERROR);
        }

        String osName = System.getProperty("os.name").toLowerCase();
        if (osName.contains("nix") || osName.contains("nux"))
        {
            code = Python3SandBoxConstants.MEMORY_LIMIT_PREFIX_CODE + "\r\n" + code;
            System.out.println("安全控制后的代码：\n" + code);
        }
        // 5. 保存代码
        File userCodeFile = dumpsCodeToFile(code,
                language,
                Python3SandBoxConstants.GLOBAL_PYTHON_FILE_NAME,
                executeCodeRequest.getRequest());
        // 6. 执行代码
        List<ExecuteMessage> executeMessageList = runCode(userCodeFile, inputList);
        // 7. 收集运行结果
        ExecuteCodeResponse response = judgeOutputAndMakeResponse(executeMessageList);
        // 8. 删除代码文件
        boolean deleteResult = deleteCodeFile(userCodeFile);
        // 9. 删除代码文件失败处理
        if (!deleteResult)
        {
            log.error("删除代码文件失败，文件路径：{}", userCodeFile.getAbsolutePath());
        }
        return response;
    }

    /**
     * 执行代码
     *
     * @author CAIXYPROMISE
     * @version 1.0
     * @since 2024/2/2 21:39
     */
    private List<ExecuteMessage> runCode(File codeFile, List<String> inputList)
    {
        // 1. 获取文件代码路径
        String fileAbsolutePath = codeFile.getAbsolutePath();
        // 2. 初始化结果输出链表，长度为样例输入个数
        List<ExecuteMessage> executeMessageList = new ArrayList<>(inputList.size());
        for (String inputArg : inputList)
        {
            // 3. 初始化执行命令
            String runCmd = String.format("%s %s.py %s", CMD_PREFIX, fileAbsolutePath, inputArg);
            // 4. 运行程序
            ExecuteMessage result = this.getRuntime(runCmd, codeFile.getName(),
                    inputArg,
                    JavaSandBoxConstants.TIME_OUT);
            executeMessageList.add(result);
            // 如果程序运行结果不是0，则直接返回
            if (result.getExitValue() != 0)
            {
                return executeMessageList;
            }
        }
        return executeMessageList;
    }
}
