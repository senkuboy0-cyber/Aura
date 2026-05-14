package com.aura.todonotes.domain.model

enum class ThemeOption(val value: Int) {
    SYSTEM(0),
    DARK(1),
    LIGHT(2);

    companion object {
        fun fromValue(value: Int): ThemeOption = entries.find { it.value == value } ?: SYSTEM
    }
}

enum class FontSize(val value: Int) {
    SMALL(0),
    MEDIUM(1),
    LARGE(2);
    companion object {
        fun fromValue(value: Int): FontSize = entries.find { it.value == value } ?: MEDIUM
    }
}

enum class ViewMode(val value: Int) {
    GRID(0),
    LIST(1);
    companion object {
        fun fromValue(value: Int): ViewMode = entries.find { it.value == value } ?: LIST
    }
}

enum class SortOrder(val value: Int) {
    NEWEST(0),
    OLDEST(1),
    ALPHABETICAL_AZ(2),
    ALPHABETICAL_ZA(3);
    companion object {
        fun fromValue(value: Int): SortOrder = entries.find { it.value == value } ?: NEWEST
    }
}

data class AppSettings(
    val themeMode: ThemeOption = ThemeOption.SYSTEM,
    val fontSize: FontSize = FontSize.MEDIUM,
    val defaultView: ViewMode = ViewMode.LIST,
    val defaultColor: String = "#FFFFFF",
    val defaultSort: SortOrder = SortOrder.NEWEST,
    val showPinnedFirst: Boolean = true,
    val noteLockEnabled: Boolean = false,
    val pinCode: String? = null
)
