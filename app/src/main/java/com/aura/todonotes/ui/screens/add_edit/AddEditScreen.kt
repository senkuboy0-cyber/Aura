package com.aura.todonotes.ui.screens.add_edit

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.LockOpen
import androidx.compose.material.icons.filled.PushPin
import androidx.compose.material.icons.filled.RadioButtonUnchecked
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
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
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel

private fun parseColorHex(colorHex: String): Color {
    return try {
        Color(android.graphics.Color.parseColor(colorHex))
    } catch (e: Exception) {
        Color.Unspecified
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditScreen(
    noteId: Long?,
    onNavigateBack: () -> Unit,
    viewModel: AddEditViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(noteId) {
        if (noteId != null && noteId > 0) {
            viewModel.loadNote(noteId)
        } else {
            viewModel.resetState()
        }
    }

    val parsedColor = remember(uiState.colorHex) { parseColorHex(uiState.colorHex) }
    val backgroundColor = if (parsedColor != Color.Unspecified) parsedColor else MaterialTheme.colorScheme.surface

    val textColor = if (isColorDark(backgroundColor)) Color.White else MaterialTheme.colorScheme.onSurface
    val subtleColor = if (isColorDark(backgroundColor)) Color.White.copy(alpha = 0.6f) else MaterialTheme.colorScheme.onSurfaceVariant

    val pinScale by animateFloatAsState(
        targetValue = if (uiState.isPinned) 1.2f else 1f,
        animationSpec = spring(stiffness = Spring.StiffnessMedium),
        label = "pin_scale"
    )

    val fabScale by animateFloatAsState(
        targetValue = if (uiState.isSaving) 0.9f else 1f,
        animationSpec = spring(stiffness = Spring.StiffnessMedium),
        label = "fab_scale"
    )

    LaunchedEffect(uiState.saveComplete) {
        if (uiState.saveComplete) {
            onNavigateBack()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = if (uiState.isEditMode) "Edit Note" else "New Note",
                        color = textColor,
                        style = MaterialTheme.typography.titleLarge
                    )
                },
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
                    IconButton(onClick = { viewModel.togglePin() }) {
                        Icon(
                            imageVector = Icons.Default.PushPin,
                            contentDescription = "Pin",
                            tint = if (uiState.isPinned) MaterialTheme.colorScheme.primary else textColor,
                            modifier = Modifier.scale(pinScale)
                        )
                    }

                    IconButton(onClick = { viewModel.toggleLock() }) {
                        Icon(
                            imageVector = if (uiState.isLocked) Icons.Default.Lock else Icons.Default.LockOpen,
                            contentDescription = "Lock",
                            tint = if (uiState.isLocked) MaterialTheme.colorScheme.primary else textColor
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = backgroundColor)
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { viewModel.saveNote() },
                containerColor = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .scale(fabScale)
                    .navigationBarsPadding()
            ) {
                if (uiState.isSaving) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = Color.White,
                        strokeWidth = 2.dp
                    )
                } else {
                    Icon(Icons.Default.Check, "Save", tint = Color.White)
                }
            }
        },
        containerColor = backgroundColor
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .imePadding()
                .navigationBarsPadding()
                .padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            item { Spacer(modifier = Modifier.height(8.dp)) }

            item {
                BasicTextField(
                    value = uiState.title,
                    onValueChange = { viewModel.updateTitle(it) },
                    textStyle = MaterialTheme.typography.headlineSmall.copy(color = textColor),
                    cursorBrush = SolidColor(textColor),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    decorationBox = { innerTextField ->
                        Box {
                            if (uiState.title.isEmpty()) {
                                Text(
                                    text = "Title",
                                    style = MaterialTheme.typography.headlineSmall,
                                    color = subtleColor
                                )
                            }
                            innerTextField()
                        }
                    }
                )
            }

            item {
                BasicTextField(
                    value = uiState.content,
                    onValueChange = { viewModel.updateContent(it) },
                    textStyle = MaterialTheme.typography.bodyLarge.copy(color = textColor.copy(alpha = 0.9f)),
                    cursorBrush = SolidColor(textColor),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp)
                        .padding(vertical = 8.dp),
                    decorationBox = { innerTextField ->
                        Box {
                            if (uiState.content.isEmpty()) {
                                Text(
                                    text = "Write your note...",
                                    style = MaterialTheme.typography.bodyLarge,
                                    color = subtleColor
                                )
                            }
                            innerTextField()
                        }
                    }
                )
            }

            if (uiState.tasks.isNotEmpty()) {
                item {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(top = 16.dp)
                    ) {
                        Text(
                            text = "Tasks",
                            style = MaterialTheme.typography.titleMedium,
                            color = textColor
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "(${uiState.tasks.count { it.isCompleted }}/${uiState.tasks.size})",
                            style = MaterialTheme.typography.bodySmall,
                            color = subtleColor
                        )
                    }
                }

                itemsIndexed(
                    items = uiState.tasks,
                    key = { _, task -> task.id }
                ) { index, task ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .background(if (task.isCompleted) subtleColor.copy(alpha = 0.1f) else Color.Transparent)
                            .clickable(onClick = { viewModel.toggleTask(index) })
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = if (task.isCompleted) Icons.Default.CheckCircle else Icons.Default.RadioButtonUnchecked,
                            contentDescription = if (task.isCompleted) "Completed" else "Not completed",
                            tint = if (task.isCompleted) Color(0xFF4CAF50) else subtleColor,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            text = task.content,
                            style = MaterialTheme.typography.bodyMedium,
                            color = if (task.isCompleted) textColor.copy(alpha = 0.5f) else textColor,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }

            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(24.dp)
                            .clip(CircleShape)
                            .background(subtleColor.copy(alpha = 0.2f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = null,
                            tint = subtleColor,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    BasicTextField(
                        value = uiState.newTaskContent,
                        onValueChange = { viewModel.updateNewTaskContent(it) },
                        textStyle = MaterialTheme.typography.bodyMedium.copy(color = textColor),
                        cursorBrush = SolidColor(textColor),
                        modifier = Modifier.weight(1f),
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                        keyboardActions = KeyboardActions(onDone = { viewModel.addTask() }),
                        decorationBox = { innerTextField ->
                            Box {
                                if (uiState.newTaskContent.isEmpty()) {
                                    Text(
                                        text = "Add a task...",
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = subtleColor
                                    )
                                }
                                innerTextField()
                            }
                        }
                    )
                    if (uiState.newTaskContent.isNotBlank()) {
                        IconButton(onClick = { viewModel.addTask() }) {
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = "Add Task",
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                }
            }

            item {
                ColorPickerRow(
                    selectedColor = uiState.colorHex,
                    onColorSelected = { viewModel.updateColor(it) },
                    textColor = textColor,
                    modifier = Modifier.padding(vertical = 16.dp)
                )
            }

            item { Spacer(modifier = Modifier.height(100.dp)) }
        }
    }
}

@Composable
private fun ColorPickerRow(
    selectedColor: String,
    onColorSelected: (String) -> Unit,
    textColor: Color,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Text(
            text = "Color",
            style = MaterialTheme.typography.labelMedium,
            color = textColor.copy(alpha = 0.6f),
            modifier = Modifier.padding(bottom = 12.dp)
        )

        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            val colors = listOf(
                "#FFFFFFFF", "#FFFFEB3B", "#FFFF9800", "#FFFF5722",
                "#FFEF5350", "#FFBA68C8", "#FF7986CB", "#FF4DB6AC",
                "#FF66BB6A", "#FF42A5F5", "#FF26C6DA", "#FFF06292"
            )
            colors.forEach { colorHex ->
                val parsedItemColor = remember(colorHex) { parseColorHex(colorHex) }
                val color = if (parsedItemColor != Color.Unspecified) parsedItemColor else Color.White
                val isSelected = colorHex == selectedColor
                val scale by animateFloatAsState(
                    targetValue = if (isSelected) 1.2f else 1f,
                    animationSpec = spring(stiffness = Spring.StiffnessMedium),
                    label = "color_scale_$colorHex"
                )

                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .scale(scale)
                        .clip(CircleShape)
                        .background(color)
                        .clickable(onClick = { onColorSelected(colorHex) })
                )
            }
        }
    }
}

private fun isColorDark(color: Color): Boolean {
    val luminance = 0.299 * color.red + 0.587 * color.green + 0.114 * color.blue
    return luminance < 0.5
}