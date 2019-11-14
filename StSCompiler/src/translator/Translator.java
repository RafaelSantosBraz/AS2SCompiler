/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package translator;

import adapters.CtoJavaAdapter;
import adapters.JavatoCAdapter;
import analyzer.Analyzer;
import codegenerators.CGenerator;
import codegenerators.JavaGenerator;
import converter.DOTConverter;
import converter.TreeXMLConverter;
import converter.XMLConverter;
import java.io.File;
import parser.TranslationParser;
import trees.cstecst.TokenAttributes;
import trees.simpletree.Tree;
import walkers.ActionWalker;

/**
 * represents the second mechanism of the framework - encapsule the
 * convertion/translation process
 *
 * @author Rafael Braz
 */
public class Translator {

    // converts a given CST into a corresponding eCST
    public boolean createeCST(String CSTPath, String tmapPath, String initialRuleName, String outputDir) {
        TreeXMLConverter conv = new TreeXMLConverter();
        if (!conv.convertFromFile(CSTPath)) {
            return false;
        }
        return new TranslationParser(conv.getTree()).start(tmapPath, initialRuleName, outputDir);
    }

    // adapts a given eCST by the patterns of the output language
    public boolean adapteCST(String eCSTPath, String auxTmapsDir, String inputLang, String outputLang) {
        TreeXMLConverter conv = new TreeXMLConverter();
        if (!conv.convertFromFile(eCSTPath)) {
            return false;
        }
        ActionWalker adapter = null;
        if (inputLang.equals(Analyzer.C) && outputLang.equals(Analyzer.JAVA)) {
            adapter = new CtoJavaAdapter(auxTmapsDir);
        } else if (inputLang.equals(Analyzer.JAVA) && outputLang.equals(Analyzer.C)) {
            adapter = new JavatoCAdapter(auxTmapsDir);
        }
        if (adapter == null) {
            return false;
        }
        adapter.startWalking(conv.getTree());        
        if (adapter instanceof JavatoCAdapter) {
            JavatoCAdapter adp = (JavatoCAdapter) adapter;
            adp.createConstructors();
            adp.correctFuncCalls();
            adp.createFuncPrototypes();
            adp.createStructs();            
        }
        int index = eCSTPath.lastIndexOf(File.separator);        
        String xmlPath = eCSTPath.replace(eCSTPath.substring(index + 1), "eCSTadapted.xml");
        String dotPath = eCSTPath.replace(eCSTPath.substring(index + 1), "eCSTadapted.gv");
        return exportCSTDOT(conv.getTree(), dotPath) && exportCSTXML(conv.getTree(), xmlPath);
    }

    // generates object code from a fiven adapted eCST
    public boolean gerateCode(String eCSTPath, String auxTmapsDir, String outputLang) {
        TreeXMLConverter conv = new TreeXMLConverter();
        if (!conv.convertFromFile(eCSTPath)) {
            return false;
        }
        int index = eCSTPath.lastIndexOf(File.separator);
        String objCodePath = eCSTPath.replace(eCSTPath.substring(index + 1), "objcode");
        if (outputLang.equals(Analyzer.C)) {
            new CGenerator(objCodePath, auxTmapsDir).startWalking(conv.getTree());
            return true;
        } else if (outputLang.equals(Analyzer.JAVA)) {
            new JavaGenerator(objCodePath, auxTmapsDir).startWalking(conv.getTree());
            return true;
        }
        return false;
    }

    private boolean exportCSTDOT(Tree<TokenAttributes> tree, String outputPath) {
        return new DOTConverter<>(tree).convertToFile(outputPath);
    }

    private boolean exportCSTXML(Tree<TokenAttributes> tree, String outputPath) {
        return new XMLConverter(tree).convertToFile(outputPath);
    }

    // returns the path to the directory that has all the complete/partial tmap code files used in the adaptation process
    public static String inferAuxTmapsDir(String tmapPath, String inputLang, String outputLang) {
        int index = tmapPath.lastIndexOf(File.separator);
        String dir = null;
        if (inputLang.equals(Analyzer.C) && outputLang.equals(Analyzer.JAVA)) {
            dir = tmapPath.replace(tmapPath.substring(index + 1), "CtoJava");
        } else if (inputLang.equals(Analyzer.JAVA) && outputLang.equals(Analyzer.C)) {
            dir = tmapPath.replace(tmapPath.substring(index + 1), "JavatoC");
        }
        return dir;
    }

    // returns the path to the directory that has all the complete/partial tmap code files used in the writing process
    public static String inferAuxWriteTmapsDir(String tmapPath, String outputLang) {
        int index = tmapPath.lastIndexOf(File.separator);
        String dir = null;
        switch (outputLang) {
            case Analyzer.JAVA:
                dir = tmapPath.replace(tmapPath.substring(index + 1), "writeJava");
                break;
            case Analyzer.C:
                dir = tmapPath.replace(tmapPath.substring(index + 1), "writeC");
                break;
        }
        return dir;
    }
}
