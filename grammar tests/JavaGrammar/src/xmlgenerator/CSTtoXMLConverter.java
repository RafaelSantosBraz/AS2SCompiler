/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package xmlgenerator;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collections;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.antlr.v4.runtime.tree.TerminalNode;
import parser.JavaGrammarBaseListener;
import parser.JavaGrammarParser;

/**
 *
 * @author Rafael Braz
 */
public class CSTtoXMLConverter {

    public static boolean generateXMLFileVersion(JavaGrammarParser parser, ParseTree tree, String fileName) throws IOException {
        ParseTreeWalker.DEFAULT.walk(new JavaGrammarBaseListener() {
            final String INDENT = "    ";
            int level = 0;
            BufferedWriter writer = new BufferedWriter(new FileWriter(fileName, false));

            @Override
            public void enterEveryRule(final ParserRuleContext ctx) {
                try {
                    writer.write(String.format("%s<%s>%n", indent(), parser.getRuleNames()[ctx.getRuleIndex()]));
                    ++level;
                    super.enterEveryRule(ctx);
                } catch (IOException e) {

                }
            }

            @Override
            public void exitEveryRule(final ParserRuleContext ctx) {
                try {
                    --level;
                    writer.write(String.format("%s</%s>%n", indent(), parser.getRuleNames()[ctx.getRuleIndex()]));
                    super.exitEveryRule(ctx);
                    if (level == 0) {
                        // final node -> the root node
                        writer.close();
                    }
                } catch (IOException e) {

                }
            }

            @Override
            public void visitTerminal(final TerminalNode node) {
                try {
                    final String value = node.getText();
                    if (!value.matches("\\s+")) {
                        writer.write(String.format("%s<t>%s</t>%n", indent(), node.getText()));
                    }
                    super.visitTerminal(node);
                } catch (IOException e) {

                }
            }

            private String indent() {
                return String.join("", Collections.nCopies(level, INDENT));
            }

        }, tree);
        return true;
    }

}
