/*
 * The MIT License - https://github.com/RafaelSantosBraz/AS2SCompiler/blob/master/LICENSE
 * Copyright 2020 Rafael Braz.
 */
package parser;

import auxtools.BIB;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.antlr.v4.runtime.ANTLRFileStream;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.TokenStream;
import parsers.SpecificParser;
import trees.cstecst.TokenAttributes;
import trees.simpletree.Node;

/**
 * aims to analyze C code and generates a CST
 *
 * @author Rafael Braz
 */
public class CParser extends SpecificParser {

    /**
     * Starts parsing the source-code from a directory.
     *
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
                return f.isFile() && (f.getName().endsWith(".c") || f.getName().endsWith(".h"));
            });
            for (File f : files) {
                if (!syntaxChecking(f)) {
                    return false;
                }
            }
            treeUnion(getTrees());
            addCompilationUnitNames(files);
            correctIncludes();
            return exportCSTDOT(tree, outputDirectory.getAbsolutePath() + File.separator + "CST.gv") && exportCSTXML(tree, outputDirectory.getAbsolutePath() + File.separator + "CST.xml");
        } catch (RecognitionException e) {
            return false;
        }
    }

    /**
     * syntactically check the code through the ANTLR4 recognition.
     *
     * @param f file to be verified.
     * @return
     */
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

    /**
     * create a node for the file name.
     *
     * @param files all the files that have been parsed.
     */
    private void addCompilationUnitNames(File files[]) {
        List<Node<TokenAttributes>> comps = tree.getRoot().getChildren();
        for (int c = 0; c < files.length; c++) {
            File f = files[c];
            String name = f.getName().substring(0, f.getName().lastIndexOf('.'));
            Node<TokenAttributes> node = BIB.tmapOneRuleCodeCall("\"NAME\"={new_leaf(\"" + name + "\")}", null).get(0);
            node.setParent(comps.get(c));
            comps.get(c).addChildAt(node, 0);
        }
    }

    /**
     * rewrite all the 'include' String lines into tree-style elements.
     */
    private void correctIncludes() {
        List<Node<TokenAttributes>> comps = tree.getRoot().getChildren();
        comps.forEach((t) -> {
            Node<TokenAttributes> includes = BIB.getChildByText(t.getChildren(), "Includes");
            List<Node<TokenAttributes>> newNodes = new ArrayList<>();
            if (includes != null) {
                List<Node<TokenAttributes>> children = includes.getChildren();
                for (Node<TokenAttributes> inc : children) {
                    String s = inc.getNodeData().getText();
                    Node<TokenAttributes> node;
                    if (s.contains("<")) {
                        String name = s.substring(s.indexOf('<') + 1, s.indexOf('>'));
                        node = BIB.tmapOneRuleCodeCall("\"Include\" = {new_leaf(\"<\"), new_leaf(\"" + name + "\"), new_leaf(\">\")}", null).get(0);
                    } else {
                        String name = s.substring(s.indexOf('"') + 1, s.lastIndexOf('"'));
                        node = BIB.tmapOneRuleCodeCall("\"Include\" = {new_leaf(\"'\"), new_leaf(\"" + name + "\"), new_leaf(\"'\")}", null).get(0);
                    }
                    newNodes.add(node);
                }
                includes.setChildren(newNodes);
                newNodes.forEach((i) -> {
                    i.setParent(includes);
                });
            }
        });
    }

}
