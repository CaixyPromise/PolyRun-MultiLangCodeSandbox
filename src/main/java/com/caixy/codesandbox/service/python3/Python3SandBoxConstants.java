package com.caixy.codesandbox.service.python3;

import java.util.Arrays;
import java.util.List;

/**
 * @Name: com.caixy.codesandbox.service.python.Python3SandBoxConstants
 * @Description: Python3代码语法黑名单
 * @Author: CAIXYPROMISE
 * @Date: 2024-02-02 16:31
 **/
public interface Python3SandBoxConstants
{
    /**
     * 待运行代码的存放文件名
     */
    static final String GLOBAL_PYTHON_FILE_NAME = "Main.py";

    /**
     * 代码最大允许运行的时间
     */
    static final long TIME_OUT = 15000L;

    /**
     * 设置Python最大可用内存为128MB
     */
    static final String MEMORY_LIMIT_PREFIX_CODE =
            "import resource;\n" +
            "max_memory = 128;\n" +
            "resource.setrlimit(resource.RLIMIT_AS, (max_memory * (1024 ** 2), -1));\n";

    static final List<String> BLACK_LIST = Arrays.asList(
            // 文件操作相关
            "open", "os.system", "os.popen", "os.fdopen", "shutil.copy", "shutil.move", "shutil.rmtree",

            // 网络相关
            "socket", "http.client.HTTPConnection", "http.client.HTTPSConnection", "urllib.request.urlopen", "urllib.request.urlretrieve",

            // 系统命令执行相关
            "subprocess.run", "subprocess.Popen",

            // 反射相关
            "__import__", "eval", "exec",

            // 数据库相关
            "sqlite3", "MySQLdb",

            // 加密解密相关
            "cryptography",

            // 序列化相关
            "pickle",

            // 线程相关
            "threading.Thread", "multiprocessing.Process",

            // 安全管理器相关
            "java.lang.SecurityManager",

            // 其他可能导致安全问题的操作
            "ctypes.CDLL", "ctypes.WinDLL", "ctypes.CFUNCTYPE", "os.environ", "os.putenv", "atexit.register",

            // 与操作系统交互
            "os.chmod", "os.chown",

            // 文件权限控制
            "os.access", "os.setuid", "os.setgid",

            // 环境变量操作
            "os.environ['SOME_VAR']", "os.putenv('SOME_VAR', 'value')",

            // 不安全的输入
            "input", "raw_input",

            // 不安全的字符串拼接
            "eval(f'expr {var}')", "var = var1 + var2",

            // 定时器相关
            "time.sleep",

            // 定时任务
            "schedule",

            // 本地文件包含
            "exec(open('filename').read())",

            // 不安全的网站访问
            "urllib.urlopen",

            // 系统退出
            "exit",

            // 其他危险操作
            "os.remove", "os.unlink", "os.rmdir", "os.removedirs", "os.rename", "os.execvp", "os.execlp",

            // 不安全的随机数生成
            "random",

            // 不安全的正则表达式
            "re.compile",

            // 使用 eval 解析 JSON
            "eval(json_string)",

            // 使用 pickle 处理不受信任的数据
            "pickle.loads",

            // 使用 exec 执行不受信任的代码
            "exec(code)",

            // 不安全的 HTML 解析
            "BeautifulSoup",

            // 不安全的 XML 解析
            "xml.etree.ElementTree",

            // 使用自定义反序列化
            "pickle.Unpickler", "marshal.loads",

            // 在代码中直接拼接 SQL
            "sqlalchemy.text",

            // 不安全的图像处理
            "PIL.Image",

            // 使用 ctypes 执行外部 C 代码
            "ctypes.CDLL",

            // 不安全的邮件操作
            "smtplib", "poplib",

            // 不安全的 URL 拼接
            "urllib.parse.urljoin",

            // 使用 eval 执行 JavaScript 代码
            "execjs.eval",

            // 不安全的 Web 框架设置
            "Flask.app.secret_key",

            // 不安全的 API 请求
            "requests.get", "requests.post",

            // 不安全的模板引擎
            "Jinja2.Template",

            // 不安全的数据反序列化
            "pickle.loads", "marshal.loads",

            // 不安全的文件上传
            "werkzeug.FileStorage",

            // 不安全的命令行参数解析
            "argparse.ArgumentParser");
}
