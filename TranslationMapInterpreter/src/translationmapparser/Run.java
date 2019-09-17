/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package translationmapparser;

import converter.TreeXMLConverter;
import interpreter.TranslationVisitor;
import java.awt.HeadlessException;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import org.antlr.v4.gui.TreeViewer;
import org.antlr.v4.runtime.ANTLRFileStream;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import parser.*;
import trees.cstecst.TokenAttributes;
import trees.simpletree.Tree;

/**
 *
 * @author Rafael Braz
 */
public class Run {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {
        CharStream stream = new ANTLRFileStream("test.tmap.txt");
        TranslationGrammarLexer lexer = new TranslationGrammarLexer(stream);
        TokenStream tokens = new CommonTokenStream(lexer);
        TranslationGrammarParser parser = new TranslationGrammarParser(tokens);
        TranslationGrammarParser.ProgContext prog
                = parser.prog();  
        TreeXMLConverter conv = new TreeXMLConverter();
        conv.convertFromFile("D:\\GitHub\\StS-Compilation-Framework\\specific parsers\\JavaParser\\test\\output\\CST.xml");
        TranslationParser t = new TranslationParser(conv.getTree());
        t.start("test.tmap.txt", "\"ruleinitial\"");
        System.out.println(t.geteCST());
        showParseTreeFrame(prog, parser);         
    }
 
    private static void showParseTreeFrame(ParseTree tree, TranslationGrammarParser parser) throws HeadlessException {
        JFrame frame = new JFrame("SRC: " + tree.getText());
        TreeViewer viewr = new TreeViewer(Arrays.asList(
                parser.getRuleNames()), tree);
        viewr.setScale(1);
        JPanel panel = new JPanel();
        panel.add(viewr);
        JScrollPane scroll = new JScrollPane(panel);
        scroll.setAutoscrolls(true);
        frame.add(scroll);
        frame.setSize(1000, 600);
        frame.setState(JFrame.MAXIMIZED_BOTH);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        //panelToImagem(panel);
    }
}
