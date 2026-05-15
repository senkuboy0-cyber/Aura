package com.aura.todonotes.ui.screens.detail

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Archive
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.PushPin
import androidx.compose.material.icons.filled.RadioButtonUnchecked
import androidx.compose.material.icons.filled.Share
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.aura.todonotes.domain.model.Task
import com.aura.todonotes.ui.components.AuraLoadingIndicator
import com.aura.todonotes.ui.components.ConfirmDialog
import com.aura.todonotes.ui.theme.isColorDark
import com.aura.todonotes.ui.theme.parseColorHex
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(
    noteId: Long,
    onNavigateBack: () -> Unit,
    onNavigateToEdit: (Long) -> Unit,
    onNavigateToLocked: (Long) -> Unit,
    viewModel: DetailViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    var showMenu by remember { mutableStateOf(false) }
    var showDeleteDialog by remember { mutableStateOf(false) }

    LaunchedEffect(noteId) { viewModel.loadNote(noteId) }

    val note = uiState.note
    val bgColor = parseColorHex(note?.colorHex).let { if (it != Color.Unspecified) it else MaterialTheme.colorScheme.surface }
    val isDark = isColorDark(bgColor)
    val textColor = if (isDark) Color.White else MaterialTheme.colorScheme.onSurface
    val subtleColor = if (isDark) Color.White.copy(alpha = 0.6f) else MaterialTheme.colorScheme.onSurfaceVariant

    val pinScale by animateFloatAsState(
        if (note?.isPinned == true) 1.2f else 1f,
        spring(Spring.DampingRatioMediumBouncy, Spring.StiffnessMedium),
        label = "pin_scale"
    )

    LaunchedEffect(uiState.isDeleted) { if (uiState.isDeleted) onNavigateBack() }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {},
                navigationIcon = { IconButton(onClick = onNavigateBack) { Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back", tint = textColor) } },
                actions = {
                    note?.let { n ->
                        IconButton(onClick = { viewModel.togglePin() }) {
                            Icon(Icons.Default.PushPin, "Pin", tint = if (n.isPinned) MaterialTheme.colorScheme.primary else textColor, modifier = Modifier.scale(pinScale))
                        }
                        IconButton(onClick = { onNavigateToEdit(n.id) }) { Icon(Icons.Default.Edit, "Edit", tint = textColor) }
                        Box {
                            IconButton(onClick = { showMenu = true }) { Icon(Icons.Default.MoreVert, "More", tint = textColor) }
                            DropdownMenu(expanded = showMenu, onDismissRequest = { showMenu = false }, shape = RoundedCornerShape(16.dp)) {
                                DropdownMenuItem(text = { Text("Archive") }, onClick = { viewModel.toggleArchive(); showMenu = false }, leadingIcon = { Icon(Icons.Default.Archive, null) })
                                DropdownMenuItem(text = { Text("Share") }, onClick = { showMenu = false }, leadingIcon = { Icon(Icons.Default.Share, null) })
                                DropdownMenuItem(text = { Text("Delete", color = MaterialTheme.colorScheme.error) }, onClick = { showDeleteDialog = true; showMenu = false }, leadingIcon = { Icon(Icons.Default.Delete, null, tint = MaterialTheme.colorScheme.error) })
                            }
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = bgColor)
            )
        },
        containerColor = bgColor
    ) { padding ->
        when {
            uiState.isLoading -> Box(Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) { AuraLoadingIndicator() }
            note == null -> Box(Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) { Text("Note not found", style = MaterialTheme.typography.bodyLarge, color = textColor) }
            else -> {
                LazyColumn(
                    Modifier.fillMaxSize().padding(padding).padding(horizontal = 20.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    item { Spacer(Modifier.height(8.dp)) }

                    if (note.isLocked) {
                        item {
                            Row(
                                Modifier.fillMaxWidth().clip(RoundedCornerShape(12.dp)).background(textColor.copy(alpha = 0.1f)).padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(Icons.Default.Lock, null, tint = textColor, modifier = Modifier.size(20.dp))
                                Spacer(Modifier.width(8.dp))
                                Text("This note is locked", color = textColor, style = MaterialTheme.typography.bodySmall)
                            }
                        }
                    }

                    if (note.title.isNotEmpty()) {
                        item { Text(note.title, style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold, color = textColor) }
                    }
                    if (note.content.isNotEmpty()) {
                        item { Text(note.content, style = MaterialTheme.typography.bodyLarge, color = subtleColor) }
                    }

                    if (uiState.tasks.isNotEmpty()) {
                        item {
                            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(top = 8.dp)) {
                                Text("Tasks", style = MaterialTheme.typography.titleMedium, color = textColor, fontWeight = FontWeight.SemiBold)
                                Spacer(Modifier.width(8.dp))
                                Text("(${uiState.tasks.count { it.isCompleted }}/${uiState.tasks.size})", style = MaterialTheme.typography.bodySmall, color = subtleColor)
                            }
                        }
                        items(uiState.tasks, key = { it.id }) { task ->
                            TaskRow(task = task, textColor = textColor, subtleColor = subtleColor, onToggle = { viewModel.toggleTask(task.id) })
                        }
                    }

                    item {
                        Spacer(Modifier.height(16.dp))
                        Text(formatDate(note.updatedAt), style = MaterialTheme.typography.labelMedium, color = subtleColor.copy(alpha = 0.7f))
                    }
                    item { Spacer(Modifier.height(100.dp)) }
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

@Composable
private fun TaskRow(task: Task, textColor: Color, subtleColor: Color, onToggle: () -> Unit) {
    Row(
        Modifier.fillMaxWidth().clip(RoundedCornerShape(12.dp)).background(if (task.isCompleted) subtleColor.copy(alpha = 0.1f) else Color.Transparent).clickable(onClick = onToggle).padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            if (task.isCompleted) Icons.Default.CheckCircle else Icons.Default.RadioButtonUnchecked,
            null,
            tint = if (task.isCompleted) Color(0xFF22C55E) else subtleColor,
            modifier = Modifier.size(24.dp)
        )
        Spacer(Modifier.width(12.dp))
        Text(task.content, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Medium, color = if (task.isCompleted) textColor.copy(alpha = 0.5f) else textColor, modifier = Modifier.weight(1f))
    }
}

private fun formatDate(timestamp: Long): String = SimpleDateFormat("MMM dd, yyyy 'at' hh:mm a", Locale.getDefault()).format(Date(timestamp))