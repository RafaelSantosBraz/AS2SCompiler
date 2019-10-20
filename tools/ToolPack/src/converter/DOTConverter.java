/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package converter;

import trees.cstecst.TokenAttributes;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import trees.simpletree.*;

/**
 *
 * @author Rafael Braz
 * @param <T>
 */
public class DOTConverter<T> {

    private Tree<T> tree;
    private final List<Node<T>> treeAsList;

    public DOTConverter(Tree<T> tree) {
        if (tree == null) {
            this.tree = new Tree<>();
        } else {
            this.tree = tree;
        }
        treeAsList = this.tree.getTreeAsIndexOrderedList();
    }

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
