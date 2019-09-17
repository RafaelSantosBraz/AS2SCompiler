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

    /**
     * Represents the stored data -- in CSTs and eCSTs it is going to be a
     * ConcreteToken or a UniversalToken
     */
    private T nodeData;
    /**
     * The parent node of the current node in the tree
     */
    private Node<T> parent;
    /**
     * All the children nodes of the current node
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

    public void addChild(Node<T> child) {
        children.add(child);
    }

    public void addChildren(List<Node<T>> children){
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
    
    public Node<T> getClone(){
        Node<T> newNode = new Node<>(parent);
        newNode.setChildren(children);
        newNode.setNodeData(nodeData);
        return newNode;
    }
}
