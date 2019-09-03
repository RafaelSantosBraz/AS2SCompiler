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
 */
public class Node {

    private NodeData nodeData;
    private Node parent;
    private List<Node> children;

    public Node() {
        nodeData = new NodeData();
        parent = null;
        children = new ArrayList<>();
    }

    public Node(NodeData nodeData) {
        this.nodeData = nodeData;
        parent = null;
        children = new ArrayList<>();
    }

    public NodeData getNodeData() {
        return nodeData;
    }

    public Node getParent() {
        return parent;
    }

    public List<Node> getChildren() {
        return children;
    }

    public void setNodeData(NodeData nodeData) {
        this.nodeData = nodeData;
    }

    public void setParent(Node parent) {
        this.parent = parent;
    }

    public void setChildren(List<Node> children) {
        this.children = children;
    }

}
