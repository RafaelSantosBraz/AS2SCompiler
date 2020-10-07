/*
 * The MIT License - https://github.com/RafaelSantosBraz/AS2SCompiler/blob/master/LICENSE
 * Copyright 2020 Rafael Braz.
 */
package frontend.parsers;

import trees.converters.DOTConverter;
import trees.converters.XMLConverter;
import java.util.ArrayList;
import java.util.List;
import org.antlr.v4.runtime.ParserRuleContext;
import trees.TokenAttributes;
import trees.UniversalToken;
import trees.converters.CSTtoTree;
import trees.Tree;

/**
 * Abstract class to encapsule the methods and structures for a specific parser
 *
 * @author Rafael Braz
 */
public abstract class SpecificParser {

    /**
     * marks each of the "CompilationUnit" nodes.
     */
    protected final List<ParserRuleContext> rootRules;
    /**
     * the generated CST as simple tree.
     */
    protected final Tree<TokenAttributes> tree;

    public SpecificParser() {
        rootRules = new ArrayList<>();
        tree = new Tree<>(new UniversalToken("root", -1));
    }

    /**
     * starts parsing the source-code from a directory -> abstract method.
     *
     * @param inputDir
     * @param outputDir
     * @return
     */
    public abstract boolean startParsing(String inputDir, String outputDir);

    /**
     * parses all the "CompilationUnit" nodes and return the simple trees.
     *
     * @return
     */
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

    /**
     * puts all the trees together in a unique tree.
     *
     * @param trees
     */
    protected void treeUnion(ArrayList<Tree<TokenAttributes>> trees) {
        //tree.setRoot(new Node<>(new UniversalToken("CompilationUnit", 31)));
        trees.forEach((t) -> {
            tree.getRoot().addChildren(t.getRoot().getChildren());
        });
    }

    /**
     * exorts a DOT version of the given tree.
     *
     * @param tree
     * @param outputPath
     * @return
     */
    protected boolean exportCSTDOT(Tree<TokenAttributes> tree, String outputPath) {
        return new DOTConverter<>(tree).convertToFile(outputPath);
    }

    /**
     * exports a DOT version of the given tree.
     *
     * @param tree
     * @param outputPath
     * @return
     */
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
