grammar Easyql;
query :
 selectq
| deleteq
| updateq;
selectq :
SELECT  FROM  ANYNAME (where_clause)? (limit_clause)?
;
updateq:
UPDATE  ANYNAME  set_clause (where_clause)?
;
deleteq:
DELETE  FROM ANYNAME (where_clause)?
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
where_clause :
WHERE  pair (agr pair)*;

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
value:
ANYNAME
|
NUMBER
;
SET:
'SET'|'set'
;
set_clause:
SET  equalpair  (COMMA  equalpair)*
;
equalpair:
ANYNAME  EQUAL  value;
pair:
ANYNAME  condition  value;
limit_clause:
'LIMIT'|'limit'  NUMBER
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
