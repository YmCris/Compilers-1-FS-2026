package ymcris.compilers.flowchart.backend.model.pseudocode

import java_cup.runtime.Symbol
import ymcris.compilers.flowchart.backend.analyzer.parser.sym
import ymcris.compilers.flowchart.backend.core.exceptions.ListException
import ymcris.compilers.flowchart.backend.model.elements.Element
import ymcris.compilers.flowchart.backend.model.elements.ElementType
import ymcris.compilers.flowchart.backend.model.elements.FigureType
import ymcris.compilers.flowchart.backend.model.elements.TextFont


class Pseudocode {

    fun convertLinesToElements(lines: ArrayList<ArrayList<Symbol>>): ArrayList<Element> {

        if(lines.isEmpty()) throw ListException("Lines can't be empty")

        val elementsLists: ArrayList<Element> = ArrayList<Element>()

        val simpleText = StringBuilder()

        var insideBlock = false
        val blockText = StringBuilder()
        var blockType: ElementType? = null

        for (line in lines) {
            if (line.isEmpty()) continue

            val tokenType = line[0].sym

            if (tokenType == sym.START) {

                if (simpleText.isNotEmpty()) {
                    elementsLists.add(createElement(
                            ElementType.BLOCK,
                            FigureType.RECTANGLE,
                            simpleText.toString()
                        )
                    )
                    simpleText.setLength(0)
                }

                elementsLists.add(createElement(
                        ElementType.START,
                        FigureType.ELIPSE,
                        buildText(line)
                    )
                )
            } else if (tokenType == sym.IF) {
                if (simpleText.isNotEmpty()) {
                    elementsLists.add(
                        createElement(
                            ElementType.BLOCK,
                            FigureType.RECTANGLE,
                            simpleText.toString()
                        )
                    )
                    simpleText.setLength(0)
                }

                insideBlock = true
                blockType = ElementType.CONDITIONAL
                blockText.setLength(0)
                blockText.append(buildText(line)).append("\n")
            } else if (tokenType == sym.WHILE) {
                if (simpleText.isNotEmpty()) {
                    elementsLists.add(
                        createElement(
                            ElementType.BLOCK,
                            FigureType.RECTANGLE,
                            simpleText.toString()
                        )
                    )
                    simpleText.setLength(0)
                }

                insideBlock = true
                blockType = ElementType.CYCLE
                blockText.setLength(0)
                blockText.append(buildText(line)).append("\n")
            } else if (insideBlock &&
                (tokenType == sym.IF_END || tokenType == sym.WHILE_END)
            ) {
                blockText.append(buildText(line))

                val figure =
                    if (blockType === ElementType.CONDITIONAL)
                        FigureType.RHOMBUS
                    else
                        FigureType.ROUNDED_RECTANGLE

                elementsLists.add(createElement(
                        blockType,
                        figure,
                        blockText.toString()
                    )
                )

                insideBlock = false
                blockType = null
            } else if (insideBlock) {
                blockText.append(buildText(line)).append("\n")
            } else if (tokenType == sym.END) {
                if (simpleText.isNotEmpty()) {
                    elementsLists.add(createElement(
                            ElementType.BLOCK,
                            FigureType.RECTANGLE,
                            simpleText.toString()
                        )
                    )
                    simpleText.setLength(0)
                }

                elementsLists.add(createElement(
                        ElementType.END,
                        FigureType.ELIPSE,
                        buildText(line)
                    )
                )
            } else {
                simpleText.append(buildText(line)).append("\n")
            }
        }

        if (simpleText.isNotEmpty()) {
            elementsLists.add(createElement(
                    ElementType.BLOCK,
                    FigureType.RECTANGLE,
                    simpleText.toString()
                )
            )
        }

        return elementsLists
    }

    private fun createElement(type: ElementType?,figure: FigureType,text: String): Element {
        return Element(
            type!!,
            figure,
            "ROJO",
            text,
            "NEGRO",
            TextFont.ARIAL,
            12
        )
    }

    private fun buildText(line: List<Symbol>): String {
        val builder: StringBuilder = StringBuilder()
        for (symbol in line) {
            if (symbol.value != null) {
                builder.append(symbol.value.toString()).append(" ")
            }
        }
        return builder.toString().trim { it <= ' ' }
    }

}