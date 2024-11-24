package b.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import b.ui.ui.theme.UiTheme
import b.ui.utils.Glass

class MainActivity : ComponentActivity() {
    private lateinit var glass: Glass
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.Theme_Ui) // Apply main transparent theme
        glass = Glass(
            context = this,
            window = window,
            backgroundBlurRadius = 80,
            blurBehindRadius = 20
        )
        glass.init()
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
    val navController = rememberNavController()
    glass.BlurAlphaWrapper {
        Scaffold(
            containerColor = Color.Transparent,
            modifier = Modifier.fillMaxSize()
        ) { innerPadding ->
            NavigationComponent(navController = navController, modifier = Modifier.padding(innerPadding))
        }
    }
}

@Composable
fun NavigationComponent(navController: NavHostController, modifier: Modifier = Modifier) {
    NavHost(navController = navController, startDestination = "home") {
        composable("home") {
            HomeScreen(navController)
        }
        composable("details") {
            DetailsScreen()
        }
    }
}

@Composable
fun HomeScreen(navController: NavController) {
    Button(onClick = { navController.navigate("details") }) {
        Text(text = "Go to Details")
    }
}

@Composable
fun DetailsScreen() {
    Text(text = "This is the Details Screen")
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    UiTheme {
    }
}
