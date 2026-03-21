package main;

import ymcris.pkmforms.analyzer.ast.AST;
import ymcris.pkmforms.analyzer.ast.Node;
import ymcris.pkmforms.analyzer.tokens.errors.ErrorToken;
import ymcris.pkmforms.form.creation.analyzer.lexer.FormCreationLexer;
import ymcris.pkmforms.form.creation.analyzer.parser.CreationFormParser;

import java.io.StringReader;
import java.util.List;

public class Main {

    // -------------------------------------------------------------------------
    // ENTRY POINT
    // -------------------------------------------------------------------------
    public static void main(String[] args) {
        String input = """
            $ ==========================================
            $ 1. DEFINICIÓN DE VARIABLES GLOBAL
            $ ==========================================
            number contador = 0
            number intentos = 3
            string titulo = "Encuesta de Entrenadores @[:star:3:]"
            
            /*
            Las variables especiales deben inicializarse siempre
            y pueden contener comodines '?'
            */
            special preguntaDinamica = OPEN_QUESTION [
                width: ?,
                height: ?,
                label: "Describe a tu Pokémon favorito @[:smile:]"
            ]
            
            $ ==========================================
            $ 2. MAQUETACIÓN PRINCIPAL (SECCIONES Y TABLAS)
            $ ==========================================
            SECTION [
                width: 800,      $ Valores literales, no variables
                height: 600,
                pointX: 0,
                pointY: 0,
                orientation: VERTICAL,
            
                elements: {
                    
                    TEXT [
                        content: "¡Bienvenido a la Liga Pokémon! @[:heart:]"
                    ],
            
                    DROP_QUESTION [
                        width: 300,
                        height: 40,
                        label: "Elige tu inicial de Kanto:",
                        $ Uso de la función a la PokéAPI
                        options: who_is_that_pokemon(2.2, 1, 3),
                        correct: 1,
                    ],
            
                    TABLE [
                        width: 600,
                        height: 150,
                        pointX: 10,
                        pointY: 10,
                        elements: {
                            [
                                {
                                    TEXT [
                                        content: "¿Eres líder de gimnasio?"
                                    ]
                                },
                                {
                                    SELECT_QUESTION [
                                        label: "Elige tu inicial de Kanto:",
                                        options: {"Sí", "No"},
                                        correct: 1,
                                    ]
                                }
                            ]
                        },
                       
                        styles [
                            "color": BLACK,
                            "background color": BLACK,
                            "font family": MONO,
                            "text size": 14,
                            "border": (1.2, LINE, BLACK)
                        ]
                    ]
                },
            
                styles [
                    "color": BLACK,
                    "background color": WHITE,
                    "font family": SANS_SERIF,
                    "text size": 16,
                    "border": (2.0, DOUBLE, BLUE)
                ]
            ]
            
            $ ==========================================
            $ 3. USO DE VARIABLES ESPECIALES
            $ ==========================================
            $ Se reemplazan los comodines '?' por 400 (width) y 80 (height)
            preguntaDinamica.draw(400, 80)
            
            $ ==========================================
            $ 4. BLOQUES DE CÓDIGO Y LÓGICA
            $ ==========================================
            
            $ Condicional con un solo tipo de operador lógico (&&)
            IF (10 > 5 && intentos == 3) {
                
                SECTION [
                    width: 300,
                    height: 100,
                    pointX: 0,
                    pointY: 0,
                    elements: {
                        TEXT [
                            content: "¡Tienes todos tus intentos intactos! @[:cat:]"
                        ]
                    },
                    styles [
                        "color": GREEN,
                        "background color": WHITE,
                        "font family": MONO,
                        "text size": 14,
                        "border": (1.2, LINE, GREEN)
                    ]
                ]
            
            } ELSE {
                contador = contador + 1
            }
            
            $ Ciclo WHILE válido usando la variable 'contador' ya declarada
            WHILE (contador < 3) {
                contador = contador + 1
            }
            
            $ Ciclo FOR de rango. La pregunta OBLIGATORIAMENTE va dentro de una SECTION.
            FOR (i in 1 .. 3) {
                SECTION [
                    width: 400,
                    height: 150,
                    pointX: 0,
                    pointY: 0,
                    elements: {
                        MULTIPLE_QUESTION [
                            label: "Elige tu inicial de Kanto:",
                            options: {"Ataque", "Defensa", "Velocidad"},
                            correct: {0, 2},
                        ]
                    },
                    styles [
                        "color": RED,
                        "background color": WHITE,
                        "font family": MONO,
                        "text size": 12,
                        "border": (1.0, DOTTED, RED)
                    ]
                ]
            }
            """;

        System.out.println("╔══════════════════════════════════════════╗");
        System.out.println("║         PKM FORMS - ANALYZER TEST        ║");
        System.out.println("╚══════════════════════════════════════════╝\n");

        try {
            FormCreationLexer lexer = runLexerFromString(input);
            if (lexer == null) {
                return;
            }

            AST ast = runParser(lexer);
            if (ast == null) {
                System.out.println("AST null");
                return;
            }

            printAST(ast);
            printErrors(lexer.getLexicalErrors());
            printSymbolTable(lexer);

        } catch (Exception e) {
            System.err.println("Error inesperado: " + e.getMessage());
            e.printStackTrace();
        }
    }

// -------------------------------------------------------------------------
// LEXER DESDE STRING
// -------------------------------------------------------------------------
    private static FormCreationLexer runLexerFromString(String input) {
        System.out.println("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
        System.out.println("  FASE 1: ANÁLISIS LÉXICO");
        System.out.println("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");

        try {
            StringReader reader = new StringReader(input);
            FormCreationLexer lexer = new FormCreationLexer(reader);
            System.out.println("✔ Lexer inicializado desde string.");
            return lexer;
        } catch (Exception e) {
            System.err.println("✘ Error al inicializar el lexer: " + e.getMessage());
            return null;
        }
    }

    // -------------------------------------------------------------------------
    // PARSER
    // -------------------------------------------------------------------------
    private static AST runParser(FormCreationLexer lexer) {
        System.out.println("\n━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
        System.out.println("  FASE 2: ANÁLISIS SINTÁCTICO");
        System.out.println("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");

        try {
            CreationFormParser parser = new CreationFormParser(lexer);
            java_cup.runtime.Symbol result = parser.parse();

            if (result != null && result.value instanceof Node) {
                Node root = (Node) result.value;
                AST ast = new AST(root);
                System.out.println("✔ Parsing completado correctamente.");
                return ast;
            } else {
                System.err.println("✘ El parser no retornó un nodo válido.");
                return null;
            }

        } catch (Exception e) {
            System.err.println("✘ Error durante el parsing: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    // -------------------------------------------------------------------------
    // PRINT AST
    // -------------------------------------------------------------------------
    private static void printAST(AST ast) {
        System.out.println("\n━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
        System.out.println("  ÁRBOL SINTÁCTICO ABSTRACTO (AST)");
        System.out.println("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");

        if (ast.getRoot() == null) {
            System.out.println("(árbol vacío)");
            return;
        }

        printNode(ast.getRoot(), "", true);
    }

    /**
     * Imprime el árbol con formato visual tipo árbol (├── └──)
     */
    private static void printNode(Node node, String prefix, boolean isLast) {
        if (node == null) {           // ← agrega esto
            System.out.println(prefix + "└── (null)");
            return;
        }

        String connector = isLast ? "└── " : "├── ";
        String label = node.getType().toString();
        if (node.getValue() != null) {
            label += " [" + node.getValue() + "]";
        }
        System.out.println(prefix + connector + label);

        List<Node> children = node.getChildren();
        String childPrefix = prefix + (isLast ? "    " : "│   ");

        for (int i = 0; i < children.size(); i++) {
            boolean lastChild = (i == children.size() - 1);
            printNode(children.get(i), childPrefix, lastChild);
        }
    }

    // -------------------------------------------------------------------------
    // ERRORS
    // -------------------------------------------------------------------------
    private static void printErrors(List<ErrorToken> errors) {
        System.out.println("\n━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
        System.out.println("  ERRORES ENCONTRADOS");
        System.out.println("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");

        if (errors == null || errors.isEmpty()) {
            System.out.println("✔ No se encontraron errores léxicos ni sintácticos.");
            return;
        }

        System.out.printf("✘ Total de errores: %d%n%n", errors.size());
        System.out.printf("%-10s %-12s %-6s %-6s %s%n",
                "TIPO", "LEXEMA", "LÍNEA", "COL", "DESCRIPCIÓN");
        System.out.println("─".repeat(70));

        for (ErrorToken error : errors) {
            System.out.printf("%-10s %-12s %-6d %-6d %s%n",
                    error.getType(),
                    truncate(error.getLexeme(), 12),
                    error.getRow(),
                    error.getColumn(),
                    truncate(error.getDescription(), 40)
            );
        }
    }

    // -------------------------------------------------------------------------
    // SYMBOL TABLE
    // -------------------------------------------------------------------------
    private static void printSymbolTable(FormCreationLexer lexer) {
        System.out.println("\n━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
        System.out.println("  TABLA DE SÍMBOLOS");
        System.out.println("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");

        try {
            lexer.getTable().printTable(); // asume que tu SymbolTable tiene print()
        } catch (Exception e) {
            System.out.println("(No se pudo imprimir la tabla de símbolos: " + e.getMessage() + ")");
        }
    }

    // -------------------------------------------------------------------------
    // UTILS
    // -------------------------------------------------------------------------
    private static String truncate(String s, int maxLen) {
        if (s == null) {
            return "null";
        }
        return s.length() <= maxLen ? s : s.substring(0, maxLen - 3) + "...";
    }
}
