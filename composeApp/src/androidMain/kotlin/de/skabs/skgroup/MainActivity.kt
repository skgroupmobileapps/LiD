package de.skabs.skgroup

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import de.skabs.skgroup.core.navigation.Deeplinks

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        
        // Parse deeplink from intent
        val initialRoute = parseDeeplinkFromIntent(intent)

        setContent {
            App(initialDeeplinkRoute = initialRoute)
        }
    }
    
    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        // Handle deeplink when app is already running
        // For simplicity, we recreate the activity to navigate
        setIntent(intent)
        recreate()
    }
    
    private fun parseDeeplinkFromIntent(intent: Intent?): String? {
        val data = intent?.data ?: return null
        val url = data.toString()
        return Deeplinks.parseRoute(url)
    }
}

@Preview
@Composable
fun AppAndroidPreview() {
    App()
}