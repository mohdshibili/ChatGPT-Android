package com.codeware.chatgpt.preference

import android.content.SharedPreferences

class Preferences constructor(
    val sharedPreferences: SharedPreferences
) {
    var userImage: String by stringPreferences(KEY_USER_IMAGE, "https://api.dicebear.com/5.x/initials/png?seed=ChatGPT&radius=20")

    companion object {
        private const val KEY_USER_IMAGE = "user_image"
    }
}