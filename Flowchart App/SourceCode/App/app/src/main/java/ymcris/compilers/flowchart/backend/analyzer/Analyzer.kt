package ymcris.compilers.flowchart.backend.analyzer

import java_cup.runtime.Symbol
import ymcris.compilers.flowchart.backend.analyzer.lexer.Lexer
import ymcris.compilers.flowchart.backend.analyzer.parser.parser
import ymcris.compilers.flowchart.backend.core.exceptions.AnalyzerException
import java.io.StringReader

class Analyzer {

    lateinit var lexer: Lexer
    lateinit var par: parser

    fun analyze(text: String): ArrayList<ArrayList<Symbol>> {
        try {
            lexer= Lexer(StringReader(text))
            par = parser(lexer)
            par.parse();

            println("ANALISIS FINALIZADO:")
            println("Operadores: " + par.getOperators().size)
            println("Estructuras: " + par.getControlStructures().size)
            println("Errores: " + par.getErrorList().size)

            return lexer.lines
        } catch (e: Exception) {
            val message: String = e.message ?: "ANALYZER ERROR"
            throw AnalyzerException(message);
            e.printStackTrace()
        }

    }

    fun getOperators(): String{
        var builder : StringBuilder = StringBuilder()
        builder.append("OPERADOR   ")
        builder.append("LÍNEA   ")
        builder.append("COLUMNA   ")
        builder.append("OCURRENCIA   ")
        builder.append("\n")
        for (i in 0 until par.operators.size){
            builder.append(" | "+par.operators.get(i).type+" | ")
            builder.append(" | "+par.operators.get(i).row+" | ")
            builder.append(" | "+par.operators.get(i).column+" | ")
            builder.append(" | "+par.operators.get(i).occurrence+" | ")
            builder.append("\n")
        }
        return  builder.toString()
    }

    fun getStructures(): String{
        var builder : StringBuilder = StringBuilder()
        builder.append("OBJETO   ")
        builder.append("LÍNEA   ")
        builder.append("CONDICIÓN   ")
        builder.append("\n")
        for (i in 0 until par.controlStructures.size){
            builder.append(" | "+par.controlStructures.get(i).type+" | ")
            builder.append(" | "+par.controlStructures.get(i).row+" | ")
            builder.append(" | "+par.controlStructures.get(i).column+" | ")
            builder.append("\n")
        }
        return  builder.toString()
    }

    fun getErrors(): String{
        var builder : StringBuilder = StringBuilder()
        builder.append("LEXEMA   ")
        builder.append("LÍNEA   ")
        builder.append("COLUMNA   ")
        builder.append("TIPO   ")
        builder.append("DESCRIPCIÓN   ")
        builder.append("\n")
        for (i in 0 until par.errorList.size){
            builder.append(" | "+par.errorList.get(i).lexeme+" | ")
            builder.append(" | "+par.errorList.get(i).row+" | ")
            builder.append(" | "+par.errorList.get(i).column+" | ")
            builder.append(" | "+par.errorList.get(i).type+" | ")
            builder.append(" | "+par.errorList.get(i).message+" | ")
            builder.append("\n")
        }
        return  builder.toString()
    }

}