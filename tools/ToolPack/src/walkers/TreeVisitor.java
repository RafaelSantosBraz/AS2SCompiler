/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package walkers;

import java.lang.reflect.Method;
import java.util.List;
import trees.cstecst.TokenAttributes;
import trees.simpletree.Node;
import trees.simpletree.Tree;

/**
 * Simulates ANTLR4 visitors for simple trees
 *
 * @author Rafael Braz
 * @param <T>
 */
public abstract class TreeVisitor<T> {

    // starts visiting the nodes
    public T startWalking(Tree<TokenAttributes> tree) {
        if (tree.getRoot() != null) {
            return visit(tree.getRoot());
        }
        return null;
    }

    // method called for all the nodes
    protected T visit(Node<TokenAttributes> node) {
        return callSpecializedAction(node);
    }

    // if there is a corresponding action, it will be executed
    private T callSpecializedAction(Node<TokenAttributes> node) {
        try {
            Method method = this.getClass().getDeclaredMethod(getMethodAppropriateName(node), node.getClass());
            return (T) method.invoke(this, node);
        } catch (Exception e) {
            List<Node<TokenAttributes>> children = node.getChildren();
            if (children == null || children.isEmpty()) {
                return defaultTermAction(node);
            }
            return defaultNonTermAction(node);
        }
    }

    // returns the name of the specialized action for the current node
    protected String getMethodAppropriateName(Node<TokenAttributes> node) {
        return "action" + node.getNodeData().getText();
    }

    // default action for terminal nodes (leafs)
    public T defaultTermAction(Node<TokenAttributes> node) {
        return null;
    }

    // default action for non-terminal nodes
    public T defaultNonTermAction(Node<TokenAttributes> node) {
        return visitChildren(node.getChildren());
    }

    // method to visit each one of the children
    protected T visitChildren(List<Node<TokenAttributes>> children) {
        if (children == null) {
            return null;
        }
        T result = null;
        for (int c = 0; c < children.size(); c++) {
            result = aggregateResult(result, visit(children.get(c)));
        }
        return result;
    }

    // aggregate the results os all the children nodes (two at a time)
    protected T aggregateResult(T aggregate, T nextResult) {
        if (aggregate == null && nextResult == null) {
            return null;
        }
        if (nextResult == null) {
            return aggregate;
        }
        return nextResult;
    }

}
