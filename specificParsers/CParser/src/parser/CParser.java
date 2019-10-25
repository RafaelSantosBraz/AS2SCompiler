/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package parser;

import java.io.File;
import java.io.IOException;
import org.antlr.v4.runtime.ANTLRFileStream;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.TokenStream;
import parsers.SpecificParser;

/**
 * aims to analyze C code and generates a CST
 *
 * @author Rafael Braz
 */
public class CParser extends SpecificParser {

    // starts parsing the source-code from a directory
    @Override
    public boolean startParsing(String inputDir, String outputDir) {
        try {
            File directory = new File(inputDir);
            File outputDirectory = new File(outputDir);
            File[] files = directory.listFiles((File f) -> {
                return f.isFile() && (f.getName().endsWith(".c") || f.getName().endsWith(".h"));
            });
            for (File f : files) {
                if (!syntaxChecking(f)) {
                    return false;
                }
            }
            treeUnion(getTrees());
            return exportCSTDOT(tree, outputDirectory.getAbsolutePath() + File.separator + "CST.gv") && exportCSTXML(tree, outputDirectory.getAbsolutePath() + File.separator + "CST.xml");
        } catch (RecognitionException e) {
            return false;
        }
    }

    // syntactically check the code through the ANTLR4 recognition
    private boolean syntaxChecking(File f) {
        try {
            CharStream stream = new ANTLRFileStream(f.getAbsolutePath(), "UTF-8");
            CGrammarLexer lexer = new CGrammarLexer(stream);
            TokenStream tokens = new CommonTokenStream(lexer);
            CGrammarParser parser = new CGrammarParser(tokens);
            rootRules.add(parser.compilationUnit());
            return true;
        } catch (IOException | RecognitionException e) {
            return false;
        }
    }

}
