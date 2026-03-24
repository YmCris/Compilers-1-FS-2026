package ymcris.pkmforms.app.ui.render

import androidx.compose.runtime.Composable
import ymcris.pkmforms.app.ui.state.FormViewModel
import ymcris.pkmforms.domain.model.forms.elements.questions.Question
import ymcris.pkmforms.domain.model.forms.elements.questions.multipleresponse.DropQuestion
import ymcris.pkmforms.domain.model.forms.elements.questions.multipleresponse.MultipleQuestion
import ymcris.pkmforms.domain.model.forms.elements.questions.singleresponse.OpenQuestion
import ymcris.pkmforms.domain.model.forms.elements.questions.singleresponse.SelectQuestion

@Composable
fun RenderQuestion(q: Question, vm: FormViewModel) {
    val key = System.identityHashCode(q).toString()
    when (q) {
        is OpenQuestion     -> RenderOpenQuestion(q, key, vm)
        is DropQuestion     -> RenderDropQuestion(q, key, vm)
        is SelectQuestion   -> RenderSelectQuestion(q, key, vm)
        is MultipleQuestion -> RenderMultipleQuestion(q, key, vm)
        else -> {/* :( */}
    }
}