package de.skgroup.einburgerungstest

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import de.skgroup.einburgerungstest.designsystem.theme.PreviewSurface

@Preview
@Composable
private fun AppBottomNavigationBarPreview() {
    PreviewSurface(contentPadding = PaddingValues()) {
        Scaffold(
            bottomBar = {
                AppBottomNavigationBar(
                    currentRoute = "learn",
                    onNavigate = {}
                )
            }
        ) { paddingValues ->
            Surface(modifier = Modifier.padding(paddingValues)) {}
        }
    }
}
