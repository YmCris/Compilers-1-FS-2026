/********************************** LEXER FILE ********************************/
// JAVA IMPORTS ----------------------------------------------------------------
package ymcris.pkmforms.form.saved.analyzer.lexer;

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
%class FormSavedLexer
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
    // RELATIONSHIP OPERATORS
    "="                         { return symbol(sym.ASSIGN); }

    // GROUPING SYMBOLS
    "("                         { return symbol(sym.OPEN_PARENTS); }
    ")"                         { return symbol(sym.CLOSED_PARENTS); }
    "{"                         { return symbol(sym.OPEN_CURLY_BRACKETS); }
    "}"                         { return symbol(sym.CLOSED_CURLY_BRACKETS); }
    
    "</"                        { return symbol(sym.OTHER_CLOSE_LABEL); }
    "/>"                        { return symbol(sym.SELF_CLOSE_LABEL); }
    "<"                         { return symbol(sym.OPEN_LABEL); }
    ">"                         { return symbol(sym.CLOSED_LABEL); }
    
    // END OF LINE
    ","                         { return symbol(sym.COMMA); }
    ":"                         { return symbol(sym.TWO_POINTS); }
    
    // SECTIONS
    "section"|"SECTION"         { return symbol(sym.SECTION); }
    "VERTICAL"|"vertical"       { return symbol(sym.VERTICAL); }
    "HORIZONTAL"|"horizontal"   { return symbol(sym.HORIZONTAL); }
    
    // TABLES
    "table"|"TABLE"             { return symbol(sym.TABLE); }
    "line"|"LINE"               { return symbol(sym.LINE); }
    "element"|"ELEMENT"         { return symbol(sym.ELEMENT); }
        
    // TEXT
    "open"|"OPEN"               { return symbol(sym.OPEN); }
    "content"|"CONTENT"         { return symbol(sym.CONTENT); }
    
    //STYLES
    "style"|"STYLE"             { return symbol(sym.STYLE); }
    "color"|"COLOR"             { return symbol(sym.COLOR); }
    "background color"|"BACKGROUND COLOR"  { return symbol(sym.BACKGROUND_COLOR); }
    "font family"|"FONT FAMILY" { return symbol(sym.FONT_FAMILY); }
    
    "MONO"|"mono"               { return symbol(sym.MONO); }
    "SANS_SERIF"|"sans_serif"   { return symbol(sym.SANS_SERIF); }
    "CURSIVE"|"cursive"         { return symbol(sym.CURSIVE); }
    
    "text size"|"TEXT SIZE"     { return symbol(sym.TEXT_SIZE); }
    "border"|"BORDER"           { return symbol(sym.BORDER); }
    //"LINE"|"line"             { return symbol(sym.LINE_BORDER); }
    "DOTTED"|"dotted"           { return symbol(sym.DOTTED_BORDER); }
    "DOUBLE"|"double"           { return symbol(sym.DOUBLE_BORDER); }
    
    // QUESTIONS
    "drop"|"DROP"               { return symbol(sym.DROP); }
    \"opcion 1\"|\"OPCION 1\"   { return symbol(sym.FIRST); }
    \"option 1\"|\"OPTION 1\"   { return symbol(sym.FIRST); }
    \"opcion 2\"|\"OPCION 2\"   { return symbol(sym.SECOND); }
    \"option 2\"|\"OPTION 2\"   { return symbol(sym.SECOND); }
    \"opcion 3\"|\"OPCION 3\"   { return symbol(sym.THIRD); }
    \"option 3\"|\"OPTION 3\"   { return symbol(sym.THIRD); }
    \"opcion 4\"|\"OPCION 4\"   { return symbol(sym.FOURTH); }
    \"option 4\"|\"OPTION 4\"   { return symbol(sym.FOURTH); }
    \"opcion 5\"|\"OPCION 5\"   { return symbol(sym.FIFTH); }
    \"option 5\"|\"OPTION 5\"   { return symbol(sym.FIFTH); }
    
    "SELECT"|"select"           { return symbol(sym.SELECT); }
    
    "MULTIPLE"|"multiple"       { return symbol(sym.MULTIPLE); }
    
    // Block comment
    "###"                       { yybegin(COMMENT_BLOCK); }
    
    // Line comment
    "#"                         { yybegin(COMMENT); }
    
    // MACROS
    {Double}                    { return symbol(sym.DOUBLE, Double.valueOf(yytext())); }
    {Integer}                   { return symbol(sym.INTEGER, Integer.valueOf(yytext())); }

    {Identifier}                {
                                    table.addVariable(yytext(), null, null,
                                        yyline+1, yycolumn+1);

                                    return symbol(sym.IDENTIFIER, yytext());
                                }
    
    {WhiteSpace}+               { /* IGNORE */ }
    
    {Smile}                     { return symbol(sym.SMILE); }
    {Sad}                       { return symbol(sym.SAD); }
    {Serious}                   { return symbol(sym.SERIOUS); }
    {Heart}                     { return symbol(sym.HEART); }
    {Star}                      { return symbol(sym.STAR); }
    {StarNumber}                { return symbol(sym.STAR_NUMBER); }
    {Cat}                       { return symbol(sym.CAT); }
    
    // String
    \"                          { 
                                    buffer.setLength(0);
                                    yybegin(STRING); 
                                }
    
    .                           {
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

    "###"                { yybegin(YYINITIAL); }
    
    [^]                { /* IGNORE */ }

}

<COMMENT_BLOCK><<EOF>> {
                        error(yytext(), "The comment is not closed EOF");
                        return symbol(sym.EOF);
}


<<EOF>>           {
                        return symbol(sym.EOF);
                  }
