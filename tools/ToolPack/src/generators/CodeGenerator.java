/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package generators;

import java.util.ArrayList;
import java.util.List;
import trees.cstecst.TokenAttributes;
import trees.cstecst.UniversalToken;
import trees.simpletree.Node;
import walkers.TreeVisitor;

/**
 * provides methods to code generators like JavaGenerator and CGenerator
 *
 * @author Rafael Braz
 */
public abstract class CodeGenerator extends TreeVisitor<Object> {

    protected final String auxTmapsDir; // path to all files of partial/complete tmap code    
    protected final String outputPath; // path to output directory

    public CodeGenerator(String outputPath, String auxTmapsDir) {
        this.outputPath = outputPath;
        this.auxTmapsDir = auxTmapsDir;
    }

    // returns a list of strings from the given node's children
    protected List<String> stringifyEachChildren(List<Node<TokenAttributes>> nodes) {
        Node<TokenAttributes> parent = new Node<>(new UniversalToken("aux", -1));
        parent.setChildren(nodes);
        return stringifyChildren(parent);
    }

    // returns a list of strings from the given node's children
    protected List<String> stringifyChildren(Node<TokenAttributes> node) {
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
    protected String getText(Node<TokenAttributes> node) {
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

    public String getAuxTmapsDir() {
        return auxTmapsDir;
    }

    public String getOutputPath() {
        return outputPath;
    }

}
