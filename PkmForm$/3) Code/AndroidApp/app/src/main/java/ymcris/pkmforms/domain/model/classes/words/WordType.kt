package ymcris.pkmforms.domain.model.classes.words

enum class WordType(val color: WordColor) {

    ARITHMETIC_OPERATOR(WordColor.GREEN),
    VARIABLE(WordColor.WHITE),
    STRING(WordColor.ORANGE),
    NUMBER(WordColor.SKY_BLUE),
    RESERVED_WORD(WordColor.PURPLE),
    GROUPING_SYMBOL(WordColor.BLUE),
    EMOJI(WordColor.YELLOW),
    OTHER(WordColor.WHITE)

}