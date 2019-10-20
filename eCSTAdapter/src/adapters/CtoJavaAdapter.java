/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package adapters;

import auxtools.BIB;
import java.util.List;
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

    // const to final
    public void actionconst(Node<TokenAttributes> node) {
        node.getNodeData().setText("final");
    }

    // function main to method main
    public void actionFUNCTION_DECL(Node<TokenAttributes> node) {
        if (BIB.getChildByText(node.getChildren(), "NAME").getChildren().get(0).getNodeData().getText().equals("main")) {
            Node<TokenAttributes> func = BIB.createNode("FUNCTION_DECL");
            Node<TokenAttributes> modList = BIB.createNodeWithParent(func, "MODIFIER_LIST");
            BIB.createNodeWithParent(modList, "public");
            BIB.createNodeWithParent(modList, "static");
            Node<TokenAttributes> type = BIB.createNodeWithParent(func, "TYPE");
            BIB.createNodeWithParent(type, "void");
            Node<TokenAttributes> name = BIB.createNodeWithParent(func, "NAME");
            BIB.createNodeWithParent(name, "main");
            Node<TokenAttributes> formList = BIB.createNodeWithParent(func, "FORMAL_PARAM_LIST");
            Node<TokenAttributes> paramDecl = BIB.createNodeWithParent(formList, "PARAMETER_DECL");
            Node<TokenAttributes> type2 = BIB.createNodeWithParent(paramDecl, "TYPE");
            Node<TokenAttributes> name2 = BIB.createNodeWithParent(type2, "NAME");
            BIB.createNodeWithParent(name2, "String[]");
            Node<TokenAttributes> name3 = BIB.createNodeWithParent(paramDecl, "NAME");
            BIB.createNodeWithParent(name3, "args");
            Node<TokenAttributes> block = BIB.getChildByText(node.getChildren(), "BLOCK_SCOPE");
            block.setParent(func);
            func.addChild(block);
            List<Node<TokenAttributes>> children = node.getParent().getChildren();
            int index = children.indexOf(node);
            children.remove(node);
            children.add(index, func);
        }
    }

    // remove C double TYPE
    public void actionTYPE(Node<TokenAttributes> node) {
        if (node.getChildren().get(0).getNodeData().getText().equals("TYPE")) {
            BIB.removeNode(node);
        }
    }

    public void actionFUNCTION_CAL(Node<TokenAttributes> node) {
         if (BIB.getChildByText(node.getChildren(), "NAME").getChildren().get(0).getNodeData().getText().equals("printf")) {
             
         }
    }

}
