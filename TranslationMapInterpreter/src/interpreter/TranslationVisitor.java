/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package interpreter;

import java.util.ArrayList;
import org.antlr.v4.runtime.tree.ParseTree;
import parser.TranslationGrammarBaseVisitor;
import parser.TranslationGrammarParser;
import trees.cstecst.ConcreteToken;
import trees.cstecst.TokenAttributes;
import trees.cstecst.UniversalToken;
import trees.simpletree.Node;
import trees.simpletree.Tree;

/**
 *
 * @author Rafael Braz
 */
public class TranslationVisitor extends TranslationGrammarBaseVisitor<Object> {

    private final ArrayList<TranslationGrammarParser.RuleeContext> rules;
    private Node<TokenAttributes> current_node;

    public TranslationVisitor(Node<TokenAttributes> current_node) {
        this.current_node = current_node;
        rules = new ArrayList<>();
    }

    // override the original ANTLR method because it was returning null when there were no more children
    @Override
    protected Object aggregateResult(Object aggregate, Object nextResult) {
        if (aggregate == null && nextResult == null){
           return null; 
        }
        if (nextResult == null){
            return aggregate;
        }
        return nextResult;
    }

    
    
    public Tree<TokenAttributes> start(TranslationGrammarParser.ProgContext ctx, String firstRuleName) {
        rules.addAll(ctx.rulee());
        Tree<TokenAttributes> eCST = new Tree<>(new UniversalToken("root", -1));
        ArrayList<Node<TokenAttributes>> result = (ArrayList<Node<TokenAttributes>>) magicVisit(getRuleContext(firstRuleName), current_node);
        if (result == null) {
            return null;
        }
        eCST.getRoot().addChildren(result);
        result.forEach((t) -> {
            t.setParent(eCST.getRoot());
        });
        return eCST;
    }

    private Object magicVisit(ParseTree ctx, Node<TokenAttributes> CST_node) {
        current_node = CST_node;
        ArrayList<Node<TokenAttributes>> result = (ArrayList<Node<TokenAttributes>>) visit(ctx);     
        current_node = CST_node;
        return result;
    }

    @Override
    public Object visitRuleebody(TranslationGrammarParser.RuleebodyContext ctx) {
        ArrayList<Node<TokenAttributes>> result = new ArrayList<>();
        ArrayList<Node<TokenAttributes>> partResult = (ArrayList<Node<TokenAttributes>>) magicVisit(ctx.partialrulee(), current_node);
        ArrayList<Node<TokenAttributes>> ruleBodyresult = new ArrayList<>();
        if (partResult == null) {
            return null;
        }
        if (ctx.ruleebody() != null) {
            ArrayList<Node<TokenAttributes>> temp = (ArrayList<Node<TokenAttributes>>) magicVisit(ctx.ruleebody(), current_node);
            if (temp == null) {
                return null;
            }
            ruleBodyresult = temp;
        }
        result.addAll(partResult);
        result.addAll(ruleBodyresult);
        return result;
    }

    @Override
    public Object visitOptionalpartialrulee(TranslationGrammarParser.OptionalpartialruleeContext ctx) {
        ArrayList<Node<TokenAttributes>> result = new ArrayList<>();
        ArrayList<Node<TokenAttributes>> fragResult = (ArrayList<Node<TokenAttributes>>) magicVisit(ctx.ruleefragment(), current_node);
        if (fragResult == null) {
            return result;
        }
        result.addAll(fragResult);
        return result;
    }

    @Override
    public Object visitRuleecall(TranslationGrammarParser.RuleecallContext ctx) {
        ArrayList<Node<TokenAttributes>> result = new ArrayList<>();
        ArrayList<Node<TokenAttributes>> new_nodes = (ArrayList<Node<TokenAttributes>>) magicVisit(ctx.visitingsequence(), current_node);
        if (new_nodes == null) {
            return null;
        }
        for (Node<TokenAttributes> t : new_nodes) {
            //System.out.println(ctx.NODE_NAME().getSymbol().getText());
            //System.out.println(t.getNodeData().getText());
            ArrayList<Node<TokenAttributes>> temp = (ArrayList<Node<TokenAttributes>>) magicVisit(getRuleContext(ctx.NODE_NAME().getSymbol().getText()), t);
            if (temp != null) {
                result.addAll(temp);
            } 
            else if (ctx.ANY() == null) {
                return null;
            }
        }
        return result;
    }

    @Override
    public Object visitNewleafinvoke(TranslationGrammarParser.NewleafinvokeContext ctx) {
        ArrayList<Node<TokenAttributes>> result = new ArrayList<>();
        Node<TokenAttributes> newNode = new Node<>(new ConcreteToken(0, normalizeTmapText(ctx.NODE_NAME().getSymbol().getText()), ctx.NODE_NAME().getSymbol().getType(), ctx.NODE_NAME().getSymbol().getLine(), ctx.NODE_NAME().getSymbol().getCharPositionInLine()));
        result.add(newNode);
        return result;
    }

    @Override
    public Object visitParentsetchildren(TranslationGrammarParser.ParentsetchildrenContext ctx) {
        ArrayList<Node<TokenAttributes>> result = new ArrayList<>();
        ArrayList<Node<TokenAttributes>> resultBody = (ArrayList<Node<TokenAttributes>>) magicVisit(ctx.ruleebody(), current_node);
        if (resultBody == null) {
            return null;
        }
        Node<TokenAttributes> newNode = new Node<>(new UniversalToken(normalizeTmapText(ctx.NODE_NAME().getSymbol().getText()), ctx.NODE_NAME().getSymbol().getType()));
        if (!resultBody.isEmpty()) {
            newNode.setChildren(resultBody);
            resultBody.forEach((t) -> {
                t.setParent(newNode);
            });
        }
        result.add(newNode);
        return result;
    }

    @Override
    public Object visitVisitingsequence(TranslationGrammarParser.VisitingsequenceContext ctx) {
        ArrayList<Node<TokenAttributes>> result = new ArrayList<>();
        ArrayList<Node<TokenAttributes>> resultMention = (ArrayList<Node<TokenAttributes>>) magicVisit(ctx.mention(), current_node);
        if (resultMention == null) {
            return null;
        }
        if (resultMention.size() > 1 && ctx.visitingsequence() != null) {
            return null;
        }
        if (ctx.visitingsequence() != null) {
            if (resultMention.isEmpty()) {
                return null;
            }
            ArrayList<Node<TokenAttributes>> temp = (ArrayList<Node<TokenAttributes>>) magicVisit(ctx.visitingsequence(), resultMention.get(0));
            if (temp == null) {
                return null;
            }
            result.addAll(temp);
        } else {
            result.addAll(resultMention);
        }
        return result;
    }

    @Override
    public Object visitDirectnodevisiting(TranslationGrammarParser.DirectnodevisitingContext ctx) {
        ArrayList<Node<TokenAttributes>> result = new ArrayList<>();
        current_node.getChildren().forEach((t) -> {
            if (t.getNodeData().getText().equals(normalizeTmapText(ctx.NODE_NAME().getSymbol().getText()))) {
                result.add(t);
            }
        });
        if (result.size() != 1) {
            return null;
        }
        return result;
    }

    @Override
    public Object visitChildinvoke(TranslationGrammarParser.ChildinvokeContext ctx) {
        ArrayList<Node<TokenAttributes>> result = new ArrayList<>();
        if (current_node.getChildren().size() != 1) {
            return null;
        }
        result.add(current_node.getChildren().get(0));
        return result;
    }

    @Override
    public Object visitSimplechildreninvoke(TranslationGrammarParser.SimplechildreninvokeContext ctx) {
        ArrayList<Node<TokenAttributes>> result = new ArrayList<>();
        if (current_node.getChildren().isEmpty()) {
            return null;
        }
        result.addAll(current_node.getChildren());
        return result;
    }

    @Override
    public Object visitComplexchildreninvoke(TranslationGrammarParser.ComplexchildreninvokeContext ctx) {
        ArrayList<Node<TokenAttributes>> result = new ArrayList<>();
        ArrayList<Node<TokenAttributes>> temp = new ArrayList<>();
        current_node.getChildren().forEach((t) -> {
            if (t.getNodeData().getText().equals(normalizeTmapText(ctx.NODE_NAME().getSymbol().getText()))) {
                temp.add(t);
            }
        });
        if (temp.isEmpty()) {
            return null;
        }
        result.addAll(temp);
        return result;
    }

    @Override
    public Object visitLastinvoke(TranslationGrammarParser.LastinvokeContext ctx) {
        ArrayList<Node<TokenAttributes>> result = new ArrayList<>();
        Node<TokenAttributes> theLast = findLast(current_node);
        result.add(theLast);
        return result;
    }

    private Node<TokenAttributes> findLast(Node<TokenAttributes> current_node) {
        if (current_node.getChildren().isEmpty() || current_node.getChildren().size() > 1) {
            return current_node;
        }
        return findLast(current_node.getChildren().get(0));
    }

    @Override
    public Object visitParentinvoke(TranslationGrammarParser.ParentinvokeContext ctx) {
        ArrayList<Node<TokenAttributes>> result = new ArrayList<>();
        if (current_node.getParent() == null) {
            return null;
        }
        result.add(current_node.getParent());
        return result;
    }

    @Override
    public Object visitCurrentnodeinvoke(TranslationGrammarParser.CurrentnodeinvokeContext ctx) {
        ArrayList<Node<TokenAttributes>> result = new ArrayList<>();
        result.add(current_node);
        return result;
    }

    private TranslationGrammarParser.RuleeContext getRuleContext(String ruleName) {
        for (TranslationGrammarParser.RuleeContext p : rules) {
            if (p.NODE_NAME().getSymbol().getText().equals(ruleName)) {
                return p;
            }
        }
        return null;
    }

    private String normalizeTmapText(String text) {
        return text.substring(1, text.length() - 1);
    }

//    private ArrayList<Node<TokenAttributes>> removeNullValues(ArrayList<Node<TokenAttributes>> list) {
//        for (int c = 0; c < list.size(); c++) {
//            if (list.get(c) == null) {
//                list.remove(c);
//            }
//        }
//        return list;
//    }
}
