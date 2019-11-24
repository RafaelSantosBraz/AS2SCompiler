/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shorteners;

import auxtools.BIB;
import trees.cstecst.TokenAttributes;
import trees.cstecst.UniversalToken;
import trees.simpletree.Node;
import trees.simpletree.Tree;

/**
 * aims to create a shorter version of tree (CST or eCST)
 *
 * @author Rafael Braz
 */
public class TreeShortener {

    // converts a given tree into a shorten version (without chains of nodes that have just one child)
    public Tree<TokenAttributes> shortenTree(Tree<TokenAttributes> tree) {
        Tree<TokenAttributes> resTree = new Tree<>();
        resTree.setRoot(tree.getRoot().getChainClone());
        startShorten(resTree.getRoot());
        return resTree;
    }

    // recursively shorten the nodes of the tree
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
