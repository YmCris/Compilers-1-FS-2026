/********************************** LEXER FILE ********************************/
// JAVA IMPORTS ----------------------------------------------------------------
package ymcris.pkmforms.form.creation.analyzer.lexer;

// CUP
import java.util.*;
import java_cup.runtime.*;
import ymcris.pkmforms.form.creation.analyzer.parser.sym;

// SYMBOL TABLE
import ymcris.pkmforms.analyzer.symbols.table.SymbolTable;

// TOKENS
import ymcris.pkmforms.analyzer.tokens.errors.ErrorType;
import ymcris.pkmforms.analyzer.tokens.errors.ErrorToken;

%%
/******************************* JFLEX DECLARATIONS ***************************/
%public
%unicode
%class FormCreationLexer
%cup
%line
%column

// STATES ----------------------------------------------------------------------
%state STRING
%state COMMENT
%state COMMENT_BLOCK

// CONSTRUCTOR -----------------------------------------------------------------
%init{
    
    table = new SymbolTable();
    buffer = new StringBuffer();
    errorList = new ArrayList<>();

%init}

// MACROS ----------------------------------------------------------------------

Letter                  = [:jletter:]
LetterDigit             = [:jletterdigit:]

Integer                 = [0-9]+
PositiveInteger         = [1-9][0-9]*
Double                  = {Integer}\.{Integer}

Identifier              = {Letter}({LetterDigit})*

LineTerminator          = \r|\n|\r\n
WhiteSpace              = {LineTerminator} | [ \t\f]

Smile                   = "@[:\)+]" | "@[:smile:]"
Sad                     = "@[:\(+]" | "@[:sad:]"
Serious                 = "@[:\|+]" | "@[:serious:]"
Heart                   = "@[\<+\3+]"| "@[:heart:]"
Star                    = "@[:star:]"
StarNumber              = "@[:star:"{PositiveInteger}":]" | "@[:star-"{PositiveInteger}":]"
Cat                     = "@[:^^:]" | "@[:cat:]"

// JAVA CODE -------------------------------------------------------------------
%{

    // REFERENCE VARIABLES -----------------------------------------------------
    private SymbolTable table;
    private StringBuffer buffer;
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
    "IF"                { return symbol(sym.IF); }
    "ELSE IF"           { return symbol(sym.ELSE_IF); }
    "ELSE"              { return symbol(sym.ELSE); }
    
    "WHILE"             { return symbol(sym.WHILE); }
    "DO"                { return symbol(sym.DO); }
    
    "FOR"               { return symbol(sym.FOR); }
    
    "number"            { return symbol(sym.NUMBER_VARIABLE); }
    "string"            { return symbol(sym.STRING_VARIABLE); }
    "special"           { return symbol(sym.SPECIAL_VARIABLE); }
    
    "draw"              { return symbol(sym.DRAW); }
    
    "width"             { return symbol(sym.WIDTH); }
    "height"            { return symbol(sym.HEIGHT); }
    
    // ARITHMETIC OPERATORS
    "+"                 { return symbol(sym.PLUS); }
    "-"                 { return symbol(sym.MINUS); }
    "*"                 { return symbol(sym.MULTIPLY); }
    "/"                 { return symbol(sym.DIVIDE); }
    "^"                 { return symbol(sym.EXPO); }
    "%"                 { return symbol(sym.MODULE); }
    
    // RELATIONSHIP OPERATORS
    "=="                { return symbol(sym.EQUAL); }
    "!!"                { return symbol(sym.DIFERENT); }
    ">="                { return symbol(sym.GOE); }
    "<="                { return symbol(sym.LOE); }
    ">"                 { return symbol(sym.GREATER); }
    "<"                 { return symbol(sym.LESS); }
    "="                 { return symbol(sym.ASSIGN); }

    // LOGIC OPERATORS
    "&&"                { return symbol(sym.AND); }
    "||"                { return symbol(sym.OR); }
    "~"                 { return symbol(sym.NOT); }

    // GROUPING SYMBOLS
    "("                 { return symbol(sym.OPEN_PARENT); }
    ")"                 { return symbol(sym.CLOSED_PARENT); }
    "["                 { return symbol(sym.OPEN_SQR_BRACKETS); }
    "]"                 { return symbol(sym.CLOSED_SQR_BRACKETS); }
    "{"                 { return symbol(sym.OPEN_CURLY_BRACKETS); }
    "}"                 { return symbol(sym.CLOSED_CURLY_BRACKETS); }
    
    // END OF LINE
    ","                 { return symbol(sym.COMMA); }
    ":"                 { return symbol(sym.TWO_POINTS); }
    
    // SECTIONS
    "pointX"            { return symbol(sym.POINT_X); }
    "pointY"            { return symbol(sym.POINT_Y); }
    "orientation"       { return symbol(sym.ORIENTATION); }
    "VERTICAL"          { return symbol(sym.VERTICAL); }
    "HORIZONTAL"        { return symbol(sym.HORIZONTAL); }
    
    "elements"          { return symbol(sym.ELEMENTS); }
    
    // TABLE
    "TABLE"             { return symbol(sym.TABLE); }
    
    // TEXT
    "TEXT"              { return symbol(sym.TEXT); }
    "content"           { return symbol(sym.CONTENT); }
    
    //STYLES
    "styles"            { return symbol(sym.STYLES); }
    \"color\"           { return symbol(sym.COLOR); }
    \"background color\" { return symbol(sym.BACKGROUND_COLOR); }
    \"font family\"     { return symbol(sym.FONT_FAMILY); }
    
    "MONO"              { return symbol(sym.MONO); }
    "SANS_SERIF"        { return symbol(sym.SANS_SERIF); }
    "CURSIVE"           { return symbol(sym.CURSIVE); }
    
    \"text size\"       { return symbol(sym.TEXT_SIZE); }
    \"border\"          { return symbol(sym.BORDER); }
    "LINE"              { return symbol(sym.LINE_BORDER); }
    "DOTTED"            { return symbol(sym.DOTTED_BORDER); }
    "DOUBLE"            { return symbol(sym.DOUBLE_BORDER); }
    
    // QUESTIONS
    "label"             { return symbol(sym.LABEL); }
    "OPEN_QUESTION"     { return symbol(sym.OPEN_QUESTION); }
    "DROP_QUESTION"     { return symbol(sym.DROP_QUESTION); }
    "SELECT_QUESTION"   { return symbol(sym.SELECT_QUESTION); }
    "MULTIPLE_QUESTION" { return symbol(sym.MULTIPLE_QUESTION); }
    "options"           { return symbol(sym.OPTIONS); }
    "first"             { return symbol(sym.FIRST); }
    "second"            { return symbol(sym.SECOND); }
    "third"             { return symbol(sym.THIRD); }
    "fourth"            { return symbol(sym.FOURTH); }
    "fifth"             { return symbol(sym.FIFTH); }
    "correct"           { return symbol(sym.CORRECT); }
    "who_is_that_pokemon" { return symbol(sym.WHO_IS_THAT_POKEMON); }
    
    // Line comment
    \$                  { yybegin(COMMENT); }
    
    // MACROS
    {Double}            { return symbol(sym.DOUBLE, Double.valueOf(yytext())); }
    {Integer}           { return symbol(sym.INTEGER, Integer.valueOf(yytext())); }

    {Identifier}        {
                            table.addVariable(yytext(), null, null,
                                yyline+1, yycolumn+1);
                            
                            return symbol(sym.IDENTIFIER, yytext());
                        }
    
    {WhiteSpace}+       { /* IGNORE */ }
    
    {Smile}             { return symbol(sym.SMILE); }
    {Sad}               { return symbol(sym.SAD); }
    {Serious}           { return symbol(sym.SERIOUS); }
    {Heart}             { return symbol(sym.HEART); }
    {Star}              { return symbol(sym.STAR); }
    {StarNumber}        { return symbol(sym.STAR_NUMBER); }
    {Cat}               { return symbol(sym.CAT); }
    

    // STATES
    // Block comment
    "/*"                { yybegin(COMMENT_BLOCK); }

    // String
    \"                  { 
                            buffer.setLength(0);
                            yybegin(STRING); 
                        }
    
    .                   {
                            error(yytext(), "The symbol doesn't exist in this language");
                            return symbol(sym.error, yytext());
                        }

}

<STRING> {
    
    \"                  {
                            yybegin(YYINITIAL);
                            return symbol(sym.STRING, buffer.toString());
                        }
                  
    [^\n\r\"\\]+        { buffer.append( yytext() ); }
    \\n                 { buffer.append('\n'); }
    \\t                 { buffer.append('\t'); }
    \\r                 { buffer.append('\r'); }
    \\\"                { buffer.append('\"'); }
    \\\\                { buffer.append('\\'); }
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

<COMMENT_BLOCK>{

    "*/"                { yybegin(YYINITIAL); }
    
    [^]                { /* IGNORE */ }

}

<COMMENT_BLOCK><<EOF>> {
                        error(yytext(), "The comment is not closed EOF");
                        return symbol(sym.EOF);
}


<<EOF>>           {
                        return symbol(sym.EOF);
                  }
