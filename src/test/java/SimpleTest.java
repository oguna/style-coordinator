import org.eclipse.jdt.core.compiler.InvalidInputException;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class SimpleTest {

    private static String loadSource(String name) throws IOException {
        String content;
        try (InputStream is = SimpleTest.class.getResourceAsStream(name);
             InputStreamReader isr = new InputStreamReader(is)) {
            StringBuilder sb = new StringBuilder();
            char[] buffer = new char[1024];
            int length;
            while ((length = isr.read(buffer, 0, buffer.length)) >= 0) {
                sb.append(buffer, 0, length);
            }
            content = sb.toString();
        }
        return content;
    }

    @Test
    public void test1() throws IOException, InvalidInputException {
        String source = loadSource("/b.java");
        String actual = ApplicationEx.breakStyle(source);
        String expected = "package org . apache . commons . lang3 ; public class StringUtils { public static final String LF = \"\\n\" ; public static final String CR = \"\\r\" ; } ";
        Assert.assertEquals(expected, actual);
    }
}
