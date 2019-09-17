/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package parser;

import converter.DOTConverter;
import interpreter.TranslationVisitor;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import org.antlr.v4.runtime.ANTLRFileStream;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.RuleContext;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.TerminalNodeImpl;
import trees.cstecst.TokenAttributes;
import trees.simpletree.Node;
import trees.simpletree.Tree;

/**
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

    public boolean start(String tmapPath, String firstRuleName) {
        try {
            CharStream stream = new ANTLRFileStream(tmapPath);
            TranslationGrammarLexer lexer = new TranslationGrammarLexer(stream);
            TokenStream tokens = new CommonTokenStream(lexer);
            TranslationGrammarParser parser = new TranslationGrammarParser(tokens);
            TranslationVisitor t = new TranslationVisitor(CST.getRoot());
            eCST = t.start(parser.prog(), firstRuleName);
            return exportDOTeCST();
        } catch (IOException | RecognitionException e) {
            return false;
        }
    }

    public Tree<TokenAttributes> geteCST() {
        return eCST;
    }
    
    private boolean exportDOTeCST(){
        DOTConverter<TokenAttributes> conv = new DOTConverter<>(eCST);
        return conv.convertToFile("eCST.gv");
    }

}
