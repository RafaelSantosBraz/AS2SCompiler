/*
 * The MIT License - https://github.com/RafaelSantosBraz/AS2SCompiler/blob/master/LICENSE
 * Copyright 2020 Rafael Braz.
 */
package translationmapparser;

import converter.TreeXMLConverter;
import parser.*;

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
        conv.convertFromFile("D:\\GitHub\\StS-Compilation-Framework\\specific parsers\\JavaParser\\test\\output\\CST.xml");
        TranslationParser t = new TranslationParser(conv.getTree());
        boolean resp = t.start("D:\\GitHub\\StS-Compilation-Framework\\TranslationMapInterpreter\\Complete Java CST to eCST.tmap.txt", "\"ruleinitial\"", "D:\\GitHub\\StS-Compilation-Framework\\specific parsers\\JavaParser\\test\\output");
        System.out.println(resp);    
    }

}
