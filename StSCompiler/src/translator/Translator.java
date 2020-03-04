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

    /**
     * converts a given CST into a corresponding eCST.
     * 
     * @param CSTPath
     * @param tmapPath
     * @param initialRuleName
     * @param outputDir
     * @return
     */
    public boolean createeCST(String CSTPath, String tmapPath, String initialRuleName, String outputDir) {
        TreeXMLConverter conv = new TreeXMLConverter();
        if (!conv.convertFromFile(CSTPath)) {
            return false;
        }
        return new TranslationParser(conv.getTree()).start(tmapPath, initialRuleName, outputDir);
    }

    /**
     * adapts a given eCST through the patterns of the output language.
     * 
     * @param eCSTPath
     * @param auxTmapsDir directory of the auxiliar tmap files used to adapt the
     *                    eCST.
     * @param inputLang
     * @param outputLang
     * @return
     */
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
            adp.correctAttrAccess(conv.getTree());
            adp.removeModifierLists(conv.getTree());
        }
        int index = eCSTPath.lastIndexOf(File.separator);
        String xmlPath = eCSTPath.replace(eCSTPath.substring(index + 1), "eCSTadapted.xml");
        String dotPath = eCSTPath.replace(eCSTPath.substring(index + 1), "eCSTadapted.gv");
        return exportCSTDOT(conv.getTree(), dotPath) && exportCSTXML(conv.getTree(), xmlPath);
    }

    /**
     * generates the Object Code from a given already adapted eCST.
     * 
     * @param eCSTPath
     * @param auxTmapsDir directory of the auxiliar tmap files used to convert the
     *                    eCST.
     * @param outputLang
     * @return
     */
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

    /**
     * converts a given simple tree into a DOT version.
     * 
     * @param tree
     * @param outputPath
     * @return
     */
    private boolean exportCSTDOT(Tree<TokenAttributes> tree, String outputPath) {
        return new DOTConverter<>(tree).convertToFile(outputPath);
    }

    /**
     * converts a given simple tree into a XML version.
     * 
     * @param tree
     * @param outputPath
     * @return
     */
    private boolean exportCSTXML(Tree<TokenAttributes> tree, String outputPath) {
        return new XMLConverter(tree).convertToFile(outputPath);
    }

    /**
     * returns the path of the directory that has all the complete or partial tmap
     * code files used in the adaptation process.
     * 
     * @param tmapPath   main tmap path.
     * @param inputLang
     * @param outputLang
     * @return
     */
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

    /**
     * returns the path of the directory that has all the complete or partial tmap
     * code files used in the code writing process.
     * 
     * @param tmapPath
     * @param outputLang
     * @return
     */
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
