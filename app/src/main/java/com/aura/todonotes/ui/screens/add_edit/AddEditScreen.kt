package com.aura.todonotes.ui.screens.add_edit

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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.aura.todonotes.ui.components.ColorPicker
import com.aura.todonotes.ui.theme.isColorDark
import com.aura.todonotes.ui.theme.parseColorHex

@OptIn(androidx.compose.material3.ExperimentalMaterial3Api::class)
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

    val bgColor = parseColorHex(uiState.colorHex).let {
        if (it != Color.Unspecified) it else MaterialTheme.colorScheme.surface
    }
    val isDark = isColorDark(bgColor)
    val textColor = if (isDark) Color.White else MaterialTheme.colorScheme.onSurface
    val subtleColor = if (isDark) Color.White.copy(alpha = 0.6f) else MaterialTheme.colorScheme.onSurfaceVariant

    val fabInteraction = remember { MutableInteractionSource() }
    val fabPressed by fabInteraction.collectIsPressedAsState()
    val fabScale by animateFloatAsState(
        targetValue = if (fabPressed) 0.92f else 1f,
        animationSpec = spring(Spring.DampingRatioMediumBouncy, Spring.StiffnessMedium),
        label = "fab_scale"
    )

    val pinInteraction = remember { MutableInteractionSource() }
    val pinPressed by pinInteraction.collectIsPressedAsState()
    val pinRotation by animateFloatAsState(
        targetValue = if (uiState.isPinned) 45f else 0f,
        animationSpec = spring(Spring.DampingRatioMediumBouncy, Spring.StiffnessMedium),
        label = "pin_rotation"
    )

    LaunchedEffect(uiState.saveComplete) {
        if (uiState.saveComplete) onNavigateBack()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        if (uiState.isEditMode) "Edit Note" else "New Note",
                        color = textColor,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.SemiBold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back", tint = textColor)
                    }
                },
                actions = {
                    IconButton(onClick = { viewModel.togglePin() }) {
                        Icon(
                            Icons.Default.PushPin,
                            "Pin",
                            tint = if (uiState.isPinned) MaterialTheme.colorScheme.primary else textColor,
                            modifier = Modifier.rotate(pinRotation)
                        )
                    }
                    IconButton(onClick = { viewModel.toggleLock() }) {
                        Icon(
                            if (uiState.isLocked) Icons.Default.Lock else Icons.Default.LockOpen,
                            "Lock",
                            tint = if (uiState.isLocked) MaterialTheme.colorScheme.primary else textColor
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = bgColor)
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { viewModel.saveNote() },
                containerColor = MaterialTheme.colorScheme.primary,
                modifier = Modifier.scale(fabScale).navigationBarsPadding(),
                shape = RoundedCornerShape(16.dp)
            ) {
                if (uiState.isSaving) {
                    com.aura.todonotes.ui.components.AuraLoadingIndicator(size = 24)
                } else {
                    Icon(Icons.Default.Check, "Save", tint = Color.White)
                }
            }
        },
        containerColor = bgColor
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
            item { Spacer(Modifier.height(8.dp)) }

            // Title
            item {
                BasicTextField(
                    value = uiState.title,
                    onValueChange = { viewModel.updateTitle(it) },
                    textStyle = MaterialTheme.typography.headlineSmall.copy(
                        color = textColor,
                        fontWeight = FontWeight.SemiBold
                    ),
                    cursorBrush = SolidColor(textColor),
                    modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                    decorationBox = { inner ->
                        Box {
                            if (uiState.title.isEmpty()) Text("Title", style = MaterialTheme.typography.headlineSmall, color = subtleColor, fontWeight = FontWeight.SemiBold)
                            inner()
                        }
                    }
                )
            }

            // Content
            item {
                BasicTextField(
                    value = uiState.content,
                    onValueChange = { viewModel.updateContent(it) },
                    textStyle = MaterialTheme.typography.bodyLarge.copy(color = textColor.copy(alpha = 0.9f)),
                    cursorBrush = SolidColor(textColor),
                    modifier = Modifier.fillMaxWidth().height(200.dp).padding(vertical = 8.dp),
                    decorationBox = { inner ->
                        Box {
                            if (uiState.content.isEmpty()) Text("Write your note...", style = MaterialTheme.typography.bodyLarge, color = subtleColor)
                            inner()
                        }
                    }
                )
            }

            // Tasks
            if (uiState.tasks.isNotEmpty()) {
                item {
                    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(top = 16.dp)) {
                        Text("Tasks", style = MaterialTheme.typography.titleMedium, color = textColor, fontWeight = FontWeight.SemiBold)
                        Spacer(Modifier.width(8.dp))
                        Text("(${uiState.tasks.count { it.isCompleted }}/${uiState.tasks.size})", style = MaterialTheme.typography.bodySmall, color = subtleColor)
                    }
                }
                itemsIndexed(uiState.tasks, key = { _, t -> t.id }) { index, task ->
                    val taskInteraction = remember { MutableInteractionSource() }
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(if (task.isCompleted) subtleColor.copy(alpha = 0.1f) else Color.Transparent)
                            .clickable(interactionSource = taskInteraction, indication = null) { viewModel.toggleTask(index) }
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            if (task.isCompleted) Icons.Default.CheckCircle else Icons.Default.RadioButtonUnchecked,
                            contentDescription = null,
                            tint = if (task.isCompleted) Color(0xFF22C55E) else subtleColor,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(Modifier.width(12.dp))
                        Text(task.content, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Medium, color = if (task.isCompleted) textColor.copy(alpha = 0.5f) else textColor, modifier = Modifier.weight(1f))
                    }
                }
            }

            // Add Task
            item {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(Modifier.size(26.dp).clip(CircleShape).background(subtleColor.copy(alpha = 0.2f)), contentAlignment = Alignment.Center) {
                        Icon(Icons.Default.Add, null, tint = subtleColor, modifier = Modifier.size(16.dp))
                    }
                    Spacer(Modifier.width(12.dp))
                    BasicTextField(
                        value = uiState.newTaskContent,
                        onValueChange = { viewModel.updateNewTaskContent(it) },
                        textStyle = MaterialTheme.typography.bodyMedium.copy(color = textColor),
                        cursorBrush = SolidColor(textColor),
                        modifier = Modifier.weight(1f),
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                        keyboardActions = KeyboardActions(onDone = { viewModel.addTask() }),
                        decorationBox = { inner ->
                            Box {
                                if (uiState.newTaskContent.isEmpty()) Text("Add a task...", style = MaterialTheme.typography.bodyMedium, color = subtleColor)
                                inner()
                            }
                        }
                    )
                    if (uiState.newTaskContent.isNotBlank()) {
                        IconButton(onClick = { viewModel.addTask() }) { Icon(Icons.Default.Add, "Add", tint = MaterialTheme.colorScheme.primary) }
                    }
                }
            }

            // Color Picker
            item {
                ColorPickerRow(
                    selectedColor = uiState.colorHex,
                    onColorSelected = { viewModel.updateColor(it) },
                    textColor = textColor,
                    modifier = Modifier.padding(vertical = 16.dp)
                )
            }

            item { Spacer(Modifier.height(100.dp)) }
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
    Column(modifier) {
        Text("Color", style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.SemiBold, color = textColor.copy(alpha = 0.6f), modifier = Modifier.padding(bottom = 12.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier.fillMaxWidth()) {
            val colors = listOf(
                "#FFFFFFFF", "#FFFDE047", "#FFFB923C", "#FFF97316",
                "#FFEF4444", "#FFF472B6", "#FFC084FC", "#FF818CF8",
                "#FF60A5FA", "#FF22D3EE", "#FF2DD4BF", "#FF4ADE80", "#FFA3E635", "#FFFACC15"
            )
            colors.forEach { hex ->
                val c = parseColorHex(hex).let { if (it != Color.Unspecified) it else Color.White }
                val isSel = hex == selectedColor
                val s by animateFloatAsState(if (isSel) 1.15f else 1f, spring(Spring.DampingRatioMediumBouncy, Spring.StiffnessMedium), label = "cs_$hex")
                Box(Modifier.size(34.dp).scale(s).clip(CircleShape).background(c).clickable { onColorSelected(hex) })
            }
        }
    }
}