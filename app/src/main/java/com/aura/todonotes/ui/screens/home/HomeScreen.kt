package com.aura.todonotes.ui.screens.home

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
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
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Archive
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.PushPin
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Sort
import androidx.compose.material.icons.filled.ViewAgenda
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.material3.rememberStandardBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.aura.todonotes.domain.model.Note
import com.aura.todonotes.domain.model.SortOrder
import com.aura.todonotes.domain.model.ViewMode
import com.aura.todonotes.ui.components.ConfirmDialog
import com.aura.todonotes.ui.components.EmptyState
import com.aura.todonotes.ui.components.NoteCard
import kotlinx.coroutines.launch

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
    var noteToPin by remember { mutableStateOf<Note?>(null) }
    var noteToArchive by remember { mutableStateOf<Note?>(null) }
    val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
    ) {
        // Top App Bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Aura",
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.primary
            )
            Row {
                IconButton(onClick = onNavigateToSearch) {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search"
                    )
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
                            text = { Text("List View") },
                            onClick = { showViewMenu = false },
                            leadingIcon = { Icon(Icons.Default.List, null) }
                        )
                        DropdownMenuItem(
                            text = { Text("Grid View") },
                            onClick = { showViewMenu = false },
                            leadingIcon = { Icon(Icons.Default.GridView, null) }
                        )
                    }
                }
                Box {
                    IconButton(onClick = { showSortMenu = true }) {
                        Icon(
                            imageVector = Icons.Default.Sort,
                            contentDescription = "Sort"
                        )
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
                    Icon(
                        imageVector = Icons.Default.Settings,
                        contentDescription = "Settings"
                    )
                }
            }
        }

        // Notes Content
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

    // Bottom Navigation
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        IconButton(onClick = onNavigateToTrash) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(Icons.Default.Delete, "Trash")
                Text("Trash", style = MaterialTheme.typography.labelSmall)
            }
        }
    }

    // FAB
    FloatingActionButton(
        onClick = onNavigateToAddEdit,
        modifier = Modifier
            .align(Alignment.BottomEnd)
            .padding(16.dp)
            .padding(bottom = 60.dp)
    ) {
        Icon(Icons.Default.Add, "Add Note")
    }

    // Confirm Delete Dialog
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

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun AnimatedNoteItem(
    note: Note,
    onClick: () -> Unit,
    onLongClick: () -> Unit
) {
    var isVisible by remember { mutableStateOf(true) }
    val alpha by animateFloatAsState(
        targetValue = if (isVisible) 1f else 0f,
        animationSpec = tween(300),
        label = "alpha"
    )

    Box(
        modifier = Modifier
            .graphicsLayer { this.alpha = alpha }
            .animateItem()
            .combinedClickable(
                onClick = onClick,
                onLongClick = onLongClick
            )
    ) {
        NoteCard(
            note = note,
            onClick = onClick
        )
    }
}
