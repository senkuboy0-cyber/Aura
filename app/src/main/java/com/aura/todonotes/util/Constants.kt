package com.aura.todonotes.util

import androidx.compose.ui.graphics.Color

object Constants {
    const val DATABASE_NAME = "aura_database"
    const val PREFERENCES_NAME = "aura_settings"

    val DEFAULT_COLORS = listOf(
        "#FFFFFFFF",
        "#FFFFEB3B",
        "#FFFF9800",
        "#FFFF5722",
        "#FFEF5350",
        "#FFE57373",
        "#FFF06292",
        "#FFBA68C8",
        "#FF9575CD",
        "#FF7986CB",
        "#FF5C6BC0",
        "#FF42A5F5",
        "#FF29B6F6",
        "#FF26C6DA",
        "#FF4DB6AC",
        "#FF66BB6A",
        "#FF9CCC65",
        "#FFDCE775"
    )
}

fun Color.Companion.parse(colorHex: String): Color {
    return try {
        Color(android.graphics.Color.parseColor(colorHex))
    } catch (e: Exception) {
        Color.White
    }
}
