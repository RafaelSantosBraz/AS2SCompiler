/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package codegenerators;

import auxtools.BIB;
import generators.CodeGenerator;
import java.util.ArrayList;
import java.util.List;
import trees.cstecst.TokenAttributes;
import trees.simpletree.Node;

/**
 * aims to translate an adapted eCST into an Object Code (C code)
 *
 * @author Rafael Braz
 */
public class CGenerator extends CodeGenerator {

    public CGenerator(String outputPath, String auxTmapsDir) {
        super(outputPath, auxTmapsDir);
    }

    public Object actionCOMPILATION_UNIT(Node<TokenAttributes> node) {
        String name = getText(BIB.tmapOneRuleCodeCall("\"PACKAGE_DECL\".\"CONCRETE_UNIT_DECL\".\"NAME\".child", node).get(0));
        return writeToFile(name, ".c", node);
    }

    public Object actionIMPORT_DECL(Node<TokenAttributes> node) {
        List<String> res = new ArrayList<>();
        res.add(String.format("#include<%s>%s", getText(node.getChildren().get(0).getChildren().get(0)), System.lineSeparator()));
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
        if (node.getChildren().size() == 3) {
            res.addAll((List<String>) visit(node.getChildren().get(1)));
            res.addAll((List<String>) visit(node.getChildren().get(0)));
            res.addAll((List<String>) visit(node.getChildren().get(2)));
        } else {
            res.addAll((List<String>) visit(node.getChildren().get(0)));
            res.addAll((List<String>) visit(node.getChildren().get(1)));
        }
        res.add(";");
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
                break;
        }
        return res;
    }

    public Object actionFUNCTION_CALL(Node<TokenAttributes> node) {
        List<String> res = new ArrayList<>();
        res.addAll((List<String>) visitChildren(node.getChildren()));
        res.add(";");
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
        res.addAll((List<String>) visit(node.getChildren().get(0)));
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
        res.addAll((List<String>) visit(node.getChildren().get(0)));
        if (node.getChildren().size() == 2) {
            res.addAll((List<String>) visit(node.getChildren().get(1)));
        }
        res.add(",");
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
            if (getText(node.getChildren().get(0)).equals("NAME") || getText(node.getChildren().get(0)).equals("TYPE")) {
                res.addAll((List<String>) visit(node.getChildren().get(0)));
                if (res.get(0).equals("struct")){
                    res.remove(0);
                }
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
                if (temp.size() == 1 && !isPrimitiveType(temp.get(0))) {
                    res.add("struct");
                }
                res.addAll(temp);
            } else {
                String s = getText(node.getChildren().get(0));
                if (!isPrimitiveType(s)) {
                    res.add("struct");
                }
                res.add(s);
            }
        } else {
            res.addAll((List<String>) visit(node.getChildren().get(0)));
            res.add(getText(node.getChildren().get(1)));
            res.addAll((List<String>) visit(node.getChildren().get(2)));
        }
        return res;
    }

    // check if a given type is a valid C primitive type
    private boolean isPrimitiveType(String type) {
        switch (type) {
            case "int":
            case "double":
            case "char":
            case "float":
            case "void":
                return true;
            default:
                return false;
        }
    }
}
