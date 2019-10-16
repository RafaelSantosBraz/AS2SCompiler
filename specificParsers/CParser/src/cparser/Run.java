/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cparser;

import converter.XMLDOTConverter;
import parser.CParser;

/**
 *
 * @author Rafael Braz
 */
public class Run {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        CParser parser = new CParser();
        parser.startParsing(
                "D:\\GitHub\\StS-Compilation-Framework\\specific parsers\\JavaParser\\test\\input",
                "D:\\GitHub\\StS-Compilation-Framework\\specific parsers\\JavaParser\\test\\output"
        );
        new XMLDOTConverter().convertFromDir(
                "D:\\GitHub\\StS-Compilation-Framework\\specific parsers\\JavaParser\\test\\temp\\output",
                "D:\\GitHub\\StS-Compilation-Framework\\specific parsers\\JavaParser\\test\\ecst"
        );
    }
    
}
