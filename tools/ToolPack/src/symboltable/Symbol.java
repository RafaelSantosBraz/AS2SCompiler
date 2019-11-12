/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package symboltable;

import trees.cstecst.TokenAttributes;
import trees.simpletree.Node;

/**
 * represents a symbol to be inserted into a Symbol Table
 *
 * @author Rafael Braz
 */
public class Symbol {

    public static final int NON_STATIC_FUNC = 0; // function/method of an object
    public static final int STATIC_FUNC = 1; // function/method of a class
    public static final int CLASS = 2; // class / concrete unit
    public static final int CONSTRUCTOR = 3; // constructor of an object
    public static final int NON_STATIC_GLOB_VAR = 4; // attribute of an object
    public static final int STATIC_GLOB_VAR = 5; // attribute of a class / global variable

    private final String name; // key that represents the symbol
    private final int type; // type of the symbol -- one of the static constants declared here
    private final Node<TokenAttributes> node; // real tree node of the symbol

    public Symbol(String name, int type, Node<TokenAttributes> node) {
        this.name = name;
        this.type = type;
        this.node = node;
    }

    public String getName() {
        return name;
    }

    public int getType() {
        return type;
    }

    public Node<TokenAttributes> getNode() {
        return node;
    }

}
