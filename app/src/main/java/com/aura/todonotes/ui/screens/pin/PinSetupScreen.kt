package com.aura.todonotes.ui.screens.pin

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Lock
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

    LaunchedEffect(uiState.isComplete) {
        if (uiState.isComplete) {
            onPinSet()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Set PIN") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .statusBarsPadding(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = Icons.Default.Lock,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(bottom = 24.dp)
            )

            Text(
                text = when {
                    uiState.hasExistingPin -> "Change PIN"
                    uiState.isConfirming -> "Confirm PIN"
                    else -> "Enter a 4-digit PIN"
                },
                style = MaterialTheme.typography.titleLarge
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = when {
                    uiState.hasExistingPin -> "Enter your new PIN"
                    uiState.isConfirming -> "Enter PIN again to confirm"
                    else -> "This PIN will be used to lock notes"
                },
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 32.dp)
            )

            Spacer(modifier = Modifier.height(32.dp))

            PinInput(
                pin = if (uiState.isConfirming) uiState.confirmPin else uiState.pin,
                onPinChange = { }
            )

            if (uiState.error != null) {
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = uiState.error!!,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.error
                )
            }

            Spacer(modifier = Modifier.height(48.dp))

            PinKeyboard(
                onNumberClick = { viewModel.onNumberClick(it) },
                onBackspaceClick = { viewModel.onBackspaceClick() },
                onClearClick = { viewModel.onClearClick() },
                modifier = Modifier.padding(horizontal = 32.dp)
            )

            Spacer(modifier = Modifier.height(24.dp))

            if (uiState.isConfirming && uiState.confirmPin.length == 4) {
                // PIN confirmed automatically
            } else if (!uiState.isConfirming && uiState.pin.length == 4) {
                TextButton(onClick = { viewModel.proceedToConfirm() }) {
                    Text("Continue")
                }
            }

            if (uiState.hasExistingPin) {
                Spacer(modifier = Modifier.height(16.dp))
                TextButton(onClick = { viewModel.removePin() }) {
                    Text(
                        text = "Remove PIN",
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }
        }
    }
}
