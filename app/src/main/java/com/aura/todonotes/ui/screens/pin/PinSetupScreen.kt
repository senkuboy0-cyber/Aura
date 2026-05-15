package com.aura.todonotes.ui.screens.pin

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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.aura.todonotes.ui.components.PinInput
import com.aura.todonotes.ui.components.PinKeyboard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PinSetupScreen(
    onNavigateBack: () -> Unit,
    onPinSet: () -> Unit,
    viewModel: PinSetupViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(uiState.isComplete) { if (uiState.isComplete) onPinSet() }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Set PIN") },
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
                label = "lock_icon"
            )

            Icon(Icons.Default.Lock, null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(64.dp).scale(iconScale).padding(bottom = 24.dp))

            Text(
                when {
                    uiState.hasExistingPin -> "Change PIN"
                    uiState.isConfirming -> "Confirm PIN"
                    else -> "Create a 4-digit PIN"
                },
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.SemiBold
            )

            Spacer(Modifier.height(8.dp))

            Text(
                when {
                    uiState.hasExistingPin -> "Enter your new 4-digit PIN"
                    uiState.isConfirming -> "Enter the PIN again to confirm"
                    else -> "This PIN protects your locked notes"
                },
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 32.dp)
            )

            Spacer(Modifier.height(40.dp))

            PinInput(
                pin = if (uiState.isConfirming) uiState.confirmPin else uiState.pin,
                onPinChange = {},
                isError = uiState.error != null
            )

            if (uiState.error != null) {
                Spacer(Modifier.height(16.dp))
                Text(uiState.error!!, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.error)
            }

            Spacer(Modifier.height(40.dp))

            PinKeyboard(
                onNumberClick = { viewModel.onNumberClick(it) },
                onBackspaceClick = { viewModel.onBackspaceClick() },
                onClearClick = { viewModel.onClearClick() },
                modifier = Modifier.padding(horizontal = 32.dp)
            )

            if (!uiState.isConfirming && uiState.pin.length == 4) {
                Spacer(Modifier.height(16.dp))
                Button(
                    onClick = { viewModel.proceedToConfirm() },
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier.padding(horizontal = 32.dp)
                ) {
                    Text("Continue", fontWeight = FontWeight.Medium)
                }
            }

            if (uiState.hasExistingPin) {
                Spacer(Modifier.height(16.dp))
                TextButton(onClick = { viewModel.removePin() }) {
                    Text("Remove PIN", color = MaterialTheme.colorScheme.error, fontWeight = FontWeight.Medium)
                }
            }
        }
    }
}