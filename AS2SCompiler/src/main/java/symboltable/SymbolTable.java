/*
 * The MIT License - https://github.com/RafaelSantosBraz/AS2SCompiler/blob/master/LICENSE
 * Copyright 2020 Rafael Braz.
 */
package symboltable;

import java.util.ArrayList;
import java.util.List;

/**
 * provides structures and methods to keep important information about a tree
 * and its nodes
 *
 * @author Rafael Braz
 */
public class SymbolTable {

    /**
     * The Symbol Table represented as an array.
     */
    private final List<Symbol> table;

    public SymbolTable() {
        table = new ArrayList<>();
    }

    /**
     * returns all symbols that have the given name.
     *
     * @param name
     * @return
     */
    public List<Symbol> getSymbolsByName(String name) {
        List<Symbol> res = new ArrayList<>();
        table.stream().filter((b) -> (b.getName().equals(name))).forEachOrdered((b) -> {
            res.add(b);
        });
        return res;
    }

    /**
     * inserts a given symbol into the symbol table.
     *
     * @param symbol
     */
    public void addSymbol(Symbol symbol) {
        if (table.stream().anyMatch((b) -> (b.getName().equals(symbol.getName()) && b.getNode().equals(symbol.getNode())))) {
            return;
        }
        table.add(symbol);
    }

    /**
     * checks if a given symbol (by name) is in given context.
     *
     * @param name
     * @param context
     * @return
     */
    public boolean isInContextByName(String name, Symbol context) {
        return table.stream().anyMatch((t) -> {
            if (t.getName().equals(name) && t.getContext() != null) {
                if (t.getContext().equals(context)) {
                    return true;
                }
            }
            return false;
        });
    }

    /**
     * checks if a given symbol (by name) is in given context (by name).
     *
     * @param name
     * @param context
     * @return
     */
    public boolean isInContextByNames(String name, String context) {
        return table.stream().anyMatch((t) -> {
            if (t.getName().equals(name) && t.getContext() != null) {
                if (t.getContext().getName().equals(context)) {
                    return true;
                }
            }
            return false;
        });
    }

    /**
     * returns all the symbols of CLASS type.
     *
     * @return
     */
    public List<Symbol> getClasses() {
        return simpleTypeSearch(Symbol.CLASS);
    }

    /**
     * returns a class that has a given name.
     *
     * @param name
     * @return
     */
    public Symbol getClassByName(String name) {
        try {
            return (Symbol) table.stream().filter((b) -> {
                return b.getName().equals(name) && b.getType() == Symbol.CLASS;
            }).toArray()[0];
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * returns all static functions.
     *
     * @return
     */
    public List<Symbol> getStaticFunctions() {
        return simpleTypeSearch(Symbol.STATIC_FUNC);
    }

    /**
     * returns all function calls.
     *
     * @return
     */
    public List<Symbol> getFunctionCalls() {
        return simpleTypeSearch(Symbol.FUNC_CALL);
    }

    /**
     * returns all non static functions.
     *
     * @return
     */
    public List<Symbol> getNonStaticFunctions() {
        return simpleTypeSearch(Symbol.NON_STATIC_FUNC);
    }

    /**
     * returns all non static global variables.
     *
     * @return
     */
    public List<Symbol> getNonStaticVariables() {
        return simpleTypeSearch(Symbol.NON_STATIC_GLOB_VAR);
    }

    /**
     * returns all static and non static functions.
     *
     * @return
     */
    public List<Symbol> getAllFunctions() {
        List<Symbol> res = new ArrayList<>();
        table.forEach((t) -> {
            if (t.getType() == Symbol.STATIC_FUNC || t.getType() == Symbol.NON_STATIC_FUNC) {
                res.add(t);
            }
        });
        return res;
    }

    /**
     * returns all the symbols of CONSTRUCTOR type.
     *
     * @return
     */
    public List<Symbol> getConstructors() {
        return simpleTypeSearch(Symbol.CONSTRUCTOR);
    }

    /**
     * returns a constructor symbol that has a given name.
     *
     * @param name
     * @return
     */
    public Symbol getConstructorByName(String name) {
        try {
            return (Symbol) table.stream().filter((b) -> {
                return b.getName().equals(name) && b.getType() == Symbol.CONSTRUCTOR;
            }).toArray()[0];
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * check if a symbol is a constructor by its name.
     *
     * @param name
     * @return
     */
    public boolean isConstructorByName(String name) {
        return simpleNameTypeCheck(name, Symbol.CONSTRUCTOR);
    }

    /**
     * check if a symbol is a static function by its name.
     *
     * @param name
     * @return
     */
    public boolean isStaticFunctionByName(String name) {
        return simpleNameTypeCheck(name, Symbol.STATIC_FUNC);
    }

    /**
     * check if a symbol is a non static function by its name.
     *
     * @param name
     * @return
     */
    public boolean isNonStaticFunctionByName(String name) {
        return simpleNameTypeCheck(name, Symbol.NON_STATIC_FUNC);
    }

    /**
     * check if a symbol is a non static global variable by its name.
     *
     * @param name
     * @return
     */
    public boolean isNonStaticGlobVarByName(String name) {
        return simpleNameTypeCheck(name, Symbol.NON_STATIC_GLOB_VAR);
    }

    /**
     * check if a symbol is a static global variable by its name.
     *
     * @param name
     * @return
     */
    public boolean isStaticGlobVarByName(String name) {
        return simpleNameTypeCheck(name, Symbol.STATIC_GLOB_VAR);
    }

    /**
     * check if a symbol is a class by its name.
     *
     * @param name
     * @return
     */
    public boolean isClassByName(String name) {
        return simpleNameTypeCheck(name, Symbol.CLASS);
    }

    /**
     * searches on the table for symbols that have a given type.
     *
     * @param type
     * @return
     */
    private List<Symbol> simpleTypeSearch(int type) {
        List<Symbol> res = new ArrayList<>();
        table.forEach((t) -> {
            if (t.getType() == type) {
                res.add(t);
            }
        });
        return res;
    }

    /**
     * check if there is a symbol that has given name and type.
     *
     * @param name
     * @param type
     * @return
     */
    private boolean simpleNameTypeCheck(String name, int type) {
        return table.stream().anyMatch((b) -> (b.getName().equals(name) && b.getType() == type));
    }
}
