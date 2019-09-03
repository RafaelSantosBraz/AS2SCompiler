/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simpletree;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Rafael Braz
 * @param <T>
 */
public class Node<T> {

    private T nodeData;
    private Node<T> parent;
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

}
