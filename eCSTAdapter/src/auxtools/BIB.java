/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package auxtools;

import java.util.List;
import trees.cstecst.TokenAttributes;
import trees.cstecst.UniversalToken;
import trees.simpletree.Node;

/**
 *
 * @author Rafael Braz
 */
public class BIB {

    public static Node<TokenAttributes> createNodeWithParent(Node<TokenAttributes> parent, String text) {
        Node<TokenAttributes> node = new Node<>(parent);
        node.setNodeData(new UniversalToken(text, -1));
        parent.addChild(node);
        return node;
    }

    public static Node<TokenAttributes> createNodeWithParent(Node<TokenAttributes> parent, String text, int index) {
        Node<TokenAttributes> node = new Node<>(parent);
        node.setNodeData(new UniversalToken(text, -1));
        parent.getChildren().add(index, node);
        return node;
    }

    public static Node<TokenAttributes> createNode(String text) {
        return new Node<>(new UniversalToken(text, -1));
    }

    public static void replaceNode(Node<TokenAttributes> originalNode, Node<TokenAttributes> newNode) {
        if (originalNode == null || newNode == null) {
            return;
        }
        Node<TokenAttributes> parent = originalNode.getParent();
        if (parent == null) {
            return;
        }
        List<Node<TokenAttributes>> parentChildren = parent.getChildren();
        if (parentChildren.isEmpty()) {
            parent.addChild(newNode);
        } else {
            int index = parentChildren.indexOf(originalNode);
            parentChildren.remove(index);
            parentChildren.add(index, newNode);
        }
    }

    public static void removeNode(Node<TokenAttributes> node) {
        if (node != null) {
            if (node.getParent() == null) {
                if (node.getChildren() != null && !node.getChildren().isEmpty()) {
                    node.getChildren().forEach((t) -> {
                        t.setParent(null);
                    });
                }
            } else {
                int index = node.getParent().getChildren().indexOf(node);
                node.getParent().getChildren().remove(node);
                if (node.getChildren() != null && !node.getChildren().isEmpty()) {
                    node.getChildren().forEach((t) -> {
                        t.setParent(node.getParent());
                        node.getParent().addChildAt(t, index);
                    });
                }
            }
        }
    }

    public static Node<TokenAttributes> getChildByText(List<Node<TokenAttributes>> set, String text) {
        for (Node<TokenAttributes> c : set) {
            if (c.getNodeData().getText().equals(text)) {
                return c;
            }
        }
        return null;
    }
}
