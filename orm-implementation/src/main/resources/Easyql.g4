grammar Easyql;
eql :
 query
 ;
query :
 selectq
| deleteq
| updateq;
selectq :
SELECT SPACE FROM SPACE TNAME (where_clause)? (limit_clause)?
;
SPACE :
 '\s+'
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
column SPACE condition SPACE value;
condition:
LIKE
| EQUAL
;
LIKE:
'LIKE'
|'like'
;
EQUAL:
'='
;
column:
;
value:
;
updateq:
;
deleteq:
;
FROM:
'FROM'|'from'
;
TNAME:
'[a-zA-Z]+'
;
SELECT:
'SELECT'|'select'
;
limit_clause:
'LIMIT'|'limit' SPACE '[0-9]+'
;


