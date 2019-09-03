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

    public Node<T> createNode(Node<T> parent) {
        Node<T> currentNode = new Node<>(parent);
        if (parent != null) {
            parent.addChild(currentNode);
        }
        return currentNode;
    }

    private int getNodeIndexNumber(Node<T> node) {
        ArrayList<Node<T>> list = (ArrayList<Node<T>>) getTreeAsIndexOrderedList();
        return list.indexOf(node) + 1;
    }

    public List<Node<T>> getTreeAsIndexOrderedList() {
        ArrayList<Node<T>> tempList = new ArrayList<>();
        tempList.add(root);
        return getNodeChildren(tempList);
    }

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
