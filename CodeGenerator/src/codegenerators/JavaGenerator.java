/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package codegenerators;

import auxtools.BIB;
import generators.CodeGenerator;
import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import trees.cstecst.TokenAttributes;
import trees.simpletree.Node;

/**
 * aims to translate adapted eCST into an Object Code (Java code)
 *
 * @author Rafael Braz
 */
public class JavaGenerator extends CodeGenerator {

    public JavaGenerator(String outputPath, String auxTmapsDir) {
        super(outputPath, auxTmapsDir);
    }

    public Object actionCOMPILATION_UNIT(Node<TokenAttributes> node) {
        String name = getText(BIB.tmapOneRuleCodeCall("\"PACKAGE_DECL\".\"CONCRETE_UNIT_DECL\".\"NAME\".child", node).get(0));
        return writeToFile(name, ".java", node);
    }

    public Object actionCONCRETE_UNIT_DECL(Node<TokenAttributes> node) {
        List<String> res = new ArrayList<>();
        res.add(String.format("public class %s {", getText(node.getChildren().get(0).getChildren().get(0))));
        List<String> body = (List<String>) visitChildren(node.getChildren());
        body.remove(0);
        res.addAll(body);
        res.add("}");
        return res;
    }

    public Object actionATTRIBUTE_DECL(Node<TokenAttributes> node) {
        ArrayList<String> res = new ArrayList<>();
        res.addAll(stringifyChildren(node.getChildren().get(0)));
        res.addAll(stringifyEachChildren(BIB.tmapOneRuleCodeCall("\"TYPE\".last", node)));
        res.addAll((List<String>) visit(BIB.getChildByText(node.getChildren(), "NAME")));
        List<String> items = stringifyChildren(node);
        if (items.contains("[")) {
            res.add("[]");
        }
        Node<TokenAttributes> value = BIB.getChildByText(node.getChildren(), "VALUE");
        if (value != null && !value.getChildren().isEmpty()) {
            res.add("=");
            res.addAll((List<String>) visit(value));
        }
        res.add(";");
        return res;
    }

    public Object actionVALUE(Node<TokenAttributes> node) {
        List<String> res = new ArrayList<>();
        List<Node<TokenAttributes>> children = node.getChildren();
        List<String> ss = stringifyChildren(node);
        if (children.size() == 1) {
            return visit(children.get(0));
        }
        if (ss.get(0).equals("{")) {
            res.add("{");
            children.forEach((n) -> {
                if (getText(n).equals("VALUE")) {
                    res.addAll((List<String>) visit(n));
                    res.add(",");
                }
            });
            if (res.get(res.size() - 1).equals(",")) {
                res.remove(res.size() - 1);
            }
            res.add("}");
        } else if (ss.contains("[")) {
            res.addAll((List<String>) visit(node.getChildren().get(0)));
            int index = ss.indexOf("[");
            if (ss.get(index + 1).equals("]")) {
                res.add("[]");
            } else {
                res.add("[");
                res.addAll((List<String>) visit(node.getChildren().get(index + 1)));
                res.add("]");
            }
        }
        return res;
    }

    public Object actionCONST(Node<TokenAttributes> node) {
        List<String> res = new ArrayList<>();
        res.add(getText(BIB.tmapOneRuleCodeCall("last", node).get(0)));
        return res;
    }

    public Object actionINSTANTIATES(Node<TokenAttributes> node) {
        List<String> res = new ArrayList<>();
        List<Node<TokenAttributes>> children = node.getChildren();
        res.add("new");
        if (children.size() == 1) {

        } else {
            res.add(getText(children.get(0)));
            res.add("[");
            res.addAll((List<String>) visit(children.get(2)));
            res.add("]");
        }
        return res;
    }

    public Object actionFUNCTION_DECL(Node<TokenAttributes> node) {
        List<String> res = new ArrayList<>();
        res.addAll(stringifyChildren(node.getChildren().get(0)));
        res.addAll(stringifyEachChildren(BIB.tmapOneRuleCodeCall("\"TYPE\".last", node)));
        res.addAll((List<String>) visit(BIB.getChildByText(node.getChildren(), "NAME")));
        res.add("(");
        res.addAll((List<String>) visit(BIB.getChildByText(node.getChildren(), "FORMAL_PARAM_LIST")));
        res.add(")");
        res.addAll((List<String>) visit(BIB.getChildByText(node.getChildren(), "BLOCK_SCOPE")));
        return res;
    }

    public Object actionFORMAL_PARAM_LIST(Node<TokenAttributes> node) {
        List<String> res = new ArrayList<>();
        List<Node<TokenAttributes>> children = node.getChildren();
        children.forEach((t) -> {
            res.addAll((List<String>) visit(t));
            res.add(",");
        });
        if (res.get(res.size() - 1).equals(",")) {
            res.remove(res.size() - 1);
        }
        return res;
    }

    public Object actionPARAMETER_DECL(Node<TokenAttributes> node) {
        List<String> res = new ArrayList<>();
        res.addAll(stringifyEachChildren(BIB.tmapOneRuleCodeCall("\"TYPE\".last", node)));
        res.addAll((List<String>) visit(BIB.getChildByText(node.getChildren(), "NAME")));
        Node<TokenAttributes> sep = BIB.getChildByText(node.getChildren(), "SEPARATOR");
        if (sep != null) {
            res.add(1, "[]");
        }
        return res;
    }

    public Object actionBLOCK_SCOPE(Node<TokenAttributes> node) {
        List<String> res = new ArrayList<>();
        res.add("{");
        res.addAll((List<String>) visitChildren(node.getChildren()));
        res.add("}");
        return res;
    }

    public Object actionVAR_DECL(Node<TokenAttributes> node) {
        List<String> res = new ArrayList<>();
        List<String> ss = stringifyChildren(node);
        if (ss.get(ss.size() - 1).equals("NAME")) {
            res.addAll(stringifyEachChildren(BIB.tmapOneRuleCodeCall("\"TYPE\".last", node)));
            res.addAll((List<String>) visit(BIB.getChildByText(node.getChildren(), "NAME")));
            res.add(";");
        } else {
            res.addAll(stringifyEachChildren(BIB.tmapOneRuleCodeCall("\"TYPE\".last", node)));
            res.addAll((List<String>) visit(BIB.getChildByText(node.getChildren(), "ASSIGNMENT_STATEMENT")));
        }
        return res;
    }

    public Object actionASSIGNMENT_STATEMENT(Node<TokenAttributes> node) {
        List<String> res = new ArrayList<>();
        res.addAll((List<String>) visitChildren(node.getChildren()));
        if (!res.get(res.size() - 1).equals(";")) {
            res.add(";");
        }
        return res;
    }

    public Object actionJUMP_STATEMENT(Node<TokenAttributes> node) {
        List<String> res = new ArrayList<>();
        List<String> ss = stringifyChildren(node);
        res.add(getText(BIB.tmapOneRuleCodeCall("\"KEYWORD\".last", node).get(0)));
        if (ss.size() != 1) {
            res.addAll((List<String>) visit(node.getChildren().get(1)));
        }
        res.add(";");
        return res;
    }

    public Object actionCONDITION(Node<TokenAttributes> node) {
        List<String> res = new ArrayList<>();
        res.add("(");
        res.addAll((List<String>) visitChildren(node.getChildren()));
        res.add(")");
        return res;
    }

    public Object actionBRANCH(Node<TokenAttributes> node) {
        List<String> res = new ArrayList<>();
        switch (getText(BIB.tmapOneRuleCodeCall("\"KEYWORD\".last", node).get(0))) {
            case "if":
                res.add("if");
                res.addAll((List<String>) visit(BIB.getChildByText(node.getChildren(), "CONDITION")));
                break;
            case "else":
                res.add("else");
                break;
        }
        res.addAll((List<String>) visit(BIB.getChildByText(node.getChildren(), "BLOCK_SCOPE")));
        return res;
    }

    public Object actionCOMPARISON_OPERATOR(Node<TokenAttributes> node) {
        return actionOPERATOR(node);
    }

    public Object actionLOGICAL_OPERATOR(Node<TokenAttributes> node) {
        return actionOPERATOR(node);
    }

    public Object actionLOOP_STATEMENT(Node<TokenAttributes> node) {
        List<String> res = new ArrayList<>();
        //List<String> ss = stringifyChildren(node);
        switch (getText(BIB.tmapOneRuleCodeCall("\"KEYWORD\".last", node).get(0))) {
            case "while":
                res.add("while");
                res.addAll((List<String>) visit(BIB.getChildByText(node.getChildren(), "CONDITION")));
                res.addAll((List<String>) visit(BIB.getChildByText(node.getChildren(), "BLOCK_SCOPE")));
                break;
            case "for":
                res.add("for");
                res.add("(");
                res.addAll((List<String>) visit(BIB.getChildByText(node.getChildren(), "INIT")));
                if (!res.get(res.size() - 1).equals(";")) {
                    res.add(";");
                }
                res.addAll((List<String>) visit(BIB.getChildByText(node.getChildren(), "CONDITION")));
                res.add(";");
                res.addAll((List<String>) visit(BIB.getChildByText(node.getChildren(), "STEP")));
                if (res.get(res.size() - 1).equals(";")) {
                    res.remove(res.size() - 1);
                }
                res.add(")");
                res.addAll((List<String>) visit(BIB.getChildByText(node.getChildren(), "BLOCK_SCOPE")));
                break;
            case "do":
                res.add("do");
                res.addAll((List<String>) visit(BIB.getChildByText(node.getChildren(), "BLOCK_SCOPE")));
                res.add("while");
                res.addAll((List<String>) visit(BIB.getChildByText(node.getChildren(), "CONDITION")));
                break;
        }
        return res;
    }

    public Object actionOPERATOR(Node<TokenAttributes> node) {
        List<String> res = new ArrayList<>();
        res.add("(");
        res.addAll((List<String>) actionASSIGN_OPERATOR(node));
        res.add(")");
        return res;
    }

    public Object actionASSIGN_OPERATOR(Node<TokenAttributes> node) {
        List<String> res = new ArrayList<>();
        List<String> ss = stringifyChildren(node);
        switch (ss.size()) {
            case 2:
                if ((ss.contains("++") || ss.contains("--")) && ss.contains("NAME")) {
                    if (ss.get(0).equals("NAME")) {
                        res.addAll((List<String>) visit(BIB.getChildByText(node.getChildren(), "NAME")));
                        res.add(ss.get(1));
                    } else {
                        res.add(ss.get(0));
                        res.addAll((List<String>) visit(BIB.getChildByText(node.getChildren(), "NAME")));
                    }
                }
                break;
            case 3:
                res.addAll((List<String>) visit(node.getChildren().get(1)));
                res.add(ss.get(0));
                res.addAll((List<String>) visit(node.getChildren().get(2)));
                break;
            default:
                if (ss.indexOf("[") != -1) {
                    if (ss.indexOf("[") == ss.lastIndexOf("[")) {
                        if (ss.contains("{")) {
                            res.addAll((List<String>) visit(BIB.getChildByText(node.getChildren(), "NAME")));
                            res.add("[");
                            int index = ss.indexOf("[");
                            if (!ss.get(index + 1).equals("]")) {
                                res.addAll((List<String>) visit(node.getChildren().get(index + 1)));
                            }
                            res.add("]");
                            res.add(ss.get(0));
                            res.add("{");
                            List<Node<TokenAttributes>> children = node.getChildren();
                            children.forEach((t) -> {
                                if (getText(t).equals("VALUE")) {
                                    res.addAll((List<String>) visit(t));
                                    res.add(",");
                                }
                            });
                            if (res.get(res.size() - 1).equals(",")) {
                                res.remove(res.size() - 1);
                            }
                            res.add("}");
                        } else if (ss.contains("VALUE")) {
                            res.addAll((List<String>) visit(BIB.getChildByText(node.getChildren(), "NAME")));
                            res.add("[");
                            int index = ss.indexOf("[");
                            if (!ss.get(index + 1).equals("]")) {
                                res.addAll((List<String>) visit(node.getChildren().get(index + 1)));
                            }
                            res.add("]");
                            res.add(ss.get(0));
                            res.addAll((List<String>) visit(node.getChildren().get(ss.indexOf("VALUE"))));
                        } else {
                            int index = ss.indexOf("NAME");
                            if (ss.get(index + 1).equals("NAME")) {
                                res.addAll((List<String>) visit(node.getChildren().get(index)));
                                res.add(ss.get(0));
                                res.addAll((List<String>) visit(node.getChildren().get(index + 1)));
                                res.add("[");
                                index = ss.indexOf("[");
                                if (!ss.get(index + 1).equals("]")) {
                                    res.addAll((List<String>) visit(node.getChildren().get(index + 1)));
                                }
                                res.add("]");
                            } else {
                                res.addAll((List<String>) visit(node.getChildren().get(index)));
                                res.add("[");
                                index = ss.indexOf("[");
                                if (!ss.get(index + 1).equals("]")) {
                                    res.addAll((List<String>) visit(node.getChildren().get(index + 1)));
                                }
                                res.add("]");
                                res.add(ss.get(0));
                                res.addAll((List<String>) visit(node.getChildren().get(node.getChildren().size() - 1)));
                            }
                        }
                    } else {
                        res.addAll((List<String>) visit(node.getChildren().get(1)));
                        int index = ss.indexOf("[");
                        if (ss.get(index + 1).equals("]")) {
                            res.add("[]");
                        } else {
                            res.add("[");
                            res.addAll((List<String>) visit(node.getChildren().get(index + 1)));
                            res.add("]");
                        }
                        res.add(ss.get(0));
                        index = ss.lastIndexOf("[");
                        res.addAll((List<String>) visit(node.getChildren().get(index - 1)));
                        if (ss.get(index + 1).equals("]")) {
                            res.add("[]");
                        } else {
                            res.add("[");
                            res.addAll((List<String>) visit(node.getChildren().get(index + 1)));
                            res.add("]");
                        }
                    }
                }
        }
        return res;
    }

    public Object actionNAME(Node<TokenAttributes> node) {
        List<String> res = new ArrayList<>();
        List<String> ss = stringifyChildren(node);
        if (ss.size() == 1) {
            res.addAll(stringifyEachChildren(BIB.tmapOneRuleCodeCall("last", node)));
        } else {
            res.addAll((List<String>) visit(node.getChildren().get(0)));
            res.add(".");
            res.addAll((List<String>) visit(node.getChildren().get(2)));
        }
        return res;
    }

    public Object actionFUNCTION_CALL(Node<TokenAttributes> node) {
        List<String> res = new ArrayList<>();
        res.addAll((List<String>) visit(node.getChildren().get(0)));
        res.add("(");
        res.addAll((List<String>) visit(node.getChildren().get(1)));
        res.add(")");
        res.add(";");
        return res;
    }

    public Object actionARGUMENT_LIST(Node<TokenAttributes> node) {
        return actionFORMAL_PARAM_LIST(node);
    }
}
