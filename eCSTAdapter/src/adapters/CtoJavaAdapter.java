/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package adapters;

import auxtools.BIB;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import trees.cstecst.TokenAttributes;
import trees.cstecst.UniversalToken;
import trees.simpletree.Node;
import walkers.ActionWalker;

/**
 *
 * @author Rafael Braz
 */
public class CtoJavaAdapter extends ActionWalker {

    private final String auxTmapsDir;

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

    public void actionFUNCTION_CAL(Node<TokenAttributes> node) {
        if (BIB.getChildByText(node.getChildren(), "NAME").getChildren().get(0).getNodeData().getText().equals("printf")) {
            Node<TokenAttributes> name1 = BIB.createNode("NAME");
            Node<TokenAttributes> name2 = BIB.createNodeWithParent(name1, "NAME");
            BIB.createNodeWithParent(name1, ".");
            Node<TokenAttributes> name3 = BIB.createNodeWithParent(name1, "NAME");
            BIB.createNodeWithParent(name3, "printf");
            Node<TokenAttributes> name4 = BIB.createNodeWithParent(name2, "NAME");
            BIB.createNodeWithParent(name2, ".");
            Node<TokenAttributes> name5 = BIB.createNodeWithParent(name2, "NAME");
            BIB.createNodeWithParent(name5, "out");
            BIB.createNodeWithParent(name4, "System");
            name1.setParent(node);
            List<Node<TokenAttributes>> children = node.getChildren();
            Node<TokenAttributes> child = BIB.getChildByText(children, "NAME");
            int index = children.indexOf(child);
            children.remove(child);
            children.add(index, name1);
        }
    }

}
