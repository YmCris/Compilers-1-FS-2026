package ymcris.compilers.flowchart;

import java.io.StringReader;
import java.util.List;
import ymcris.compilers.flowchart.lexer.Lexer;
import ymcris.compilers.flowchart.parser.parser;

/**
 *
 * @author YmCris
 */
public class Flowchart {

    public static void main(String[] args) {

    }

    private static void testLexer() {
        String text = """
INICIO
VAR a = 10
VAR b = 20
SI (a < b) ENTONCES
MOSTRAR "a es menor que b"
FIN SI
MIENTRAS (a < 15) HACER
a = a + 1
MOSTRAR a
FIN MIENTRAS
MOSTRAR "Fin del programa"
FIN
                      """;

        StringReader reader = new StringReader(text);
        Lexer lexer = new Lexer(reader);
        while (!lexer.yyatEOF()) {
            
            
        }
    }

    private static void testParser() {
        String text = """
INICIO
VAR a = 10
VAR b = 20
SI (a < b) ENTONCES
MOSTRAR "a es menor que b"
FIN SI
MIENTRAS (a < 15) HACER
a = a + 1
MOSTRAR a
FIN MIENTRAS
MOSTRAR "Fin del programa"
FIN
                      """;

        StringBuilder stringBuilder = new StringBuilder();
        Lexer lexer = new Lexer(new StringReader(text));
        parser parser = new parser(lexer);

        try {
            parser.parse();

            stringBuilder.append(getErrors("ERRORES LEXICOS", lexer.getLexicalErrors()));
            stringBuilder.append(getErrors("ERRORES SINTACTICOS", parser.getSyntaxErrors()));

            if (lexer.getLexicalErrors().isEmpty() && parser.getSyntaxErrors().isEmpty()) {
                System.out.println("No hay errores");
            } else {
                System.out.println("Hay errores....");
                System.out.println(stringBuilder.toString());
            }
        } catch (Exception e) {
            System.out.println("Ocurrio un error inesperado");
        }
    }

    private static String getErrors(String title, List<String> errorsList) {
        StringBuilder builder = new StringBuilder(title);
        builder.append("\n");
        builder.append("---------------------------------------");
        builder.append("\n");
        if (errorsList.isEmpty()) {
            builder.append("No hay errores\n\n");
        } else {
            for (String error : errorsList) {
                builder.append(error);
                builder.append("\n");
            }
        }
        return builder.toString();
    }
}
