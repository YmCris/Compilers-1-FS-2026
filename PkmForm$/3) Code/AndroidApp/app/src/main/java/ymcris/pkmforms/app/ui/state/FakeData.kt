package ymcris.pkmforms.app.ui.state

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import ymcris.pkmforms.app.ui.theme.PkmFormsTheme
import ymcris.pkmforms.domain.model.forms.Form
import ymcris.pkmforms.domain.model.forms.elements.questions.Question
import ymcris.pkmforms.domain.model.forms.elements.sections.OrientationType
import ymcris.pkmforms.domain.model.forms.elements.sections.Point
import ymcris.pkmforms.domain.model.forms.elements.sections.Section
import ymcris.pkmforms.domain.model.forms.style.FontFamily
import ymcris.pkmforms.domain.model.forms.style.Style
import ymcris.pkmforms.domain.model.forms.style.borders.Border
import ymcris.pkmforms.domain.model.forms.style.borders.BorderType

fun fakeForm(): Form {
    
    val question = Question(
        width = 100.0,
        height = 50.0,
        style = Style("xxx", "2", FontFamily.SANS_SERIF,12.0, Border(12.0, BorderType.LINE, "1.0")),
        label = "¿Cómo te llamas?"
    )
    
    val section = Section(
        width = 100.0,
        height = 100.0,
        style = Style("xxx", "2", FontFamily.SANS_SERIF,12.0, Border(12.0, BorderType.LINE, "1.0")),
        position = Point(0.0, 0.0),
        orientation = OrientationType.VERTICAL,
        elements = listOf(question)
    )
    
    return Form().apply {
        elements = listOf(section)
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    PkmFormsTheme {
        Greeting("Android")
    }
}