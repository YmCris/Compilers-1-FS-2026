/********************************** LEXER FILE ********************************/
// JAVA IMPORTS ----------------------------------------------------------------
package ymcris.compilers.flowchart.lexer;

import java.util.*;
import java_cup.runtime.*;
import ymcris.compilers.flowchart.parser.sym;
import ymcris.compilers.flowchart.backend.errors.ErrorToken;
import ymcris.compilers.flowchart.backend.errors.ErrorType;

%%
/******************************* JFLEX DECLARATIONS ***************************/
%public
%unicode
%class Lexer
%cup
%line
%column

// STATES ----------------------------------------------------------------------
%state STRING
%state COMMENT

// CONSTRUCTOR -----------------------------------------------------------------
%init{
    
    buffer = new StringBuffer();
    errorList = new ArrayList<>();

%init}

// MACROS ----------------------------------------------------------------------

Letter                  = [:jletter:]
LetterDigit             = [:jletterdigit:]

Integer                 = ([0-9])+
Double                  = {Integer}\.{Integer}

Identifier              = {Letter}({LetterDigit})*

Space                   = [ \t]+
LineTerminator          = \r|\n|\r\n
WhiteSpace              = {LineTerminator} | [ \t\f]

// JAVA CODE -------------------------------------------------------------------
%{

    // REFERENCE VARIABLES -----------------------------------------------------
    StringBuffer buffer;
    private List<ErrorToken> errorList;

    // SPECIFIC METHODS --------------------------------------------------------
    // ERROR REPORT
    private void error(String lexeme, String message){
        errorList.add(
            new ErrorToken(lexeme, message, ErrorType.LEXICAL,(yyline+1),(yycolumn+1))
        );
    }

     // PARSER METHDOS
    private Symbol symbol(int type){
        return new Symbol(type, yyline+1, yycolumn+1);
    }
    
    private Symbol symbol(int type, Object value){
        return new Symbol(type, yyline+1, yycolumn+1, value);
    }

    // GETTERS -----------------------------------------------------------------
    public List<ErrorToken> getLexicalErrors(){
        return this.errorList;
    }

%}

%%
/********************************* LEXICAL RULES ******************************/
<YYINITIAL>{
    // RESERVED WORDS
    "FIN"{Space}"SI"          { return symbol(sym.IF_END); }
    "FIN"{Space}"MIENTRAS"    { return symbol(sym.WHILE_END); }
    "INICIO"          { return symbol(sym.START); }
    "VAR"             { return symbol(sym.VAR); }
    "FIN"             { return symbol(sym.END); }
    "SI"              { return symbol(sym.IF); }
    "ENTONCES"        { return symbol(sym.THEN); }
    "MIENTRAS"        { return symbol(sym.WHILE); }
    "HACER"           { return symbol(sym.DO); }
    "MOSTRAR"         { return symbol(sym.SHOW); }
    "LEER"            { return symbol(sym.READ); }

    // ARITHMETIC OPERATORS
    "+"               { return symbol(sym.PLUS); }
    "-"               { return symbol(sym.MINUS); }
    "*"               { return symbol(sym.MULTIPLY); }
    "/"               { return symbol(sym.DIVIDE); }

    // RELATIONSHIP OPERATORS
    "=="              { return symbol(sym.EQUAL); }
    "!="              { return symbol(sym.DIFERENT); }
    ">="              { return symbol(sym.GOE); }
    "<="              { return symbol(sym.LOE); }
    ">"               { return symbol(sym.GREATER); }
    "<"               { return symbol(sym.LESS); }
    "="               { return symbol(sym.ASSIGN); }

    // LOGIC OPERATORS
    "&&"              { return symbol(sym.AND); }
    "||"              { return symbol(sym.OR); }
    "!"               { return symbol(sym.NOT); }

    // GROUPING SYMBOLS
    "("               { return symbol(sym.OPEN_PARENT); }
    ")"               { return symbol(sym.CLOSED_PARENT); }

    // MACROS
    {Double}          { return symbol(sym.DOUBLE, Double.valueOf(yytext())); }
    {Integer}         { return symbol(sym.INTEGER, Integer.valueOf(yytext())); }

    {Identifier}      { return symbol(sym.IDENTIFIER, yytext()); }

    {WhiteSpace}+     { /* IGNORE */ }

    // STATES
    "#"               { yybegin(COMMENT); }

    // String
    \"                { buffer.setLength(0); yybegin(STRING); }
    
    .                 {
                        error(yytext(), "The symbol doesn't exist in the language");
                        return symbol(sym.error, yytext());
                      }

}

<STRING> {
    
    \"                {
                        yybegin(YYINITIAL);
                        return symbol(sym.STRING, buffer.toString());
                      }
                  
    [^\n\r\"\\]+      { buffer.append( yytext() ); }
    \\n               { buffer.append('\n'); }
    \\t               { buffer.append('\t'); }
    \\r               { buffer.append('\r'); }
    \\\"              { buffer.append('\"'); }
    \\\\              { buffer.append('\\'); }
}

<STRING>\n {
                        error(yytext(), "The string is not closed");
                        yybegin(YYINITIAL);
                        return symbol(sym.error);
}

<STRING><<EOF>> {
                        error(yytext(), "The string is not closed EOF");
                        return symbol(sym.EOF);
}

<COMMENT>{

    \n                { yybegin(YYINITIAL); }
    .                 { /* IGNORE */ }

}

<COMMENT><<EOF>> {
                        error(yytext(), "The comment is not closed EOF");
                        return symbol(sym.EOF);
}

<<EOF>>           {
                        return symbol(sym.EOF);
                  }

