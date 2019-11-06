/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package codegenerator;

import codegenerators.JavaGenerator;
import converter.DOTConverter;
import converter.TreeXMLConverter;

/**
 *
 * @author Rafael Braz
 */
public class Run {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        JavaGenerator generator = new JavaGenerator(
                "D:\\GitHub\\StS-Compilation-Framework\\runtime\\output\\objcode",
                "D:\\GitHub\\StS-Compilation-Framework\\runtime\\Tmaps\\writeJava"
        );
        TreeXMLConverter conv = new TreeXMLConverter();
        conv.convertFromFile("D:\\GitHub\\StS-Compilation-Framework\\runtime\\output\\eCSTadapted.xml");
        System.out.println(generator.start(conv.getTree()));   
        //DOTConverter dot = new DOTConverter(conv.getTree());
        //dot.convertToFile("D:\\GitHub\\StS-Compilation-Framework\\runtime\\output\\eCSTAdapted.gv");      
    }
    
}