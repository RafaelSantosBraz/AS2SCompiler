/*
 * The MIT License - https://github.com/RafaelSantosBraz/AS2SCompiler/blob/master/LICENSE
 * Copyright 2020 Rafael Braz.
 */
package frontend.parsers.visitors;

import frontend.parsers.CGrammarBaseVisitor;

/**
 *
 * @author rafin
 */
public class CVisitor extends CGrammarBaseVisitor<String> {

    private final StringBuilder eCST;

    public CVisitor() {
        eCST = new StringBuilder("");
    }

}
