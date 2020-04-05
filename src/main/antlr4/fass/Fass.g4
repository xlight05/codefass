grammar Fass;

parse
 : block EOF
 ;

import_def
: IMPORT ID
;

orchestrate_def
: ORCHESTRATE param_block orchestrate_block
;

orchestrate_block
 : OBRACE orc_block CBRACE
 ;

orc_block
 : orchestrate_stat*
 ;

orchestrate_stat
: if_stat
| function_def
| sequence_def
;

param_block
 : OPAR (ID COMMA?)* CPAR
 ;

sequence_def
: SEQUENCE ID ASSIGN sequence_block SCOL
;

sequence_block
: SOBRACE sequence_repeat SCBRACE
;

sequence_repeat
: sequence_stat*
;

sequence_stat
: ID
;

SEQUENCE : 'sequence';

parallel_def
: PARALLEL ID ASSIGN parallel_block SCOL
;

parallel_block
: SOBRACE parallel_repeat SCBRACE
;

parallel_repeat
: parallel_stat*
;

parallel_stat
: ID
;

PARALLEL : 'parallel';

function_def
: FUNCTION ID ASSIGN function_block SCOL
;

function_repeat
: function_stat*
;

function_stat
: function_handler
| function_name
| function_language
| OTHER {System.err.println("unknown char: " + $OTHER.text);}
;

function_block
: OBRACE function_repeat CBRACE
;

function_handler
: HANDLER SEMI STRING
;

function_name
: NAME SEMI STRING
;

function_language
: LANGUAGE SEMI STRING
;

FUNCTION : 'function';
HANDLER : 'handler';
NAME : 'name';
LANGUAGE : 'language';

block
 : stat*
 ;

stat
 : assignment
// | if_stat
 | while_stat
 | log
 | function_def
// | sequence_def
 | import_def
 | orchestrate_def
 | ID
 | OTHER {System.err.println("unknown char: " + $OTHER.text);}
 ;

assignment
 : ID ASSIGN expr SCOL
 ;

if_stat
 : IF condition_block (ELSE IF condition_block)* (ELSE orchestrate_block)?
 ;

condition_block
 : expr orchestrate_block
 ;

stat_block
 : OBRACE block CBRACE
 ;

while_stat
 : WHILE expr stat_block
 ;

log
 : LOG expr SCOL
 ;

expr
 :NOT expr                              #notExpr
 | expr op=(MULT | DIV | MOD) expr      #multiplicationExpr
 | expr op=(PLUS | MINUS) expr          #additiveExpr //needed?
 | expr op=(LTEQ | GTEQ | LT | GT) expr #relationalExpr
 | expr op=(EQ | NEQ) expr              #equalityExpr
 | expr AND expr                        #andExpr
 | expr OR expr                         #orExpr
 | atom                                 #atomExpr
 ;

atom
 : OPAR expr CPAR #parExpr
 | (INT | FLOAT)  #numberAtom
 | (TRUE | FALSE) #booleanAtom
 | ID             #idAtom
 | STRING         #stringAtom
 | NIL            #nilAtom
 ;

OR : '||';
AND : '&&';
EQ : '==';
NEQ : '!=';
GT : '>';
LT : '<';
GTEQ : '>=';
LTEQ : '<=';
PLUS : '+';
MINUS : '-';
MULT : '*';
DIV : '/';
MOD : '%';
POW : '^';
NOT : '!';

SCOL : ';';
SEMI : ':';
ASSIGN : '=';
OPAR : '(';
CPAR : ')';
OBRACE : '{';
CBRACE : '}';
SOBRACE : '[';
SCBRACE : ']';
COMMA : ',';

TRUE : 'true';
FALSE : 'false';
NIL : 'nil';
IF : 'if';
ELSE : 'else';
WHILE : 'while';
LOG : 'log';
ORCHESTRATE : 'orchestrate';
IMPORT : 'import';

ID
 : [a-zA-Z_] [a-zA-Z_0-9]*
 ;

PARAM
: [a-zA-Z_] [a-zA-Z_0-9]*
;

INT
 : [0-9]+
 ;

FLOAT
 : [0-9]+ '.' [0-9]* 
 | '.' [0-9]+
 ;

STRING
 : '"' (~["\r\n] | '""')* '"'
 ;

COMMENT
 : '#' ~[\r\n]* -> skip
 ;

SPACE
 : [ \t\r\n] -> skip
 ;

OTHER
 : . 
 ;
