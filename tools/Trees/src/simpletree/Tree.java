/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simpletree;

/**
 *
 * @author Rafael Braz
 */
public class Tree {

    private Node root;

    public Tree() {
        root = new Node();
    }

    public Tree(NodeData nodeData) {
        root = new Node(nodeData);
    }

    public Node getRoot() {
        return root;
    }

    public void setRoot(Node root) {
        this.root = root;
    }

}
