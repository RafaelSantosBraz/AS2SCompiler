/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package xmltotree;

import converter.DOTConverter;
import converter.TreeXMLConverter;
import cstecst.TokenAttributes;
import simpletree.Tree;

/**
 *
 * @author Rafael Braz
 */
public class Run {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        TreeXMLConverter conv = new TreeXMLConverter();
        conv.convertFromFile("D:\\GitHub\\StS-Compilation-Framework\\eCSTgenerator\\output\\teste.java_eCST_V1567882491289.xml");
        Tree<TokenAttributes> tree = conv.getTree();   
        DOTConverter<TokenAttributes> dot = new DOTConverter<>(tree);
        dot.convertToFile("D:\\GitHub\\StS-Compilation-Framework\\eCST.gv");
    }
    
}
