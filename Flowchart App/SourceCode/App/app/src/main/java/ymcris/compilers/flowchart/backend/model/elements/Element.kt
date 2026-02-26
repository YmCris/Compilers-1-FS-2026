package ymcris.compilers.flowchart.backend.model.elements

class Element
{
    var type: ElementType
    lateinit var figure: FigureType
    lateinit var figureColor: String
    var text: String
    lateinit var textColor: String
    lateinit var textFont: TextFont
    var textSize: Int =0


    constructor(type: ElementType,figure: FigureType,figureColor: String,text: String,textColor: String,
                textFont: TextFont,textSize: Int){
        this.type = type
        this.figure = figure
        this.figureColor = figureColor
        this.text = text
        this.textColor = textColor
        this.textFont = textFont
        this.textSize = textSize
    }

    constructor(type: ElementType, text: String){
        this.type = type
        this.text = text
        setDefaultValues(type)
    }

    fun setDefaultValues(type: ElementType){
        if (type.equals(ElementType.START)){
            setValues(FigureType.CIRCLE,"RED","BLACK", TextFont.TIMES_NEW_ROMAN, 12);
            return;
        }
        if (type.equals(ElementType.END)){
            setValues(FigureType.CIRCLE,"RED","BLACK", TextFont.TIMES_NEW_ROMAN, 12);
            return;
        }
        if (type.equals(ElementType.BLOCK)){
            setValues(FigureType.PARALLELOGRAM,"BLUE","BLACK", TextFont.TIMES_NEW_ROMAN, 12);
            return;
        }
        if (type.equals(ElementType.CYCLE)){
            setValues(FigureType.RECTANGLE,"GREEN","BLACK", TextFont.TIMES_NEW_ROMAN, 12);
            return;
        }
        if (type.equals(ElementType.CONDITIONAL)){
            setValues(FigureType.RHOMBUS,"YELLOW","BLACK", TextFont.TIMES_NEW_ROMAN, 12);
            return;
        }
    }

    fun setValues(figure: FigureType,figureColor: String,textColor: String,
                  textFont: TextFont,textSize: Int){
        this.figure = figure
        this.figureColor = figureColor
        this.textColor = textColor
        this.textFont = textFont
        this.textSize = textSize
    }


}

