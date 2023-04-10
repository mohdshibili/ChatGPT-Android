package com.codeware.chatgpt.preference

import androidx.core.content.edit
import kotlin.reflect.KProperty

class StringPreferenceDelegate(
    private val key: String,
    private val defaultValue: String = ""
) {
    operator fun getValue(preferences: Preferences, property: KProperty<*>): String {
        return preferences.sharedPreferences.getString(key, defaultValue) ?: let {
            setValue(preferences, property, defaultValue)
            defaultValue
        }
    }

    operator fun setValue(preferences: Preferences, property: KProperty<*>, value: String) {
        preferences.sharedPreferences.edit {
            putString(key, value)
        }
    }
}

fun stringPreferences(key: String, defaultValue: String) =
    StringPreferenceDelegate(key, defaultValue)