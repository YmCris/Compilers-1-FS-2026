#!/bin/bash
set -e

# BASE ANALYZER PROJECT PATH ---------------------------------------------------
BASE="$HOME/University/Compilers I/CompilersFS2026 Projects/PkmForm$/3) Code"

# JFLEX JAR --------------------------------------------------------------------
JFLEX="$BASE/Resources/jflex-full-1.9.1.jar"

# .JFLEX FILE ------------------------------------------------------------------
LEXER="$BASE/Analyzer/JavaAnalyzer/src/main/resources/lexer/CreationFormLexer.jflex"

# CUP JAR ----------------------------------------------------------------------
CUP="$BASE/Resources/java-cup-11b.jar"

# .CUP FILE --------------------------------------------------------------------
PARSER="$BASE/Analyzer/JavaAnalyzer/src/main/resources/parser/CreationFormParser.cup"

# DESTINATION PATH -------------------------------------------------------------
DESTINATION_LEXER="$BASE/Analyzer/JavaAnalyzer/src/main/java/ymcris/pkmforms/form/creation/analyzer/lexer"
DESTINATION_PARSER="$BASE/Analyzer/JavaAnalyzer/src/main/java/ymcris/pkmforms/form/creation/analyzer/parser"

# EXECUTE SCRIPT ---------------------------------------------------------------
echo "Generating creation form analyzer..."

# CREATING JAVA CLASS ----------------------------------------------------------
java -jar "$JFLEX" -d "$DESTINATION_LEXER" "$LEXER"
java -jar "$CUP"  -expect 1 -parser CreationFormParser -symbols CreationFormSym -destdir "$DESTINATION_PARSER" "$PARSER"

echo "Analyzer generated successfully"
