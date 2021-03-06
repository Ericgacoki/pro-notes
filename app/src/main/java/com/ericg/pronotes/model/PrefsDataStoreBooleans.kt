package com.ericg.pronotes.model

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.preferencesKey
import androidx.datastore.preferences.createDataStore
import kotlinx.coroutines.flow.map

/**
 * @author eric
 * @date 10/2/20
 */

class PrefsDataStoreBooleans(context: Context) {
    private val prefsDataStore = context.createDataStore(name = "user_prefs")

    companion object {
        val AUTO_SIGN_IN_KEY = preferencesKey<Boolean>("AUTO_SIGN_IN")
        val SHOW_ONBOARD_SCREEN_KEY = preferencesKey<Boolean>("SHOW_ONBOARD_SCREEN")
    }

    suspend fun setPrefsBoolean(type: PrefsBooleanType, value: Boolean) {
        if (type == PrefsBooleanType.AUTO_SIGN_IN) {
            prefsDataStore.edit {
                it[AUTO_SIGN_IN_KEY] = value
            }
            return
        }

        if (type == PrefsBooleanType.SHOW_ONBOARD_SCREEN) {
            prefsDataStore.edit {
                it[SHOW_ONBOARD_SCREEN_KEY] = value
            }
            return
        }
    }

    val autoSignInFlow = this.prefsDataStore.data.map {
        it[AUTO_SIGN_IN_KEY] ?: false
    }

    val showOnBoardFlow = this.prefsDataStore.data.map {
        it[SHOW_ONBOARD_SCREEN_KEY] ?: true
    }
}