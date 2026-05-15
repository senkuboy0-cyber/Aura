package com.aura.todonotes.ui.theme

import androidx.compose.ui.graphics.Color

// Modern Light Theme Colors - Material 3 Inspired
val md_theme_light_primary = Color(0xFF6366F1) // Indigo
val md_theme_light_onPrimary = Color(0xFFFFFFFF)
val md_theme_light_primaryContainer = Color(0xFFE0E7FF)
val md_theme_light_onPrimaryContainer = Color(0xFF1E1B4B)
val md_theme_light_secondary = Color(0xFF8B5CF6) // Violet
val md_theme_light_onSecondary = Color(0xFFFFFFFF)
val md_theme_light_secondaryContainer = Color(0xFFEDE9FE)
val md_theme_light_onSecondaryContainer = Color(0xFF2E1065)
val md_theme_light_tertiary = Color(0xFFEC4899) // Pink
val md_theme_light_onTertiary = Color(0xFFFFFFFF)
val md_theme_light_tertiaryContainer = Color(0xFFFCE4EC)
val md_theme_light_onTertiaryContainer = Color(0xFF4C0519)
val md_theme_light_error = Color(0xFFEF4444)
val md_theme_light_onError = Color(0xFFFFFFFF)
val md_theme_light_errorContainer = Color(0xFFFEE2E2)
val md_theme_light_onErrorContainer = Color(0xFF450A0A)
val md_theme_light_background = Color(0xFFFAFAFA)
val md_theme_light_onBackground = Color(0xFF18181B)
val md_theme_light_surface = Color(0xFFFFFFFF)
val md_theme_light_onSurface = Color(0xFF18181B)
val md_theme_light_surfaceVariant = Color(0xFFF4F4F5)
val md_theme_light_onSurfaceVariant = Color(0xFF52525B)
val md_theme_light_outline = Color(0xFFD4D4D8)
val md_theme_light_outlineVariant = Color(0xFFE4E4E7)
val md_theme_light_inverseSurface = Color(0xFF27272A)
val md_theme_light_inverseOnSurface = Color(0xFFF4F4F5)
val md_theme_light_inversePrimary = Color(0xFFA5B4FC)
val md_theme_light_surfaceTint = Color(0xFF6366F1)

// Dark Theme Colors
val md_theme_dark_primary = Color(0xFFA5B4FC)
val md_theme_dark_onPrimary = Color(0xFF1E1B4B)
val md_theme_dark_primaryContainer = Color(0xFF4338CA)
val md_theme_dark_onPrimaryContainer = Color(0xFFE0E7FF)
val md_theme_dark_secondary = Color(0xFFC4B5FD)
val md_theme_dark_onSecondary = Color(0xFF2E1065)
val md_theme_dark_secondaryContainer = Color(0xFF6D28D9)
val md_theme_dark_onSecondaryContainer = Color(0xFFEDE9FE)
val md_theme_dark_tertiary = Color(0xFFF9A8D4)
val md_theme_dark_onTertiary = Color(0xFF4C0519)
val md_theme_dark_tertiaryContainer = Color(0xFFBE185D)
val md_theme_dark_onTertiaryContainer = Color(0xFFFCE4EC)
val md_theme_dark_error = Color(0xFFFCA5A5)
val md_theme_dark_onError = Color(0xFF450A0A)
val md_theme_dark_errorContainer = Color(0xFFB91C1C)
val md_theme_dark_onErrorContainer = Color(0xFFFEE2E2)
val md_theme_dark_background = Color(0xFF09090B)
val md_theme_dark_onBackground = Color(0xFFFAFAFA)
val md_theme_dark_surface = Color(0xFF121214)
val md_theme_dark_onSurface = Color(0xFFE4E4E7)
val md_theme_dark_surfaceVariant = Color(0xFF1C1C1F)
val md_theme_dark_onSurfaceVariant = Color(0xFFA1A1AA)
val md_theme_dark_outline = Color(0xFF3F3F46)
val md_theme_dark_outlineVariant = Color(0xFF27272A)
val md_theme_dark_inverseSurface = Color(0xFFE4E4E7)
val md_theme_dark_inverseOnSurface = Color(0xFF27272A)
val md_theme_dark_inversePrimary = Color(0xFF6366F1)
val md_theme_dark_surfaceTint = Color(0xFFA5B4FC)

// Note Colors Palette - Modern vibrant colors
val noteColors = listOf(
    Color(0xFFFFFFFF), // White
    Color(0xFFFDE047), // Yellow
    Color(0xFFFB923C), // Orange
    Color(0xFFF97316), // Deep Orange
    Color(0xFFEF4444), // Red
    Color(0xFFF472B6), // Pink
    Color(0xFFC084FC), // Purple
    Color(0xFF818CF8), // Indigo
    Color(0xFF60A5FA), // Blue
    Color(0xFF22D3EE), // Cyan
    Color(0xFF2DD4BF), // Teal
    Color(0xFF4ADE80), // Green
    Color(0xFFA3E635), // Lime
    Color(0xFFFACC15), // Amber
)

val noteColorHexes = listOf(
    "#FFFFFFFF",
    "#FFFDE047",
    "#FFFB923C",
    "#FFF97316",
    "#FFEF4444",
    "#FFF472B6",
    "#FFC084FC",
    "#FF818CF8",
    "#FF60A5FA",
    "#FF22D3EE",
    "#FF2DD4BF",
    "#FF4ADE80",
    "#FFA3E635",
    "#FFFACC15",
)

// Utility function to determine if a color is dark
fun isColorDark(color: Color): Boolean {
    val luminance = 0.299 * color.red + 0.587 * color.green + 0.114 * color.blue
    return luminance < 0.5
}

fun parseColorHex(colorHex: String?): Color {
    return try {
        colorHex?.let { Color(android.graphics.Color.parseColor(it)) } ?: Color.Unspecified
    } catch (e: Exception) {
        Color.Unspecified
    }
}