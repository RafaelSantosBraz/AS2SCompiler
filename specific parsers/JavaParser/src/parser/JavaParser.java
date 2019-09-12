/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package parser;

import converter.DOTConverter;
import cstecst.TokenAttributes;
import csttotree.CSTtoTree;
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
import simpletree.Tree;

/**
 *
 * @author Rafael Braz
 */
public class JavaParser {

    private final List<ParserRuleContext> rootRules;
    private final Tree<TokenAttributes> tree;

    public JavaParser() {
        rootRules = new ArrayList<>();
        tree = new Tree<>();
    }

    public boolean startParsing(String inputDir, String outputDir) {
        try {
            File directory = new File(inputDir);
            File[] files = directory.listFiles((File f) -> {
                return f.isFile() && f.getName().endsWith(".java");
            });
            for (File f : files) {
                if (!semanticChecking(f) || !syntaxChecking(f)) {
                    return false;
                }
            }
            treeUnion(getTrees());
            if (exportCSTDOT(tree, outputDir + "CST.gv")){
                return false;
            }          
            return true;
        } catch (RecognitionException e) {
            return false;
        }
    }

    private boolean semanticChecking(File f) {
        return true;
    }

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
        trees.forEach((t) -> {
            tree.getRoot().addChildren(t.getRoot().getChildren());
        });
    }

    private boolean exportCSTDOT(Tree<TokenAttributes> tree, String outputPath) {
        return new DOTConverter<>(tree).convertToFile(outputPath);
    }    
    
    public List<ParserRuleContext> getRootRules() {
        return rootRules;
    }

    public Tree<TokenAttributes> getTree() {
        return tree;
    }
    
    
}
