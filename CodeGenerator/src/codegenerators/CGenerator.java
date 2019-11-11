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
 * aims to translate adapted eCST into an Object Code (C code)
 *
 * @author Rafael Braz
 */
public class CGenerator extends CodeGenerator {

    public CGenerator(String outputPath, String auxTmapsDir) {
        super(outputPath, auxTmapsDir);
    }

    public Object actionCOMPILATION_UNIT(Node<TokenAttributes> node) {
        String name = getText(BIB.tmapOneRuleCodeCall("\"PACKAGE_DECL\".\"CONCRETE_UNIT_DECL\".\"NAME\".child", node).get(0));
        return writeToFile(name, ".c", node);
    }
    
    

}
