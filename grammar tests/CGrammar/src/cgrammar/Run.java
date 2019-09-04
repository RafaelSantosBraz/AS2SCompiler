/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cgrammar;

import csttotree.CSTtoTree;
import java.awt.Graphics;
import java.awt.HeadlessException;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import javax.imageio.ImageIO;
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

/**
 *
 * @author Rafael Braz
 */
public class Run {

    public static void main(String[] args) throws IOException {
        CharStream stream = new ANTLRFileStream("test.c");
        CGrammarLexer lexer = new CGrammarLexer(stream);
        TokenStream tokens = new CommonTokenStream(lexer);
        CGrammarParser parser = new CGrammarParser(tokens);
        CGrammarParser.CompilationUnitContext prog
                = parser.compilationUnit();
        showParseTreeFrame(prog, parser);     
        CSTtoTree conv = new CSTtoTree();
        conv.startVisiting(prog, conv.getTree().getRoot());
    }

    private static void showParseTreeFrame(ParseTree tree, CGrammarParser parser) throws HeadlessException {
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
        panelToImagem(panel);
    }

    private static void panelToImagem(JPanel panel) {
        BufferedImage image = new BufferedImage(panel.getWidth(), panel.getHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics g = image.createGraphics();
        panel.paint(g);
        g.dispose();
        try {
            ImageIO.write(image, "png", new File("CST.png"));
        } catch (Exception e) {
        }
    }
}
