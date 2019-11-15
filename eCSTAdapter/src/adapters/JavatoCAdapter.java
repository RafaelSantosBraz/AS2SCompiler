/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package adapters;

import auxtools.BIB;
import java.util.ArrayList;
import java.util.List;
import symboltable.Symbol;
import symboltable.SymbolTable;
import trees.cstecst.TokenAttributes;
import trees.cstecst.UniversalToken;
import trees.simpletree.Node;
import trees.simpletree.Tree;
import walkers.ActionWalker;

/**
 * aims to adapt a eCST generated from Java code to a C eCST
 *
 * @author Rafael Braz
 */
public class JavatoCAdapter extends ActionWalker {

    private final String auxTmapsDir; // path to all files of partial/complete tmap code
    private String curUnitName; // current concrete unit name
    private final SymbolTable symbolTable; // Symbol Table of the important values/nodes of the tree

    public JavatoCAdapter(String auxTmapsDir) {
        this.auxTmapsDir = auxTmapsDir;
        symbolTable = new SymbolTable();
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
        node.getChildren().clear();
    }

    // attribute declaration to variable declaration
    public void actionATTRIBUTE_DECL(Node<TokenAttributes> node) {
        Node<TokenAttributes> value = BIB.getChildByText(node.getChildren(), "VALUE");
        String name = BIB.getText(BIB.getChildByText(node.getChildren(), "NAME").getChildren().get(0));
        node.getNodeData().setText("VAR_DECL");
        if (isStatic(node)) {
            symbolTable.addSymbol(new Symbol(name, Symbol.STATIC_GLOB_VAR, node));
        } else {
            symbolTable.addSymbol(new Symbol(name, Symbol.NON_STATIC_GLOB_VAR, node));
        }
        if (value.getChildren().isEmpty()) {
            BIB.removeChain(value);
        } else {
            String code = BIB.getTmapCodeFromFile(auxTmapsDir, "attrJavatoC.tmap");
            Node<TokenAttributes> as = BIB.tmapOneRuleCodeCall(code, node).get(0);
            as.setParent(node);
            List<Node<TokenAttributes>> children = node.getChildren();
            children.add(children.size(), as);
            Node<TokenAttributes> op = BIB.tmapOneRuleCodeCall("child", as).get(0);
            Node<TokenAttributes> nameNode = BIB.getChildByText(node.getChildren(), "NAME");
            Node<TokenAttributes> valueNode = BIB.getChildByText(node.getChildren(), "VALUE");
            op.getChildren().add(1, nameNode);
            op.getChildren().add(2, valueNode);
            nameNode.setParent(op);
            valueNode.setParent(op);
            node.getChildren().remove(nameNode);
            node.getChildren().remove(valueNode);
        }
        actionVAR_DECL(node);
    }

    // arrays declaration to C pattern
    public void actionVAR_DECL(Node<TokenAttributes> node) {
        Node<TokenAttributes> sep = BIB.getChildByText(node.getChildren(), "SEPARATOR");
        if (sep != null) {
            Node<TokenAttributes> sep2 = node.getChildren().get(node.getChildren().indexOf(sep) + 1);
            BIB.removeChain(sep);
            BIB.removeChain(sep2);
            Node<TokenAttributes> asOp = BIB.tmapOneRuleCodeCall("last_child.child", node).get(0);
            sep.setParent(asOp);
            sep2.setParent(asOp);
            asOp.getChildren().add(asOp.getChildren().size() - 1, sep);
            asOp.getChildren().add(asOp.getChildren().size() - 1, sep2);
            arrayCPattern(node, asOp, sep, sep2);
        } else {
            Node<TokenAttributes> asOp = BIB.tmapOneRuleCodeCall("last_child.child", node).get(0);
            sep = BIB.getChildByText(asOp.getChildren(), "SEPARATOR");
            Node<TokenAttributes> sep2 = asOp.getChildren().get(asOp.getChildren().indexOf(sep) + 1);
            if (sep != null) {
                arrayCPattern(node, asOp, sep, sep2);
            }
        }
    }

    // remove useless new ClassType
    public void actionINSTANTIATES(Node<TokenAttributes> node) {
        if (node.getChildren().size() == 1) {
            BIB.removeChain(node);
        }
    }

    // adapting function parameter list and constructors
    public void actionFUNCTION_DECL(Node<TokenAttributes> node) {
        String name = BIB.getText(BIB.getChildByText(node.getChildren(), "NAME").getChildren().get(0));
        if (name.equals(curUnitName)) {
            BIB.searchDownFor(node, "void").getNodeData().setText(name);
            symbolTable.addSymbol(new Symbol(name, Symbol.CONSTRUCTOR, node));
        } else {
            if (!isStatic(node)) {
                Node<TokenAttributes> parList = BIB.getChildByText(node.getChildren(), "FORMAL_PARAM_LIST");
                addThisReference(parList);
                symbolTable.addSymbol(new Symbol(name, Symbol.NON_STATIC_FUNC, node));
            } else {
                if (name.equals("main")) {
                    BIB.getChildByText(node.getChildren(), "FORMAL_PARAM_LIST").getChildren().clear();
                }
                symbolTable.addSymbol(new Symbol(name, Symbol.STATIC_FUNC, node));
            }
        }
    }

    // keeping the unit name 
    public void actionCONCRETE_UNIT_DECL(Node<TokenAttributes> node) {
        curUnitName = BIB.getText(BIB.getChildByText(node.getChildren(), "NAME").getChildren().get(0));
        symbolTable.addSymbol(new Symbol(curUnitName, Symbol.CLASS, node));
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
        Node<TokenAttributes> nodeName = BIB.getChildByText(node.getChildren(), "NAME");
        String name = rewriteName(nodeName);
        if (!name.equals("System.out.printf") && !name.equals("System.out.println")) {
            symbolTable.addSymbol(new Symbol(name, Symbol.FUNC_CALL, node));
            return;
        }
        if (name.equals("System.out.println")) {
            System.err.println("Warning: Use 'System.out.printf' instead of 'System.out.println'!");
        }
        replacePrintf(nodeName);
    }

    // correct the new array to array[size]
    private void arrayCPattern(Node<TokenAttributes> node, Node<TokenAttributes> asOp, Node<TokenAttributes> sep, Node<TokenAttributes> sep2) {
        Node<TokenAttributes> val = asOp.getChildren().get(asOp.getChildren().size() - 1);
        if (val.getChildren().size() == 1) {
            Node<TokenAttributes> num = val.getChildren().get(0).getChildren().get(2);
            Node<TokenAttributes> name = asOp.getChildren().get(1);
            node.getChildren().add(node.getChildren().size() - 1, name);
            node.getChildren().add(node.getChildren().size() - 1, sep);
            node.getChildren().add(node.getChildren().size() - 1, num);
            node.getChildren().add(node.getChildren().size() - 1, sep2);
            name.setParent(node);
            sep.setParent(node);
            num.setParent(node);
            sep2.setParent(node);
            BIB.removeChain(node.getChildren().get(node.getChildren().size() - 1));
        }
    }

    // replace System.out.printf into printf
    private void replacePrintf(Node<TokenAttributes> node) {
        Node<TokenAttributes> printf = BIB.tmapOneRuleCodeCall("new_leaf(\"printf\")", node).get(0);
        node.getChildren().clear();
        printf.setParent(node);
        node.getChildren().add(printf);
    }

    // returns a complex name (multiple nodes) as a String
    private String rewriteName(Node<TokenAttributes> node) {
        List<String> names = nameUnion(node);
        String res = "";
        for (String s : names) {
            res += s;
        }
        return res;
    }

    // recursively computes a complex name
    private List<String> nameUnion(Node<TokenAttributes> node) {
        List<String> res = new ArrayList<>();
        List<Node<TokenAttributes>> children = node.getChildren();
        if (children.size() == 1) {
            res.add(BIB.getText(children.get(0)));
        } else if (children.size() == 3) {
            res.addAll(nameUnion(children.get(0)));
            res.add(".");
            res.addAll(nameUnion(children.get(2)));
        }
        return res;
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
        thetype.getNodeData().setText(curUnitName);
        if (children.isEmpty()) {
            children.add(par);
        } else {
            children.add(children.size(), par);
        }
    }

    // corrects function calls removing useless obj/class reference or adds obj/_this reference
    public void correctFuncCalls() {
        symbolTable.getFunctionCalls().forEach((t) -> {
            Node<TokenAttributes> mainName = t.getNode().getChildren().get(0);
            if (mainName.getChildren().size() == 1) {
                String funcName = BIB.tmapOneRuleCodeCall("last", mainName.getChildren().get(0)).get(0).getNodeData().getText();
                if (symbolTable.isNonStaticFunctionByName(funcName)) {
                    String code = BIB.getTmapCodeFromFile(auxTmapsDir, "addargJavatoC.tmap");
                    Node<TokenAttributes> arg = BIB.tmapOneRuleCodeCall(code, t.getNode()).get(0);
                    Node<TokenAttributes> argList = t.getNode().getChildren().get(1);
                    argList.getChildren().add(argList.getChildren().size() - 1, arg);
                    arg.setParent(argList);
                    code = BIB.getTmapCodeFromFile(auxTmapsDir, "leftrefJavatoC.tmap");
                    List<Node<TokenAttributes>> nodes = BIB.tmapOneRuleCodeCall(code, mainName);
                    nodes.get(0).setParent(mainName);
                    nodes.get(1).setParent(mainName);
                    nodes.get(2).setParent(mainName);
                    mainName.getChildren().clear();
                    mainName.setChildren(nodes);
                }
            } else {
                String leftName = BIB.tmapOneRuleCodeCall("last", mainName.getChildren().get(0)).get(0).getNodeData().getText();
                String rightName = BIB.tmapOneRuleCodeCall("last", mainName.getChildren().get(2)).get(0).getNodeData().getText();
                if (symbolTable.isClassByName(leftName)) {
                    mainName.getChildren().remove(0);
                    mainName.getChildren().remove(0);
                } else {
                    if (symbolTable.isStaticFunctionByName(rightName)) {
                        mainName.getChildren().remove(0);
                        mainName.getChildren().remove(0);
                    } else {
                        mainName.getChildren().get(1).getNodeData().setText("->");
                        String code = BIB.getTmapCodeFromFile(auxTmapsDir, "addargJavatoC.tmap");
                        Node<TokenAttributes> arg = BIB.tmapOneRuleCodeCall(code, t.getNode()).get(0);
                        BIB.searchDownFor(arg, "_this").getNodeData().setText(leftName);
                        Node<TokenAttributes> argList = t.getNode().getChildren().get(1);
                        argList.getChildren().add(argList.getChildren().size() - 1, arg);
                        arg.setParent(argList);
                    }
                }
            }
        });
    }

    // creates C structs for the classes and remove the obj attributes from the tree
    public void createStructs() {
        List<Symbol> classes = symbolTable.getClasses();
        List<Symbol> nonStaticVars = symbolTable.getNonStaticVariables();
        List<Symbol> nonStaticFuncs = symbolTable.getNonStaticFunctions();
        classes.forEach((t) -> {
            String code = BIB.getTmapCodeFromFile(auxTmapsDir, "structJavatoC.tmap");
            Node<TokenAttributes> struct = BIB.tmapOneRuleCodeCall(code, t.getNode()).get(0);
            Node<TokenAttributes> name = BIB.searchDownFor(struct, "$1");
            name.getNodeData().setText(t.getName());
            struct.setParent(t.getNode());
            t.getNode().getChildren().add(2, struct);
            Node<TokenAttributes> structBlock = struct.getChildren().get(1);
            nonStaticVars.forEach((v) -> {
                if (v.getNode().getParent().equals(t.getNode())) {
                    BIB.removeChain(v.getNode());
                    v.getNode().setParent(structBlock);
                    structBlock.getChildren().add(structBlock.getChildren().size() - 1, v.getNode());
                }
            });
            nonStaticFuncs.forEach((v) -> {
                if (v.getNode().getParent().equals(t.getNode())) {
                    Node<TokenAttributes> funcClone = v.getNode().getChainClone();
                    funcClone.setParent(structBlock);
                    structBlock.getChildren().add(structBlock.getChildren().size() - 1, funcClone);
                    Node<TokenAttributes> fType = funcClone.getChildren().get(1);
                    Node<TokenAttributes> fName = funcClone.getChildren().get(2);
                    Node<TokenAttributes> fParam = funcClone.getChildren().get(3);
                    funcClone.getChildren().clear();
                    funcClone.getChildren().add(fType);
                    funcClone.getChildren().add(fName);
                    funcClone.getChildren().add(fParam);
                }
            });
        });
    }

    // create C prototypes of all functions in the tree
    public void createFuncPrototypes() {
        List<Symbol> funcs = symbolTable.getAllFunctions();
        funcs.forEach((t) -> {
            Node<TokenAttributes> prot = new Node<>(t.getNode().getParent());
            prot.setNodeData(new UniversalToken("PROTOTYPE", -1));
            Node<TokenAttributes> typeClone = BIB.getChildByText(t.getNode().getChildren(), "TYPE").getChainClone();
            Node<TokenAttributes> nameClone = BIB.getChildByText(t.getNode().getChildren(), "NAME").getChainClone();
            Node<TokenAttributes> parmClone = BIB.getChildByText(t.getNode().getChildren(), "FORMAL_PARAM_LIST").getChainClone();
            prot.getChildren().add(typeClone);
            prot.getChildren().add(nameClone);
            prot.getChildren().add(parmClone);
            parmClone.getChildren().forEach((c) -> {
                Node<TokenAttributes> type = BIB.getChildByText(c.getChildren(), "TYPE");
                Node<TokenAttributes> sep = BIB.getChildByText(c.getChildren(), "SEPARATOR");
                if (sep == null) {
                    c.getChildren().clear();
                    c.getChildren().add(type);
                    return;
                }
                Node<TokenAttributes> sep2 = c.getChildren().get(c.getChildren().indexOf(sep) + 1);
                c.getChildren().clear();
                c.getChildren().add(type);
                c.getChildren().add(sep);
                c.getChildren().add(sep2);
            });
            prot.getParent().getChildren().add(2, prot);
        });
    }

    // if the class does not have a constructor, this method will create one
    public void createConstructors() {
        List<Symbol> classes = symbolTable.getClasses();
        classes.forEach((t) -> {
            if (!symbolTable.isConstructorByName(t.getName())) {
                String code = BIB.getTmapCodeFromFile(auxTmapsDir, "constrJavatoC.tmap");
                Node<TokenAttributes> constr = BIB.tmapOneRuleCodeCall(code, t.getNode()).get(0);
                BIB.searchDownFor(constr, "$1").getNodeData().setText(t.getName());
                BIB.searchDownFor(constr, "$2").getNodeData().setText(t.getName());
                constr.setParent(t.getNode());
                t.getNode().getChildren().add(2, constr);
                Symbol symb = new Symbol(t.getName(), Symbol.CONSTRUCTOR, constr);
                symbolTable.addSymbol(symb);
                changeConstructorBody(symb);
            } else {
                Symbol c = symbolTable.getConstructorByName(t.getName());
                t.getNode().getChildren().remove(c.getNode());
                t.getNode().getChildren().add(2, c.getNode());
                changeConstructorBody(c);
            }
        });
    }

    // change the given constructor to instantiate a struct and returns its pointer
    public void changeConstructorBody(Symbol symbol) {
        String code = BIB.getTmapCodeFromFile(auxTmapsDir, "mallocJavatoC.tmap");
        Node<TokenAttributes> node = BIB.tmapOneRuleCodeCall(code, symbol.getNode()).get(0);
        Node<TokenAttributes> body = symbol.getNode().getChildren().get(symbol.getNode().getChildren().size() - 1);
        node.setParent(body);
        body.getChildren().add(1, node);
        symbolTable.getNonStaticFunctions().forEach((t) -> {
            String fCode = BIB.getTmapCodeFromFile(auxTmapsDir, "funcassignJavatoC.tmap");
            Node<TokenAttributes> fNode = BIB.tmapOneRuleCodeCall(fCode, t.getNode()).get(0);
            fNode.setParent(body);
            body.getChildren().add(body.getChildren().size() - 1, fNode);
        });
        code = BIB.getTmapCodeFromFile(auxTmapsDir, "returnthisJavatoC.tmap");
        node = BIB.tmapOneRuleCodeCall(code, symbol.getNode()).get(0);
        node.setParent(body);
        body.getChildren().add(body.getChildren().size() - 1, node);
    }
}
