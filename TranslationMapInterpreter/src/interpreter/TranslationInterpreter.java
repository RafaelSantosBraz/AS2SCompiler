/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package interpreter;

import trees.cstecst.TokenAttributes;
import trees.simpletree.Tree;

/**
 *
 * @author Rafael Braz
 */
public class TranslationInterpreter {

    private final Tree<TokenAttributes> eCST;
    private final Tree<TokenAttributes> CST;

    public TranslationInterpreter(Tree<TokenAttributes> CST, Tree<TokenAttributes> eCST) {
        this.CST = CST;
        this.eCST = eCST;
    }

}
