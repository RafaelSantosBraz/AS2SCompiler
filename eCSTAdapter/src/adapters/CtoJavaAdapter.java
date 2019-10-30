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
 * aims to adapt a eCST generated from C code to a Java eCST
 *
 * @author Rafael Braz
 */
public class CtoJavaAdapter extends ActionWalker {

    private final String auxTmapsDir; // path to all files of partial/complete tmap code

    public CtoJavaAdapter(String auxTmapsDir) {
        this.auxTmapsDir = auxTmapsDir;
    }

    // const to final
    public void actionconst(Node<TokenAttributes> node) {
        node.getNodeData().setText("final");
    }

    // function main to method main
    public void actionFUNCTION_DECL(Node<TokenAttributes> node) {
        String tmapCode = BIB.getTmapCodeFromFile(auxTmapsDir, "mainCtoJava.tmap");
        List<Node<TokenAttributes>> nodes = BIB.tmapOneRuleCodeCall(tmapCode, node);
        BIB.replaceNode(node, nodes.get(0));
    }

    // remove C double TYPE
    public void actionTYPE(Node<TokenAttributes> node) {
        if (node.getChildren().get(0).getNodeData().getText().equals("TYPE")) {
            BIB.removeNode(node);
        }
    }

    // C printf to Java System.out.printf
    public void actionFUNCTION_CAL(Node<TokenAttributes> node) {
        String tmapCode = BIB.getTmapCodeFromFile(auxTmapsDir, "printfCtoJava.tmap");
        List<Node<TokenAttributes>> nodes = BIB.tmapOneRuleCodeCall(tmapCode, node);
        if (!nodes.isEmpty()) {
            BIB.replaceNode(BIB.getChildByText(node.getChildren(), "NAME"), nodes.get(0));
        }
    }

}
