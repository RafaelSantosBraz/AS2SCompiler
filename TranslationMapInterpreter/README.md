# Translation Map Interpreter

This project is a interpreter for a external DSL named tmap (Translation Map) that aims to facilite and specify the process of generating a eCST from a CST. To do so, the interpreter uses a given CST and tmap (examples in real aplications [here](https://github.com/RafaelSantosBraz/StS-Compilation-Framework/tree/master/runtime/Tmaps)) and generates a eCST as a result. 

Now, it is also possible to execute tmap code directly from a String, returning a tree (eCST) in memory, what may be useful to insert tmap code into a proper Java code. For that porpouse, use the "startFromString" method of [TranslationParser](https://github.com/RafaelSantosBraz/StS-Compilation-Framework/blob/master/TranslationMapInterpreter/src/parser/TranslationParser.java).

The Translation Map language is composed of a set of rules in which some translation actions are described. It is possible to sumarize the tmap's docs (complete grammar [here](https://github.com/RafaelSantosBraz/StS-Compilation-Framework/blob/master/TranslationMapInterpreter/grammar/parser/TranslationGrammar.g4) by the following:

* **Rules**:
```c++
"rule_name" -> {
    action, // mandatory action
    ? action, // optional action
    ...
}
```

* **Actions**:
    * **if**:
        ```c++
        // 'op' can be '==' or '!='
        // 'partial_cond' can be 'visiting_seq' or 'canonical_ref'
        if (partial_cond op partial_cond){
            actions // if true
        }
        ```
    * **if_else**:
        ```c++
        // 'op' can be '==' or '!='
        // 'partial_cond' can be 'visiting_seq' or 'canonical_ref'
        if (partial_cond op partial_cond){
            actions // if true
        } else {
            actions // if false
        }
        ``` 
        * **canonical_ref**: a String to be compared
            ```c++
            :"content"
            ```
    * **visiting_seq**: walking on the given tree. ANTLR grammar description:
        ```c++
        visiting_seq
            : mention ('.' visiting_seq)?
            ;
        ```
        * **mention**: a walking action on the given tree. ANTLR grammar description:
            ```c++
            mention
                : "node_name" // return the child node that has this name
                | 'child' // if there is only one child node, return this child
                | 'children' // return a list of all child nodes 
                | 'children' '(' "node_name" ')' // return a list of all child nodes that have the "node_name"
                | 'last' // return the leaf node (0 children) or the first node that has more than 1 child
                | 'first' // return the first parent node that its parent has more than 1 child, or the parent node that has no parent
                | 'parent' // return the parent node
                | 'current_node' // return the current node
                | 'first_child' // return the first one of the children list
                | 'last_child' // return the last one of the children list
                | 'child' '(' INDEX ')' // return the child node at this 'INDEX'
                ;
            ```
    * **rule_call**: calling a rule by its name and given a node as parameter.
        ```c++
        rule("rule_name", visiting_seq) // calls the named rule. If there are more than 1 node in 'visiting_seq', the rule is called for each one of them.
        rule("rule_name", any visiting_seq) // calls the named rule. If there are more than 1 node in 'visiting_seq', the rule is called for each one of them. The 'any' means optional result
        ```
    * **new_leaf**: creates a node with a given name in the output tree
        ```c++
        new_leaf("node_name")
        ```
    * **parent_set_children**: creates a new node with a given name and executes some actions as children of this new node.
        ```c++        
        "node_name" = {
            actions
        }
        ```     