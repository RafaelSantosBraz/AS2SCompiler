/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package parser;

import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.TerminalNode;

/**
 *
 * @author Rafael Braz
 */
public class JavaTraListener extends JavaGrammarBaseListener {


    
    @Override
    public void enterEveryRule(ParserRuleContext ctx) {
        
        super.enterEveryRule(ctx); 
    }

    @Override
    public void exitEveryRule(ParserRuleContext ctx) {
        super.exitEveryRule(ctx); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void visitTerminal(TerminalNode node) {
        super.visitTerminal(node); //To change body of generated methods, choose Tools | Templates.
    }

}
