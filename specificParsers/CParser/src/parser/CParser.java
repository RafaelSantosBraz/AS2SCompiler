/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package parser;

import java.io.File;
import java.io.IOException;
import java.util.List;
import org.antlr.v4.runtime.ANTLRFileStream;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.TokenStream;
import parsers.SpecificParser;
import trees.cstecst.TokenAttributes;
import trees.cstecst.UniversalToken;
import trees.simpletree.Node;

/**
 * aims to analyze C code and generates a CST
 *
 * @author Rafael Braz
 */
public class CParser extends SpecificParser {

    // starts parsing the source-code from a directory
    @Override
    public boolean startParsing(String inputDir, String outputDir) {
        try {
            File directory = new File(inputDir);
            File outputDirectory = new File(outputDir);
            File[] files = directory.listFiles((File f) -> {
                return f.isFile() && (f.getName().endsWith(".c") || f.getName().endsWith(".h"));
            });
            for (File f : files) {
                if (!syntaxChecking(f)) {
                    return false;
                }
            }
            treeUnion(getTrees());
            addCompilationUnitNames(files);
            return exportCSTDOT(tree, outputDirectory.getAbsolutePath() + File.separator + "CST.gv") && exportCSTXML(tree, outputDirectory.getAbsolutePath() + File.separator + "CST.xml");
        } catch (RecognitionException e) {
            return false;
        }
    }

    // syntactically check the code through the ANTLR4 recognition
    private boolean syntaxChecking(File f) {
        try {
            CharStream stream = new ANTLRFileStream(f.getAbsolutePath(), "UTF-8");
            CGrammarLexer lexer = new CGrammarLexer(stream);
            TokenStream tokens = new CommonTokenStream(lexer);
            CGrammarParser parser = new CGrammarParser(tokens);
            rootRules.add(parser.compilationUnit());
            return true;
        } catch (IOException | RecognitionException e) {
            return false;
        }
    }

    private void addCompilationUnitNames(File files[]) {
        List<Node<TokenAttributes>> comps = tree.getRoot().getChildren();
        for (int c = 0; c < files.length ; c++) {
            File f = files[c];                        
            String name = f.getName().substring(0, f.getName().lastIndexOf('.'));  
            Node<TokenAttributes> child = new Node<>(comps.get(c));
            child.setNodeData(new UniversalToken("NAME", -1));
            Node<TokenAttributes> childName = new Node<>(child);
            childName.setNodeData(new UniversalToken(name, -1));
            child.addChild(childName);
            comps.get(c).addChildAt(child, 0);
        }
    }
}
