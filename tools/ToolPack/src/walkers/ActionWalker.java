/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package walkers;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import trees.cstecst.TokenAttributes;
import trees.simpletree.Node;
import trees.simpletree.Tree;

/**
 *
 * @author Rafael Braz
 */
public abstract class ActionWalker {

    public void startWalking(Tree<TokenAttributes> tree) {
        if (tree.getRoot() != null) {
            visiting(tree.getRoot());
        }
    }

    private void visiting(Node<TokenAttributes> node) {
        if (!callSpecializedAction(node)) {
            defaultAction(node);
        }
        List<Node<TokenAttributes>> children = node.getChildren();
        if (children != null && !children.isEmpty()) {
            children.forEach((n) -> {
                visiting(n);
            });
        }
    }

    private boolean callSpecializedAction(Node<TokenAttributes> node) {        
        try {            
            Method method = this.getClass().getDeclaredMethod(getMethodAppropriateName(node), node.getClass()); 
            method.invoke(this, node);
            return true;
        } catch (Exception e) {
            return false;
        }        
    }

    protected String getMethodAppropriateName(Node<TokenAttributes> node){
        //char[] name = node.getNodeData().getText().toCharArray();
        //name[0] = Character.toUpperCase(name[0]);
        //return "action" + Arrays.toString(name);
        return "action" + node.getNodeData().getText();
    }
    
    public void defaultAction(Node<TokenAttributes> node) {
        // does nothing
    }

}
