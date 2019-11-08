/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package codegenerators;

import auxtools.BIB;
import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import trees.cstecst.TokenAttributes;
import trees.cstecst.UniversalToken;
import trees.simpletree.Node;
import walkers.TreeVisitor;

/**
 * aims to translate adapted eCST into an Object Code (Java code)
 *
 * @author Rafael Braz
 */
public class JavaGenerator extends TreeVisitor<Object> {

    private final String auxTmapsDir; // path to all files of partial/complete tmap code    
    private final String outputPath;

    public JavaGenerator(String outputPath, String auxTmapsDir) {
        this.outputPath = outputPath;
        this.auxTmapsDir = auxTmapsDir;
    }

    public Object actionCOMPILATION_UNIT(Node<TokenAttributes> node) {
        String name = BIB.tmapOneRuleCodeCall("\"PACKAGE_DECL\".\"CONCRETE_UNIT_DECL\".\"NAME\".child", node).get(0).getNodeData().getText();
        try {
            File file = new File(outputPath + File.separator + name + ".java");
            System.out.println(file.getPath());
            if (file.exists()) {
                file.delete();
            }
            file.createNewFile();
            PrintWriter curFile = new PrintWriter(new FileOutputStream(file), true);
            List<String> words = (List<String>) visit(node.getChildren().get(0));
            // correct
            words.forEach((t) -> {
                curFile.printf(" %s ", t);
            });
        } catch (Exception e) {
            System.err.println("Error: it was not possible to create files for the Object Code");
        }
        return new ArrayList<>();
    }

    public Object actionCONCRETE_UNIT_DECL(Node<TokenAttributes> node) {
        List<String> res = new ArrayList<>();
        res.add(String.format("public class %s {", node.getChildren().get(0).getChildren().get(0).getNodeData().getText()));
        List<String> body = (List<String>) visitChildren(node.getChildren());
        //body.remove(0);
        res.addAll(body);
        res.add("}");
        return res;
    }

    public Object actionATTRIBUTE_DECL(Node<TokenAttributes> node) {
        ArrayList<String> res = new ArrayList<>();
        res.addAll(stringifyChildren(node.getChildren().get(0)));
        res.addAll(stringifyEachChildren(BIB.tmapOneRuleCodeCall("\"TYPE\".last , \"NAME\".last", node)));
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
        if (children.size() == 1) {
            return visit(children.get(0));
        }
        if (stringifyChildren(node).get(0).equals("{")) {
            res.add("{");
            for (Node<TokenAttributes> n : children) {
                if (getText(n).equals("VALUE")) {
                    res.addAll((List<String>) visit(n));
                    res.add(",");
                }
            }
            if (res.get(res.size() - 1).equals(",")) {
                res.remove(res.size() - 1);
            }
            res.add("}");
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
        res.addAll(stringifyEachChildren(BIB.tmapOneRuleCodeCall("\"TYPE\".last , \"NAME\".last", node)));
        res.add("(");
        res.addAll((List<String>) visit(BIB.getChildByText(node.getChildren(), "FORMAL_PARAM_LIST")));
        res.add(")");
        res.addAll((List<String>) visit(BIB.getChildByText(node.getChildren(), "BLOCK_SCOPE")));
        return res;
    }

    
    public Object actionFORMAL_PARAM_LIST(Node<TokenAttributes> node) {
        List<String> res = new ArrayList<>();

        return res;
    }

    public Object actionBLOCK_SCOPE(Node<TokenAttributes> node) {
        List<String> res = new ArrayList<>();
        res.add("{");
        
        res.add("}");
        return res;
    }

    // remove some items of the generated list
    private void correctList(List<Node<TokenAttributes>> nodes) {
        for (int c = 0; c < nodes.size(); c++) {
            // end of a array instantiation
            if (nodes.get(c).getNodeData().getText().equals(",") && nodes.get(c + 1).getNodeData().getText().equals("}")) {
                nodes.remove(c);
            }
        }
    }

    // returns a list of strings from the given node's children
    private List<String> stringifyEachChildren(List<Node<TokenAttributes>> nodes) {
        Node<TokenAttributes> parent = new Node<>(new UniversalToken("aux", -1));
        parent.setChildren(nodes);
        return stringifyChildren(parent);
    }

    // returns a list of strings from the given node's children
    private List<String> stringifyChildren(Node<TokenAttributes> node) {
        List<String> fy = new ArrayList<>();
        List<Node<TokenAttributes>> children = node.getChildren();
        for (int c = 0; c < children.size(); c++) {
            Node<TokenAttributes> n = children.get(c);
            String txt = getText(n);
            if (txt.equals("OPERATOR") && stringifyChildren(n).size() == 1) {
                fy.add(stringifyChildren(n).get(0));
            } else if (txt.equals("SEPARATOR") && stringifyChildren(n).size() == 1) {
                fy.add(stringifyChildren(n).get(0));
            } else {
                fy.add(getText(n));
            }
        }
        return fy;
    }

    // returns the text of the node
    private String getText(Node<TokenAttributes> node) {
        return node.getNodeData().getText();
    }

    @Override
    // aggregate the results os all the children nodes (two at a time)
    protected Object aggregateResult(Object aggregate, Object nextResult) {
        //System.out.println(aggregate + " " + nextResult);
        if (aggregate == null && nextResult == null) {
            return null;
        }
        if (nextResult == null) {
            return aggregate;
        }
        if (aggregate == null) {
            return nextResult;
        }
        List<String> res = new ArrayList<>();
        res.addAll((List<String>) aggregate);
        res.addAll((List<String>) nextResult);
        return res;
    }

    /*
    public Object actionFORMAL_PARAM_LIST(Node<TokenAttributes> node) {
        List<String> res = new ArrayList<>();
        
        return res;
    }
     */
}
