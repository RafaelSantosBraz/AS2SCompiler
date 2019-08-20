/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javagrammar;

import java.awt.HeadlessException;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import javax.swing.JFrame;
import javax.swing.JPanel;
import org.antlr.v4.gui.TreeViewer;
import org.antlr.v4.runtime.ANTLRFileStream;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.antlr.v4.runtime.tree.TerminalNode;
import parser.*;
import xmlgenerator.CSTtoXMLConverter;

/**
 *
 * @author Rafael Braz
 */
public class Run {

    public static void main(String[] args) throws IOException {
        String filename = "test.java";
        CharStream stream = new ANTLRFileStream(filename);
        JavaGrammarLexer lexer = new JavaGrammarLexer(stream);            //Lexer
        TokenStream tokens = new CommonTokenStream(lexer);  //nextToken 
        JavaGrammarParser parser = new JavaGrammarParser(tokens);         //Parser
        JavaGrammarParser.CompilationUnitContext prog
                = parser.compilationUnit();        //Exec Parser prog        
        showParseTreeFrame(prog, parser);  
        CSTtoXMLConverter.generateXMLFileVersion(parser, prog, "result.xml");
    }

    private static void showParseTreeFrame(ParseTree tree, JavaGrammarParser parser) throws HeadlessException {
        JFrame frame = new JFrame("SRC: " + tree.getText());
        JPanel panel = new JPanel();
        TreeViewer viewr = new TreeViewer(Arrays.asList(
                parser.getRuleNames()), tree);
        viewr.setScale(1);
        panel.add(viewr);
        frame.add(panel);
        frame.setSize(1000, 600);
        frame.setState(JFrame.MAXIMIZED_HORIZ);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}
