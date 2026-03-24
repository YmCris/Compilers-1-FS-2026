package ymcris.pkmforms.domain.backend.builder

import ymcris.pkmforms.domain.backend.analyzer.ast.Node
import ymcris.pkmforms.domain.backend.analyzer.ast.NodeType
import ymcris.pkmforms.domain.backend.analyzer.symbols.table.SymbolTable
import ymcris.pkmforms.domain.model.forms.Form
import ymcris.pkmforms.domain.model.forms.elements.Element
import ymcris.pkmforms.domain.model.forms.elements.questions.multipleresponse.DropQuestion
import ymcris.pkmforms.domain.model.forms.elements.questions.multipleresponse.MultipleQuestion
import ymcris.pkmforms.domain.model.forms.elements.questions.singleresponse.OpenQuestion
import ymcris.pkmforms.domain.model.forms.elements.questions.singleresponse.SelectQuestion
import ymcris.pkmforms.domain.model.forms.elements.sections.OrientationType
import ymcris.pkmforms.domain.model.forms.elements.sections.Point
import ymcris.pkmforms.domain.model.forms.elements.sections.Section
import ymcris.pkmforms.domain.model.forms.elements.tables.Grid
import ymcris.pkmforms.domain.model.forms.elements.tables.Table
import ymcris.pkmforms.domain.model.forms.elements.texts.Text
import ymcris.pkmforms.domain.model.forms.style.FontFamily
import ymcris.pkmforms.domain.model.forms.style.Style
import ymcris.pkmforms.domain.model.forms.style.borders.Border
import ymcris.pkmforms.domain.model.forms.style.borders.BorderType

class FormBuilder(val symbolTable: SymbolTable) {
    
    
    fun build(root: Node): Form {
        val form = Form()
        val stmtList = root.children.first()
        for (node in stmtList.children) {
            buildStatement(node, form)
        }
        return form
    }
    
    private fun buildStatement(node: Node, target: Form) {
        when (node.type) {
            
            NodeType.VAR_DECLARATION -> {}
            NodeType.VAR_INITIALIZATION -> updateVariableValue(node)
            NodeType.VAR_ASSIGNMENT -> syncAssignment(node)
            NodeType.SPECIAL_INIT -> {}
            NodeType.DRAW_CALL -> {}
            
            NodeType.SECTION -> target.elements.add(buildSection(node))
            NodeType.TABLE -> target.elements.add(buildTable(node))
            
            NodeType.IF,
            NodeType.WHILE,
            NodeType.DO_WHILE,
            NodeType.FOR,
            NodeType.FOR_RANGE -> interpretControl(node, target)
            
            else -> { /* IGNORE */
            }
        }
    }
    
    private fun updateVariableValue(node: Node) {
        val name = node.children[1].value as? String ?: return
        val value = evaluateType(node.children[3])
        symbolTable.getVariable(name)?.setValue(value)
    }
    
    private fun syncAssignment(node: Node) {
        val name = node.children[0].value as? String ?: return
        val value = evaluateType(node.children[2])
        symbolTable.getVariable(name)?.setValue(value)
    }
    
    private fun interpretControl(node: Node, target: Form) {
        when (node.type) {
            
            NodeType.IF -> {
                val condition = node.children[0]
                val thenBlock = node.children[1]
                val branches = node.children.drop(2)
                
                if (evaluateBool(condition)) {
                    interpretBlock(thenBlock, target)
                } else {
                    var handled = false
                    for (branch in branches) {
                        if (branch.type == NodeType.ELSE_IF) {
                            if (!handled && evaluateBool(branch.children[0])) {
                                interpretBlock(branch.children[1], target)
                                handled = true
                            }
                        } else if (branch.type == NodeType.ELSE && !handled) {
                            interpretBlock(branch.children[0], target)
                            handled = true
                        }
                    }
                }
            }
            
            NodeType.WHILE -> {
                val condition = node.children[0]
                val body = node.children[1]
                var safety = 10_000
                while (evaluateBool(condition) && safety-- > 0) {
                    interpretBlock(body, target)
                }
            }
            
            NodeType.FOR_RANGE -> {
                val varName = node.children[0].value as? String ?: return
                val from = evaluateDouble(node.children[1]).toInt()
                val to = evaluateDouble(node.children[2]).toInt()
                val body = node.children[3]
                for (i in from..to) {
                    symbolTable.getVariable(varName)?.setValue(i.toDouble())
                    interpretBlock(body, target)
                }
            }
            
            NodeType.DO_WHILE -> {
                val body = node.children[0]
                val condition = node.children[1]
                var safety = 10_000
                do {
                    interpretBlock(body, target)
                } while (evaluateBool(condition) && safety-- > 0)
            }
            
            NodeType.FOR -> {
                
                updateVariableValue(node.children[0])
                val condition = node.children[1]
                val body = node.children[3]
                var safety = 10_000
                while (evaluateBool(condition) && safety-- > 0) {
                    interpretBlock(body, target)
                    
                    syncAssignment(node.children[2])
                }
            }
            
            else -> {/* IGNORE */
            }
        }
    }
    
    private fun interpretBlock(blockNode: Node, target: Form) {
        val stmtList = blockNode.findChild(NodeType.STMT_LIST) ?: return
        for (child in stmtList.children) {
            buildStatement(child, target)
        }
    }
    
    private fun buildSection(node: Node): Section {
        val body = node.findChild(NodeType.SECTION_BODY)!!
        val attrs = body.findChild(NodeType.ATTR_LIST)
        val styleH = body.findChild(NodeType.STYLE_BLOCK)
        val oriNd = body.findChild(NodeType.ORIENTATION)
        
        val width = attrs.attrDouble("width")
        val height = attrs.attrDouble("height")
        val pointX = attrs.attrDouble("pointX")
        val pointY = attrs.attrDouble("pointY")
        
        val style = buildStyle(styleH)
        val orientation = if (oriNd?.children?.firstOrNull()?.type == NodeType.VERTICAL)
            OrientationType.VERTICAL else OrientationType.HORIZONTAL
        
        val elements = mutableListOf<Element>()
        body.findChild(NodeType.ELEMENT_LIST)?.children?.forEach { child ->
            buildElement(child)?.let { elements.add(it) }
        }
        
        return Section(
            width = width,
            height = height,
            style = style,
            position = Point(pointX, pointY),
            orientation = orientation,
            elements = elements
        )
    }
    
    private fun buildTable(node: Node): Table {
        val tableElements = node.findChild(NodeType.TABLE_ELEMENTS)
        val attrs = node.findChild(NodeType.ATTR_LIST)
        val styleNd = node.findChild(NodeType.STYLE_BLOCK)
        
        val rows = mutableListOf<Node>()
        collectRows(tableElements, rows)
        
        val rowCount = rows.size
        val colCount = if (rows.isNotEmpty()) countCells(rows[0]) else 0
        val grid = Grid(rowCount.coerceAtLeast(1), colCount.coerceAtLeast(1))
        
        rows.forEachIndexed { r, rowNode ->
            var c = 0
            for (child in rowNode.children) {
                if (child.type == NodeType.CELL) {
                    val cellContent = child.children.firstOrNull()
                    grid[r, c] = cellContent?.let { buildElement(it) }
                    c++
                }
            }
        }
        
        return Table(
            width = attrs.attrDouble("width"),
            height = attrs.attrDouble("height"),
            style = buildStyle(styleNd),
            position = Point(attrs.attrDouble("pointX"), attrs.attrDouble("pointY")),
            rows = rowCount,
            columns = colCount,
            elements = grid
        )
    }
    
    private fun buildElement(node: Node): Element? = when (node.type) {
        NodeType.SECTION -> buildSection(node)
        NodeType.TABLE -> buildTable(node)
        NodeType.TEXT -> buildText(node)
        NodeType.OPEN_QUESTION -> buildOpenQuestion(node)
        NodeType.DROP_QUESTION -> buildDropQuestion(node)
        NodeType.SELECT_QUESTION -> buildSelectQuestion(node)
        NodeType.MULTIPLE_QUESTION -> buildMultipleQuestion(node)
        else -> null
    }
    
    private fun buildText(node: Node): Text {
        val body = node.findChild(NodeType.TEXT_BODY)
        
        val content = body
            ?.findChild(NodeType.CONTENT)
            ?.children?.firstOrNull()
            ?.value as? String ?: ""
        
        val width = body
            ?.findAttribute("width")
            ?.let { evaluateDouble(it) }
            ?: 0.0
        
        val height = body
            ?.findAttribute("height")
            ?.let { evaluateDouble(it) }
            ?: 0.0
        
        val style = body
            ?.findChild(NodeType.STYLE_BLOCK)
            ?.let { buildStyle(it) }
            ?: Style.default()
        
        return Text(
            width = width,
            height = height,
            style = style,
            content = content
        )
    }
    
    fun Node.findAttribute(name: String): Node? {
        return children.firstOrNull {
            it.type == NodeType.ATTR &&
                    it.value == name
        }?.children?.firstOrNull()
    }
    
    private fun buildOpenQuestion(node: Node): OpenQuestion {
        val (w, h, label, style) = extractQuestionBase(node)
        return OpenQuestion(width = w, height = h, style = style, label = label)
    }
    
    private fun buildDropQuestion(node: Node): DropQuestion {
        val (w, h, label, style) = extractQuestionBase(node)
        val options = extractOptions(node)
        val correct: List<Int> = node.findChild(NodeType.CORRECT)
            ?.children
            ?.map { evaluateDouble(it).toInt() }
            ?: emptyList()
        
        return DropQuestion(
            width = w,
            height = h,
            style = style,
            label = label,
            options = options,
            answers = emptyList(),
            correctAnswers = correct
        )
    }
    
    private fun buildSelectQuestion(node: Node): SelectQuestion {
        val (w, h, label, style) = extractQuestionBase(node)
        val options = extractOptions(node)
        val correct: List<Int> = node.findChild(NodeType.CORRECT)
            ?.children
            ?.map { evaluateDouble(it).toInt() }
            ?: emptyList()
        
        return SelectQuestion(
            width = w,
            height = h,
            style = style,
            label = label,
            options = options,
            answers = emptyList(),
            correctAnswers = correct
        )
    }
    
    private fun buildMultipleQuestion(node: Node): MultipleQuestion {
        val (w, h, label, style) = extractQuestionBase(node)
        val options = extractOptions(node)
        
        val correctAnswers = node.findChild(NodeType.CORRECT_LIST)
            ?.findChild(NodeType.VALUE_LIST)
            ?.children
            ?.map { evaluateDouble(it).toInt() }
            ?: emptyList()
        
        return MultipleQuestion(
            width = w,
            height = h,
            style = style,
            label = label,
            options = options,
            answers = emptyList(),
            correctAnswers = correctAnswers
        )
    }
    
    private data class QuestionBase(
        val width: Double,
        val height: Double,
        val label: String,
        val style: Style
    )
    
    private fun extractQuestionBase(node: Node): QuestionBase {
        val width = node.findDirectAttr("width") ?: 0.0
        val height = node.findDirectAttr("height") ?: 0.0
        val label = node.findChild(NodeType.LABEL)
            ?.children?.firstOrNull()
            ?.value as? String ?: ""
        val style = buildStyle(node.findChild(NodeType.STYLE_BLOCK))
        return QuestionBase(width, height, label, style)
    }
    
    private fun extractOptions(node: Node): List<String> {
        val optionsNode = node.findChild(NodeType.OPTIONS) ?: return emptyList()
        val first = optionsNode.children.firstOrNull() ?: return emptyList()
        
        return when (first.type) {
            NodeType.OPTION_LIST -> first.children.map { it.value as? String ?: "" }
            
            NodeType.WHO_IS_THAT_POKEMON -> {
                val n = evaluateDouble(first.children[0]).toInt()
                val m = evaluateDouble(first.children[1]).toInt()
                (n..m).map { it.toString() }
            }
            
            else -> emptyList()
        }
    }
    
    private fun buildStyle(node: Node?): Style {
        if (node == null) return Style.default()
        
        var color = "BLACK"
        var bgColor = "WHITE"
        var fontFamily = FontFamily.SANS_SERIF
        var textSize = 14.0
        var border = Border.default()
        
        for (styleChild in flattenStyleBlock(node)) {
            when (styleChild.type) {
                NodeType.COLOR_STYLE -> {
                    color = styleChild.children.firstOrNull()?.value as? String ?: color
                }
                
                NodeType.BACKGROUND_COLOR_STYLE -> {
                    bgColor = styleChild.children.firstOrNull()?.value as? String ?: bgColor
                }
                
                NodeType.FONT_FAMILY_STYLE -> {
                    val raw = styleChild.children.firstOrNull()?.value as? String ?: ""
                    fontFamily = FontFamily.fromString(raw)
                }
                
                NodeType.TEXT_SIZE_STYLE -> {
                    val sizeNode = styleChild.children.firstOrNull() ?: continue
                    textSize = evaluateDouble(sizeNode.children.firstOrNull() ?: sizeNode)
                }
                
                NodeType.BORDER -> {
                    border = buildBorder(styleChild)
                }
                
                else -> {}
            }
        }
        
        return Style(
            color = color,
            backgroundColor = bgColor,
            fontFamily = fontFamily,
            textSize = textSize,
            border = border
        )
    }
    
    private fun buildBorder(borderNode: Node): Border {
        val attr = borderNode.findChild(NodeType.BORDER_ATTR) ?: return Border.default()
        
        val thickness = attr.children.getOrNull(0)?.let { evaluateDouble(it) } ?: 1.0
        val typeNode = attr.children.getOrNull(1)
        val color = attr.children.getOrNull(2)?.value as? String ?: "BLACK"
        
        val borderType = when (typeNode?.type) {
            NodeType.LINE_BORDER -> BorderType.LINE
            NodeType.DOUBLE_BORDER -> BorderType.DOUBLE
            NodeType.DOTTED_BORDER -> BorderType.DOTTED
            else -> BorderType.LINE
        }
        
        return Border(thickness = thickness, type = borderType, color = color)
    }
    
    fun evaluateDouble(node: Node?): Double {
        if (node == null) return 0.0
        return when (node.type) {
            NodeType.NUMBER -> when (val v = node.value) {
                is Double -> v
                is Int -> v.toDouble()
                is Number -> v.toDouble()
                else -> 0.0
            }
            
            NodeType.ID -> {
                val name = node.value as? String ?: return 0.0
                val sym = symbolTable.getVariable(name) ?: return 0.0
                (sym.getValue() as? Number)?.toDouble() ?: 0.0
            }
            
            NodeType.ADD -> evaluateDouble(node.children[0]) + evaluateDouble(node.children[1])
            NodeType.SUB -> evaluateDouble(node.children[0]) - evaluateDouble(node.children[1])
            NodeType.MUL -> evaluateDouble(node.children[0]) * evaluateDouble(node.children[1])
            NodeType.DIV -> {
                val divisor = evaluateDouble(node.children[1])
                if (divisor == 0.0) 0.0 else evaluateDouble(node.children[0]) / divisor
            }
            
            NodeType.POW -> Math.pow(
                evaluateDouble(node.children[0]),
                evaluateDouble(node.children[1])
            )
            
            NodeType.MOD -> evaluateDouble(node.children[0]) % evaluateDouble(node.children[1])
            NodeType.NEG -> -evaluateDouble(node.children[0])
            NodeType.POS -> evaluateDouble(node.children[0])
            NodeType.TEXT_SIZE -> evaluateDouble(node.children.firstOrNull())
            else -> 0.0
        }
    }
    
    private fun evaluateBool(node: Node?): Boolean {
        if (node == null) return false
        return when (node.type) {
            NodeType.AND -> node.children.all { evaluateBool(it) }
            NodeType.OR -> node.children.any { evaluateBool(it) }
            NodeType.NOT -> !evaluateBool(node.children.firstOrNull())
            NodeType.GT -> evaluateDouble(node.children[0]) > evaluateDouble(node.children[1])
            NodeType.GE -> evaluateDouble(node.children[0]) >= evaluateDouble(node.children[1])
            NodeType.LT -> evaluateDouble(node.children[0]) < evaluateDouble(node.children[1])
            NodeType.LE -> evaluateDouble(node.children[0]) <= evaluateDouble(node.children[1])
            NodeType.EQ -> evaluateDouble(node.children[0]) == evaluateDouble(node.children[1])
            NodeType.NE -> evaluateDouble(node.children[0]) != evaluateDouble(node.children[1])
            else -> false
        }
    }
    
    private fun evaluateType(node: Node?): Any? {
        if (node == null) return null
        return when (node.type) {
            NodeType.STRING -> node.value as? String
            NodeType.NUMBER -> evaluateDouble(node)
            else -> evaluateDouble(node)
        }
    }
    
    private fun Node.findChild(type: NodeType): Node? {
        for (child in children) {
            if (child.type == type) return child
            child.findChild(type)?.let { return it }
        }
        return null
    }
    
    private fun Node.findDirectAttr(name: String): Double? {
        for (child in children) {
            if (child.type == NodeType.ATTR && child.value == name) {
                return child.children.firstOrNull()?.let { evaluateDouble(it) }
            }
        }
        return null
    }
    
    private fun Node?.attrDouble(name: String): Double {
        if (this == null) return 0.0
        for (child in children) {
            if (child.type == NodeType.ATTR && child.value == name) {
                return child.children.firstOrNull()?.let { evaluateDouble(it) } ?: 0.0
            }
        }
        return 0.0
    }
    
    private fun flattenStyleBlock(node: Node): List<Node> {
        val result = mutableListOf<Node>()
        for (child in node.children) {
            if (child.type == NodeType.STYLE_BLOCK) {
                result.addAll(flattenStyleBlock(child))
            } else {
                result.add(child)
            }
        }
        return result
    }
    
    private fun collectRows(node: Node?, rows: MutableList<Node>) {
        if (node == null) return
        for (child in node.children) {
            if (child.type == NodeType.ROW) rows.add(child)
            else collectRows(child, rows)
        }
    }
    
    private fun countCells(row: Node): Int =
        row.children.sumOf { child ->
            when (child.type) {
                NodeType.CELL -> 1
                NodeType.ROW -> countCells(child)
                else -> 0
            }
        }
}