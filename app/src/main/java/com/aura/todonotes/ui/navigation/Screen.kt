package com.aura.todonotes.ui.navigation

sealed class Screen(val route: String) {
    data object Home : Screen("home")
    
    data object AddEdit : Screen("add_edit?noteId={noteId}") {
        fun createRoute(noteId: Long? = null): String {
            return if (noteId != null) "add_edit?noteId=$noteId" else "add_edit"
        }
    }
    
    data object Detail : Screen("detail/{noteId}") {
        fun createRoute(noteId: Long) = "detail/$noteId"
    }
    
    data object Trash : Screen("trash")
    data object Archive : Screen("archive")
    data object Search : Screen("search")
    data object Settings : Screen("settings")
    
    data object Locked : Screen("locked/{noteId}") {
        fun createRoute(noteId: Long) = "locked/$noteId"
    }
    
    data object PinSetup : Screen("pin_setup")
}