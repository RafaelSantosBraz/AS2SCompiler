/******************************************************
 * A multi-line Javadoc-like comment about my grammar *
 ******************************************************/
grammar TranslationGrammar;

@header{
    package parser;
}

prog
        : rulee*
        ;

rulee
        : NODE_NAME '->' '{' ruleebody? '}' 
        ;

ruleebody
        : partialrulee (',' ruleebody)?
        ;

partialrulee
        : ruleefragment 
        | optionalpartialrulee 
        ;

optionalpartialrulee
        : '?' ruleefragment
        ;

ruleefragment
        : ruleecall
        | methodcall
        | parentsetchildren
        | visitingsequence 
        ;

parentsetchildren
        : NODE_NAME '=' '{' ruleebody '}'
        ;

ruleecall
        : 'rule' '(' NODE_NAME ',' ANY? visitingsequence ')' 
        ;


visitingsequence
        : mention ('.' visitingsequence)?
        ;

mention
        : directnodevisiting 
        | childinvoke
        | childreninvoke
        | lastinvoke
        | parentinvoke
        | currentnodeinvoke
        ;

currentnodeinvoke
        : 'current_node'
        ;

parentinvoke
        : 'parent'
        ;

lastinvoke
        : 'last'
        ;

childreninvoke
        : simplechildreninvoke 
        | complexchildreninvoke 
        ;

simplechildreninvoke
        : 'children'
        ;

complexchildreninvoke
        : 'children' '(' NODE_NAME ')'
        ;

directnodevisiting
        : NODE_NAME
        ;

childinvoke
        : 'child'
        ;

methodcall
        : newleafinvoke
        ;

newleafinvoke
        : 'new_leaf' '(' NODE_NAME  ')'
        ;

ANY : 'any';
NODE_NAME : '"'(~["\r\n])+'"';
WS : [ \t\r\n]+ -> skip;
COM     : '//'(~[\r\n])*'\r'?'\n' -> skip;