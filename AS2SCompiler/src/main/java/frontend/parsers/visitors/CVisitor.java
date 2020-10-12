/*
 * The MIT License - https://github.com/RafaelSantosBraz/AS2SCompiler/blob/master/LICENSE
 * Copyright 2020 Rafael Braz.
 */
package frontend.parsers.visitors;

import auxtools.JSONHandler;
import frontend.parsers.CGrammarBaseVisitor;
import frontend.parsers.CGrammarParser;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.RuleNode;
import org.json.JSONObject;

/**
 *
 * @author Rafael Braz
 */
public class CVisitor extends CGrammarBaseVisitor<String> {

    /**
     * the built eCST.
     */
    private final JSONObject eCST;

    /**
     * pointer to the "current" JSONObject in the eCST.
     */
    private JSONObject current;

    public CVisitor() {
        current = eCST = new JSONObject();
    }

    /**
     * visit a given ANTLR tree node. Only call this method if you want to
     * change the current pointer.
     *
     * @param tree ANTLR node to be visited.
     * @param current the new current pointer.
     * @return result String.
     */
    public String visit(ParseTree tree, JSONObject current) {
        var previous = this.current;
        this.current = current;
        var res = visit(tree);
        this.current = previous;
        return res;
    }

    /**
     * visit all childrn of a given ANTLR tree node. Only call this method if
     * you want to change the current pointer.
     *
     * @param node parent ANTLR node. Its children will be visited.
     * @param current the new current pointer.
     * @return result String.
     */
    public String visitChildren(RuleNode node, JSONObject current) {
        var previous = this.current;
        var result = defaultResult();
        int n = node.getChildCount();
        // adapted from ANTLR visitChildren
        for (int i = 0; i < n; i++) {
            if (!shouldVisitNextChild(node, result)) {
                break;
            }
            var c = node.getChild(i);
            this.current = current;
            var childResult = c.accept(this);
            result = aggregateResult(result, childResult);
        }
        this.current = previous;
        return result;
    }

    /**
     * start parsin the ANTLR tree.
     *
     * @param ctx the initial ANTLR node.
     * @return the built eCST as a JSONObject.
     */
    public JSONObject start(ParseTree ctx) {
        visit(ctx);
        return eCST;
    }

    @Override
    public String visitCompilationUnit(CGrammarParser.CompilationUnitContext ctx) {
        var json = JSONHandler.buildJSONComplete(current, "COMPILATION_UNIT", 0);
        return visitChildren(ctx, json);
    }

}
