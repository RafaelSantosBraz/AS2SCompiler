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
import walkers.TreeVisitor;

/**
 * aims to translate adapted eCST into an Object Code (Java code)
 *
 * @author Rafael Braz
 */
public class JavaGenerator extends TreeVisitor<Object> {

    private PrintWriter curFile;
    private final String outputPath;

    public JavaGenerator(String outputPath) {
        curFile = null;
        this.outputPath = outputPath;
    }

    public Object actionCOMPILATION_UNIT(Node<TokenAttributes> node) {
        Node<TokenAttributes> name = node.getChildren().get(0).getChildren().get(0).getChildren().get(0).getChildren().get(0);
        try {
            File file = new File(outputPath + File.separator + name.getNodeData().getText() + ".java");
            System.out.println(file.getPath());
            if (file.exists()) {
                file.delete();
            }
            file.createNewFile();
            curFile = new PrintWriter(new FileOutputStream(file), true);
            visit(node.getChildren().get(0));
        } catch (Exception e) {
            System.err.println("Error: it was not possible to create files for the Object Code");
        }
        return null;
    }

    public Object actionCONCRETE_UNIT_DECL(Node<TokenAttributes> node) {
        curFile.printf("public class %s { \n", BIB.tmapOneRuleCodeCall("\"NAME\".child", node).get(0).getNodeData().getText());
        visitChildren(node.getChildren());
        curFile.println("}");
        return null;
    }

    public Object actionATTRIBUTE_DECL(Node<TokenAttributes> node) {
        String modifiers = (String) visit(BIB.getChildByText(node.getChildren(), "MODIFIER_LIST"));
        String type = BIB.getChildByText(node.getChildren(), "TYPE").getChildren().get(0).getNodeData().getText();
        String name = BIB.getChildByText(node.getChildren(), "NAME").getChildren().get(0).getNodeData().getText();
        curFile.printf("%s %s %s ", modifiers, type, name);
        if (BIB.getChildByText(node.getChildren(), "SEPARATOR") != null) {
            curFile.print(" []");
        }
        String value = (String) visit(BIB.getChildByText(node.getChildren(), "VALUE"));
        if (value.isEmpty()){
            curFile.println(";");
        } else {
            curFile.println(" = " + value + ";");
        }        
        return null;
    }

    public Object actionMODIFIER_LIST(Node<TokenAttributes> node) {
        String mod = "";
        for (Node<TokenAttributes> n : node.getChildren()) {
            mod += n.getNodeData().getText() + " ";
        }
        return mod;
    }
    
    public Object actionVALUE(Node<TokenAttributes> node) {
        String val = "";
        List<Node<TokenAttributes>> children = node.getChildren();
        if (!children.isEmpty()){
            if (children.size() == 1){
                if (children.get(0).getNodeData().getText().equals("CONST")){
                    return children.get(0).getChildren().get(0).getNodeData().getText();
                }
            }
        }
        return val;
    }

}
