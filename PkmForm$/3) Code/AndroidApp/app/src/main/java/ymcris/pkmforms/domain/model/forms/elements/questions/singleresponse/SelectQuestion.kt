package ymcris.pkmforms.domain.model.forms.elements.questions.singleresponse

import ymcris.pkmforms.domain.backend.core.interfaces.MultipleResponsible
import ymcris.pkmforms.domain.model.forms.elements.questions.Question
import ymcris.pkmforms.domain.model.forms.style.Style

data class SelectQuestion(
    override val width: Double,
    override val height: Double,
    override val style: Style,
    override val label: String,
    
    override val options: List<String>,
    override val answers: List<String>,
    override val correctAnswers: List<Int>
) : Question(width, height, style, label), MultipleResponsible