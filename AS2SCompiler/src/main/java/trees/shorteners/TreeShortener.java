/*
 * The MIT License - https://github.com/RafaelSantosBraz/AS2SCompiler/blob/master/LICENSE
 * Copyright 2020 Rafael Braz.
 */
package trees.shorteners;

import auxtools.BIB;
import trees.*;

/**
 * aims to create a shorter version of tree (CST or eCST)
 *
 * @author Rafael Braz
 */
public class TreeShortener {

    /**
     * converts a given tree into a shorten version (without chains of nodes
     * that have just one child).
     *
     * @param tree
     * @return
     */
    public Tree<TokenAttributes> shortenTree(Tree<TokenAttributes> tree) {
        Tree<TokenAttributes> resTree = new Tree<>();
        resTree.setRoot(tree.getRoot().getChainClone());
        startShorten(resTree.getRoot());
        return resTree;
    }

    /**
     * recursively shorten the nodes of the tree.
     *
     * @param node node to start shorten.
     */
    private void startShorten(Node<TokenAttributes> node) {
        if (node.getChildren().size() == 1) {
            Node<TokenAttributes> last = BIB.tmapOneRuleCodeCall("last", node).get(0);
            if (!BIB.getText(node.getChildren().get(0)).equals(BIB.getText(last))) {
                Node<TokenAttributes> dots = new Node<>(node);
                dots.setNodeData(new UniversalToken("...", -1));
                dots.getChildren().add(last);
                node.getChildren().clear();
                node.getChildren().add(dots);
                last.setParent(dots);
            }
        }
        node.getChildren().forEach((t) -> {
            startShorten(t);
        });
    }

}
