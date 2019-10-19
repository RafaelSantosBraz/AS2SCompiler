/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ecstadapter;

import adapters.CtoJavaAdapter;
import converter.TreeXMLConverter;
import trees.cstecst.TokenAttributes;
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
        CtoJavaAdapter adapter = new CtoJavaAdapter();
        TreeXMLConverter conv = new TreeXMLConverter();
        conv.convertFromFile("D:\\GitHub\\StS-Compilation-Framework\\runtime\\output\\eCST.xml");
        adapter.startWalking(conv.getTree());
    }
    
}
