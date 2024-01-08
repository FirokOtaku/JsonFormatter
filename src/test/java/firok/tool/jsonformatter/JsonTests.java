package firok.tool.jsonformatter;

import com.fasterxml.jackson.core.util.DefaultIndenter;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.core.util.Separators;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

public class JsonTests
{
//    @Test
    public void test() throws Exception
    {
        var om = new ObjectMapper();
        DefaultPrettyPrinter.Indenter i = new DefaultIndenter("  ", DefaultIndenter.SYS_LF);
        var printer = new DefaultPrettyPrinter();
        printer.indentObjectsWith(i);
        printer.indentArraysWith(i);

        var json = om.readTree("""
                { "a" : 123, "b": "abc",
                 "list": [
                 { "a": 1, "b": 2 },
                 { "a": 1, "b": 2 },
                 { "a": 1, "b": 2 },
                 { "a": 1, "b": 2 }
                 ]
                }
                """);
        var str = om.writer(printer).writeValueAsString(json);
        System.out.println(str);
    }
}
