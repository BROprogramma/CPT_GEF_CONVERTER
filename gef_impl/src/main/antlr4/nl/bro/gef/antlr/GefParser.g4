parser grammar GefParser;
options { tokenVocab=GefLexer; }

// File definitie (parser gedeelte):

file : header EOH datablock EOF;

header : headerrow+ ;
datablock : datablockrow+ ;

headerrow : HASH SPACE* variabele SPACE* EQUALS waarde HR_LF | HASH SPACE* variabele SPACE* EQUALS HR_LF | DF_LF;
datablockrow : datavalues DB_LF+ | datavalues;

datavalues : DATABLOCK_STRING;
waarde : HEADER_STRING;
variabele : VARIABELE;
