package ymcris.compilers.flowchart.backend.application.engine

import java_cup.runtime.Symbol
import ymcris.compilers.flowchart.backend.analyzer.Analyzer
import ymcris.compilers.flowchart.backend.model.elements.Element
import ymcris.compilers.flowchart.backend.model.flowchart.Flowchart
import ymcris.compilers.flowchart.backend.model.pseudocode.Pseudocode
import java.util.ArrayList

class Engine {

    var analyzer: Analyzer = Analyzer()
    var pseudocode: Pseudocode = Pseudocode()
    var flowchart: Flowchart = Flowchart()

    fun createFlowchart(text: String){
        var lines: ArrayList<ArrayList<Symbol>> = analyzer.analyze(text)
        var elementsList: List<Element>  = pseudocode.convertLinesToElements(lines)
    }
}