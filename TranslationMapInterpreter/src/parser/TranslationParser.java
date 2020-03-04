/*
 * The MIT License - https://github.com/RafaelSantosBraz/AS2SCompiler/blob/master/LICENSE
 * Copyright 2020 Rafael Braz.
 */
package parser;

import converter.DOTConverter;
import converter.XMLConverter;
import interpreter.TranslationVisitor;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import org.antlr.v4.runtime.ANTLRFileStream;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import trees.cstecst.TokenAttributes;
import trees.simpletree.Node;
import trees.simpletree.Tree;

/**
 * aims to convert a CST into a eCST by a given tmap code
 *
 * @author Rafael Braz
 */
public class TranslationParser {

    private Tree<TokenAttributes> eCST;
    private final Tree<TokenAttributes> CST;
    private final ArrayList<ParseTree> rules;

    public TranslationParser(Tree<TokenAttributes> CST) {
        this.CST = CST;
        eCST = new Tree<>();
        rules = new ArrayList<>();
    }

    // converts a CST into a eCST by using a tmap code on a file, exporting the result as a XML file
    public boolean start(String tmapPath, String firstRuleName, String outputDir) {
        try {
            CharStream stream = new ANTLRFileStream(tmapPath);
            TranslationGrammarLexer lexer = new TranslationGrammarLexer(stream);
            TokenStream tokens = new CommonTokenStream(lexer);
            TranslationGrammarParser parser = new TranslationGrammarParser(tokens);
            TranslationVisitor t = new TranslationVisitor(CST.getRoot());
            eCST = t.start(parser.prog(), firstRuleName);
            return (exportDOTeCST(outputDir + File.separator + "eCST.gv") && exportXMLeCST(eCST, outputDir + File.separator + "eCST.xml"));
        } catch (IOException | RecognitionException e) {
            return false;
        }
    }

    // converts a CST into a eCST by using a tmap code as a String, returning the built tree
    public static Tree<TokenAttributes> startFromString(String tmapCode, String firstRuleName, Node<TokenAttributes> currentNode) {
        try {
            CharStream stream = new ANTLRInputStream(tmapCode);
            TranslationGrammarLexer lexer = new TranslationGrammarLexer(stream);
            TokenStream tokens = new CommonTokenStream(lexer);
            TranslationGrammarParser parser = new TranslationGrammarParser(tokens);
            TranslationVisitor t = new TranslationVisitor(currentNode);
            return t.start(parser.prog(), firstRuleName);
        } catch (RecognitionException e) {
            return null;
        }
    }

    public Tree<TokenAttributes> geteCST() {
        return eCST;
    }

    private boolean exportDOTeCST(String GVeCSTPath) {
        DOTConverter<TokenAttributes> conv = new DOTConverter<>(eCST);
        return conv.convertToFile(GVeCSTPath);
    }

    private boolean exportXMLeCST(Tree<TokenAttributes> tree, String outputPath) {
        return new XMLConverter(tree).convertToFile(outputPath);
    }

}
