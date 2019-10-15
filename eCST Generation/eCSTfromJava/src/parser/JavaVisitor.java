/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package parser;

import java.util.ArrayList;
import java.util.List;
import trees.cstecst.ConcreteToken;
import trees.cstecst.TokenAttributes;
import trees.cstecst.UniversalToken;
import trees.simpletree.Node;
import trees.simpletree.Tree;

/**
 *
 * @author Rafael Braz
 */
public class JavaVisitor extends JavaGrammarBaseVisitor<Object> {

    private Node<TokenAttributes> createUniversalNodeWithParent(String text, Node<TokenAttributes> parent) {
        Node<TokenAttributes> node = new Node<>(new UniversalToken(text, 0));
        node.setParent(parent);
        parent.addChild(node);
        return node;
    }

    private Node<TokenAttributes> createUniversalNode(String text) {
        return new Node<>(new UniversalToken(text, 0));        
    }
    
    private void setParentChildRelationship(Node<TokenAttributes> parent, Node<TokenAttributes> child) {
        parent.addChild(child);
        child.addChild(child);
    }
    
    private void setParentChildrenRelationShip(Node<TokenAttributes> parent, List<Node<TokenAttributes>> children){
        parent.addChildren(children);
        children.forEach((t) -> {
            t.setParent(parent);
        });
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

    public Tree<TokenAttributes> start(JavaGrammarParser.CompilationUnitContext ctx, String firstRuleName) {
        Tree<TokenAttributes> eCST = new Tree<>(new UniversalToken("root", -1));
        ArrayList<Node<TokenAttributes>> result = (ArrayList<Node<TokenAttributes>>) visit(ctx);
        if (result == null) {
            return null;
        }
        eCST.getRoot().addChildren(result);
        result.forEach((t) -> {
            t.setParent(eCST.getRoot());
        });
        return eCST;
    }

    @Override
    public Object visitCompilationUnit(JavaGrammarParser.CompilationUnitContext ctx) {
        Node<TokenAttributes> node = createUniversalNode("COMPILATION_UNIT");
        Node<TokenAttributes> pack = createUniversalNodeWithParent("PACKAGE_DECL", node);
        if (ctx.packageDeclaration() != null) {            
            setParentChildRelationship(createUniversalNodeWithParent("NAME", pack), (Node<TokenAttributes>) visit(ctx.packageDeclaration()));
        }
        if (ctx.importDeclaration() != null) {
            ctx.importDeclaration().forEach((t) -> {
                setParentChildRelationship(pack, (Node<TokenAttributes>) visit(t));
            });
        }

        return node;
    }

    @Override
    public Object visitPackageDeclaration(JavaGrammarParser.PackageDeclarationContext ctx) {
        return new Node<>(new UniversalToken(ctx.packageName().Identifier().getSymbol().getText(), 0));
    }

    @Override
    public Object visitSingleTypeImportDeclaration(JavaGrammarParser.SingleTypeImportDeclarationContext ctx) {
        Node<TokenAttributes> node = createUniversalNode("IMPORT_DECL");
        Node<TokenAttributes> impName = createUniversalNodeWithParent("NAME", node);        
        setParentChildrenRelationShip(impName, (List<Node<TokenAttributes>>) visit(ctx.typeName()));
        return node;
    }

    @Override
    public Object visitTypeName(JavaGrammarParser.TypeNameContext ctx) {
        ArrayList<Node<TokenAttributes>> names = new ArrayList<>();
        if (ctx.packageOrTypeName() == null) {
            names.add(createUniversalNode(ctx.Identifier().getSymbol().getText()));
            return names;
        }
        names.add((Node<TokenAttributes>) visit(ctx.packageOrTypeName()));
        names.add(new Node<>(new UniversalToken(".", 0)));
        Node<TokenAttributes> name = new Node<>(new UniversalToken("NAME", 0));
        setParentChildRelationship(name, new Node<>(new UniversalToken(ctx.Identifier().getSymbol().getText(), 0)));
        names.add(name);
        return names;
    }

    @Override
    public Object visitPackageOrTypeName(JavaGrammarParser.PackageOrTypeNameContext ctx) {
        Node<TokenAttributes> name = new Node<>(new UniversalToken(text, 0));
    }

    
    
}
