lexer grammar GefLexer;

//  lexer gedeelte
DF_LF : '\r'|'\n'|'\r\n' ;
EOH : '#EOH=' DF_LF+ -> mode(DATA_BLOCK_MODE);
HASH : '#' -> mode(VAR_MODE);
SPACE : ' ' ;

mode VAR_MODE;

EQUALS : '=' -> mode(HDR_DATA_MODE) ;
VARIABELE : ('a'..'z' | 'A'..'Z' | ' ')+;


mode DATA_BLOCK_MODE;

DB_LF : DF_LF;
DATABLOCK_STRING : (~[\r\n])+;


mode HDR_DATA_MODE;

HR_LF : DF_LF -> mode(DEFAULT_MODE);
HEADER_STRING : DATABLOCK_STRING;
