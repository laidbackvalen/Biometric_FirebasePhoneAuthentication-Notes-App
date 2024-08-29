package com.valenpatel.paisefy.preferences

import android.content.Context

class SharedPreference(val context: Context) {
    val preferences = context.getSharedPreferences("UserSetPassword", Context.MODE_PRIVATE)
    val editor = preferences.edit()

    fun setPassword(password: String) {
        editor.putString("password", password)
        editor.commit()
    }

    fun getPassword(): String? {
        return preferences.getString("password", null)
    }
}