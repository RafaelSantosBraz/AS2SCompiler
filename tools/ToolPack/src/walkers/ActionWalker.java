/*
 * The MIT License - https://github.com/RafaelSantosBraz/AS2SCompiler/blob/master/LICENSE
 * Copyright 2020 Rafael Braz.
 */
package walkers;

import java.lang.reflect.Method;
import java.util.List;
import trees.cstecst.TokenAttributes;
import trees.simpletree.Node;
import trees.simpletree.Tree;

/**
 * Simulates ANTLR4 listeners for simple trees
 *
 * @author Rafael Braz
 */
public abstract class ActionWalker {

    // starts walking on the tree
    public void startWalking(Tree<TokenAttributes> tree) {
        if (tree.getRoot() != null) {
            visiting(tree.getRoot());
        }
    }

    // recursive method to visit all the nodes
    private void visiting(Node<TokenAttributes> node) {
        if (!callSpecializedAction(node)) {
            defaultAction(node);
        }
        visitingChildren(node);
    }

    private void visitingChildren(Node<TokenAttributes> node) {
        List<Node<TokenAttributes>> children = node.getChildren();
        if (children != null) {
            int prevHash = children.hashCode();
            for (int c = 0; c < children.size(); c++) {
                visiting(children.get(c));
            }
            if (prevHash != children.hashCode()) {
                visitingChildren(node);
            }
        }
    }

    // if there is a corresponding action, it will be executed
    private boolean callSpecializedAction(Node<TokenAttributes> node) {
        try {
            Method method = this.getClass().getDeclaredMethod(getMethodAppropriateName(node), node.getClass());
            method.invoke(this, node);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    // returns the name of the specialized action for the current node
    protected String getMethodAppropriateName(Node<TokenAttributes> node) {
        return "action" + node.getNodeData().getText();
    }

    // action to be executed if there is no specialized actions
    public void defaultAction(Node<TokenAttributes> node) {
        // does nothing
    }

}
