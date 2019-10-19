/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package adapters;

import trees.cstecst.TokenAttributes;
import trees.simpletree.Node;
import walkers.ActionWalker;

/**
 *
 * @author Rafael Braz
 */
public class CtoJavaAdapter extends ActionWalker {

    public void actionconst(Node<TokenAttributes> node) {
        System.out.println("funcionou " + node.getNodeData().getText());
    }
    
    public void actionroot(Node<TokenAttributes> node) {
        System.out.println("funcionou " + node.getNodeData().getText());
    }

    @Override
    public void defaultAction(Node<TokenAttributes> node) {
        System.out.println(":)");
    }
    
    
}
