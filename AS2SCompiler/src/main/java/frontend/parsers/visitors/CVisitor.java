/*
 * The MIT License - https://github.com/RafaelSantosBraz/AS2SCompiler/blob/master/LICENSE
 * Copyright 2020 Rafael Braz.
 */
package frontend.parsers.visitors;

import auxtools.ECST;
import auxtools.JSONHandler;
import auxtools.TreeStringBuilder;
import frontend.parsers.CGrammarBaseVisitor;
import frontend.parsers.CGrammarParser;
import org.antlr.v4.runtime.tree.ParseTree;

/**
 *
 * @author Rafael Braz
 */
public class CVisitor extends CGrammarBaseVisitor<String> {

    /**
     * the built eCST.
     */
    private final TreeStringBuilder eCST;

    public CVisitor() {
        eCST = new TreeStringBuilder(10000);
    }

    /**
     * start parsin the ANTLR tree.
     *
     * @param ctx the initial ANTLR node.
     * @return the built eCST as a JSONObject.
     */
    public TreeStringBuilder start(ParseTree ctx) {
        visit(ctx);
        return eCST;
    }

    @Override
    public String visitCompilationUnit(CGrammarParser.CompilationUnitContext ctx) {
        eCST
                .appendTokenOpenChildren(
                        ECST.COMPILATION_UNIT_LABEL,
                        ECST.COMPILATION_UNIT_TYPE
                )
                .appendTokenOpenChildren(
                        ECST.PACKAGE_DECL_LABEL,
                        ECST.PACKAGE_DECL_TYPE
                );
        visitChildren(ctx);
        eCST
                .appendCloseTokenChildren()
                .appendCloseTokenChildren();
        return null;
    }

    @Override
    public String visitDeclaration(CGrammarParser.DeclarationContext ctx) {
        eCST
                .appendComplete(
                        ECST.VAR_DECL_LABEL,
                        ECST.VAR_DECL_TYPE,
                        this,
                        ctx.getChild(CGrammarParser.DeclarationSpecifiersContext.class, 0)
                );
        return null;
    }

    @Override
    public String visitTypeSpecifier(CGrammarParser.TypeSpecifierContext ctx) {
        eCST
                .appendTokenOpenChildren(
                        ECST.TYPE_LABEL,
                        ECST.TYPE_TYPE
                )
                .appendComplete(ctx.getText(), ECST.LEAF_TYPE)
                .appendCloseTokenChildren();
        return null;
    }

}
