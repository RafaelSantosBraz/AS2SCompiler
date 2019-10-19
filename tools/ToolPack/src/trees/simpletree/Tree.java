/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package trees.simpletree;

import java.util.ArrayList;
import java.util.List;

/**
 * Generic structure that can represent eCSTs, CSTs, and other tree structures
 *
 * @author Rafael Braz
 * @param <T>
 */
public class Tree<T> {

    private Node<T> root;

    public Tree() {
        root = new Node<>();
    }

    public Tree(T nodeData) {
        root = new Node<>(nodeData);
    }

    public Node<T> getRoot() {
        return root;
    }

    public void setRoot(Node<T> root) {
        this.root = root;
    }

    /**
     * Generates a new node and establishes parent-son relationship.
     *
     * @param parent
     * @return the generated node
     */
    public Node<T> createNode(Node<T> parent) {
        Node<T> currentNode = new Node<>(parent);
        if (parent != null) {
            parent.addChild(currentNode);
        }
        return currentNode;
    }

    /**
     * Method to convert the tree to a list of nodes ordered by index. It means
     * that nodes with the same level in the tree have consecutive indexes
     *
     * @return ArrayList of nodes ordered by index
     */
    public List<Node<T>> getTreeAsIndexOrderedList() {
        ArrayList<Node<T>> tempList = new ArrayList<>();
        tempList.add(root);
        return getNodeChildren(tempList);
    }

    /**
     * Recursive method to order all nodes in the tree
     *
     * @param tempList
     * @return ArrayList of ordered nodes by index
     */
    private ArrayList<Node<T>> getNodeChildren(ArrayList<Node<T>> tempList) {
        ArrayList<Node<T>> list = new ArrayList<>();
        ArrayList<Node<T>> newTempList = new ArrayList<>();
        list.addAll(tempList);
        tempList.forEach((t) -> {
            newTempList.addAll(t.getChildren());
        });
        if (!newTempList.isEmpty()) {
            list.addAll(getNodeChildren(newTempList));
        }
        return list;
    }

    public static void replaceNode(Node originalNode, Node newNode) {
        if (originalNode == null || newNode == null) {
            return;
        }
        Node parent = originalNode.getParent();
        if (parent == null) {
            return;
        }
        List<Node> parentChildren = parent.getChildren();
        if (parentChildren.isEmpty()) {
            parent.addChild(newNode);
        } else {
            int index = parentChildren.indexOf(originalNode);
            parentChildren.remove(index);
            parentChildren.add(index, newNode);
        }
    }
}
