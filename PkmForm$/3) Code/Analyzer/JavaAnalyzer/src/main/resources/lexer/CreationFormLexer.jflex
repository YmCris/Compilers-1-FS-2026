/********************************** LEXER FILE ********************************/
// JAVA IMPORTS ----------------------------------------------------------------
package ymcris.pkmforms.form.creation.analyzer.lexer;

// CUP
import java.util.*;
import java_cup.runtime.*;
import ymcris.pkmforms.form.creation.analyzer.parser.CreationFormSym;

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
Number                  = {Integer}\.{Integer}

Identifier              = {Letter}({LetterDigit})*

LineTerminator          = \r|\n|\r\n
WhiteSpace              = {LineTerminator} | [ \t\f]

HexColor                = \#[0-9a-fA-F]{6}
RGBColor                = \({Integer},{Integer},{Integer}\)
HSLColor                = \<{Integer},{Integer},{Integer}\>
BaseColor               = "RED"|"BLUE"|"GREEN"|"PURPLE"|"SKY"|"YELLOW"|"BLACK"|"WHITE"

Smile                   = "@[:\)+]"|"@[:smile:]"
Sad                     = "@[:\(+]"|"@[:sad:]"
Serious                 = "@[:\|+]"|"@[:serious:]"
Heart                   = "@[\<+\3+]"|"@[:heart:]"
Cat                     = "@[:^^:]"|"@[:cat:]"
Star                    = "@[:star:]"
StarNumber              = "@[:star:"{PositiveInteger}":]"|"@[:star-"{PositiveInteger}":]"

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

    public SymbolTable getTable(){
        return this.table;
    }
%}

%%
/********************************* LEXICAL RULES ******************************/
<YYINITIAL>{
    
    // Line comment
    \$                  { yybegin(COMMENT); }

    \"color\"           { return symbol(CreationFormSym.COLOR); }
    \"background\ color\" { return symbol(CreationFormSym.BACKGROUND_COLOR); }
    \"font\ family\"     { return symbol(CreationFormSym.FONT_FAMILY); }
    \"text\ size\"       { return symbol(CreationFormSym.TEXT_SIZE); }
    \"border\"          { return symbol(CreationFormSym.BORDER); }
    
    // RESERVED WORDS
    "IF"                { return symbol(CreationFormSym.IF); }
    "ELSE IF"           { return symbol(CreationFormSym.ELSE_IF); }
    "ELSE"              { return symbol(CreationFormSym.ELSE); }
    
    "WHILE"             { return symbol(CreationFormSym.WHILE); }
    "DO"                { return symbol(CreationFormSym.DO); }
    
    "FOR"               { return symbol(CreationFormSym.FOR); }
    "in"               { return symbol(CreationFormSym.IN); }
    
    "number"            { return symbol(CreationFormSym.NUMBER_VARIABLE); }
    "string"            { return symbol(CreationFormSym.STRING_VARIABLE); }
    "special"           { return symbol(CreationFormSym.SPECIAL_VARIABLE); }
    
    "draw"              { return symbol(CreationFormSym.DRAW); }
    
    "width"             { return symbol(CreationFormSym.WIDTH); }
    "height"            { return symbol(CreationFormSym.HEIGHT); }
    
    // ARITHMETIC OPERATORS
    "+"                 { return symbol(CreationFormSym.PLUS); }
    "-"                 { return symbol(CreationFormSym.MINUS); }
    "*"                 { return symbol(CreationFormSym.MULTIPLY); }
    "/"                 { return symbol(CreationFormSym.DIVIDE); }
    "^"                 { return symbol(CreationFormSym.EXPO); }
    "%"                 { return symbol(CreationFormSym.MODULE); }
    
    // RELATIONSHIP OPERATORS
    "=="                { return symbol(CreationFormSym.EQUAL); }
    "!!"                { return symbol(CreationFormSym.DIFERENT); }
    ">="                { return symbol(CreationFormSym.GOE); }
    "<="                { return symbol(CreationFormSym.LOE); }
    ">"                 { return symbol(CreationFormSym.GREATER); }
    "<"                 { return symbol(CreationFormSym.LESS); }
    "="                 { return symbol(CreationFormSym.ASSIGN); }

    // LOGIC OPERATORS
    "&&"                { return symbol(CreationFormSym.AND); }
    "||"                { return symbol(CreationFormSym.OR); }
    "~"                 { return symbol(CreationFormSym.NOT); }

    // GROUPING SYMBOLS
    "("                 { return symbol(CreationFormSym.OPEN_PARENT); }
    ")"                 { return symbol(CreationFormSym.CLOSED_PARENT); }
    "["                 { return symbol(CreationFormSym.OPEN_SQR_BRACKETS); }
    "]"                 { return symbol(CreationFormSym.CLOSED_SQR_BRACKETS); }
    "{"                 { return symbol(CreationFormSym.OPEN_CURLY_BRACKETS); }
    "}"                 { return symbol(CreationFormSym.CLOSED_CURLY_BRACKETS); }
    
    // END OF LINE
    ".."                 { return symbol(CreationFormSym.RANGE); }
    "."                 { return symbol(CreationFormSym.DOT); }
    ","                 { return symbol(CreationFormSym.COMMA); }
    ":"                 { return symbol(CreationFormSym.TWO_POINTS); }
    
    // SECTIONS
    "SECTION"           { return symbol(CreationFormSym.SECTION); }
    "pointX"            { return symbol(CreationFormSym.POINT_X); }
    "pointY"            { return symbol(CreationFormSym.POINT_Y); }
    "orientation"       { return symbol(CreationFormSym.ORIENTATION); }
    "VERTICAL"          { return symbol(CreationFormSym.VERTICAL); }
    "HORIZONTAL"        { return symbol(CreationFormSym.HORIZONTAL); }
    
    "elements"          { return symbol(CreationFormSym.ELEMENTS); }
    
    // TABLE
    "TABLE"             { return symbol(CreationFormSym.TABLE); }
    
    // TEXT
    "TEXT"              { return symbol(CreationFormSym.TEXT); }
    "content"           { return symbol(CreationFormSym.CONTENT); }
    
    //STYLES
    "styles"            { return symbol(CreationFormSym.STYLES); }
    
    "MONO"              { return symbol(CreationFormSym.MONO); }
    "SANS_SERIF"        { return symbol(CreationFormSym.SANS_SERIF); }
    "CURSIVE"           { return symbol(CreationFormSym.CURSIVE); }
    
    "LINE"              { return symbol(CreationFormSym.LINE_BORDER); }
    "DOTTED"            { return symbol(CreationFormSym.DOTTED_BORDER); }
    "DOUBLE"            { return symbol(CreationFormSym.DOUBLE_BORDER); }
    
    // QUESTIONS
    "label"             { return symbol(CreationFormSym.LABEL); }
    \?                  { return symbol(CreationFormSym.QUESTION); }
    "OPEN_QUESTION"     { return symbol(CreationFormSym.OPEN_QUESTION); }
    "DROP_QUESTION"     { return symbol(CreationFormSym.DROP_QUESTION); }
    "SELECT_QUESTION"   { return symbol(CreationFormSym.SELECT_QUESTION); }
    "MULTIPLE_QUESTION" { return symbol(CreationFormSym.MULTIPLE_QUESTION); }
    "options"           { return symbol(CreationFormSym.OPTIONS); }
    "first"             { return symbol(CreationFormSym.FIRST); }
    "second"            { return symbol(CreationFormSym.SECOND); }
    "third"             { return symbol(CreationFormSym.THIRD); }
    "fourth"            { return symbol(CreationFormSym.FOURTH); }
    "fifth"             { return symbol(CreationFormSym.FIFTH); }
    "correct"           { return symbol(CreationFormSym.CORRECT); }
    "who_is_that_pokemon" { return symbol(CreationFormSym.WHO_IS_THAT_POKEMON); }
    
    // MACROS
    {Number}            { return symbol(CreationFormSym.NUMBER, Double.valueOf(yytext())); }
    {Integer}           { return symbol(CreationFormSym.INTEGER, Integer.valueOf(yytext())); }

    {WhiteSpace}+       { /* IGNORE */ }
    
    {HexColor}          { return symbol(CreationFormSym.COLOR_VALUE, yytext()); }
    {RGBColor}          { return symbol(CreationFormSym.COLOR_VALUE, yytext()); }
    {HSLColor}          { return symbol(CreationFormSym.COLOR_VALUE, yytext()); }
    {BaseColor}         { return symbol(CreationFormSym.COLOR_VALUE, yytext()); }
    
    {Smile}             { return symbol(CreationFormSym.SMILE); }
    {Sad}               { return symbol(CreationFormSym.SAD); }
    {Serious}           { return symbol(CreationFormSym.SERIOUS); }
    {Heart}             { return symbol(CreationFormSym.HEART); }
    {Star}              { return symbol(CreationFormSym.STAR); }
    {StarNumber}        { return symbol(CreationFormSym.STAR_NUMBER); }
    {Cat}               { return symbol(CreationFormSym.CAT); }
    
    {Identifier}        {/*
                            table.addVariable(yytext(), null, null,
                            yyline+1, yycolumn+1);
                           */ 
                            return symbol(CreationFormSym.IDENTIFIER, yytext());
                        }

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
                            return symbol(CreationFormSym.error, yytext());
                        }

}

<STRING> {
    
    \"                  {
                            yybegin(YYINITIAL);
                            return symbol(CreationFormSym.STRING, buffer.toString());
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
                        return symbol(CreationFormSym.error);
}

<STRING><<EOF>> {
                        error(yytext(), "The string is not closed EOF");
                        return symbol(CreationFormSym.EOF);
}

<COMMENT>{

    \n                { yybegin(YYINITIAL); }
    .                 { /* IGNORE */ }

}

<COMMENT><<EOF>> {
                        error(yytext(), "The comment is not closed EOF");
                        return symbol(CreationFormSym.EOF);
}

<COMMENT_BLOCK>{

    "*/"                { yybegin(YYINITIAL); }
    
    [^]                { /* IGNORE */ }

}

<COMMENT_BLOCK><<EOF>> {
                        error(yytext(), "The comment is not closed EOF");
                        return symbol(CreationFormSym.EOF);
}


<<EOF>>           {
                        return symbol(CreationFormSym.EOF);
                  }
