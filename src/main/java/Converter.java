import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.ToolFactory;
import org.eclipse.jdt.core.compiler.InvalidInputException;
import org.eclipse.jdt.core.formatter.CodeFormatter;
import org.eclipse.jdt.internal.compiler.classfmt.ClassFileConstants;
import org.eclipse.jdt.internal.compiler.parser.Scanner;
import org.eclipse.jdt.internal.formatter.DefaultCodeFormatterOptions;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.Document;
import org.eclipse.jface.text.IDocument;
import org.eclipse.text.edits.MalformedTreeException;
import org.eclipse.text.edits.TextEdit;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.Objects;

import static org.eclipse.jdt.internal.compiler.parser.TerminalTokens.TokenNameEOF;

public class Converter {
    private final Path homePath = Paths.get(System.getProperty("user.home"));

    /**
     * Javaのソースコードをトークン毎の行リストに変換する
     */
    public String convertSourceCode2TokenLines(String source) throws InvalidInputException {
        Objects.nonNull(source);
        Scanner scanner = new Scanner();
        scanner.recordLineSeparator = true;
        scanner.sourceLevel = ClassFileConstants.JDK1_8;
        scanner.setSource(source.toCharArray());
        StringBuilder sb = new StringBuilder();
        while (scanner.getNextToken() != TokenNameEOF) {
            sb.append(scanner.getCurrentTokenString());
            sb.append('\n');
        }
        return sb.toString();
    }

    /**
     * Eclipseの標準のスタイルでソースコードをフォーマットする
     */
    public String format(String source) throws IOException, ParserConfigurationException, SAXException {
        Objects.nonNull(source);
        // take default Eclipse formatting options
        Map<String,String> options = DefaultCodeFormatterOptions.getEclipseDefaultSettings().getMap();
        // HOMEディレクトリにstyle.xmlがあれが、スタイル設定を読み込む
        Path styleXml = homePath.resolve("style.xml");
        if (Files.exists(styleXml)) {
            SAXParser parser = SAXParserFactory.newInstance().newSAXParser();
            parser.parse(styleXml.toFile(), new DefaultHandler() {
                @Override
                public void startElement(String uri, String localName, String qName, Attributes attributes) {
                    if (qName.equals("setting")) {
                        String id = attributes.getValue("id");
                        String value = attributes.getValue("value");
                        options.put(id, value);
                    }
                }
            });
        }
        // initialize the compiler settings to be able to format 1.8 code
        options.put(JavaCore.COMPILER_COMPLIANCE, JavaCore.VERSION_1_8);
        options.put(JavaCore.COMPILER_CODEGEN_TARGET_PLATFORM, JavaCore.VERSION_1_8);
        options.put(JavaCore.COMPILER_SOURCE, JavaCore.VERSION_1_8);

        // instantiate the default code formatter with the given options
        final CodeFormatter codeFormatter = ToolFactory.createCodeFormatter(options);

        // retrieve the source to format
        String separator = System.getProperty("line.separator");
        final TextEdit edit = codeFormatter.format(
                CodeFormatter.K_COMPILATION_UNIT, // format a compilation unit
                source, // source to format
                0, // starting position
                source.length(), // length
                0, // initial indentation
                separator // line separator
        );

        IDocument document = new Document(source);
        try {
            edit.apply(document);
        } catch (MalformedTreeException | BadLocationException e) {
            e.printStackTrace();
        }

        return document.get();
    }

    public static void main(String[] args) throws IOException, ParserConfigurationException, SAXException {
        Converter converter = new Converter();
        String source = String.join(" ", Files.readAllLines(Paths.get(args[0])));
        System.out.println(converter.format(source));
    }
}