/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package adapters;

import auxtools.BIB;
import java.util.ArrayList;
import java.util.List;
import trees.cstecst.TokenAttributes;
import trees.simpletree.Node;
import walkers.ActionWalker;

/**
 * aims to adapt a eCST generated from Java code to a C eCST
 *
 * @author Rafael Braz
 */
public class JavatoCAdapter extends ActionWalker {

    private final String auxTmapsDir; // path to all files of partial/complete tmap code
    private String curUnitName; // current concrete unit name
    private final List<String> nonStaticFuncs; // represents all the non static methods adapted to receive a "_this" reference
    private final List<String> classes; // represents all the classes / concrete units declarated

    public JavatoCAdapter(String auxTmapsDir) {
        this.auxTmapsDir = auxTmapsDir;
        nonStaticFuncs = new ArrayList<>();
        classes = new ArrayList<>();
    }

    // Java array to C array format -- parameters
    public void actionPARAMETER_DECL(Node<TokenAttributes> node) {
        List<Node<TokenAttributes>> children = node.getChildren();
        if (children.size() != 3 && !BIB.getText(children.get(children.size() - 1)).equals("SEPARATOR")) {
            Node<TokenAttributes> last = children.remove(children.size() - 1);
            children.add(2, last);
        }
    }

    // adding C includes
    public void actionPACKAGE_DECL(Node<TokenAttributes> node) {
        String code = BIB.getTmapCodeFromFile(auxTmapsDir, "importJavatoC.tmap");
        List<Node<TokenAttributes>> nodes = BIB.tmapOneRuleCodeCall(code, node);
        nodes.forEach((t) -> {
            t.setParent(node);
        });
        node.getChildren().addAll(0, nodes);
    }

    // Removing Java imports
    public void actionIMPORT_DECL(Node<TokenAttributes> node) {
        String s = BIB.getText(node.getChildren().get(0).getChildren().get(0));
        if (!(s.equals("stdlib.h") || s.equals("stdio.h"))) {
            BIB.removeChain(node);
        }
    }

    // removing modifiers
    public void actionMODIFIER_LIST(Node<TokenAttributes> node) {
        //node.getChildren().clear();
        BIB.removeChain(node);
    }

    // adapting function parameter list
    public void actionFUNCTION_DECL(Node<TokenAttributes> node) {
        if (!isStatic(node)) {
            Node<TokenAttributes> parList = BIB.getChildByText(node.getChildren(), "FORMAL_PARAM_LIST");
            addThisReference(parList);
            String name = BIB.getText(BIB.getChildByText(node.getChildren(), "NAME").getChildren().get(0));
            nonStaticFuncs.add(name);
        }
    }

    // keeping the unit name 
    public void actionCONCRETE_UNIT_DECL(Node<TokenAttributes> node) {
        curUnitName = BIB.getText(BIB.getChildByText(node.getChildren(), "NAME").getChildren().get(0));
        classes.add(curUnitName);
    }

    // true to 1 
    public void actiontrue(Node<TokenAttributes> node) {
        node.getNodeData().setText("1");
    }

    // false to 0 
    public void actionfalse(Node<TokenAttributes> node) {
        node.getNodeData().setText("0");
    }

    // boolean to int
    public void actionboolean(Node<TokenAttributes> node) {
        node.getNodeData().setText("int");
    }

    // System.out.printf to printf
    public void actionFUNCTION_CALL(Node<TokenAttributes> node) {
        
    }
    
    // returns if the node contains a modifier list which has the static keyword
    private boolean isStatic(Node<TokenAttributes> node) {
        Node<TokenAttributes> mods = BIB.getChildByText(node.getChildren(), "MODIFIER_LIST");
        if (mods != null) {
            if (mods.getChildren().stream().anyMatch((n) -> (BIB.getText(n).equals("static")))) {
                return true;
            }
        }
        return false;
    }

    // add a reference to the class/struct into the parameters list of a function
    private void addThisReference(Node<TokenAttributes> parList) {
        List<Node<TokenAttributes>> children = parList.getChildren();
        String code = BIB.getTmapCodeFromFile(auxTmapsDir, "thisJavatoC.tmap");
        Node<TokenAttributes> par = BIB.tmapOneRuleCodeCall(code, parList).get(0);
        par.setParent(parList);
        Node<TokenAttributes> thetype = BIB.searchDownFor(par, "$");
        System.out.println("correct this!");
        thetype.getNodeData().setText(curUnitName);        
        if (children.isEmpty()) {
            children.add(par);
        } else {
            children.add(children.size(), par);
        }
    }

    // returns if the given node is a clas / concrete unit 
    public boolean isClass(Node<TokenAttributes> node) {
        String txt = BIB.getText(node);
        return classes.stream().anyMatch((s) -> (s.equals(txt)));
    }

    // returns if the given node is a non static function/method
    public boolean isNonStaticFunction(Node<TokenAttributes> node) {
        String txt = BIB.getText(node);
        return nonStaticFuncs.stream().anyMatch((s) -> (s.equals(txt)));
    }

}
