/*
 * The MIT License - https://github.com/RafaelSantosBraz/AS2SCompiler/blob/master/LICENSE
 * Copyright 2020 Rafael Braz.
 */
package parsers;

import converter.DOTConverter;
import converter.XMLConverter;
import java.util.ArrayList;
import java.util.List;
import org.antlr.v4.runtime.ParserRuleContext;
import trees.cstecst.TokenAttributes;
import trees.cstecst.UniversalToken;
import trees.csttotree.CSTtoTree;
import trees.simpletree.Tree;

/**
 * Abstract class to encapsule the methods and structures for a specific parser
 *
 * @author Rafael Braz
 */
public abstract class SpecificParser {

    protected final List<ParserRuleContext> rootRules; // marks each of the "CompilationUnit" nodes
    protected final Tree<TokenAttributes> tree; // the generated CST as simple tree

    public SpecificParser() {
        rootRules = new ArrayList<>();
        tree = new Tree<>(new UniversalToken("root", -1));
    }

    // starts parsing the source-code from a directory -> abstract method
    public abstract boolean startParsing(String inputDir, String outputDir);

    // parses all the "CompilationUnit" nodes and return the simple trees
    protected ArrayList<Tree<TokenAttributes>> getTrees() {
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

    // puts all the trees together in a unique tree
    protected void treeUnion(ArrayList<Tree<TokenAttributes>> trees) {
        //tree.setRoot(new Node<>(new UniversalToken("CompilationUnit", 31)));
        trees.forEach((t) -> {
            tree.getRoot().addChildren(t.getRoot().getChildren());
        });
    }

    protected boolean exportCSTDOT(Tree<TokenAttributes> tree, String outputPath) {
        return new DOTConverter<>(tree).convertToFile(outputPath);
    }

    protected boolean exportCSTXML(Tree<TokenAttributes> tree, String outputPath) {
        return new XMLConverter(tree).convertToFile(outputPath);
    }

    public List<ParserRuleContext> getRootRules() {
        return rootRules;
    }

    public Tree<TokenAttributes> getTree() {
        return tree;
    }
}
