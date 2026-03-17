#!/bin/bash
set -e

# BASE ANALYZER PROJECT PATH ---------------------------------------------------
BASE="$HOME/University/Compilers I/CompilersFS2026 Projects/PkmForm$/3) Code"

# JFLEX JAR --------------------------------------------------------------------
JFLEX="$BASE/Resources/jflex-full-1.9.1.jar"

# .JFLEX FILE ------------------------------------------------------------------
LEXER="$BASE/Analyzer/JavaAnalyzer/src/main/resources/lexer/SavedFormLexer.jflex"

# DESTINATION PATH -------------------------------------------------------------
DESTINATION="$BASE/Analyzer/JavaAnalyzer/src/main/java/ymcris/pkmforms/form/saved/analyzer/lexer"

# EXECUTE SCRIPT ---------------------------------------------------------------
echo "Generating saved form lexer..."

# CREATING JAVA CLASS ----------------------------------------------------------
java -jar "$JFLEX" -d "$DESTINATION" "$LEXER"

echo "Lexer generated successfully"

