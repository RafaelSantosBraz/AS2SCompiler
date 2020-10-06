/*
 * The MIT License - https://github.com/RafaelSantosBraz/AS2SCompiler/blob/master/LICENSE
 * Copyright 2020 Rafael Braz.
 */
package walkers;

import java.lang.reflect.Method;
import java.util.List;
import trees.*;

/**
 * Simulates ANTLR4 visitors for simple trees
 *
 * @author Rafael Braz
 * @param <T>
 */
public abstract class TreeVisitor<T> {

    /**
     * starts visiting the nodes.
     *
     * @param tree
     * @return
     */
    public T startWalking(Tree<TokenAttributes> tree) {
        if (tree.getRoot() != null) {
            return visit(tree.getRoot());
        }
        return null;
    }

    /**
     * method called for all the nodes.
     *
     * @param node
     * @return
     */
    protected T visit(Node<TokenAttributes> node) {
        return callSpecializedAction(node);
    }

    /**
     * if there is a corresponding action, it will be executed.
     *
     * @param node
     * @return
     */
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

    /**
     * returns the name of the specialized action for the current node.
     *
     * @param node
     * @return
     */
    protected String getMethodAppropriateName(Node<TokenAttributes> node) {
        return "action" + node.getNodeData().getText();
    }

    /**
     * default action for terminal nodes (leafs).
     *
     * @param node
     * @return
     */
    public T defaultTermAction(Node<TokenAttributes> node) {
        return null;
    }

    /**
     * default action for non-terminal nodes.
     *
     * @param node
     * @return
     */
    public T defaultNonTermAction(Node<TokenAttributes> node) {
        return visitChildren(node.getChildren());
    }

    /**
     * method to visit each one of the children.
     *
     * @param children
     * @return
     */
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

    /**
     * aggregate the results os all the children nodes (two at a time).
     *
     * @param aggregate first result.
     * @param nextResult second result.
     * @return
     */
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
