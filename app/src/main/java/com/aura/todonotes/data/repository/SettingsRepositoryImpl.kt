package com.aura.todonotes.data.repository

import com.aura.todonotes.data.preferences.PreferencesManager
import com.aura.todonotes.domain.model.AppSettings
import com.aura.todonotes.domain.model.FontSize
import com.aura.todonotes.domain.model.SortOrder
import com.aura.todonotes.domain.model.ThemeOption
import com.aura.todonotes.domain.model.ViewMode
import com.aura.todonotes.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class SettingsRepositoryImpl @Inject constructor(
    private val preferencesManager: PreferencesManager
) : SettingsRepository {

    override fun getSettings(): Flow<AppSettings> = combine(
        preferencesManager.themeMode,
        preferencesManager.fontSize,
        preferencesManager.defaultView,
        preferencesManager.defaultColor,
        preferencesManager.defaultSort,
        preferencesManager.showPinnedFirst,
        preferencesManager.noteLockEnabled,
        preferencesManager.pinCode
    ) { values ->
        AppSettings(
            themeMode = ThemeOption.fromValue(values[0] as Int),
            fontSize = FontSize.fromValue(values[1] as Int),
            defaultView = ViewMode.fromValue(values[2] as Int),
            defaultColor = values[3] as String,
            defaultSort = SortOrder.fromValue(values[4] as Int),
            showPinnedFirst = values[5] as Boolean,
            noteLockEnabled = values[6] as Boolean,
            pinCode = values[7] as String?
        )
    }

    override suspend fun setThemeMode(mode: ThemeOption) {
        preferencesManager.setThemeMode(mode.value)
    }


    override suspend fun setNoteLockEnabled(enabled: Boolean) {
        preferencesManager.setNoteLockEnabled(enabled)
    }


    override suspend fun setPinCode(pin: String?) {
        preferencesManager.setPinCode(pin)
    }

    override suspend fun verifyPin(pin: String): Boolean {
        return preferencesManager.verifyPin(pin)
    }
}
