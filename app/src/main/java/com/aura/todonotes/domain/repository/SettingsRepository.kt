package com.aura.todonotes.domain.repository

import com.aura.todonotes.domain.model.AppSettings
import com.aura.todonotes.domain.model.ThemeOption
import kotlinx.coroutines.flow.Flow

interface SettingsRepository {
    fun getSettings(): Flow<AppSettings>
    suspend fun setThemeMode(mode: ThemeOption)
    suspend fun setNoteLockEnabled(enabled: Boolean)
    suspend fun setPinCode(pin: String?)
    suspend fun verifyPin(pin: String): Boolean
}
