package app.thdev.glassnavlab

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import app.thdev.glassnavlab.ui.demo.LiquidGlassDemoScreen
import app.thdev.glassnavlab.ui.theme.GlassNavLabTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            GlassNavLabTheme {
                LiquidGlassDemoScreen()
            }
        }
    }
}
