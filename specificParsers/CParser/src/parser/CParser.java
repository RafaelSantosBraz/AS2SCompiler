/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package parser;

import converter.DOTConverter;
import converter.XMLConverter;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.antlr.v4.runtime.ANTLRFileStream;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.TokenStream;
import trees.cstecst.TokenAttributes;
import trees.cstecst.UniversalToken;
import trees.csttotree.CSTtoTree;
import trees.simpletree.Tree;

/**
 *
 * @author Rafael Braz
 */
public class CParser {

    private final List<ParserRuleContext> rootRules;
    private final Tree<TokenAttributes> tree;

    public CParser() {
        rootRules = new ArrayList<>();
        tree = new Tree<>(new UniversalToken("root", -1));
    }

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

    private ArrayList<Tree<TokenAttributes>> getTrees() {
        ArrayList<Tree<TokenAttributes>> trees = new ArrayList<>();
        rootRules.stream().map((r) -> {
            CSTtoTree conv = new CSTtoTree();
            conv.startVisiting(r, conv.getTree().getRoot());
            return conv;
        }).forEachOrdered((conv) -> {
            trees.add(conv.getTree());
        });
        return trees;
    }

    private void treeUnion(ArrayList<Tree<TokenAttributes>> trees) {
        //tree.setRoot(new Node<>(new UniversalToken("CompilationUnit", 31)));
        trees.forEach((t) -> {
            tree.getRoot().addChildren(t.getRoot().getChildren());
        });
    }

    private boolean exportCSTDOT(Tree<TokenAttributes> tree, String outputPath) {
        return new DOTConverter<>(tree).convertToFile(outputPath);
    }

    private boolean exportCSTXML(Tree<TokenAttributes> tree, String outputPath) {
        return new XMLConverter(tree).convertToFile(outputPath);
    }

    public List<ParserRuleContext> getRootRules() {
        return rootRules;
    }

    public Tree<TokenAttributes> getTree() {
        return tree;
    }

}
