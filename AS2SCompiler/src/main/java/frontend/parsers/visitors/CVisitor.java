/*
 * The MIT License - https://github.com/RafaelSantosBraz/AS2SCompiler/blob/master/LICENSE
 * Copyright 2020 Rafael Braz.
 */
package frontend.parsers.visitors;

import auxtools.ECST;
import auxtools.JSONHandler;
import frontend.parsers.CGrammarBaseVisitor;
import frontend.parsers.CGrammarParser;
import org.antlr.v4.runtime.tree.ParseTree;
import org.json.JSONObject;

/**
 *
 * @author Rafael Braz
 */
public class CVisitor extends CGrammarBaseVisitor<JSONObject> {

    /**
     * start parsin the ANTLR tree.
     *
     * @param ctx the initial ANTLR node.
     * @return the built eCST as a JSONObject.
     */
    public JSONObject start(ParseTree ctx) {
        return visit(ctx);
    }

    @Override
    protected JSONObject aggregateResult(JSONObject aggregate, JSONObject nextResult) {
        if (aggregate == null && nextResult == null) {
            return null;
        }
        if (aggregate == null) {
            return nextResult;
        }
        if (nextResult == null) {
            return aggregate;
        }
        var wrap = JSONHandler.wrapCopy(aggregate);
        JSONHandler.appendChildren(wrap, nextResult);
        return wrap;
    }

    @Override
    public JSONObject visitCompilationUnit(CGrammarParser.CompilationUnitContext ctx) {
        var unit = JSONHandler
                .buildFirstComplete(
                        ECST.COMPILATION_UNIT_LABEL,
                        ECST.COMPILATION_UNIT_TYPE
                );
        var pack = JSONHandler
                .buildComplete(unit,
                        ECST.PACKAGE_DECL_LABEL,
                        ECST.PACKAGE_DECL_TYPE
                );
        var children = visitChildren(ctx);
        JSONHandler.appendChildren(pack, children);
        // does not wrap because it is the "head" of the tree.
        return unit;
    }

    @Override
    public JSONObject visitDeclaration(CGrammarParser.DeclarationContext ctx) {
        var var_dec = JSONHandler
                .buildFirstComplete(
                        ECST.VAR_DECL_LABEL,
                        ECST.VAR_DECL_TYPE
                );

        return JSONHandler.wrap(var_dec);
    }

    @Override
    public JSONObject visitTypeSpecifier(CGrammarParser.TypeSpecifierContext ctx) {

        return null;
    }

}
