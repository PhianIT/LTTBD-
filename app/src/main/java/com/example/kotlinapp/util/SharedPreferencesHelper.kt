package com.example.kotlinapp.util

import android.content.Context
import android.content.SharedPreferences

object SharedPreferencesHelper {

    private const val PREF_NAME = "user_prefs"
    private const val KEY_EMAIL = "email"
    private const val KEY_NAME = "name"

    private fun getPrefs(context: Context): SharedPreferences {
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    }

    fun saveUser(context: Context, email: String, name: String) {
        val editor = getPrefs(context).edit()
        editor.putString(KEY_EMAIL, email)
        editor.putString(KEY_NAME, name)
        editor.apply()
    }

    fun getUserEmail(context: Context): String? {
        return getPrefs(context).getString(KEY_EMAIL, null)
    }

    fun getUserName(context: Context): String? {
        return getPrefs(context).getString(KEY_NAME, null)
    }

    fun clearUser(context: Context) {
        getPrefs(context).edit().clear().apply()
    }
}
