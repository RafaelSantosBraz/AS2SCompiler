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
        | ifstatement
        | ifelsestatement
        ;

ifstatement
        : 'if' '(' condition ')' '{' ruleebody? '}'
        ;

ifelsestatement
        : 'if' '(' condition ')' '{' t=ruleebody? '}' 'else' '{' f=ruleebody? '}'
        ;

condition
        : p1=partialcondition conditionaloperator p2=partialcondition
        ;

partialcondition
        : visitingsequence 
        | canonicalreference
        ;

canonicalreference
        : ':' NODE_NAME 
        ;

conditionaloperator
        : EQUALS 
        | NEQ
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
        | firstinvoke
        | parentinvoke
        | currentnodeinvoke
        | firstchildinvoke
        | lastchildinvoke
        | childnumberinvoke
        ;

firstchildinvoke
        : 'first_child'
        ;

lastchildinvoke
        : 'last_child'
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

firstinvoke
        : 'first'
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

childnumberinvoke
        : 'child' '(' NUMBER ')'
        ;

methodcall
        : newleafinvoke
        ;

newleafinvoke
        : 'new_leaf' '(' NODE_NAME  ')'
        ;

NUMBER : [0-9]+;
EQUALS : '==';
NEQ : '!=';
ANY : 'any';
NODE_NAME : '"'(~["\r\n])+'"';
WS : [ \t\r\n]+ -> skip;
COM     : '//'(~[\r\n])*'\r'?'\n' -> skip;