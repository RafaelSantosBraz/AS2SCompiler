/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package translator;

import analyzer.Analyzer;
import converter.TreeXMLConverter;
import java.io.File;
import parser.JavaParser;
import parser.TranslationParser;

/**
 *
 * @author Rafael Braz
 */
public class Translator {

    public boolean createeCST(String CSTPath, String tmapPath, String initialRuleName, String outputDir) {
        TreeXMLConverter conv = new TreeXMLConverter();
        if (!conv.convertFromFile(CSTPath)) {
            return false;
        }
        return new TranslationParser(conv.getTree()).start(tmapPath, initialRuleName);
    }

}
