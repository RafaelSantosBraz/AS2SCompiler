/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package interpreter;

import java.util.ArrayList;
import java.util.HashMap;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.TerminalNode;
import parser.TranslationGrammarBaseVisitor;
import parser.TranslationGrammarParser;
import trees.cstecst.ConcreteToken;
import trees.cstecst.TokenAttributes;
import trees.cstecst.UniversalToken;
import trees.simpletree.Node;
import trees.simpletree.Tree;

/**
 * extends the ANTLR visitor for the tmap grammar and executes the rules and
 * actions
 *
 * @author Rafael Braz
 */
public class TranslationVisitor extends TranslationGrammarBaseVisitor<Object> {

    private final HashMap<String, TranslationGrammarParser.RuleeContext> rules;
    private Node<TokenAttributes> current_node;

    public TranslationVisitor(Node<TokenAttributes> current_node) {
        this.current_node = current_node;
        rules = new HashMap<>();
    }

    // override the original ANTLR method because it was returning null when there were no more children
    @Override
    protected Object aggregateResult(Object aggregate, Object nextResult) {
        if (aggregate == null && nextResult == null) {
            return null;
        }
        if (nextResult == null) {
            return aggregate;
        }
        return nextResult;
    }

    // executes the tmap code starting from a initial rule, return the resulting tree
    public Tree<TokenAttributes> start(TranslationGrammarParser.ProgContext ctx, String firstRuleName) {
        ctx.rulee().forEach((t) -> {
            rules.put(t.NODE_NAME().getSymbol().getText(), t);
        });
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

    // retains the previous node to simulate a stack of nodes
    private Object magicVisit(ParseTree ctx, Node<TokenAttributes> CST_node) {
        current_node = CST_node;
        Object result = visit(ctx);
        current_node = CST_node;
        return result;
    }

    @Override
    public Object visitIfstatement(TranslationGrammarParser.IfstatementContext ctx) {
        ArrayList<Node<TokenAttributes>> result = new ArrayList<>();
        Boolean cond = (Boolean) magicVisit(ctx.condition(), current_node);
        if (cond == null) {
            return null;
        }
        if (cond) {
            ArrayList<Node<TokenAttributes>> temp = (ArrayList<Node<TokenAttributes>>) magicVisit(ctx.ruleebody(), current_node);
            if (temp == null) {
                return null;
            }
            return temp;
        }
        return result;
    }

    @Override
    public Object visitIfelsestatement(TranslationGrammarParser.IfelsestatementContext ctx) {
        //ArrayList<Node<TokenAttributes>> result = new ArrayList<>();
        Boolean cond = (Boolean) magicVisit(ctx.condition(), current_node);
        if (cond == null) {
            return null;
        }
        ArrayList<Node<TokenAttributes>> temp;
        if (cond) {
            temp = (ArrayList<Node<TokenAttributes>>) magicVisit(ctx.t, current_node);
        } else {
            temp = (ArrayList<Node<TokenAttributes>>) magicVisit(ctx.f, current_node);
        }
        if (temp == null) {
            return null;
        }
        return temp;
    }

    @Override
    public Object visitCondition(TranslationGrammarParser.ConditionContext ctx) {
        Node<TokenAttributes> temp1 = (Node<TokenAttributes>) magicVisit(ctx.p1, current_node);
        Node<TokenAttributes> temp2 = (Node<TokenAttributes>) magicVisit(ctx.p2, current_node);
        TerminalNode op = (TerminalNode) magicVisit(ctx.conditionaloperator(), current_node);
        if (temp1 == null || temp2 == null) {
            return null;
        }
        switch (op.getSymbol().getType()) {
            case TranslationGrammarParser.EQUALS:
                return temp1.getNodeData().getText().equals(temp2.getNodeData().getText());
            case TranslationGrammarParser.NEQ:
                return !temp1.getNodeData().getText().equals(temp2.getNodeData().getText());
        }
        return null;
    }

    @Override
    public Object visitPartialcondition(TranslationGrammarParser.PartialconditionContext ctx) {
        if (ctx.visitingsequence() != null) {
            ArrayList<Node<TokenAttributes>> temp = (ArrayList<Node<TokenAttributes>>) magicVisit(ctx.visitingsequence(), current_node);
            if (temp == null) {
                return null;
            }
            if (temp.size() != 1) {
                return null;
            }
            return temp.get(0);
        }
        TerminalNode temp = (TerminalNode) magicVisit(ctx.canonicalreference(), current_node);
        return new Node<>(new UniversalToken(normalizeTmapText(temp.getSymbol().getText()), -1));
    }

    @Override
    public Object visitCanonicalreference(TranslationGrammarParser.CanonicalreferenceContext ctx) {
        return ctx.NODE_NAME();
    }

    @Override
    public Object visitConditionaloperator(TranslationGrammarParser.ConditionaloperatorContext ctx) {
        if (ctx.EQUALS() != null) {
            return ctx.EQUALS();
        }
        return ctx.NEQ();
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
            ArrayList<Node<TokenAttributes>> temp = (ArrayList<Node<TokenAttributes>>) magicVisit(getRuleContext(ctx.NODE_NAME().getSymbol().getText()), t);
            if (temp != null) {
                result.addAll(temp);
            } else if (ctx.ANY() == null) {
                return null;
            }
        }
        return result;
    }

    @Override
    public Object visitNewleafinvoke(TranslationGrammarParser.NewleafinvokeContext ctx) {
        ArrayList<Node<TokenAttributes>> result = new ArrayList<>();
        Node<TokenAttributes> newNode = new Node<>(new ConcreteToken(-1, normalizeTmapText(ctx.NODE_NAME().getSymbol().getText()), ctx.NODE_NAME().getSymbol().getType(), ctx.NODE_NAME().getSymbol().getLine(), ctx.NODE_NAME().getSymbol().getCharPositionInLine()));
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
            ArrayList<Node<TokenAttributes>> resultBodyCopy = new ArrayList<>();
            resultBody.forEach((t) -> {
                resultBodyCopy.add(t.getClone());
            });
            newNode.setChildren(resultBodyCopy);
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
    public Object visitChildnumberinvoke(TranslationGrammarParser.ChildnumberinvokeContext ctx) {
        ArrayList<Node<TokenAttributes>> result = new ArrayList<>();
        if (current_node.getChildren().isEmpty()) {
            return null;
        }
        try {
            int number = Integer.parseInt(ctx.NUMBER().getSymbol().getText());
            result.add(current_node.getChildren().get(number));
            return result;
        } catch (Exception e) {
            return null;
        }
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
    public Object visitFirstchildinvoke(TranslationGrammarParser.FirstchildinvokeContext ctx) {
        ArrayList<Node<TokenAttributes>> result = new ArrayList<>();
        if (current_node.getChildren().isEmpty()) {
            return null;
        }
        result.add(current_node.getChildren().get(0));
        return result;
    }

    @Override
    public Object visitLastchildinvoke(TranslationGrammarParser.LastchildinvokeContext ctx) {
        ArrayList<Node<TokenAttributes>> result = new ArrayList<>();
        if (current_node.getChildren().isEmpty()) {
            return null;
        }
        result.add(current_node.getChildren().get(current_node.getChildren().size() - 1));
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
    public Object visitFirstinvoke(TranslationGrammarParser.FirstinvokeContext ctx) {
        ArrayList<Node<TokenAttributes>> result = new ArrayList<>();
        Node<TokenAttributes> theFirst = findFirst(current_node);
        result.add(theFirst);
        return result;
    }

    private Node<TokenAttributes> findFirst(Node<TokenAttributes> current_node) {
        if (current_node.getParent() != null && current_node.getParent().getChildren().size() == 1) {
            return findFirst(current_node.getParent());
        }
        if (current_node.getParent() == null) {
            return current_node;
        }
        return current_node;
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

    // search for a tmap rule context that has tha given name
    private TranslationGrammarParser.RuleeContext getRuleContext(String ruleName) {
        return rules.get(ruleName);
    }

    // remove \" ... \" of a String from the CST
    private String normalizeTmapText(String text) {
        return text.substring(1, text.length() - 1);
    }

}
