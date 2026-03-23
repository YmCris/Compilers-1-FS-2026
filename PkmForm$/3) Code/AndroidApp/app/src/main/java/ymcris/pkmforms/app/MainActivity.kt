package ymcris.pkmforms.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.LaunchedEffect
import ymcris.pkmforms.app.ui.FormScreen
import ymcris.pkmforms.app.ui.state.FormViewModel
import androidx.lifecycle.viewmodel.compose.viewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //enableEdgeToEdge()
        setContent {
            val vm: FormViewModel = viewModel()
            
            LaunchedEffect(Unit) {
                vm.loadForm()
            }
            
            vm.form?.let {
                FormScreen(it, vm)
            }
        }
    }
}