/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simpletree;

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

}
