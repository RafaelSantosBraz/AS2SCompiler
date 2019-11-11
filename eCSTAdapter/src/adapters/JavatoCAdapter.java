/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package adapters;

import auxtools.BIB;
import java.util.List;
import trees.cstecst.TokenAttributes;
import trees.simpletree.Node;
import walkers.ActionWalker;

/**
 * aims to adapt a eCST generated from Java code to a C eCST
 *
 * @author Rafael Braz
 */
public class JavatoCAdapter extends ActionWalker {

    private final String auxTmapsDir; // path to all files of partial/complete tmap code

    public JavatoCAdapter(String auxTmapsDir) {
        this.auxTmapsDir = auxTmapsDir;
    }

    // Java array to C array format -- parameters
    public void actionPARAMETER_DECL(Node<TokenAttributes> node) {
        List<Node<TokenAttributes>> children = node.getChildren();
        if (children.size() != 3 && !BIB.getText(children.get(children.size() - 1)).equals("SEPARATOR")) {
            Node<TokenAttributes> last = children.remove(children.size() - 1);
            children.add(2, last);
        }
    }

    // adding C includes
    public void actionPACKAGE_DECL(Node<TokenAttributes> node) {
        String code = BIB.getTmapCodeFromFile(auxTmapsDir, "importJavatoC.tmap");
        List<Node<TokenAttributes>> nodes = BIB.tmapOneRuleCodeCall(code, node);
        nodes.forEach((t) -> {
            t.setParent(node);
        });
        node.getChildren().addAll(0, nodes);
    }

    // Removing Java imports
    public void actionIMPORT_DECL(Node<TokenAttributes> node) {
        String s = BIB.getText(node.getChildren().get(0).getChildren().get(0));
        if (!(s.equals("stdlib.h") || s.equals("stdio.h"))) {
            BIB.removeChain(node);
        }
    }

    // removing modifiers
    public void actionMODIFIER_LIST(Node<TokenAttributes> node) {
        node.getChildren().clear();
    }

    // adapting functions 
    public void actionFUNCTION_DECL(Node<TokenAttributes> node) {
        
    }

    // true to 1 
    public void actiontrue(Node<TokenAttributes> node) {
        node.getNodeData().setText("1");
    }

    // false to 0 
    public void actionfalse(Node<TokenAttributes> node) {
        node.getNodeData().setText("0");
    }

    // boolean to int
    public void actionboolean(Node<TokenAttributes> node) {
        node.getNodeData().setText("int");
    }
    
    
}
