/*
 * The MIT License - https://github.com/RafaelSantosBraz/AS2SCompiler/blob/master/LICENSE
 * Copyright 2020 Rafael Braz.
 */
package auxtools;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import frontend.parsers.TranslationParser;
import trees.*;

/**
 * provides static methods to eCST adapters and Parsers
 *
 * @author Rafael Braz
 */
public class BIB {

    /**
     * returns a new node and establishes a parent-child realationship.
     *
     * @param parent
     * @param text
     * @return
     */
    public static Node<TokenAttributes> createNodeWithParent(Node<TokenAttributes> parent, String text) {
        Node<TokenAttributes> node = new Node<>(parent);
        node.setNodeData(new UniversalToken(text, -1));
        parent.addChild(node);
        return node;
    }

    /**
     * returns a new node and establishes a parent-child realationship at a
     * given index of the children list.
     *
     * @param parent
     * @param text
     * @param index
     * @return
     */
    public static Node<TokenAttributes> createNodeWithParent(Node<TokenAttributes> parent, String text, int index) {
        Node<TokenAttributes> node = new Node<>(parent);
        node.setNodeData(new UniversalToken(text, -1));
        parent.getChildren().add(index, node);
        return node;
    }

    /**
     * retuns a new node WITHOUT a parent-child relationship.
     *
     * @param text
     * @return
     */
    public static Node<TokenAttributes> createNode(String text) {
        return new Node<>(new UniversalToken(text, -1));
    }

    /**
     * replace the original node with a new node.
     *
     * @param originalNode
     * @param newNode
     */
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

    /**
     * remove a given node from the tree but keeps its children.
     *
     * @param node
     */
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

    /**
     * delete a node and all its children.
     *
     * @param node
     */
    public static void removeChain(Node<TokenAttributes> node) {
        if (node != null) {
            if (node.getParent() != null) {
                node.getParent().getChildren().remove(node);
            }
        }
    }

    /**
     * searches for a node that has the given name and return it (the first to
     * appear).
     *
     * @param set list of nodes.
     * @param text
     * @return
     */
    public static Node<TokenAttributes> getChildByText(List<Node<TokenAttributes>> set, String text) {
        for (Node<TokenAttributes> c : set) {
            if (c.getNodeData().getText().equals(text)) {
                return c;
            }
        }
        return null;
    }

    /**
     * returns the tmap code as a string from a file.
     *
     * @param auxTmapsDir
     * @param fileName
     * @return
     */
    public static String getTmapCodeFromFile(String auxTmapsDir, String fileName) {
        try {
            return new String(Files.readAllBytes(Paths.get(auxTmapsDir + File.separator + fileName)));
        } catch (IOException ex) {
            return null;
        }
    }

    /**
     * receives a tmap code that do NOT belong to a defined tmap rule,
     * encapsules it and executes it. Returns the generated tree.
     *
     * @param tmapCode
     * @param currentNode
     * @return
     */
    public static List<Node<TokenAttributes>> tmapOneRuleCodeCall(String tmapCode, Node<TokenAttributes> currentNode) {
        String code = "\"rule_tmap_standard_\" -> {" + tmapCode + "}";
        Tree<TokenAttributes> tree = TranslationParser.startFromString(code, "\"rule_tmap_standard_\"", currentNode);
        if (tree.getRoot() != null) {
            return tree.getRoot().getChildren();
        }
        return null;
    }

    /**
     * receveives a tmap code that HAS defined rules and executes it from the
     * initial one. Returns the generated tree.
     *
     * @param tmapCode
     * @param initialRuleName
     * @param currentNode
     * @return
     */
    public static List<Node<TokenAttributes>> tmapCompleteCodeCall(String tmapCode, String initialRuleName, Node<TokenAttributes> currentNode) {
        Tree<TokenAttributes> tree = TranslationParser.startFromString(tmapCode, initialRuleName, currentNode);
        if (tree.getRoot() != null) {
            return tree.getRoot().getChildren();
        }
        return null;
    }

    /**
     * returns the text value of the given node.
     *
     * @param node
     * @return
     */
    public static String getText(Node<TokenAttributes> node) {
        return node.getNodeData().getText();
    }

    /**
     * searchs for a node that has the given name.
     *
     * @param node
     * @param name
     * @return
     */
    public static Node<TokenAttributes> searchDownFor(Node<TokenAttributes> node, String name) {
        for (Node<TokenAttributes> n : node.getChildren()) {
            if (getText(n).equals(name)) {
                return n;
            }
        }
        for (Node<TokenAttributes> n : node.getChildren()) {
            Node<TokenAttributes> search = searchDownFor(n, name);
            if (search != null) {
                return search;
            }
        }
        return null;
    }
}
