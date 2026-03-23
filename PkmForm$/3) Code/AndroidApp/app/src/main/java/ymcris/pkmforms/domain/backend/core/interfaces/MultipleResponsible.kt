package ymcris.pkmforms.domain.backend.core.interfaces

interface MultipleResponsible {
    val options: List<String>
    val answers: List<String>
    val correctAnswers: List<Int>
}

