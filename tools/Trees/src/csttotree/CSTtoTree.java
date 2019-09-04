/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package csttotree;

import cstecst.*;
import java.util.ArrayList;
import org.antlr.v4.runtime.RuleContext;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.tree.ParseTree;
import simpletree.*;

/**
 *
 * @author Rafael Braz
 */
public class CSTtoTree {
    
    private final Tree<TokenAttributes> tree;
    
    public CSTtoTree() {
        tree = new Tree<>();
    }
    
    public void startVisiting(ParseTree visitingNode, Node<TokenAttributes> parent) {
        visit(visitingNode, parent);
        Node<TokenAttributes> newRoot = tree.getRoot().getChildren().get(0);
        newRoot.setParent(null);
        tree.setRoot(newRoot);
        ArrayList<Node<TokenAttributes>> list = (ArrayList<Node<TokenAttributes>>) tree.getTreeAsIndexOrderedList();
        list.forEach((t) -> {
            if (t.getNodeData() instanceof ConcreteToken) {
                ((ConcreteToken) t.getNodeData()).setIndex(list.indexOf(t) + 1);
            }
        });
    }
    
    private void visit(ParseTree visitingNode, Node<TokenAttributes> parent) {
        Object payload = visitingNode.getPayload();
        Node<TokenAttributes> currentNode = tree.createNode(parent);
        if (payload instanceof RuleContext) {
            RuleContext rule = (RuleContext) payload;
            currentNode.setNodeData(new UniversalToken(getRuleRealName(rule), rule.getRuleIndex()));
            for (int c = 0; c < rule.getChildCount(); c++) {
                visit(rule.getChild(c), currentNode);
            }
            return;
        }
        Token token = (Token) payload;
        currentNode.setNodeData(new ConcreteToken(-1, token.getText(), token.getType(), token.getLine(), token.getCharPositionInLine()));
    }
    
    private String getRuleRealName(RuleContext rule) {
        String s = rule.getClass().getSimpleName();
        return s.substring(0, s.length() - 7);
    }
    
    public Tree<TokenAttributes> getTree() {
        return tree;
    }
    
}
