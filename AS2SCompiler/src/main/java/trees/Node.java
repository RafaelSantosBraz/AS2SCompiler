/*
 * The MIT License - https://github.com/RafaelSantosBraz/AS2SCompiler/blob/master/LICENSE
 * Copyright 2020 Rafael Braz.
 */
package trees;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents each one of the nodes of a simple tree structure
 *
 * @author Rafael Braz
 * @param <T>
 */
public class Node<T> {

    /**
     * represents the stored data.
     */
    private T nodeData;
    /**
     * the parent node of the current node in the tree.
     */
    private Node<T> parent;
    /**
     * all the children nodes of the current node.
     */
    private List<Node<T>> children;

    public Node() {
        nodeData = null;
        parent = null;
        children = new ArrayList<>();
    }

    public Node(T nodeData) {
        this.nodeData = nodeData;
        parent = null;
        children = new ArrayList<>();
    }

    public Node(Node<T> parent) {
        nodeData = null;
        this.parent = parent;
        children = new ArrayList<>();
    }

    /**
     * add a given child to the children list.
     *
     * @param child
     */
    public void addChild(Node<T> child) {
        children.add(child);
    }

    /**
     * add a given child to the children list at a given index.
     *
     * @param child
     * @param index
     */
    public void addChildAt(Node<T> child, int index) {
        children.add(index, child);
    }

    /**
     * add a given child list.
     *
     * @param children
     */
    public void addChildren(List<Node<T>> children) {
        this.children.addAll(children);
    }

    public T getNodeData() {
        return nodeData;
    }

    public Node<T> getParent() {
        return parent;
    }

    public List<Node<T>> getChildren() {
        return children;
    }

    public void setNodeData(T nodeData) {
        this.nodeData = nodeData;
    }

    public void setParent(Node<T> parent) {
        this.parent = parent;
    }

    public void setChildren(List<Node<T>> children) {
        this.children = children;
    }

    /**
     * returns a clone of the CURRENT node and ITS data .
     *
     * @return
     */
    public Node<T> getClone() {
        Node<T> newNode = new Node<>(parent);
        newNode.setChildren(children);
        newNode.setNodeData(nodeData);
        return newNode;
    }

    /**
     * returns a clone of the CURRENT node, ITS data and clones of ALL its
     * children.
     *
     * @return
     */
    public Node<T> getChainClone() {
        Node<T> newNode = new Node<>(parent);
        List<Node<T>> newChildren = new ArrayList<>();
        children.forEach((t) -> {
            newChildren.add(t.getChainClone());
        });
        newNode.setChildren(newChildren);
        newNode.setNodeData(nodeData);
        return newNode;
    }
}
