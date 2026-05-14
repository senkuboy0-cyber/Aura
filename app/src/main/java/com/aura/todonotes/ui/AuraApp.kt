package com.aura.todonotes.ui


import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import com.aura.todonotes.domain.model.ThemeOption
import com.aura.todonotes.ui.theme.AuraTheme

@Composable
fun AuraApp(
    themeViewModel: ThemeViewModel = hiltViewModel()
) {
    val settings by themeViewModel.settings.collectAsState()
    
    AuraTheme(
        themeOption = settings.themeMode,
        content = {
            // Main content is handled by NavHost in MainActivity
        }
    )
}
