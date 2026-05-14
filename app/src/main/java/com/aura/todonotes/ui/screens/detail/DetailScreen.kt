package com.aura.todonotes.ui.screens.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Archive
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.PushPin
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.aura.todonotes.ui.components.ConfirmDialog
import com.aura.todonotes.ui.components.TaskItem
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(
    onNavigateBack: () -> Unit,
    onNavigateToEdit: (Long) -> Unit,
    onNavigateToLocked: (Long) -> Unit,
    viewModel: DetailViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    var showMenu by remember { mutableStateOf(false) }
    var showDeleteDialog by remember { mutableStateOf(false) }

    val note = uiState.note
    val noteColorHex = note?.colorHex ?: "#FFFFFF"

    // Parse color with remember
    val backgroundColorParsed = remember(noteColorHex) {
        try {
            Color(android.graphics.Color.parseColor(noteColorHex))
        } catch (e: Exception) {
            Color.Unspecified
        }
    }

    val backgroundColor = if (backgroundColorParsed == Color.Unspecified) {
        MaterialTheme.colorScheme.surface
    } else {
        backgroundColorParsed
    }

    val textColor = if (isColorDark(backgroundColor)) Color.White else Color.Black

    LaunchedEffect(uiState.isDeleted) {
        if (uiState.isDeleted) {
            onNavigateBack()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = textColor
                        )
                    }
                },
                actions = {
                    note?.let { n ->
                        IconButton(onClick = { viewModel.togglePin() }) {
                            Icon(Icons.Default.PushPin, "Pin", tint = textColor)
                        }
                        IconButton(onClick = { onNavigateToEdit(n.id) }) {
                            Icon(Icons.Default.Edit, "Edit", tint = textColor)
                        }
                        Box {
                            IconButton(onClick = { showMenu = true }) {
                                Icon(Icons.Default.MoreVert, "More", tint = textColor)
                            }
                            DropdownMenu(
                                expanded = showMenu,
                                onDismissRequest = { showMenu = false }
                            ) {
                                DropdownMenuItem(
                                    text = { Text("Archive") },
                                    onClick = { viewModel.toggleArchive(); showMenu = false },
                                    leadingIcon = { Icon(Icons.Default.Archive, null) }
                                )
                                DropdownMenuItem(
                                    text = { Text("Delete") },
                                    onClick = { showDeleteDialog = true; showMenu = false },
                                    leadingIcon = { Icon(Icons.Default.Delete, null) }
                                )
                            }
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = backgroundColor)
            )
        }
    ) { padding ->
        when {
            uiState.isLoading -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    CircularProgressIndicator()
                }
            }
            note == null -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text("Note not found")
                }
            }
            else -> {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(backgroundColor)
                        .padding(padding)
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    if (note.isLocked) {
                        item {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    Icons.Default.Lock,
                                    "Locked",
                                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                Text(
                                    "This note is locked",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    modifier = Modifier.padding(start = 8.dp)
                                )
                            }
                        }
                    }

                    if (note.title.isNotEmpty()) {
                        item {
                            Text(
                                note.title,
                                style = MaterialTheme.typography.headlineMedium,
                                color = textColor
                            )
                        }
                    }

                    if (note.content.isNotEmpty()) {
                        item {
                            Text(
                                note.content,
                                style = MaterialTheme.typography.bodyLarge,
                                color = textColor.copy(alpha = 0.9f)
                            )
                        }
                    }

                    if (uiState.tasks.isNotEmpty()) {
                        item {
                            Text(
                                "Tasks",
                                style = MaterialTheme.typography.titleMedium,
                                color = textColor
                            )
                        }
                        items(items = uiState.tasks, key = { it.id }) { task ->
                            TaskItem(
                                task = task,
                                onToggle = { viewModel.toggleTask(task.id) },
                                onDelete = { }
                            )
                        }
                    }

                    item {
                        Text(
                            "Last updated: ${formatDate(note.updatedAt)}",
                            style = MaterialTheme.typography.bodySmall,
                            color = textColor.copy(alpha = 0.5f)
                        )
                    }
                }
            }
        }
    }

    if (showDeleteDialog) {
        ConfirmDialog(
            title = "Move to Trash?",
            message = "This note will be moved to trash.",
            confirmText = "Move",
            onConfirm = { viewModel.moveToTrash(); showDeleteDialog = false },
            onDismiss = { showDeleteDialog = false },
            isDestructive = true
        )
    }
}

private fun isColorDark(color: Color): Boolean {
    val luminance = 0.299 * color.red + 0.587 * color.green + 0.114 * color.blue
    return luminance < 0.5
}

private fun formatDate(timestamp: Long): String {
    return SimpleDateFormat("MMM dd, yyyy 'at' hh:mm a", Locale.getDefault()).format(Date(timestamp))
}