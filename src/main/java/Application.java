import org.eclipse.jdt.core.compiler.InvalidInputException;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.*;

public class Application {
    public static void main(String[] args) throws IOException, InvalidInputException, ParserConfigurationException, SAXException {
        String inputSourceCode;
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            int len;
            byte[] buffer = new byte[1024];
            while ((len = System.in.read(buffer)) > 0) {
                baos.write(buffer, 0, len);
            }
            inputSourceCode = baos.toString();
        }
        String outputSourceCode;
        Converter converter = new Converter();
        switch (args[0]) {
            case "-l":
                outputSourceCode = converter.convertSourceCode2TokenLines(inputSourceCode);
                break;
            case "-f":
                outputSourceCode = converter.format(inputSourceCode);
                break;
            default:
                throw new RuntimeException();
        }
        System.out.println(outputSourceCode);
        System.out.flush();
        System.out.close();
    }
}