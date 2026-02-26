package ymcris.compilers.flowchart;

import java.io.IOException;
import java.io.StringReader;
import java_cup.runtime.Symbol;
import ymcris.compilers.flowchart.lexer.Lexer;
import ymcris.compilers.flowchart.parser.parser;
import ymcris.compilers.flowchart.parser.sym;

/**
 *
 * @author YmCris
 */
public class Flowchart {

    public static void main(String[] args) {
        //testLexer();
        testParser();
    }

    private static void testLexer() {
        try {
            String input = """
                INICIO
                           
                VAR a = 10 ;
                VAR b = 20
                SI (a < b) ENTONCES
                MOSTRAR "a es menor que b"
                FIN SI
                MIENTRAS (a < 15) HACER
                MOSTRAR a
                a = a + 1
                FIN MIENTRAS
                MOSTRAR "Fin del programa"
                FIN
                # comment
                """;

            Lexer lexer = new Lexer(new StringReader(input));

            Symbol token;

            System.out.println("===== TOKENS =====");

            while ((token = lexer.next_token()).sym != sym.EOF) {

                System.out.println(
                        "Token: " + sym.terminalNames[token.sym]
                        + " | Lexeme: " + (token.value != null ? token.value : "")
                        + " | Line: " + token.left
                        + " | Column: " + token.right
                );
            }

            if (!lexer.getLexicalErrors().isEmpty()) {
                System.out.println("\n===== LEXICAL ERRORS =====");
                lexer.getLexicalErrors().forEach(System.out::println);
            } else {
                System.out.println("\nNo lexical errors detected.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

        private static void testParser() {
            try {

                String codigo = """
                    INICIO
                    SI (a < b) ENTONCES
                                               MOSTRAR "a es menor que b"
                                               FIN SI           
                                VAR A
                                VAR =
                                VAR a = 10
                    VAR b = 20
                                a = a + 1
                    SI (a < b) ENTONCES
                    MOSTRAR "a es menor que b"

                    FIN SI
                    MIENTRAS (a < 15) HACER
                    MIENTRAS (a < 15) HACER
                    a = a + 1
                    MOSTRAR a
                                MOSTRAR "
                    FIN MIENTRAS
                    MOSTRAR "Fin del programa"
                    FIN
                    # comment
                    """;

                Lexer lexer = new Lexer(new StringReader(codigo));
                parser p = new parser(lexer);

                p.parse();

                System.out.println("=== ANÁLISIS FINALIZADO ===");

                System.out.println("Operadores: " + p.getOperators().size());
                System.out.println("Estructuras: " + p.getControlStructures().size());
                System.out.println("Errores: " + p.getErrorList().size());

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
}
