/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package symboltable;

import java.util.HashMap;

/**
 * provides structures and methods to keep important information about a tree
 * and its nodes
 *
 * @author Rafael Braz
 */
public class SymbolTable {

    private final HashMap<String, Symbol> table;

    public SymbolTable() {
        table = new HashMap<>();
    }

    public Symbol getSymbol(String key) {
        return table.get(key);
    }

    public boolean hasKey(String key) {
        return table.containsKey(key);
    }

    public void addValue(String key, Symbol value) {
        table.put(key, value);
    }

    public void removeValue(String key) {
        table.remove(key);
    }

    public int getSymbolType(String key) {
        if (hasKey(key)) {
            return getSymbol(key).getType();
        }
        return -1;
    }

}
