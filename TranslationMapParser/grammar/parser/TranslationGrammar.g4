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
        : NODE_NAME '->' '{' ruleebody '}' 
        ;

ruleebody
        : partialrulee (',' ruleebody)*
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
        : 'rule' '(' NODE_NAME ',' visitingsequence ')' 
        ;

visitingsequence
        : mention ('.' visitingsequence)*
        ;

mention
        : directnodevisiting 
        | childinvoke
        | textinvoke
        | childreninvoke
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

textinvoke
        : 'text'
        ;

directnodevisiting
        : NODE_NAME
        ;

childinvoke
        : 'child'
        ;

methodcall
        : textmethod
        ;

textmethod
        : 'text' '(' NODE_NAME  ')'
        ;

NODE_NAME : '"'(~["\r\n])+'"';
WS : [ \t\r\n]+ -> skip;