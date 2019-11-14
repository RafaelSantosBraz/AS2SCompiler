/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
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

    private final List<Symbol> table;

    public SymbolTable() {
        table = new ArrayList<>();
    }

    // returns all symbols that have the given name
    public List<Symbol> getSymbolsByName(String name) {
        List<Symbol> res = new ArrayList<>();
        table.stream().filter((b) -> (b.getName().equals(name))).forEachOrdered((b) -> {
            res.add(b);
        });
        return res;
    }

    public void addSymbol(Symbol symbol) {
        if (table.stream().anyMatch((b) -> (b.getName().equals(symbol.getName()) && b.getNode().equals(symbol.getNode())))) {
            return;
        }
        table.add(symbol);
    }

    // returns all the symbols of CLASS type
    public List<Symbol> getClasses() {
        List<Symbol> classes = new ArrayList<>();
        table.forEach((t) -> {
            if (t.getType() == Symbol.CLASS) {
                classes.add(t);
            }
        });
        return classes;
    }

    // returns all static functions
    public List<Symbol> getStaticFunctions() {
        List<Symbol> res = new ArrayList<>();
        table.forEach((t) -> {
            if (t.getType() == Symbol.STATIC_FUNC) {
                res.add(t);
            }
        });
        return res;
    }

    // returns all non static functions
    public List<Symbol> getNonStaticFunctions() {
        List<Symbol> res = new ArrayList<>();
        table.forEach((t) -> {
            if (t.getType() == Symbol.NON_STATIC_FUNC) {
                res.add(t);
            }
        });
        return res;
    }

    // returns all non static global variables
    public List<Symbol> getNonStaticVariables() {
        List<Symbol> res = new ArrayList<>();
        table.forEach((t) -> {
            if (t.getType() == Symbol.NON_STATIC_GLOB_VAR) {
                res.add(t);
            }
        });
        return res;
    }

    // returns all static and non static functions
    public List<Symbol> getAllFunctions() {
        List<Symbol> res = new ArrayList<>();
        table.forEach((t) -> {
            if (t.getType() == Symbol.STATIC_FUNC || t.getType() == Symbol.NON_STATIC_FUNC) {
                res.add(t);
            }
        });
        return res;
    }

    // returns all the symbols of CONSTRUCTOR type
    public List<Symbol> getConstructors() {
        List<Symbol> res = new ArrayList<>();
        table.forEach((t) -> {
            if (t.getType() == Symbol.CONSTRUCTOR) {
                res.add(t);
            }
        });
        return res;
    }

    // returns a constructor symbol that has a given name
    public Symbol getConstructorByName(String name) {
        try {
            return (Symbol) table.stream().filter((b) -> {
                return b.getName().equals(name) && b.getType() == Symbol.CONSTRUCTOR;
            }).toArray()[0];
        } catch (Exception e) {
            return null;
        }
    }

    // check if a symbol is a constructor by its name
    public boolean isConstructorByName(String name) {
        return table.stream().anyMatch((b) -> (b.getName().equals(name) && b.getType() == Symbol.CONSTRUCTOR));
    }
}
