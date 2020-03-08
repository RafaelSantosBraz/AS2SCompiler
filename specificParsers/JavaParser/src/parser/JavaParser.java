/*
 * The MIT License - https://github.com/RafaelSantosBraz/AS2SCompiler/blob/master/LICENSE
 * Copyright 2020 Rafael Braz.
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
 * aims to analyze Java code and generates a CST
 *
 * @author Rafael Braz
 */
public class JavaParser extends SpecificParser {

    /**
     * starts parsing the source-code from a directory.
     * @param inputDir
     * @param outputDir
     * @return 
     */
    @Override
    public boolean startParsing(String inputDir, String outputDir) {
        try {
            File directory = new File(inputDir);
            File outputDirectory = new File(outputDir);
            File[] files = directory.listFiles((File f) -> {
                return f.isFile() && f.getName().endsWith(".java");
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

    /**
     * syntactically check the code through the ANTLR4 recognition
     * @param f file to be verified.
     * @return 
     */
    private boolean syntaxChecking(File f) {
        try {
            CharStream stream = new ANTLRFileStream(f.getAbsolutePath(), "UTF-8");
            JavaGrammarLexer lexer = new JavaGrammarLexer(stream);
            TokenStream tokens = new CommonTokenStream(lexer);
            JavaGrammarParser parser = new JavaGrammarParser(tokens);
            rootRules.add(parser.compilationUnit());
            return true;
        } catch (IOException | RecognitionException e) {
            return false;
        }
    }

}
