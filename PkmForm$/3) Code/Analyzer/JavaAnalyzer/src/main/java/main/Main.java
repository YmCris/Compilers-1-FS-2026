package main;

import java.io.StringReader;
import java_cup.runtime.Symbol;

import ymcris.pkmforms.form.creation.analyzer.lexer.FormCreationLexer;
import ymcris.pkmforms.form.creation.analyzer.parser.CreationFormParser;

import ymcris.pkmforms.analyzer.ast.Node;

public class Main {

    public static void main(String[] args) {
        String input = """
            number x = 10
            x = x + 5
            """;

        System.out.println("===== LEXER TEST =====");
        testLexer(input);

        System.out.println("\n===== PARSER TEST =====");
        Node ast = testParser(input);

        System.out.println("\n===== SEMANTIC TEST =====");
        testSemantic(ast);
    }

    // ========================= LEXER =========================
    public static void testLexer(String input) {
        try {
            FormCreationLexer lexer = new FormCreationLexer(new StringReader(input));

            Symbol token;
            do {
                token = lexer.next_token();

                System.out.println(
                    "Token: " + token.sym +
                    " | Value: " + token.value +
                    " | Line: " + token.left +
                    " | Col: " + token.right
                );

            } while (token.sym != 0); // EOF

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ========================= PARSER =========================
    public static Node testParser(String input) {
        try {
            FormCreationLexer lexer = new FormCreationLexer(new StringReader(input));
            CreationFormParser parser = new CreationFormParser(lexer);

            Node ast = (Node) parser.parse().value;

            System.out.println("AST generado correctamente:");
            printAST(ast, 0);

            return ast;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // ========================= SEMANTIC =========================
    public static void testSemantic(Node ast) {
        if (ast == null) {
            System.out.println("No hay AST para analizar.");
            return;
        }

        try {
            // 🔴 AQUÍ conectas tu analizador semántico real
            // Ejemplo:
            // SemanticAnalyzer analyzer = new SemanticAnalyzer();
            // analyzer.analyze(ast);

            System.out.println("Semántica ejecutada (placeholder)");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ========================= PRINT AST =========================
    public static void printAST(Node node, int level) {
        if (node == null) return;

        // indentación
        for (int i = 0; i < level; i++) {
            System.out.print("  ");
        }

        System.out.println(node.getType() + 
            (node.getValue() != null ? " -> " + node.getValue() : "")
        );

        if (node.getChildren() != null) {
            for (Node child : node.getChildren()) {
                printAST(child, level + 1);
            }
        }
    }
}