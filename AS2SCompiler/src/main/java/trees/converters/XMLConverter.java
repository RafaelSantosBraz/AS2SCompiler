/*
 * The MIT License - https://github.com/RafaelSantosBraz/AS2SCompiler/blob/master/LICENSE
 * Copyright 2020 Rafael Braz.
 */
package trees.converters;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import trees.*;

/**
 * converts a simple tree to a corresponding XML file
 *
 * @author Rafael Braz
 */
public class XMLConverter {

    /**
     * Simple tree to be converted.
     */
    private Tree<TokenAttributes> tree;

    public XMLConverter(Tree<TokenAttributes> tree) {
        if (tree == null) {
            this.tree = new Tree<>();
        } else {
            this.tree = tree;
        }
    }

    /**
     * Starts converting to XML.
     *
     * @param outputPath
     * @return
     */
    public boolean convertToFile(String outputPath) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputPath, false))) {
            writer.write("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>");
            writer.newLine();
            return convertion(tree.getRoot(), writer);
        } catch (IOException e) {
            return false;
        }
    }

    /**
     * recursively converts the nodes.
     *
     * @param node current node to be converted.
     * @param writer file writer.
     * @return
     */
    private boolean convertion(Node<TokenAttributes> node, BufferedWriter writer) {
        try {
            if (node != null && node.getNodeData() != null) {
                writer.write("<childElement>");
                writer.newLine();
                writer.write("<token column=\"" + node.getNodeData().getColumn() + "\" index=\"" + node.getNodeData().getIndex() + "\" line=\"" + node.getNodeData().getLine() + "\" text=" + normalizeLessThan(node.getNodeData().getText()) + " type=\"" + node.getNodeData().getType() + "\"/>");
                writer.newLine();
                if (!node.getChildren().isEmpty()) {
                    node.getChildren().forEach((n) -> {
                        convertion(n, writer);
                    });
                }
                writer.write("</childElement>");
                writer.newLine();
            }
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    /**
     * rewrite all the forbiden tokens to a XML version.
     *
     * @param text
     * @return
     */
    private String normalizeLessThan(String text) {
        String txtTemp = text.replace("<", "&#60;");
        txtTemp = txtTemp.replace("&", "&#38;");
        //txtTemp = txtTemp.replace("\\n", "\\\\n");
        // txtTemp = txtTemp.replace("\\n", "\\n");
        if (txtTemp.charAt(0) == '\'') {
            txtTemp = "\"" + txtTemp + "\"";
        } else {
            txtTemp = "\'" + txtTemp + "\'";
        }
        return txtTemp;
    }

    public Tree<TokenAttributes> getTree() {
        return tree;
    }

    public void setTree(Tree<TokenAttributes> tree) {
        if (tree == null) {
            this.tree = new Tree<>();
            return;
        }
        this.tree = tree;
    }

}
