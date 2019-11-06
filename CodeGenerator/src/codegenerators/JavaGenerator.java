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
import java.util.List;
import trees.cstecst.TokenAttributes;
import trees.simpletree.Node;
import trees.simpletree.Tree;

/**
 * aims to translate adapted eCST into an Object Code (Java code)
 *
 * @author Rafael Braz
 */
public class JavaGenerator {

    private final String outputPath; // directory path to export the object code
    private final String auxTmapsDir; // path to all files of partial/complete tmap code

    public JavaGenerator(String outputPath, String auxTmapsDir) {
        this.outputPath = outputPath;
        this.auxTmapsDir = auxTmapsDir;
    }

    // starts to generate Java code for the given eCST
    public boolean start(Tree<TokenAttributes> eCST) {
        for (Node<TokenAttributes> n : eCST.getRoot().getChildren()) {
            if (!actionCOMPILATION_UNIT(n)) {
                return false;
            }
        }
        return true;
    }

    // creates a ".java" file for the compilation unit and writes all the class inside it
    public boolean actionCOMPILATION_UNIT(Node<TokenAttributes> node) {
        Node<TokenAttributes> name = node.getChildren().get(0).getChildren().get(0).getChildren().get(0).getChildren().get(0);
        try {
            File file = new File(outputPath + File.separator + name.getNodeData().getText() + ".java");
            System.out.println(file.getPath());
            if (file.exists()) {
                file.delete();
            }
            file.createNewFile();
            PrintWriter curFile = new PrintWriter(new FileOutputStream(file), true);
            String tmapCode = BIB.getTmapCodeFromFile(auxTmapsDir, "JavaCode.tmap");
            List<Node<TokenAttributes>> nodes = BIB.tmapCompleteCodeCall(tmapCode, "\"rule_concrete_unit\"", node.getChildren().get(0).getChildren().get(0));
            nodes.forEach((t) -> {
                curFile.printf(" " + t.getNodeData().getText() + " ");
            });
            return true;
        } catch (Exception e) {
            System.err.println("Error: it was not possible to create files for the Object Code");
        }
        return false;
    }

}
