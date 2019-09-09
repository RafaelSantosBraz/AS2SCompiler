/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package parser;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import org.antlr.v4.runtime.ANTLRFileStream;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.TokenStream;

/**
 *
 * @author Rafael Braz
 */
public class JavaParser {

    public ParserRuleContext parsingFiles(String inputDir) {
        try {  
            File directory = new File(inputDir);            
            File[] files = directory.listFiles((File f) -> {
                return f.isFile() && f.getName().endsWith(".java");
            });
            for (File f : files){
                
            }
            CharStream stream = new ANTLRFileStream("exemplo.java");
            JavaGrammarLexer lexer = new JavaGrammarLexer(stream);
            TokenStream tokens = new CommonTokenStream(lexer);
            JavaGrammarParser parser = new JavaGrammarParser(tokens);
            JavaGrammarParser.CompilationUnitContext prog
                    = parser.compilationUnit();
            return prog;
        } catch (IOException | RecognitionException e) {
            return null;
        }
    }

}
