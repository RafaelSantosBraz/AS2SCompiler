/*
 * The MIT License - https://github.com/RafaelSantosBraz/AS2SCompiler/blob/master/LICENSE
 * Copyright 2020 Rafael Braz.
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
    public static final int FUNC_CALL = 6; // function/method call

    private final String name; // key that represents the symbol
    private final int type; // type of the symbol -- one of the static constants declared here
    private final Node<TokenAttributes> node; // real tree node of the symbol
    private Symbol context; // parent context (class/method/function) of the symbol

    public Symbol(String name, int type, Node<TokenAttributes> node) {
        this.name = name;
        this.type = type;
        this.node = node;
    }

    public Symbol(String name, int type, Node<TokenAttributes> node, Symbol context) {
        this.name = name;
        this.type = type;
        this.node = node;
        this.context = context;
    }

    public Symbol getContext() {
        return context;
    }

    public void setContext(Symbol context) {
        this.context = context;
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
