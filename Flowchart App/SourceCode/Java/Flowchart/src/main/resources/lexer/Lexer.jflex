/********************************** LEXER FILE ********************************/
// JAVA IMPORTS ----------------------------------------------------------------
package ymcris.compilers.flowchart.lexer;

import java_cup.runtime.*;
import java.util.*;

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

Digit                   = [:jdigit:]
Letter                  = [:jletter:]
LetterDigit             = [:jletterdigit:]

Integer                 = (Digit)+
Double                  = {Integer}\.{Integer}

Identifier              = {Letter}({LetterDigit})*

LineTerminator          = \r|\n|\r\n
WhiteSpace              = {LineTerminator} | [ \t\f]

// JAVA CODE -------------------------------------------------------------------
%{

    // REFERENCE VARIABLES -----------------------------------------------------
    StringBuffer buffer;
    private List<String> errorList;

    // SPECIFIC METHODS --------------------------------------------------------
    // ERROR REPORT
    private void error(String message){
         errorList.add("Error in line: " + (yyline+1) + ", column: " +
         (yycolumn+1) + " : " + message);
    }

     // PARSER METHDOS
    private Symbol symbol(int type){
        return new Symbol(type, yyline+1, yycolumn+1);
    }
    
    private Symbol symbol(int type, Object value){
        return new Symbol(type, yyline+1, yycolumn+1, value);
    }

    // GETTERS -----------------------------------------------------------------
    public List<String> getLexicalErrors(){
        return this.errorList;
    }

%}

%%
/********************************* LEXICAL RULES ******************************/
<YYINITIAL>{
    // RESERVED WORDS
    "INICIO"          { return symbol(sym.START); }
    "VAR"             { return symbol(sym.VAR); }
    "FIN"             { return symbol(sym.END); }
    "SI"              { return symbol(sym.IF); }
    "ENTONCES"        { return symbol(sym.THEN); }
    "FINSI"           { return symbol(sym.IF_END); }
    "MIENTRAS"        { return symbol(sym.WHILE); }
    "HACER"           { return symbol(sym.DO); }
    "FINMIENTRAS"     { return symbol(sym.WHILE_END); }
    "MOSTRAR"         { return symbol(sym.SHOW); }

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
    {Double}          { return symbol(sym.DOUBLE, Double.parseDouble(yytext())); }
    {Integer}         { return symbol(sym.INTEGER, Integer.parseInt(yytext())); }

    {Identifier}      { return symbol(sym.IDENTIFIER, yytext()); }

    {WhiteSpace}+     { /* IGNORE */ }

    // STATES
    "#"               { yybegin(COMMENT); }

    // String
    \"                { buffer.setLength(0); yybegin(STRING); }
    
    .                 {
                        error("Lexical error: " + yytext());
                        return symbol(sym.ERROR, yytext());
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
                        error("Unclosed string literal");
                        yybegin(YYINITIAL);
                        return symbol(sym.ERROR);
}

<STRING><<EOF>> {
                        error("Unclosed string at EOF");
                        return symbol(sym.EOF);
}

<COMMENT>{

    \n                { yybegin(YYINITIAL); }
    .                 { /* IGNORE */ }

}

<COMMENT><<EOF>> {
                        error("Unclosed comment at EOF");
                        return symbol(sym.EOF);
}

<<EOF>>           {
                        return symbol(sym.EOF);
                  }

