/*
 * The MIT License - https://github.com/RafaelSantosBraz/AS2SCompiler/blob/master/LICENSE
 * Copyright 2020 Rafael Braz.
 */
package codegenerators;

import auxtools.BIB;
import generators.CodeGenerator;
import java.util.ArrayList;
import java.util.List;
import symboltable.Symbol;
import symboltable.SymbolTable;
import trees.cstecst.TokenAttributes;
import trees.simpletree.Node;

/**
 * aims to translate an adapted eCST into an Object Code (C code)
 *
 * @author Rafael Braz
 */
public class CGenerator extends CodeGenerator {

    /**
     * provide all important information about variables, files, functions and other
     * abstract symbols.
     */
    private final SymbolTable symbolTable;

    public CGenerator(String outputPath, String auxTmapsDir) {
        super(outputPath, auxTmapsDir);
        symbolTable = new SymbolTable();
    }

    // all the "actionSOMETHING" methods implements a ANTLR visit-like method.

    public Object actionroot(Node<TokenAttributes> node) {
        List<String> res = new ArrayList<>();
        node.getChildren().forEach((t) -> {
            String name = getText(
                    BIB.tmapOneRuleCodeCall("\"PACKAGE_DECL\".\"CONCRETE_UNIT_DECL\".\"NAME\".child", t).get(0));
            symbolTable.addSymbol(new Symbol(name, Symbol.CLASS, t));
        });
        return visitChildren(node.getChildren());
    }

    public Object actionCOMPILATION_UNIT(Node<TokenAttributes> node) {
        String name = getText(
                BIB.tmapOneRuleCodeCall("\"PACKAGE_DECL\".\"CONCRETE_UNIT_DECL\".\"NAME\".child", node).get(0));
        return writeToFile(name, ".c", node);
    }

    public Object actionIMPORT_DECL(Node<TokenAttributes> node) {
        List<String> res = new ArrayList<>();
        res.add(String.format("#include<%s>%s", getText(node.getChildren().get(0).getChildren().get(0)),
                System.lineSeparator()));
        return res;
    }

    public Object actionSTRUCT(Node<TokenAttributes> node) {
        List<String> res = new ArrayList<>();
        res.addAll((List<String>) visit(node.getChildren().get(0)));
        res.addAll((List<String>) visit(node.getChildren().get(1)));
        res.add(";");
        return res;
    }

    public Object actionBLOCK_SCOPE(Node<TokenAttributes> node) {
        List<String> res = new ArrayList<>();
        res.add("{");
        if (node.getChildren().size() > 2) {
            res.addAll((List<String>) visitChildren(node.getChildren()));
        }
        res.add("}");
        return res;
    }

    public Object actionVAR_DECL(Node<TokenAttributes> node) {
        List<String> res = new ArrayList<>();
        res.addAll((List<String>) visitChildren(node.getChildren()));
        if (res.get(0).equals("struct")) {
            res.add(2, "*");
        }
        res.add(";");
        return res;
    }

    public Object actionASSIGN_OPERATOR(Node<TokenAttributes> node) {
        List<String> res = new ArrayList<>();
        switch (node.getChildren().size()) {
            case 2:
                res.addAll((List<String>) visit(node.getChildren().get(0)));
                res.addAll((List<String>) visit(node.getChildren().get(1)));
                break;
            case 3:
                res.addAll((List<String>) visit(node.getChildren().get(1)));
                res.addAll((List<String>) visit(node.getChildren().get(0)));
                res.addAll((List<String>) visit(node.getChildren().get(2)));
                break;
            default:
                res.addAll((List<String>) visitChildren(node.getChildren().subList(1, node.getChildren().size() - 1)));
                res.addAll((List<String>) visit(node.getChildren().get(0)));
                res.addAll((List<String>) visit(node.getChildren().get(node.getChildren().size() - 1)));
        }
        res.add(";");
        return res;
    }

    public Object actionVALUE(Node<TokenAttributes> node) {
        List<String> res = new ArrayList<>();
        if (getText(node.getChildren().get(0)).equals("SEPARATOR")) {
            res.add("{");
            node.getChildren().subList(1, node.getChildren().size() - 1).forEach((t) -> {
                res.addAll((List<String>) visit(t));
                res.add(",");
            });
            if (res.get(res.size() - 1).equals(",")) {
                res.remove(res.size() - 1);
            }
            res.add("}");
        } else {
            res.addAll((List<String>) visitChildren(node.getChildren()));
        }
        return res;
    }

    public Object actionJUMP_STATEMENT(Node<TokenAttributes> node) {
        List<String> res = new ArrayList<>();
        res.addAll((List<String>) visitChildren(node.getChildren()));
        res.add(";");
        return res;
    }

    public Object actionKEYWORD(Node<TokenAttributes> node) {
        List<String> res = new ArrayList<>();
        res.add(getText(node.getChildren().get(0)));
        return res;
    }

    public Object actionCONST(Node<TokenAttributes> node) {
        List<String> res = new ArrayList<>();
        res.add(getText(node.getChildren().get(0)));
        return res;
    }

    public Object actionCONDITION(Node<TokenAttributes> node) {
        List<String> res = new ArrayList<>();
        res.add("(");
        res.addAll((List<String>) visit(node.getChildren().get(1)));
        res.add(")");
        return res;
    }

    public Object actionCOMPARISON_OPERATOR(Node<TokenAttributes> node) {
        List<String> res = new ArrayList<>();
        res.addAll((List<String>) actionOPERATOR(node));
        return res;
    }

    public Object actionLOGICAL_OPERATOR(Node<TokenAttributes> node) {
        List<String> res = new ArrayList<>();
        res.addAll((List<String>) actionOPERATOR(node));
        return res;
    }

    public Object actionOPERATOR(Node<TokenAttributes> node) {
        List<String> res = new ArrayList<>();
        switch (node.getChildren().size()) {
            case 1:
                res.add(getText(node.getChildren().get(0)));
                break;
            case 2:
                res.addAll((List<String>) visit(node.getChildren().get(0)));
                res.addAll((List<String>) visit(node.getChildren().get(1)));
                break;
            case 3:
                res.add("(");
                res.addAll((List<String>) visit(node.getChildren().get(1)));
                res.add(getText(node.getChildren().get(0)));
                res.addAll((List<String>) visit(node.getChildren().get(2)));
                res.add(")");
        }
        return res;
    }

    public Object actionFUNCTION_CALL(Node<TokenAttributes> node) {
        List<String> res = new ArrayList<>();
        res.addAll((List<String>) visitChildren(node.getChildren()));
        if (res.get(0).equals("struct")) {
            res.remove(0);
        }
        res.add(";");
        return res;
    }

    public Object actionLOOP_STATEMENT(Node<TokenAttributes> node) {
        List<String> res = new ArrayList<>();
        res.addAll((List<String>) visit(node.getChildren().get(0)));
        if (res.get(0).equals("for")) {
            res.add("(");
            for (int c = 1; c < node.getChildren().size() - 1; c++) {
                res.addAll((List<String>) visit(node.getChildren().get(c)));
                res.add(";");
            }
            res.add(")");
            res.addAll((List<String>) visit(node.getChildren().get(node.getChildren().size() - 1)));
        } else {
            res.addAll((List<String>) visitChildren(node.getChildren().subList(1, node.getChildren().size())));
        }
        return res;
    }

    public Object actionASSIGN_STATEMENT(Node<TokenAttributes> node) {
        List<String> res = new ArrayList<>();
        res.addAll((List<String>) visitChildren(node.getChildren()));
        res.add(";");
        return res;
    }

    public Object actionARGUMENT(Node<TokenAttributes> node) {
        List<String> res = new ArrayList<>();
        if (node.getChildren().size() == 1) {
            res.addAll((List<String>) visit(node.getChildren().get(0)));
            if (symbolTable.isClassByName(res.get(0))) {
                res.add(0, "struct");
            }
        } else {
            res.addAll((List<String>) visitChildren(node.getChildren()));
        }
        res.add(",");
        return res;
    }

    public Object actionPROTOTYPE(Node<TokenAttributes> node) {
        List<String> res = new ArrayList<>();
        res.addAll((List<String>) visit(node.getChildren().get(0)));
        res.addAll((List<String>) visit(node.getChildren().get(1)));
        res.addAll((List<String>) visit(node.getChildren().get(2)));
        res.add(";");
        return res;
    }

    public Object actionPARAMETER_DECL(Node<TokenAttributes> node) {
        List<String> res = new ArrayList<>();
        res.addAll((List<String>) visitChildren(node.getChildren()));
        res.add(",");
        return res;
    }

    public Object actionSEPARATOR(Node<TokenAttributes> node) {
        List<String> res = new ArrayList<>();
        String s = getText(node.getChildren().get(0));
        if (s.equals("[") || s.equals("]")) {
            res.add(s);
        }
        return res;
    }

    public Object actionFORMAL_PARAM_LIST(Node<TokenAttributes> node) {
        List<String> res = new ArrayList<>();
        res.add("(");
        node.getChildren().forEach((t) -> {
            List<String> temp = (List<String>) visit(t);
            if (temp.contains("struct")) {
                if (getText(node.getParent()).equals("PROTOTYPE")) {
                    temp.add(temp.size() - 1, "*");
                } else {
                    temp.add(temp.size() - 2, "*");
                }
            }
            res.addAll(temp);
        });
        res.add(")");
        return res;
    }

    public Object actionARGUMENT_LIST(Node<TokenAttributes> node) {
        List<String> res = new ArrayList<>();
        res.add("(");
        if (node.getChildren().size() > 2) {
            res.addAll((List<String>) visitChildren(node.getChildren()));
        }
        res.add(")");
        return res;
    }

    public Object actionFUNCTION_DECL(Node<TokenAttributes> node) {
        List<String> res = new ArrayList<>();
        if (getText(node.getParent()).equals("BLOCK_SCOPE")) {
            res.addAll((List<String>) visit(node.getChildren().get(0)));
            res.add("(");
            res.add("*");
            res.addAll((List<String>) visit(node.getChildren().get(1)));
            res.add(")");
            res.addAll((List<String>) visit(node.getChildren().get(2)));
            res.add(";");
        } else {
            List<String> temp = (List<String>) visitChildren(node.getChildren());
            if (temp.get(0).equals("struct")) {
                temp.add(2, "*");
            }
            res.addAll(temp);
        }
        return res;
    }

    public Object actionNAME(Node<TokenAttributes> node) {
        List<String> res = new ArrayList<>();
        if (getText(node.getParent()).equals("CONCRETE_UNIT_DECL")) {
            return res;
        }
        if (node.getChildren().size() == 1) {
            if (getText(node.getChildren().get(0)).equals("NAME")
                    || getText(node.getChildren().get(0)).equals("TYPE")) {
                res.addAll((List<String>) visit(node.getChildren().get(0)));
            } else {
                res.add(getText(node.getChildren().get(0)));
            }
        } else {
            res.addAll((List<String>) visit(node.getChildren().get(0)));
            res.add(getText(node.getChildren().get(1)));
            res.addAll((List<String>) visit(node.getChildren().get(2)));
        }
        return res;
    }

    public Object actionTYPE(Node<TokenAttributes> node) {
        List<String> res = new ArrayList<>();
        if (node.getChildren().size() == 1) {
            if (getText(node.getChildren().get(0)).equals("NAME")) {
                List<String> temp = (List<String>) visit(node.getChildren().get(0));
                if (temp.size() == 1 && symbolTable.isClassByName(temp.get(0))) {
                    res.add("struct");
                }
                res.addAll(temp);
            } else {
                String s = getText(node.getChildren().get(0));
                if (symbolTable.isClassByName(s)) {
                    res.add("struct");
                }
                res.add(s);
            }
        }
        return res;
    }
}
