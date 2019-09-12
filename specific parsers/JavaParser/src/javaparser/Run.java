/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javaparser;

import converter.TreeXMLConverter;
import converter.XMLDOTConverter;
import parser.JavaParser;

/**
 *
 * @author Rafael Braz
 */
public class Run {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        JavaParser parser = new JavaParser();
        parser.startParsing(
                "D:\\GitHub\\StS-Compilation-Framework\\specific parsers\\JavaParser\\test\\input",
                "D:\\GitHub\\StS-Compilation-Framework\\specific parsers\\JavaParser\\test\\output"
        );
        new XMLDOTConverter().convertFromFile("", "");
    }

}
