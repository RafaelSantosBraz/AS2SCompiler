/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package codegenerators;

import trees.cstecst.TokenAttributes;
import trees.simpletree.Node;
import walkers.TreeVisitor;

/**
 *
 * @author Rafael Braz
 */
public class JavaGenerator extends TreeVisitor<Object> {
    
    public Object actionCOMPILATION_UNIT(Node<TokenAttributes> node) {
        System.out.println("Nani?");
        return visit(node.getChildren().get(0));
    }
    
    public Object actionFUNCTION_DECL(Node<TokenAttributes> node) {
        System.out.println("func?");
        return true;
    }
    
}
