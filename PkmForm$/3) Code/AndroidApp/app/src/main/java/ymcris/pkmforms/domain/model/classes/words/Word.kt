package ymcris.pkmforms.domain.model.classes.words

data class Word(
    val value: String,
    var type: WordType
) {
    val ARITHMETIC_OPERATORS = listOf("+", "-", "*", "/", "^", "%")
    val RESERVED_WORDS = listOf("section", "text", "open", "select", "multiple", "drop")
    val GROUPING_SYMBOLS = listOf("(", ")", "[", "]", "{", "}", "<", ">")
    
    init {
        type = highlightWord()
    }
    
    public fun highlightWord(): WordType {
        // is arithmetic operator
        for (operator in ARITHMETIC_OPERATORS) {
            if (value == operator) {
                return WordType.ARITHMETIC_OPERATOR
            }
        }
        // is a variable
        // is a string
        if (value.startsWith("\"") && value.endsWith("\"")){
            return WordType.STRING
        }
        
        // is number
        try {
            value.toDouble()
            return WordType.NUMBER
        }catch (e: Exception){
        }
        // is reserved word
        for (word in RESERVED_WORDS) {
            if (value == word) {
                return WordType.RESERVED_WORD
            }
        }
        // is grouping symbol
        for (symbol in GROUPING_SYMBOLS) {
            if (value == symbol) {
                return WordType.GROUPING_SYMBOL
            }
        }
        // is emoji
        // is other
        return WordType.OTHER
    }
    
}