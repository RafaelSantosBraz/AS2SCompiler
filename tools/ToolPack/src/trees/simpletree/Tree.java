/*
 * The MIT License - https://github.com/RafaelSantosBraz/AS2SCompiler/blob/master/LICENSE
 * Copyright 2020 Rafael Braz.
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

    private Node<T> root; // the first node of the tree

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

    // generates a new node and establishes parent-son relationship.
    public Node<T> createNode(Node<T> parent) {
        Node<T> currentNode = new Node<>(parent);
        if (parent != null) {
            parent.addChild(currentNode);
        }
        return currentNode;
    }

    // Method to convert the tree to a list of nodes ordered by index. 
    // It means that nodes with the same level in the tree have consecutive indexes
    public List<Node<T>> getTreeAsIndexOrderedList() {
        ArrayList<Node<T>> tempList = new ArrayList<>();
        tempList.add(root);
        return getNodeChildren(tempList);
    }

    // recursive ordenation of the nodes
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
}
