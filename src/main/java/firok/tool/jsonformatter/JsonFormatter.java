package firok.tool.jsonformatter;

import com.fasterxml.jackson.core.util.DefaultIndenter;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

import java.io.File;
import java.util.HashSet;

public class JsonFormatter
{
    public static void main(String[] args)
    {
        var tsStart = System.currentTimeMillis();
        if(args == null || args.length == 0)
        {
            System.err.println("参数不可为空. 请提供一个或多个文件路径以开始处理.");
            System.exit(1);
        }
        else
        {
            var setFiles = new HashSet<File>();
            String method  = null;
            for(var arg : args)
            {
                if("-f".equals(arg))
                {
                    if(method != null)
                    {
                        System.err.println("参数错误. 请勿重复指定格式化参数.");
                        System.exit(2);
                    }
                    method = "format";
                }
                else if("-c".equals(arg))
                {
                    if(method != null)
                    {
                        System.err.println("参数错误. 请勿重复指定格式化参数.");
                        System.exit(2);
                    }
                    method = "compress";
                }
                else if("-v".equals(arg))
                {
                    method = "version";
                    break;
                }
                else
                {
                    try
                    {
                        var file = new File(arg).getCanonicalFile();
                        if(file.exists() && file.isFile())
                        {
                            setFiles.add(file);
                        }
                        else
                        {
                            System.err.println("文件不存在或不是文件: " + file);
                            System.exit(3);
                        }
                    }
                    catch (Exception e)
                    {
                        System.err.println("参数错误. 文件路径不合法: " + arg);
                        System.exit(3);
                    }
                }
            }
            if(method == null) method = "format";

            switch (method)
            {
                case "format", "compress" -> {
                    final boolean methodFormat = "format".equals(method);
                    setFiles.stream().parallel().forEach(file -> {
                        try
                        {
                            var om = new ObjectMapper();
                            ObjectWriter writer;
                            if(methodFormat)
                            {
                                var i = new DefaultIndenter("  ", DefaultIndenter.SYS_LF);
                                var printer = new DefaultPrettyPrinter();
                                printer.indentObjectsWith(i);
                                printer.indentArraysWith(i);
                                writer = om.writer(printer);
                            }
                            else
                            {
                                writer = om.writer();
                            }

                            var filename = file.getName().substring(0, file.getName().length() - 5)
                                    + (methodFormat ? ".format.json" : ".compressed.json");
                            var fileOutput = new File(file.getParentFile(), filename);

                            var json = om.readTree(file);
                            writer.writeValue(fileOutput, json);
                        }
                        catch (Exception any)
                        {
                            System.err.println("处理 JSON 出现错误");
                            any.printStackTrace(System.err);
                        }

                        var tsEnd = System.currentTimeMillis();
                        System.out.println("处理完成, 共 " + setFiles.size() + " 个文件, 耗时 " + (tsEnd - tsStart) + " ms");
                    });
                }
                case "version" -> {
                    System.out.println("Json Formatter tool v0.1.0 by Firok");
                    System.out.println("https://github.com/FirokOtaku/JsonFormatter");
                }
            }
        }

    }
}
