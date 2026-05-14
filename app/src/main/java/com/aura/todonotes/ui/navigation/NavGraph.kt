package com.aura.todonotes.ui.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.aura.todonotes.ui.screens.add_edit.AddEditScreen
import com.aura.todonotes.ui.screens.archive.ArchiveScreen
import com.aura.todonotes.ui.screens.detail.DetailScreen
import com.aura.todonotes.ui.screens.home.HomeScreen
import com.aura.todonotes.ui.screens.locked.LockedScreen
import com.aura.todonotes.ui.screens.pin.PinSetupScreen
import com.aura.todonotes.ui.screens.search.SearchScreen
import com.aura.todonotes.ui.screens.settings.SettingsScreen
import com.aura.todonotes.ui.screens.trash.TrashScreen

@Composable
fun AuraNavHost(
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Home.route,
        enterTransition = {
            slideIntoContainer(
                towards = AnimatedContentTransitionScope.SlideDirection.Left,
                animationSpec = tween(350)
            ) + fadeIn(animationSpec = tween(350))
        },
        exitTransition = {
            slideOutOfContainer(
                towards = AnimatedContentTransitionScope.SlideDirection.Left,
                animationSpec = tween(350)
            ) + fadeOut(animationSpec = tween(350))
        },
        popEnterTransition = {
            slideIntoContainer(
                towards = AnimatedContentTransitionScope.SlideDirection.Right,
                animationSpec = tween(350)
            ) + fadeIn(animationSpec = tween(350))
        },
        popExitTransition = {
            slideOutOfContainer(
                towards = AnimatedContentTransitionScope.SlideDirection.Right,
                animationSpec = tween(350)
            ) + fadeOut(animationSpec = tween(350))
        }
    ) {
        composable(Screen.Home.route) {
            HomeScreen(
                onNavigateToAddEdit = { navController.navigate(Screen.AddEdit.createRoute()) },
                onNavigateToDetail = { noteId -> navController.navigate(Screen.Detail.createRoute(noteId)) },
                onNavigateToSearch = { navController.navigate(Screen.Search.route) },
                onNavigateToTrash = { navController.navigate(Screen.Trash.route) },
                onNavigateToArchive = { navController.navigate(Screen.Archive.route) },
                onNavigateToSettings = { navController.navigate(Screen.Settings.route) }
            )
        }

        composable(
            route = Screen.AddEdit.route,
            arguments = listOf(
                navArgument("noteId") {
                    type = NavType.LongType
                    defaultValue = -1L
                }
            )
        ) { backStackEntry ->
            val noteId = backStackEntry.arguments?.getLong("noteId") ?: -1L
            AddEditScreen(
                noteId = if (noteId == -1L) null else noteId,
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable(
            route = Screen.Detail.route,
            arguments = listOf(
                navArgument("noteId") { type = NavType.LongType }
            )
        ) { backStackEntry ->
            val noteId = backStackEntry.arguments?.getLong("noteId") ?: return@composable
            DetailScreen(
                noteId = noteId,
                onNavigateBack = { navController.popBackStack() },
                onNavigateToEdit = { navController.navigate(Screen.AddEdit.createRoute(noteId)) },
                onNavigateToLocked = { navController.navigate(Screen.Locked.createRoute(noteId)) }
            )
        }

        composable(Screen.Trash.route) {
            TrashScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable(Screen.Archive.route) {
            ArchiveScreen(
                onNavigateBack = { navController.popBackStack() },
                onNavigateToDetail = { noteId -> navController.navigate(Screen.Detail.createRoute(noteId)) }
            )
        }

        composable(Screen.Search.route) {
            SearchScreen(
                onNavigateBack = { navController.popBackStack() },
                onNavigateToDetail = { noteId -> navController.navigate(Screen.Detail.createRoute(noteId)) }
            )
        }

        composable(Screen.Settings.route) {
            SettingsScreen(
                onNavigateBack = { navController.popBackStack() },
                onNavigateToPinSetup = { navController.navigate(Screen.PinSetup.route) }
            )
        }

        composable(
            route = Screen.Locked.route,
            arguments = listOf(
                navArgument("noteId") { type = NavType.LongType }
            )
        ) { backStackEntry ->
            val noteId = backStackEntry.arguments?.getLong("noteId") ?: return@composable
            LockedScreen(
                noteId = noteId,
                onNavigateBack = { navController.popBackStack() },
                onUnlocked = { navController.popBackStack() }
            )
        }

        composable(Screen.PinSetup.route) {
            PinSetupScreen(
                onNavigateBack = { navController.popBackStack() },
                onPinSet = { navController.popBackStack() }
            )
        }
    }
}