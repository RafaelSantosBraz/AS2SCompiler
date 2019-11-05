/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package auxtools;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import parser.TranslationParser;
import trees.cstecst.TokenAttributes;
import trees.cstecst.UniversalToken;
import trees.simpletree.Node;
import trees.simpletree.Tree;

/**
 * provides static methods to eCST adapters and Parsers
 *
 * @author Rafael Braz
 */
public class BIB {

    // returns a new node and establishes a parent-child realationship 
    public static Node<TokenAttributes> createNodeWithParent(Node<TokenAttributes> parent, String text) {
        Node<TokenAttributes> node = new Node<>(parent);
        node.setNodeData(new UniversalToken(text, -1));
        parent.addChild(node);
        return node;
    }

    // returns a new node and establishes a parent-child realationship at a given index of the children list
    public static Node<TokenAttributes> createNodeWithParent(Node<TokenAttributes> parent, String text, int index) {
        Node<TokenAttributes> node = new Node<>(parent);
        node.setNodeData(new UniversalToken(text, -1));
        parent.getChildren().add(index, node);
        return node;
    }

    // retuns a new node without a parent-child relationship
    public static Node<TokenAttributes> createNode(String text) {
        return new Node<>(new UniversalToken(text, -1));
    }

    // replace the original node with a new node
    public static void replaceNode(Node<TokenAttributes> originalNode, Node<TokenAttributes> newNode) {
        if (originalNode == null || newNode == null) {
            return;
        }
        Node<TokenAttributes> parent = originalNode.getParent();
        if (parent == null) {
            return;
        }
        List<Node<TokenAttributes>> parentChildren = parent.getChildren();
        if (parentChildren.isEmpty()) {
            parent.addChild(newNode);
        } else {
            int index = parentChildren.indexOf(originalNode);
            parentChildren.remove(index);
            parentChildren.add(index, newNode);
        }
    }

    // removre a given node from the tree but keeps the children
    public static void removeNode(Node<TokenAttributes> node) {
        if (node != null) {
            if (node.getParent() == null) {
                if (node.getChildren() != null && !node.getChildren().isEmpty()) {
                    node.getChildren().forEach((t) -> {
                        t.setParent(null);
                    });
                }
            } else {
                int index = node.getParent().getChildren().indexOf(node);
                node.getParent().getChildren().remove(node);
                if (node.getChildren() != null && !node.getChildren().isEmpty()) {
                    node.getChildren().forEach((t) -> {
                        t.setParent(node.getParent());
                        node.getParent().addChildAt(t, index);
                    });
                }
            }
        }
    }

    // delete the node and all the children
    public static void removeChain(Node<TokenAttributes> node) {
        if (node != null) {
            if (node.getParent() != null) {
                node.getParent().getChildren().remove(node);
            }
        }
    }

    // searches for a node that has the given name and return it (the first to appear)
    public static Node<TokenAttributes> getChildByText(List<Node<TokenAttributes>> set, String text) {
        for (Node<TokenAttributes> c : set) {
            if (c.getNodeData().getText().equals(text)) {
                return c;
            }
        }
        return null;
    }

    // returns the tmap code as a string from a file
    public static String getTmapCodeFromFile(String auxTmapsDir, String fileName) {
        try {
            return new String(Files.readAllBytes(Paths.get(auxTmapsDir + File.separator + fileName)));
        } catch (IOException ex) {
            return null;
        }
    }

    // receives a tmap code that do NOT belong to a defined tmap rule, encapsules it and executes it. Returns the generated tree
    public static List<Node<TokenAttributes>> tmapOneRuleCodeCall(String tmapCode, Node<TokenAttributes> currentNode) {
        String code = "\"rule_tmap_standard_\" -> {" + tmapCode + "}";
        Tree<TokenAttributes> tree = TranslationParser.startFromString(code, "\"rule_tmap_standard_\"", currentNode);
        if (tree.getRoot() != null) {
            return tree.getRoot().getChildren();
        }
        return null;
    }

    // receveives a tmap code that HAS defined rules and executes it from the initial one. Returns the generated tree
    public static List<Node<TokenAttributes>> tmapCompleteCodeCall(String tmapCode, String initialRuleName, Node<TokenAttributes> currentNode) {
        Tree<TokenAttributes> tree = TranslationParser.startFromString(tmapCode, initialRuleName, currentNode);
        if (tree.getRoot() != null) {
            return tree.getRoot().getChildren();
        }
        return null;
    }

}
