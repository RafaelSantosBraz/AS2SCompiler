/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package translator;

import adapters.CtoJavaAdapter;
import adapters.JavatoCAdapter;
import analyzer.Analyzer;
import converter.DOTConverter;
import converter.TreeXMLConverter;
import converter.XMLConverter;
import java.io.File;
import parser.JavaParser;
import parser.TranslationParser;
import trees.cstecst.TokenAttributes;
import trees.simpletree.Tree;
import walkers.ActionWalker;

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
        return new TranslationParser(conv.getTree()).start(tmapPath, initialRuleName, outputDir);
    }

    public boolean adapteCST(String eCSTPath, String inputLang, String outputLang) {
        TreeXMLConverter conv = new TreeXMLConverter();
        if (!conv.convertFromFile(eCSTPath)) {
            return false;
        }
        ActionWalker adapter = null;
        if (inputLang.equals(Analyzer.C) && outputLang.equals(Analyzer.JAVA)) {
            adapter = new CtoJavaAdapter();
        }
        if (inputLang.equals(Analyzer.JAVA) && outputLang.equals(Analyzer.C)) {
            adapter = new JavatoCAdapter();
        }
        if (adapter == null) {
            return true;
        }
        adapter.startWalking(conv.getTree());
        int index = eCSTPath.lastIndexOf(File.separator);
        String xmlPath = eCSTPath.replace(eCSTPath.substring(index + 1), "eCSTadapted.xml");
        String dotPath = eCSTPath.replace(eCSTPath.substring(index + 1), "eCSTadapted.gv");
        return exportCSTDOT(conv.getTree(), dotPath) && exportCSTXML(conv.getTree(), xmlPath);
    }

    private boolean exportCSTDOT(Tree<TokenAttributes> tree, String outputPath) {
        return new DOTConverter<>(tree).convertToFile(outputPath);
    }

    private boolean exportCSTXML(Tree<TokenAttributes> tree, String outputPath) {
        return new XMLConverter(tree).convertToFile(outputPath);
    }
}
