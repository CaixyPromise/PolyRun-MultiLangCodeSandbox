package com.caixy.codesandbox.service.java;

import cn.hutool.dfa.FoundWord;
import cn.hutool.dfa.WordTree;
import com.caixy.codesandbox.expection.CodeSandBoxException;
import com.caixy.codesandbox.models.ExecuteCodeRequest;
import com.caixy.codesandbox.models.ExecuteCodeResponse;
import com.caixy.codesandbox.models.ExecuteMessage;
import com.caixy.codesandbox.models.enums.CodeSandBoxCodeEnum;
import com.caixy.codesandbox.models.enums.LanguageProviderEnum;
import com.caixy.codesandbox.service.CodeSandBox;
import com.caixy.codesandbox.service.DefaultCodeSandBoxTemplate;
import com.caixy.codesandbox.utils.ProcessUtils;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


/**
 * Java代码沙箱模板方法的实现
 *
 * @name: com.caixy.codesandbox.service.java.JavaCodeSandBoxTemplate
 * @author: CAIXYPROMISE
 * @since: 2024-02-04 17:15
 **/
@Slf4j
@Component
public abstract class JavaCodeSandBoxTemplate
        extends DefaultCodeSandBoxTemplate implements CodeSandBox
{
    @Getter
    private static final String CODE_SANDBOX_NAME = LanguageProviderEnum.JAVA.getValue();

    private static final int RUNTIME_ERROR_CODE = -99999;

    /**
     * 代码黑名单字典树
     */
    private static final WordTree WORD_TREE;
    private static final String RUNCODE_CMD_PREFIX =
            "java -Xmx128m -Dfile.encoding=UTF-8 -cp %s;%s -Djava.security.manager=%s Main %s";

    private static final String COMPILER_CMD_PREFIX = "javac -encoding utf-8 %s";

    static
    {
        WORD_TREE = new WordTree();
        WORD_TREE.addWords(JavaSandBoxConstants.BLACK_LIST);

    }

    @Override
    public ExecuteCodeResponse preExecuteCode(ExecuteCodeRequest executeCodeRequest)
    {
        List<String> inputList = executeCodeRequest.getInputList();
        String code = executeCodeRequest.getCode();
        String language = executeCodeRequest.getLanguage();
        System.out.println("当前操作系统：" + System.getProperty("os.name").toLowerCase());
        System.out.println("当前代码使用语言：" + language);
        // 限制敏感代码：黑名单检测
        FoundWord foundWord = WORD_TREE.matchWord(code);
        if (foundWord != null)
        {
            throw new CodeSandBoxException(CodeSandBoxCodeEnum.FORBIDDEN_CODE_ERROR);
        }

        // 5. 保存代码
        File userCodeFile = dumpsCodeToFile(code,
                language,
                JavaSandBoxConstants.GLOBAL_JAVA_CLASS_NAME,
                executeCodeRequest.getRequest());
        // 6. 编译代码
        ExecuteMessage compileResult = compileCode(userCodeFile);
        // 7. 执行代码
        List<ExecuteMessage> executeMessageList = runCode(userCodeFile, inputList);
        // 8. 收集运行结果
        ExecuteCodeResponse response = judgeOutputAndMakeResponse(executeMessageList);
        // 9. 删除代码文件
        boolean deleteResult = deleteCodeFile(userCodeFile);
        // 10. 删除代码文件失败处理
        if (!deleteResult)
        {
            log.error("删除代码文件失败，文件路径：{}", userCodeFile.getAbsolutePath());
        }
        return response;
    }

    /**
     * 编译代码
     *
     * @param userCodeFile
     * @return
     */
    public ExecuteMessage compileCode(File userCodeFile)
    {
        String compileCmd = String.format("javac -encoding utf-8 %s", userCodeFile.getAbsolutePath());
        try
        {
            Process compileProcess = Runtime.getRuntime().exec(compileCmd);
            ExecuteMessage executeMessage = ProcessUtils.getProcessInfo(compileProcess);
            // 编译失败
            if (executeMessage.getExitValue() != 0)
            {
                executeMessage.setExitValue(1);
                executeMessage.getExecutePair().setOutputValue(CodeSandBoxCodeEnum.COMPILE_ERROR.getText());
            }
            return executeMessage;
        }
        catch (Exception e)
        {
            // 未知错误
            ExecuteMessage executeMessage = new ExecuteMessage();
            executeMessage.setExitValue(1);
            executeMessage.getExecutePair().setOutputValue(CodeSandBoxCodeEnum.COMPILE_ERROR.getText());
            return executeMessage;
        }
    }

    /**
     * 执行文件，获得执行结果列表
     *
     * @param userCodeFile
     * @param inputList
     * @return
     */
    public List<ExecuteMessage> runCode(File userCodeFile, List<String> inputList)
    {
        String userCodeParentPath = userCodeFile.getParentFile().getAbsolutePath();

        List<ExecuteMessage> executeMessageList = new ArrayList<>();
        for (String inputArgs : inputList)
        {
            String runCMD = String.format(RUNCODE_CMD_PREFIX,
                    userCodeParentPath,
                    JavaSandBoxConstants.SECURITY_MANAGER_PATH,
                    JavaSandBoxConstants.SECURITY_MANAGER_CLASS_NAME,
                    inputArgs);
            // 运行程序
            ExecuteMessage result = this.getRuntime(
                    runCMD,
                    userCodeFile.getName(),
                    inputArgs,
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
