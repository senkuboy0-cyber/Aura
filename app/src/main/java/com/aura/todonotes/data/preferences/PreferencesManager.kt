package com.aura.todonotes.data.preferences

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton


private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "aura_settings")


@Singleton
class PreferencesManager @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val dataStore = context.dataStore


    companion object {
        private val THEME_MODE = intPreferencesKey("theme_mode")
        private val FONT_SIZE = intPreferencesKey("font_size")
        private val DEFAULT_VIEW = intPreferencesKey("default_view")
        private val DEFAULT_COLOR = stringPreferencesKey("default_color")
        private val DEFAULT_SORT = intPreferencesKey("default_sort")
        private val SHOW_PINNED_FIRST = booleanPreferencesKey("show_pinned_first")
        private val NOTE_LOCK_ENABLED = booleanPreferencesKey("note_lock_enabled")
        private val PIN_CODE = stringPreferencesKey("pin_code")
    }

    // Theme Mode: 0 = System, 1 = Dark, 2 = Light
    val themeMode: Flow<Int> = dataStore.data.map { preferences ->
        preferences[THEME_MODE] ?: 0
    }

    suspend fun setThemeMode(mode: Int) {
        dataStore.edit { preferences ->
            preferences[THEME_MODE] = mode
        }
    }

    // Font Size: 0 = Small, 1 = Medium, 2 = Large
    val fontSize: Flow<Int> = dataStore.data.map { preferences ->
        preferences[FONT_SIZE] ?: 1
    }


    suspend fun setFontSize(size: Int) {
        dataStore.edit { preferences ->
            preferences[FONT_SIZE] = size
        }
    }


    // Default View: 0 = Grid, 1 = List
    val defaultView: Flow<Int> = dataStore.data.map { preferences ->
        preferences[DEFAULT_VIEW] ?: 1
    }

    suspend fun setDefaultView(view: Int) {
        dataStore.edit { preferences ->
            preferences[DEFAULT_VIEW] = view
        }
    }

    // Default Color
    val defaultColor: Flow<String> = dataStore.data.map { preferences ->
        preferences[DEFAULT_COLOR] ?: "#FFFFFF"
    }

    suspend fun setDefaultColor(color: String) {
        dataStore.edit { preferences ->
            preferences[DEFAULT_COLOR] = color
        }
    }

    // Default Sort: 0 = Newest, 1 = Oldest, 2 = A-Z, 3 = Z-A
    val defaultSort: Flow<Int> = dataStore.data.map { preferences ->
        preferences[DEFAULT_SORT] ?: 0
    }

    suspend fun setDefaultSort(sort: Int) {
        dataStore.edit { preferences ->
            preferences[DEFAULT_SORT] = sort
        }
    }

    // Show Pinned First
    val showPinnedFirst: Flow<Boolean> = dataStore.data.map { preferences ->
        preferences[SHOW_PINNED_FIRST] ?: true
    }

    suspend fun setShowPinnedFirst(show: Boolean) {
        dataStore.edit { preferences ->
            preferences[SHOW_PINNED_FIRST] = show
        }
    }


    // Note Lock Enabled
    val noteLockEnabled: Flow<Boolean> = dataStore.data.map { preferences ->
        preferences[NOTE_LOCK_ENABLED] ?: false
    }

    suspend fun setNoteLockEnabled(enabled: Boolean) {
        dataStore.edit { preferences ->
            preferences[NOTE_LOCK_ENABLED] = enabled
        }
    }


    // PIN Code
    val pinCode: Flow<String?> = dataStore.data.map { preferences ->
        preferences[PIN_CODE]
    }

    suspend fun setPinCode(pin: String?) {
        dataStore.edit { preferences ->
            if (pin != null) {
                preferences[PIN_CODE] = pin
            } else {
                preferences.remove(PIN_CODE)
            }
        }
    }

    suspend fun verifyPin(enteredPin: String): Boolean {
        var storedPin: String? = null
        dataStore.data.map { preferences ->
            preferences[PIN_CODE]
        }.collect { pin ->
            storedPin = pin
        }
        return storedPin == enteredPin
    }
}
