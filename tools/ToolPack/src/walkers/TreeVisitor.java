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
 *
 * @author Rafael Braz
 * @param <T>
 */
public abstract class TreeVisitor<T> {

    public T startWalking(Tree<TokenAttributes> tree) {
        if (tree.getRoot() != null) {
            return visit(tree.getRoot());
        }
        return null;
    }

    protected T visit(Node<TokenAttributes> node) {
        return callSpecializedAction(node);
    }

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

    protected String getMethodAppropriateName(Node<TokenAttributes> node) {
        return "action" + node.getNodeData().getText();
    }

    public T defaultTermAction(Node<TokenAttributes> node) {
        return null;
    }

    public T defaultNonTermAction(Node<TokenAttributes> node) {
        return visitChildren(node.getChildren());
    }

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
