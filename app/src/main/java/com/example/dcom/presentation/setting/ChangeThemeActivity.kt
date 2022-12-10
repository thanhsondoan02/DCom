package com.example.dcom.presentation.setting

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.example.dcom.presentation.common.THEME_KEY

class ChangeThemeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        updateTheme(getMyTheme())
        finish()
        super.onCreate(savedInstanceState)
    }

    private fun getMyTheme(): Int {
        val sharedPref = getSharedPreferences(THEME_KEY, Context.MODE_PRIVATE) ?: return 2
        return sharedPref.getInt(THEME_KEY, 2)
    }

    private fun updateTheme(theme: Int) {
        when (theme) {
            1 -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            0 -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            2 -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
        }
    }

}
