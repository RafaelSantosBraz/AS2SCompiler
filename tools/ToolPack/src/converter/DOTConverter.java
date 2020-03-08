/*
 * The MIT License - https://github.com/RafaelSantosBraz/AS2SCompiler/blob/master/LICENSE
 * Copyright 2020 Rafael Braz.
 */
package converter;

import trees.cstecst.TokenAttributes;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import trees.simpletree.*;

/**
 * converts a simple tree structure to a corresponding Graphviz file (.gv)
 *
 * @author Rafael Braz
 * @param <T>
 */
public class DOTConverter<T> {

    /**
     * Simple tree to be converted.
     */
    private Tree<T> tree;
    /**
     * An one-dimension array that represents the tree.
     */
    private final List<Node<T>> treeAsList;

    public DOTConverter(Tree<T> tree) {
        if (tree == null) {
            this.tree = new Tree<>();
        } else {
            this.tree = tree;
        }
        treeAsList = this.tree.getTreeAsIndexOrderedList();
    }

    /**
     * starts the convertion to Graphviz (DOT).
     *
     * @param outputPath
     * @return
     */
    public boolean convertToFile(String outputPath) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputPath, false))) {
            writer.write("digraph G {");
            writer.newLine();
            writer.write("\tsplines=\"TRUE\";");
            writer.newLine();
            if (convertion(tree.getRoot(), writer)) {
                writer.write("}");
                writer.close();
                return true;
            }
            return false;
        } catch (IOException e) {
            return false;
        }
    }

    /**
     * recursively converts the nodes to graphviz.
     *
     * @param node node to start the convertion.
     * @param writer file writer.
     * @return
     */
    private boolean convertion(Node<T> node, BufferedWriter writer) {
        try {
            if (node != null && node.getNodeData() != null) {
                T nodeData = node.getNodeData();
                if (nodeData instanceof TokenAttributes) {
                    TokenAttributes token = (TokenAttributes) nodeData;
                    if (!node.getChildren().isEmpty()) {
                        writer.write("\tn_" + treeAsList.indexOf(node) + " [label=\"" + normalizeText(token.getText()) + "\", shape=\"rectangle\"]");
                        writer.newLine();
                        for (Node<T> n : node.getChildren()) {
                            writer.write("\tn_" + treeAsList.indexOf(node) + " -> n_" + treeAsList.indexOf(n));
                            writer.newLine();
                            convertion(n, writer);
                        }
                    } else {
                        writer.write("\tn_" + treeAsList.indexOf(node) + " [label=\"" + normalizeText(token.getText()) + "\", shape=\"ellipse\"]");
                        writer.newLine();
                    }
                }
            }
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    /**
     * adapts the \" and \n from the source to the gv file.
     *
     * @param text
     * @return
     */
    private String normalizeText(String text) {
        String txtTemp = text.replace("\\", "\\\\");
        txtTemp = txtTemp.replace("\"", "\\\"");
        // comp. previous executions        
        //txtTemp = txtTemp.replace("\\\\n", "\\n");        
        return txtTemp;
    }

    public Tree<T> getTree() {
        return tree;
    }

    public void setTree(Tree<T> tree) {
        if (tree == null) {
            this.tree = new Tree<>();
            return;
        }
        this.tree = tree;
    }
}
