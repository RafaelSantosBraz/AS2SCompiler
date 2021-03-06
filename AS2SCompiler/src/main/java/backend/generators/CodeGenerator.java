/*
 * The MIT License - https://github.com/RafaelSantosBraz/AS2SCompiler/blob/master/LICENSE
 * Copyright 2020 Rafael Braz.
 */
package backend.generators;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import trees.*;
import walkers.TreeVisitor;

/**
 * provides methods to code generators like JavaGenerator and CGenerator
 *
 * @author Rafael Braz
 */
public abstract class CodeGenerator extends TreeVisitor<Object> {

    /**
     * path to all files of partial/complete tmap code.
     */
    protected final String auxTmapsDir;
    /**
     * path to output directory.
     */
    protected final String outputPath;

    public CodeGenerator(String outputPath, String auxTmapsDir) {
        this.outputPath = outputPath;
        this.auxTmapsDir = auxTmapsDir;
    }

    /**
     * returns a list of strings from the given node's children.
     *
     * @param nodes
     * @return
     */
    protected List<String> stringifyEachChildren(List<Node<TokenAttributes>> nodes) {
        Node<TokenAttributes> parent = new Node<>(new UniversalToken("aux", -1));
        parent.setChildren(nodes);
        return stringifyChildren(parent);
    }

    /**
     * returns a list of strings from the children of a given node.
     *
     * @param node
     * @return
     */
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

    /**
     * returns the text of the node.
     *
     * @param node
     * @return
     */
    protected String getText(Node<TokenAttributes> node) {
        return node.getNodeData().getText();
    }

    /**
     * aggregate the results os all the children nodes (two at a time).
     *
     * @param aggregate first result.
     * @param nextResult second result.
     * @return
     */
    @Override
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

    /**
     * remove ';' from wrong places.
     *
     * @param words
     * @return
     */
    protected List<String> correctList(List<String> words) {
        if (words != null) {
            for (int c = 0; c < words.size() - 1; c++) {
                if (words.get(c).equals(";")) {
                    if (words.get(c + 1).equals(";") || words.get(c + 1).equals("]") || words.get(c + 1).equals(")") || words.get(c + 1).equals(",")) {
                        words.remove(c--);
                    }
                } else if (words.get(c).equals(",")) {
                    if (words.get(c + 1).equals(",") || words.get(c + 1).equals("]") || words.get(c + 1).equals(")") || words.get(c + 1).equals(";")) {
                        words.remove(c--);
                    }
                }
            }
        }
        return words;
    }

    /**
     * encapsules the process of calling the visitor and writing the output on a
     * file.
     *
     * @param fileName
     * @param fileExtension
     * @param node first node of the tree to be written.
     * @return
     */
    protected Object writeToFile(String fileName, String fileExtension, Node<TokenAttributes> node) {
        try {
            File file = new File(outputPath + File.separator + fileName + fileExtension);
            if (file.exists()) {
                file.delete();
            }
            file.createNewFile();
            PrintWriter curFile = new PrintWriter(new FileOutputStream(file), true);
            List<String> words = correctList((List<String>) visit(node.getChildren().get(0)));
            words.forEach((t) -> {
                curFile.printf(" %s ", t);
            });
            System.out.println("Object Code: " + file.getPath());
        } catch (Exception e) {            
            System.err.println("Error: it was not possible to create files for the Object Code");
        }
        return new ArrayList<>();
    }

    public String getAuxTmapsDir() {
        return auxTmapsDir;
    }

    public String getOutputPath() {
        return outputPath;
    }

}
