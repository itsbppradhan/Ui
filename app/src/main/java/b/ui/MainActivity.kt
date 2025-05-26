package b.ui

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import b.ui.components.BScaffold
import b.ui.ui.theme.UiTheme
import b.ui.utils.Glass

class MainActivity : ComponentActivity() {
    private lateinit var glass: Glass
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.Theme_Ui) // Apply main transparent theme
        glass = Glass(
//            context = this,
            window = window,
            backgroundBlurRadius = 80,
            blurBehindRadius = 20
        )
        Handler(Looper.getMainLooper()).post {
            glass.init()
        }
        enableEdgeToEdge()
        setContent {
            UiTheme {
                Start(glass)
            }
        }
    }
}



@Composable
fun Start(glass: Glass) {
    glass.BlurAlphaWrapper {
    BScaffold()

    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    UiTheme {
    }
}
