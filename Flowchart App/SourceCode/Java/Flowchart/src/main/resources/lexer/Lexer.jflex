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
%debug

// STATES ----------------------------------------------------------------------
%state STRING

// CONSTRUCTOR -----------------------------------------------------------------
%init{
    
    errorList = new ArrayList<>();
    string = new StringBuffer();

%init}

// MACROS ----------------------------------------------------------------------
LineTerminator          = \r|\n|\r\n
WhiteSpace              = {LineTerminator} | [ \t\f]

Integer                 = [0-9]+
Double                  = {Integer}\.{Integer}

Text                    = ([:jletterdigit:]_)*
Identifier              = [:jletter:]{Text}

// JAVA CODE -------------------------------------------------------------------
%{
    StringBuffer string;

    private void reportar(String message){
        System.out.println(message + " - line: " + (yyline + 1) + " col: " +
        (yycolumn + 1));
    }

    /*---------------------------------------------
        Codigo para el manejo de errores
    -----------------------------------------------*/

    private List<String> errorList;
    
    public List<String> getLexicalErrors(){
        return this.errorList;
    }

    /*-----------------------------------------------
          Codigo para el parser
    -------------------------------------------------*/
    private Symbol symbol(int type){
        return new Symbol(type, yyline+1, yycolumn+1);
    }

    private void error(String message){
         errorList.add("Error en la linea: " + (yyline+1) + ", columna: " +
         (yycolumn+1) + " : " + message);
    }

%}

%%
/********************************* LEXICAL RULES ******************************/
<YYINITIAL> {

        /*------------ pronombres ------------*/
    "I"                 { return symbol(sym.I); }
    "You"               { return symbol(sym.YOU); }
    "She"               { return symbol(sym.SHE); }
    "He"                { return symbol(sym.HE); }
    "It"                { return symbol(sym.IT); }
    "We"                { return symbol(sym.WE); }
    "They"              { return symbol(sym.THEY); }

    /*------------- verbos ----------------*/
    "Have"              { return symbol(sym.HAVE); }
    "Has"               { return symbol(sym.HAS); }
    "Sing"              { return symbol(sym.SING); }
    "Sings"             { return symbol(sym.SINGS); }
    "Drive"             { return symbol(sym.DRIVE); }
    "Drives"            { return symbol(sym.DRIVES); }
    "Walk"              { return symbol(sym.WALK); }
    "Walks"             { return symbol(sym.WALKS); }

    /*--------------- emojis ----------------*/
    ":)"                { return symbol(sym.HAPPY); }
    ":("                { return symbol(sym.SAD); }
    ":|"                { return symbol(sym.SERIOUS); }

    /*--------------- Otros -----------------*/
    "."                 { return symbol(sym.POINT); }
    ","                 { return symbol(sym.COMMA); }
    

    \"                  { string.setLength(0); yybegin(STRING); }


    "+"                 { reportar("    +"); }
    "-"                 { reportar("    -"); }

    {IntNumber}         { reportar("Entero: " + yytext()); }
    {FloatNumber}       { reportar("Decimal: " + yytext()); }
    {Email}             { reportar("Correo: " + yytext()); }
    {Identifier}        { reportar("Identificador: " + yytext()); }

    \"                  { string.setLength(0); yybegin(STRING); }

    {WhiteSpace}        { /* ignorar */ }

}

<STRING> {
    \"                  {
                            yybegin(YYINITIAL);
                            return symbol(sym.COMPLEMENT);
                            //reportar("String: " + string.toString());
                        }
    [^\n\r\"\\]+        { string.append( yytext() ); }
    \\t                 { string.append('\t'); }
    \\n                 { string.append('\n'); }

    \\r                 { string.append('\r'); }
    \\\"                { string.append('\"'); }
    \\                  { string.append('\\'); }
}

.          { error("Lexeme: <" + yytext() + ">"); }

<<EOF>>    {
                return symbol(sym.EOF);
           }

