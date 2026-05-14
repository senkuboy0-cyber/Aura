package com.aura.todonotes.ui.screens.add_edit

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.LockOpen
import androidx.compose.material.icons.filled.PushPin
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.aura.todonotes.ui.components.ColorPicker
import com.aura.todonotes.ui.components.TaskItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditScreen(
    onNavigateBack: () -> Unit,
    viewModel: AddEditViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val backgroundColor = try {
        Color(android.graphics.Color.parseColor(uiState.colorHex))
    } catch (e: Exception) {
        MaterialTheme.colorScheme.surface
    }

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
                        color = if (isColorDark(backgroundColor)) Color.White
                               else MaterialTheme.colorScheme.onSurface
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = if (isColorDark(backgroundColor)) Color.White
                                   else MaterialTheme.colorScheme.onSurface
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { viewModel.togglePin() }) {
                        Icon(
                            imageVector = if (uiState.isPinned) Icons.Default.PushPin
                                         else Icons.Default.PushPin,
                            contentDescription = "Pin",
                            tint = if (isColorDark(backgroundColor)) Color.White
                                   else MaterialTheme.colorScheme.onSurface
                        )
                    }
                    IconButton(onClick = { viewModel.toggleLock() }) {
                        Icon(
                            imageVector = if (uiState.isLocked) Icons.Default.Lock
                                         else Icons.Default.LockOpen,
                            contentDescription = "Lock",
                            tint = if (isColorDark(backgroundColor)) Color.White
                                   else MaterialTheme.colorScheme.onSurface
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = backgroundColor
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { viewModel.saveNote() },
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                if (uiState.isSaving) {
                    CircularProgressIndicator(
                        modifier = Modifier.padding(8.dp),
                        color = Color.White
                    )
                } else {
                    Icon(Icons.Default.Check, "Save")
                }
            }
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(backgroundColor)
                .padding(padding)
                .padding(16.dp)
                .imePadding()
                .navigationBarsPadding(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                OutlinedTextField(
                    value = uiState.title,
                    onValueChange = { viewModel.updateTitle(it) },
                    placeholder = {
                        Text(
                            text = "Title",
                            color = if (isColorDark(backgroundColor))
                                Color.White.copy(alpha = 0.6f)
                            else Color.Black.copy(alpha = 0.4f)
                        )
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent
                    ),
                    textStyle = MaterialTheme.typography.titleLarge.copy(
                        color = if (isColorDark(backgroundColor)) Color.White
                               else Color.Black
                    ),
                    singleLine = true
                )
            }

            item {
                OutlinedTextField(
                    value = uiState.content,
                    onValueChange = { viewModel.updateContent(it) },
                    placeholder = {
                        Text(
                            text = "Write your note...",
                            color = if (isColorDark(backgroundColor))
                                Color.White.copy(alpha = 0.6f)
                            else Color.Black.copy(alpha = 0.4f)
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent
                    ),
                    textStyle = MaterialTheme.typography.bodyLarge.copy(
                        color = if (isColorDark(backgroundColor)) Color.White
                               else Color.Black
                    )
                )
            }

            if (uiState.tasks.isNotEmpty()) {
                item {
                    Text(
                        text = "Tasks",
                        style = MaterialTheme.typography.titleMedium,
                        color = if (isColorDark(backgroundColor)) Color.White
                               else MaterialTheme.colorScheme.onSurface
                    )
                }

                itemsIndexed(
                    items = uiState.tasks,
                    key = { index, _ -> "task_$index" }
                ) { index, task ->
                    TaskItem(
                        task = task,
                        onToggle = { viewModel.toggleTask(index) },
                        onDelete = { viewModel.removeTask(index) }
                    )
                }
            }

            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedTextField(
                        value = uiState.newTaskContent,
                        onValueChange = { viewModel.updateNewTaskContent(it) },
                        placeholder = {
                            Text(
                                text = "Add task...",
                                color = if (isColorDark(backgroundColor))
                                    Color.White.copy(alpha = 0.6f)
                                else Color.Black.copy(alpha = 0.4f)
                            )
                        },
                        modifier = Modifier.weight(1f),
                        singleLine = true,
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color.Transparent,
                            unfocusedContainerColor = Color.Transparent,
                            focusedBorderColor = if (isColorDark(backgroundColor))
                                Color.White.copy(alpha = 0.5f)
                            else Color.Black.copy(alpha = 0.3f),
                            unfocusedBorderColor = if (isColorDark(backgroundColor))
                                Color.White.copy(alpha = 0.3f)
                            else Color.Black.copy(alpha = 0.2f)
                        ),
                        textStyle = MaterialTheme.typography.bodyMedium.copy(
                            color = if (isColorDark(backgroundColor)) Color.White
                                   else Color.Black
                        ),
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                        keyboardActions = KeyboardActions(onDone = { viewModel.addTask() })
                    )
                    IconButton(
                        onClick = { viewModel.addTask() },
                        enabled = uiState.newTaskContent.isNotBlank()
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Add Task",
                            tint = if (isColorDark(backgroundColor)) Color.White
                                   else MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }

            item {
                ColorPicker(
                    selectedColor = uiState.colorHex,
                    onColorSelected = { viewModel.updateColor(it) },
                    modifier = Modifier.padding(top = 8.dp)
                )
            }

            item {
                Spacer(modifier = Modifier.height(80.dp))
            }
        }
    }
}

private fun isColorDark(color: Color): Boolean {
    val luminance = (0.299 * color.red + 0.587 * color.green + 0.114 * color.blue)
    return luminance < 0.5
}
