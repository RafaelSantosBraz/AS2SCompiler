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
        //exibir(prog);
        //showParseTreeFrame(prog, parser);        
        ParseTreeWalker.DEFAULT.walk(new JavaGrammarBaseListener() {
            final String INDENT = "    ";
            int level = 0;

            @Override
            public void enterEveryRule(final ParserRuleContext ctx) {
                System.out.printf("%s<%s>%n", indent(), parser.getRuleNames()[ctx.getRuleIndex()]);
                ++level;
                super.enterEveryRule(ctx);
            }

            @Override
            public void exitEveryRule(final ParserRuleContext ctx) {
                --level;
                System.out.printf("%s</%s>%n", indent(), parser.getRuleNames()[ctx.getRuleIndex()]);
                super.exitEveryRule(ctx);
            }

            @Override
            public void visitTerminal(final TerminalNode node) {
                final String value = node.getText();
                if (!value.matches("\\s+")) {
                    System.out.printf("%s<t>%s</t>%n", indent(), node.getText());
                }
                super.visitTerminal(node);
            }

            private String indent() {
                return String.join("", Collections.nCopies(level, INDENT));
            }
        }, prog);
    }

    public static void exibir(ParseTree tree) {
        for (int c = 0; c < tree.getChildCount(); c++) {
            System.out.println(tree.getChild(c).getPayload());
            exibir(tree.getChild(c));
        }
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
