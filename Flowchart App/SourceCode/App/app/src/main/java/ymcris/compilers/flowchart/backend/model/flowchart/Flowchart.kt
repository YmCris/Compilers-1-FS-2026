package ymcris.compilers.flowchart.backend.model.flowchart

import ymcris.compilers.flowchart.backend.model.elements.Element
import ymcris.compilers.flowchart.backend.model.elements.ElementType
import java.util.LinkedList

class Flowchart {

    var elements: LinkedList<Element> = LinkedList();

    /*
    fun createElement(components: ArrayList<Element>){
        var text : String
        var type: ElementType
        when (components[0].type) {
            ElementType.START ->
            ElementType.END ->
            ElementType.CONDITIONAL ->
            ElementType.CYCLE ->
            ElementType.BLOCK ->
            else -> println("...")
        }


        var element : Element = Element( type,text)
    }*/

}