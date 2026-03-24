package ymcris.pkmforms.domain.model.forms.elements.questions.singleresponse

import ymcris.pkmforms.domain.model.forms.elements.questions.Question
import ymcris.pkmforms.domain.model.forms.style.Style

data class OpenQuestion (
    override val width: Double,
    override val height: Double,
    override val style: Style,
    override val label: String,
) : Question(width, height, style, label)