package com.aura.todonotes.ui.screens.home

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Archive
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Sort
import androidx.compose.material.icons.filled.ViewAgenda
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.aura.todonotes.domain.model.Note
import com.aura.todonotes.domain.model.SortOrder
import com.aura.todonotes.domain.model.ViewMode
import com.aura.todonotes.ui.components.ConfirmDialog
import com.aura.todonotes.ui.components.EmptyState
import com.aura.todonotes.ui.components.NoteCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onNavigateToAddEdit: () -> Unit,
    onNavigateToDetail: (Long) -> Unit,
    onNavigateToSearch: () -> Unit,
    onNavigateToTrash: () -> Unit,
    onNavigateToArchive: () -> Unit,
    onNavigateToSettings: () -> Unit,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    var showSortMenu by remember { mutableStateOf(false) }
    var showViewMenu by remember { mutableStateOf(false) }
    var noteToDelete by remember { mutableStateOf<Note?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Aura",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                },
                actions = {
                    IconButton(onClick = onNavigateToSearch) {
                        Icon(Icons.Default.Search, "Search")
                    }
                    Box {
                        IconButton(onClick = { showViewMenu = true }) {
                            Icon(
                                imageVector = if (uiState.viewMode == ViewMode.LIST)
                                    Icons.Default.ViewAgenda else Icons.Default.GridView,
                                contentDescription = "View"
                            )
                        }
                        DropdownMenu(
                            expanded = showViewMenu,
                            onDismissRequest = { showViewMenu = false }
                        ) {
                            DropdownMenuItem(
                                text = { Text("List") },
                                onClick = { showViewMenu = false },
                                leadingIcon = { Icon(Icons.Default.List, null) }
                            )
                            DropdownMenuItem(
                                text = { Text("Grid") },
                                onClick = { showViewMenu = false },
                                leadingIcon = { Icon(Icons.Default.GridView, null) }
                            )
                        }
                    }
                    Box {
                        IconButton(onClick = { showSortMenu = true }) {
                            Icon(Icons.Default.Sort, "Sort")
                        }
                        DropdownMenu(
                            expanded = showSortMenu,
                            onDismissRequest = { showSortMenu = false }
                        ) {
                            SortOrder.entries.forEach { sort ->
                                DropdownMenuItem(
                                    text = {
                                        Text(
                                            when (sort) {
                                                SortOrder.NEWEST -> "Newest First"
                                                SortOrder.OLDEST -> "Oldest First"
                                                SortOrder.ALPHABETICAL_AZ -> "A to Z"
                                                SortOrder.ALPHABETICAL_ZA -> "Z to A"
                                            }
                                        )
                                    },
                                    onClick = { showSortMenu = false }
                                )
                            }
                        }
                    }
                    IconButton(onClick = onNavigateToSettings) {
                        Icon(Icons.Default.Settings, "Settings")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        },
        bottomBar = {
            BottomAppBar(
                containerColor = MaterialTheme.colorScheme.surface,
                tonalElevation = 8.dp
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    IconButton(onClick = onNavigateToTrash) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(Icons.Default.Delete, "Trash")
                            Text("Trash", style = MaterialTheme.typography.labelSmall)
                        }
                    }
                    IconButton(onClick = onNavigateToArchive) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(Icons.Default.Archive, "Archive")
                            Text("Archive", style = MaterialTheme.typography.labelSmall)
                        }
                    }
                }
            }
        },
        floatingActionButton = {
            val scale by animateFloatAsState(
                targetValue = 1f,
                animationSpec = spring(stiffness = Spring.StiffnessMedium),
                label = "fab_scale"
            )
            
            FloatingActionButton(
                onClick = onNavigateToAddEdit,
                containerColor = MaterialTheme.colorScheme.primary,
                modifier = Modifier.scale(scale)
            ) {
                Icon(Icons.Default.ViewAgenda, "Add Note", tint = Color.White)
            }
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { padding ->
        Column(modifier = Modifier.padding(padding)) {
            if (uiState.notes.isEmpty() && !uiState.isLoading) {
                EmptyState(
                    icon = Icons.Default.ViewAgenda,
                    title = "No Notes Yet",
                    description = "Tap the + button to create your first note"
                )
            } else {
                if (uiState.viewMode == ViewMode.LIST) {
                    LazyColumn(
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(
                            items = uiState.notes,
                            key = { it.id }
                        ) { note ->
                            AnimatedNoteItem(
                                note = note,
                                onClick = { onNavigateToDetail(note.id) },
                                onLongClick = { noteToDelete = note }
                            )
                        }
                    }
                } else {
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        contentPadding = PaddingValues(16.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(
                            items = uiState.notes,
                            key = { it.id }
                        ) { note ->
                            NoteCard(
                                note = note,
                                onClick = { onNavigateToDetail(note.id) }
                            )
                        }
                    }
                }
            }
        }
    }

    noteToDelete?.let { note ->
        ConfirmDialog(
            title = "Move to Trash?",
            message = "This note will be moved to trash.",
            confirmText = "Move",
            onConfirm = {
                viewModel.moveToTrash(note.id)
                noteToDelete = null
            },
            onDismiss = { noteToDelete = null },
            isDestructive = true
        )
    }
}

@Composable
private fun AnimatedNoteItem(
    note: Note,
    onClick: () -> Unit,
    onLongClick: () -> Unit
) {
    val scale by animateFloatAsState(
        targetValue = 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "note_scale"
    )

    Column(
        modifier = Modifier
            .scale(scale)
            .clickable(onClick = onClick)
    ) {
        NoteCard(
            note = note,
            onClick = onClick
        )
    }
}