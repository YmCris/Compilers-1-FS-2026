package ymcris.pkmforms.domain.backend.form.creation

import ymcris.pkmforms.domain.backend.analyzer.ast.AST
import ymcris.pkmforms.domain.backend.analyzer.ast.Node
import ymcris.pkmforms.domain.backend.analyzer.form.creation.analyzer.lexer.FormCreationLexer
import ymcris.pkmforms.domain.backend.analyzer.form.creation.analyzer.parser.CreationFormParser
import ymcris.pkmforms.domain.backend.analyzer.form.creation.analyzer.semantic.SemanticAnalyzer
import ymcris.pkmforms.domain.backend.analyzer.symbols.table.SymbolTable
import ymcris.pkmforms.domain.backend.analyzer.tokens.errors.ErrorToken
import ymcris.pkmforms.domain.backend.builder.FormBuilder
import ymcris.pkmforms.domain.model.forms.Form
import java.io.StringReader

class FormCreator {
    
    lateinit var table: SymbolTable
    lateinit var node: Node
    
    fun createForm(input: String) : Form {
        analyzeCode(input)
        println("Form analyzed")
        val builder = FormBuilder(table)
        val form = builder.build(node)
        println("Form built")
        return form
    }

    
    
    fun analyzeCode(input: String) {
        try {
            val lexer = runLexerFromString(input)
            if (lexer == null) {
                return
            }
            val ast = runParser(lexer)
            val semantic = SemanticAnalyzer(
                lexer.getTable(), lexer.getLexicalErrors()
            )
            semantic.analyze(ast!!.getRoot())
            
            if (ast == null) {
                println("AST null")
                return
            }
            table = lexer.getTable()
            node = ast.getRoot()
            printAST(ast)
            printErrors(lexer.getLexicalErrors())
            printSymbolTable(lexer)
        } catch (e: Exception) {
            System.err.println("Unexpected Error:: " + e.message)
            e.printStackTrace()
        }
    }
    
    private fun runLexerFromString(input: String): FormCreationLexer? {
        try {
            val reader = StringReader(input)
            val lexer = FormCreationLexer(reader)
            return lexer
        } catch (e: java.lang.Exception) {
            System.err.println("Error using string as lexer: " + e.message)
            return null
        }
    }
    
    private fun runParser(lexer: FormCreationLexer?): AST? {
        try {
            val parser = CreationFormParser(lexer)
            val result = parser.parse()
            
            if (result != null && result.value is Node) {
                val root = result.value as Node
                val ast = AST(root)
                return ast
            } else {
                System.err.println("Parser return null")
                return null
            }
        } catch (e: java.lang.Exception) {
            System.err.println("Error during parsing: " + e.message)
            e.printStackTrace()
            return null
        }
    }
    
    private fun printAST(ast: AST) {
        if (ast.getRoot() == null) {
            println("(empty tree)")
            return
        }
        
        printNode(ast.getRoot(), "", true)
    }
    
    private fun printNode(node: Node?, prefix: String?, isLast: Boolean) {
        if (node == null) {
            println(prefix + "└── (null)")
            return
        }
        
        val connector = if (isLast) "└── " else "├── "
        var label = node.getType().toString()
        if (node.getValue() != null) {
            label += " [" + node.getValue() + "]"
        }
        println(prefix + connector + label)
        
        val children = node.getChildren()
        val childPrefix = prefix + (if (isLast) "    " else "│   ")
        
        for (i in children.indices) {
            val lastChild = (i == children.size - 1)
            printNode(children.get(i), childPrefix, lastChild)
        }
    }
    
    private fun printErrors(errors: MutableList<ErrorToken>?) {
        if (errors == null || errors.isEmpty()) {
            println("Lexical and syntactic errors not founded")
            return
        }
        
        System.out.printf("Errors: %d%n%n", errors.size)
        System.out.printf(
            "%-10s %-12s %-6s %-6s %s%n",
            "Type", "Lexeme", "Line", "Col", "Description"
        )
        println("─".repeat(70))
        
        for (error in errors) {
            System.out.printf(
                "%-10s %-12s %-6d %-6d %s%n",
                error.getType(),
                truncate(error.getLexeme(), 12),
                error.getRow(),
                error.getColumn(),
                truncate(error.getDescription(), 40)
            )
        }
    }
    
    private fun printSymbolTable(lexer: FormCreationLexer) {
        println("Symbol table")
        try {
            lexer.getTable().printTable()
        } catch (e: java.lang.Exception) {
            println("(Can't print the symbol table because: " + e.message + ")")
        }
    }
    
    private fun truncate(s: String?, maxLen: Int): String {
        if (s == null) {
            return "null"
        }
        return if (s.length <= maxLen) s else s.substring(0, maxLen - 3) + "..."
    }
    
}