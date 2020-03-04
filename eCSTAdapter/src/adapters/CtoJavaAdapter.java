/*
 * The MIT License - https://github.com/RafaelSantosBraz/AS2SCompiler/blob/master/LICENSE
 * Copyright 2020 Rafael Braz.
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

    /**
     * path to all files of partial/complete tmap code.
     */
    private final String auxTmapsDir;

    public CtoJavaAdapter(String auxTmapsDir) {
        this.auxTmapsDir = auxTmapsDir;
    }

    /**
     * converts "const" to "final".
     *
     * @param node
     */
    public void actionconst(Node<TokenAttributes> node) {
        node.getNodeData().setText("final");
    }

    /**
     * converts functions to methods, inclusively "main".
     *
     * @param node
     */
    public void actionFUNCTION_DECL(Node<TokenAttributes> node) {
        String tmapCode = BIB.getTmapCodeFromFile(auxTmapsDir, "mainCtoJava.tmap");
        List<Node<TokenAttributes>> nodes = BIB.tmapOneRuleCodeCall(tmapCode, node);
        if (nodes.get(0).getNodeData().getText().equals("FUNCTION_DECL")) {
            BIB.replaceNode(node, nodes.get(0));
        } else {
            if (!nodes.get(0).getNodeData().getText().equals(node.getChildren().get(0).getNodeData().getText())) {
                nodes.get(0).setParent(node);
                node.addChildAt(nodes.get(0), 0);
            }
        }
    }

    /**
     * converts global variable declarations to Java class atributes and array
     * declarations to Java pattern
     *
     * @param node
     */
    public void actionVAR_DECL(Node<TokenAttributes> node) {
        String tmapCode = BIB.getTmapCodeFromFile(auxTmapsDir, "globalVarCtoJava.tmap");
        List<Node<TokenAttributes>> nodes = BIB.tmapOneRuleCodeCall(tmapCode, node);
        if (!nodes.isEmpty()) {
            nodes.get(0).setParent(node.getParent());
            BIB.replaceNode(node, nodes.get(0));
        } else {
            tmapCode = BIB.getTmapCodeFromFile(auxTmapsDir, "arrayCtoJava.tmap");
            nodes = BIB.tmapOneRuleCodeCall(tmapCode, node);
            if (!nodes.isEmpty()) {
                nodes.get(0).setParent(node.getParent());
                BIB.replaceNode(node, nodes.get(0));
            }
        }
    }

    /**
     * remove C double TYPE from the eCST.
     *
     * @param node
     */
    public void actionTYPE(Node<TokenAttributes> node) {
        if (node.getChildren().get(0).getNodeData().getText().equals("TYPE")) {
            BIB.removeNode(node);
        }
    }

    /**
     * C printf to Java System.out.printf.
     *
     * @param node
     */
    public void actionFUNCTION_CALL(Node<TokenAttributes> node) {
        String tmapCode = BIB.getTmapCodeFromFile(auxTmapsDir, "printfCtoJava.tmap");
        List<Node<TokenAttributes>> nodes = BIB.tmapOneRuleCodeCall(tmapCode, node);
        if (!nodes.isEmpty()) {
            BIB.replaceNode(BIB.getChildByText(node.getChildren(), "NAME"), nodes.get(0));
        }
    }

    /**
     * removing C imports.
     *
     * @param node
     */
    public void actionIMPORT_DECL(Node<TokenAttributes> node) {
        BIB.removeChain(node);
    }

}
