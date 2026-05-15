package com.aura.todonotes.ui.screens.locked

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.aura.todonotes.ui.components.AuraLoadingIndicator
import com.aura.todonotes.ui.components.PinInput
import com.aura.todonotes.ui.components.PinKeyboard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LockedScreen(
    noteId: Long,
    onNavigateBack: () -> Unit,
    onUnlocked: () -> Unit,
    viewModel: LockedViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(uiState.isUnlocked) { if (uiState.isUnlocked) onUnlocked() }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Locked Note") },
                navigationIcon = { IconButton(onClick = onNavigateBack) { Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back") } }
            )
        }
    ) { padding ->
        Column(
            Modifier.fillMaxSize().padding(padding),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            val iconScale by animateFloatAsState(
                targetValue = 1f,
                animationSpec = spring(Spring.DampingRatioMediumBouncy, Spring.StiffnessLow),
                label = "lock_icon_scale"
            )

            Icon(Icons.Default.Lock, null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(72.dp).scale(iconScale))

            Spacer(Modifier.height(24.dp))

            Text("Enter PIN to Unlock", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.SemiBold)

            Spacer(Modifier.height(32.dp))

            PinInput(pin = uiState.pin, onPinChange = {}, isError = uiState.error != null)

            if (uiState.error != null) {
                Spacer(Modifier.height(16.dp))
                Text(uiState.error!!, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.error)
            }

            Spacer(Modifier.height(40.dp))

            if (uiState.isVerifying) {
                AuraLoadingIndicator()
            } else {
                PinKeyboard(
                    onNumberClick = { viewModel.onNumberClick(it) },
                    onBackspaceClick = { viewModel.onBackspaceClick() },
                    onClearClick = { viewModel.onClearClick() },
                    modifier = Modifier.padding(horizontal = 32.dp)
                )
            }
        }
    }
}