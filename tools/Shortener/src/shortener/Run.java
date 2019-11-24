/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shortener;

import converter.DOTConverter;
import converter.TreeXMLConverter;
import shorteners.TreeShortener;
import trees.cstecst.TokenAttributes;
import trees.csttotree.CSTtoTree;
import trees.simpletree.Tree;

/**
 *
 * @author Rafael Braz
 */
public class Run {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // testing
        TreeXMLConverter conv = new TreeXMLConverter();
        conv.convertFromFile("D:\\GitHub\\StS-Compilation-Framework\\runtime\\output\\temp\\CST.xml");
        TreeShortener tShort = new TreeShortener();
        Tree<TokenAttributes> tree = tShort.shortenTree(conv.getTree());
        DOTConverter<TokenAttributes> dot = new DOTConverter<>(tree);
        dot.convertToFile("D:\\GitHub\\StS-Compilation-Framework\\runtime\\output\\temp\\CSTshort.gv");
    }

}
