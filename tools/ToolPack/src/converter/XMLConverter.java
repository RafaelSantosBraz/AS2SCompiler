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
import trees.simpletree.*;

/**
 *
 * @author Rafael Braz
 */
public class XMLConverter {

    private Tree<TokenAttributes> tree;

    public XMLConverter(Tree<TokenAttributes> tree) {
        if (tree == null) {
            this.tree = new Tree<>();
        } else {
            this.tree = tree;
        }
    }

    public boolean convertToFile(String outputPath) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputPath, false))) {
            writer.write("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>");
            writer.newLine();
            return convertion(tree.getRoot(), writer);
        } catch (IOException e) {
            return false;
        }
    }

    private boolean convertion(Node<TokenAttributes> node, BufferedWriter writer) {
        try {
            if (node != null && node.getNodeData() != null) {
                writer.write("<childElement>");
                writer.newLine();
                writer.write("<token column=\"" + node.getNodeData().getColumn() + "\" index=\"" + node.getNodeData().getIndex() + "\" line=\"" + node.getNodeData().getLine() + "\" text=\'" + node.getNodeData().getText() + "\' type=\"" + node.getNodeData().getType() + "\"/>");
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
