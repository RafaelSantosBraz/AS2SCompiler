/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package adapters;

import trees.cstecst.TokenAttributes;
import trees.cstecst.UniversalToken;
import trees.simpletree.Node;
import trees.simpletree.Tree;
import walkers.ActionWalker;

/**
 *
 * @author Rafael Braz
 */
public class CtoJavaAdapter extends ActionWalker {
    
    public void actionconst(Node<TokenAttributes> node) {
        Node<TokenAttributes> newNode = new Node<>(node.getParent());
        newNode.setNodeData(new UniversalToken("final", -1));
        newNode.setChildren(node.getChildren());
        Tree.replaceNode(node, newNode);
    }
    
}
