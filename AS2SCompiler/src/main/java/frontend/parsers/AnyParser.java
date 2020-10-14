/*
 * The MIT License - https://github.com/RafaelSantosBraz/AS2SCompiler/blob/master/LICENSE
 * Copyright 2020 Rafael Braz.
 */
package frontend.parsers;

import auxtools.JSONHandler;
import auxtools.TreeStringBuilder;
import configuration.Configuration;
import frontend.parsers.visitors.CVisitor;
import java.io.File;
import java.io.IOException;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.RecognitionException;
import org.apache.commons.io.FilenameUtils;
import org.json.JSONObject;

/**
 * Parsers C and Java into CSTs and starts buinding the eCSTs.
 *
 * @author Rafael Braz
 */
public class AnyParser {

    /**
     * start parsing a given input file.
     *
     * @param file input file.
     * @return the eCST StringBuilder for the input file.
     */
    public static TreeStringBuilder parseFile(File file) {
        var ctx = syntaxChecking(file);
        expose(ctx, file);
        switch (Configuration.INPUT_LANGUAGE) {
            case Configuration.C:
                return new CVisitor().start(ctx);
            case Configuration.JAVA:
                // put Java
                return null;
        }
        return null;
    }

    /**
     * exports the IR files.
     *
     * @param ctx initial ParserRuleContext;
     * @param file parsed file.
     */
    public static void expose(ParserRuleContext ctx, File file) {
        if (Configuration.EXPOSE_ANY) {
            JSONObject json = JSONHandler.antlrToJson(ctx);
            if (Configuration.EXPOSE_JSON) {
                JSONHandler.writeToFileJSON(
                        Configuration.CST_DIR,
                        String.format("%s.json", FilenameUtils.removeExtension(file.getName())),
                        json);
            }
            if (Configuration.EXPOSE_XML) {
                JSONHandler.writeToFileXML(
                        Configuration.CST_DIR,
                        String.format("%s.xml", FilenameUtils.removeExtension(file.getName())),
                        json);
            }
            if (Configuration.EXPOSE_DOT) {
                JSONHandler.writeToFileDOT(
                        Configuration.CST_DIR,
                        String.format("%s.dot", FilenameUtils.removeExtension(file.getName())),
                        json);
            }
        }
    }

    /**
     * syntactically check the code through the ANTLR4 recognition.
     *
     * @param f file to be verified.
     * @return the initial ParserRuleContext. System exits with failure.
     */
    private static ParserRuleContext syntaxChecking(File f) {
        try {
            var stream = CharStreams.fromFileName(f.getAbsolutePath());
            switch (Configuration.INPUT_LANGUAGE) {
                case Configuration.C: {
                    var lexer = new CGrammarLexer(stream);
                    var tokens = new CommonTokenStream(lexer);
                    var parser = new CGrammarParser(tokens);
                    return parser.compilationUnit();
                }
                case Configuration.JAVA: {
                    var lexer = new JavaGrammarLexer(stream);
                    var tokens = new CommonTokenStream(lexer);
                    var parser = new JavaGrammarParser(tokens);
                    return parser.compilationUnit();
                }
            }
        } catch (IOException e) {
            System.err.println(String.format("It was not possible to read file '%s'! %s",
                    f.getAbsolutePath(), e.getMessage()));
        } catch (RecognitionException e) {
            System.err.println(String.format("Recognition error in file '%s'! %s",
                    f.getAbsolutePath(), e.getMessage()));
        }
        System.exit(1);
        return null;
    }
}
