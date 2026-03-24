package ymcris.pkmforms.domain.model.forms.style.borders

import ymcris.pkmforms.domain.model.forms.style.FontFamily
import ymcris.pkmforms.domain.model.forms.style.Style

data class Border(
    val thickness: Double,
    val type: BorderType,
    val color: String
){
    companion object {
        fun default(): Border {
            return Border(12.0, BorderType.LINE, "BLACK")
        }
    }
}
