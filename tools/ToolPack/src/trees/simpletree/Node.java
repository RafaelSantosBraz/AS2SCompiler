/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package trees.simpletree;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents each one of the nodes of a simple tree structure
 *
 * @author Rafael Braz
 * @param <T>
 */
public class Node<T> {

    private T nodeData; // represents the stored data 
    private Node<T> parent; // the parent node of the current node in the tree
    private List<Node<T>> children; // All the children nodes of the current node

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

    public void addChild(Node<T> child) {
        children.add(child);
    }

    public void addChildAt(Node<T> child, int index) {
        children.add(index, child);
    }

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

    // returns a clone of the CURRENT node and ITS data 
    public Node<T> getClone() {
        Node<T> newNode = new Node<>(parent);
        newNode.setChildren(children);
        newNode.setNodeData(nodeData);
        return newNode;
    }    
}
