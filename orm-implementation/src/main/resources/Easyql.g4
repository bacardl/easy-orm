grammar Easyql;
query :
 selectq
| deleteq
| updateq;
selectq :
SELECT SPACE FROM SPACE ANYNAME (where_clause)? (limit_clause)?
;
updateq:
UPDATE SPACE ANYNAME SPACE set_clause (where_clause)?
;
deleteq:
DELETE SPACE FROM ANYNAME (where_clause)?
;
SELECT:
'SELECT'|'select'
;
DELETE:
'DELETE'|'delete'
;
UPDATE:
'UPDATE'|'update'
;
FROM:
'FROM'|'from'
;
SPACE :

 ;
where_clause :
WHERE SPACE pair (agr pair)*;

WHERE :
 'where'|'WHERE'
;
agr:
 AND
| OR
;
AND :
'AND'|'and'
;
OR :
'OR'|'or'
;
pair:
ANYNAME SPACE condition SPACE VALUE;
condition:
LIKE
| EQUAL
| NOTEQUAL
;
LIKE:
'LIKE'
|'like'
;
EQUAL:
'='
;
NOTEQUAL:
'!='
;
ANYNAME:
[a-zA-Z]+
;
VALUE:
.+
;

limit_clause:
'LIMIT'|'limit' SPACE NUMBER
;
SET:
'SET'|'set'
;
set_clause:
SET SPACE pair SPACE (COMMA SPACE pair)*
;
COMMA:
','
;
WS:
 [ \t\n\r] + -> skip
;
NUMBER:
[0-9]
;




