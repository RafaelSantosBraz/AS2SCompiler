/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cstecst;

/**
 *
 * @author Rafael Braz
 */
public abstract class TokenAttributes {

    public static final int ABSTRACT_NODE_LINE = -1;
    public static final int ABSTRACT_NODE_COLUMN = 0;

    protected int index;
    protected String text;
    protected int type;
    protected int line;
    protected int column;

    public TokenAttributes() {
        
    }

}
