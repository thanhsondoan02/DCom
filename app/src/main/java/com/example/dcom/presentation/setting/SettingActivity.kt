package com.example.dcom.presentation.setting

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.WindowManager
import android.widget.Button
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.dcom.R
import com.example.dcom.base.event.DeleteDatabaseEvent
import com.example.dcom.base.event.EventBusManager
import com.example.dcom.database.AppDatabase
import com.example.dcom.presentation.common.BaseView
import com.example.dcom.presentation.common.THEME_KEY
import com.example.dcom.presentation.common.emptyDatabase
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.card.MaterialCardView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.materialswitch.MaterialSwitch


class SettingActivity : AppCompatActivity(), BaseView {

    companion object {
        const val AUTO_MODE_KEY = "autoMode"
    }

    private lateinit var mtbTopBar: MaterialToolbar
    private lateinit var swAuto: MaterialSwitch
    private lateinit var mcvNotification: MaterialCardView
    private lateinit var mcvTheme: MaterialCardView
    private lateinit var btnClearData: Button
    private lateinit var tvTheme: TextView
    private lateinit var tvStorage: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.setting_activity2)
        onInitView()
    }

    override fun onInitView() {
        setUpVariables()
        setUpOnClick()
    }

    private fun setUpVariables() {
        mtbTopBar = findViewById(R.id.mtbSettingTopBar)
        swAuto = findViewById(R.id.swSettingsAutoMode)
        mcvNotification = findViewById(R.id.mcvSettingNotification)
        mcvTheme = findViewById(R.id.mcvSettingTheme)
        btnClearData = findViewById(R.id.btnSettingClearStorage)
        tvTheme = findViewById(R.id.tvSettingTheme)
        tvStorage = findViewById(R.id.tvSettingStorage)

        val sharedPref = getPreferences(Context.MODE_PRIVATE) ?: return
        val defaultValue = false
        swAuto.isChecked = sharedPref.getBoolean(AUTO_MODE_KEY, defaultValue)

        updateThemeText()
        updateStorageText()
    }

    private fun updateStorageText() {
        val storage = ((AppDatabase.getInstance(this).getStorageSize() * 30.0 / 1024).toString() + "0000").substring(0, 4)
        tvStorage.text = getString(R.string.storage_example).replace("0", storage)
    }

    private fun bytesToMeg(bytes: Long): Long {
        return bytes / 1024 / 1024
    }

    private fun bytesToMB(bytes: Long): Long {
        return bytes / 1024 / 1024
    }

    private fun updateThemeText() {
        tvTheme.text = when (getMyTheme()) {
            0 -> getString(R.string.dark)
            1 -> getString(R.string.light)
            else -> getString(R.string.system_default)
        }
    }

    private fun setUpOnClick() {
        mtbTopBar.setNavigationOnClickListener {
            finish()
        }

        btnClearData.setOnClickListener {
            showConfirmDeleteDialog()
        }

        swAuto.setOnCheckedChangeListener { buttonView, isChecked ->
            saveAutoMode(isChecked)
        }

        mcvNotification.setOnClickListener {
            // go to notification setting
            Toast.makeText(this, "go to notification setting", Toast.LENGTH_SHORT).show()
        }

        mcvTheme.setOnClickListener {
            showChooseThemeDialog()
        }
    }

    private fun clearAppData() {
        emptyDatabase()
        EventBusManager.instance?.postPending(DeleteDatabaseEvent(isFavorite = true))
        Toast.makeText(this, getString(R.string.clear_all_data_success), Toast.LENGTH_SHORT).show()
    }

    private fun saveAutoMode(isOn: Boolean) {
        val sharedPref = getPreferences(Context.MODE_PRIVATE) ?: return
        with (sharedPref.edit()) {
            putBoolean(AUTO_MODE_KEY, isOn)
            apply()
        }
    }

    private fun showChooseThemeDialog() {
        window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        val view = LayoutInflater.from(this@SettingActivity).inflate(R.layout.choose_theme_dialog, null, false)
        val dialog = MaterialAlertDialogBuilder(this@SettingActivity).setView(view)
            .setTitle(resources.getString(R.string.choose_theme))
            .setNegativeButton(resources.getString(R.string.cancel)) { dialog, _ -> }.show()

        val radioGroup = view.findViewById<RadioGroup>(R.id.radio_group)
        radioGroup.check(when(getMyTheme()) {
            0 -> R.id.rbDark
            1 -> R.id.rbLight
            else -> R.id.rbSystem
        })
        radioGroup.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.rbDark -> {
                    dialog.dismiss()
                    saveTheme(0)
                    updateTheme()
                }
                R.id.rbLight -> {
                    dialog.dismiss()
                    saveTheme(1)
                    updateTheme()
                }
                R.id.rbSystem -> {
                    dialog.dismiss()
                    saveTheme(2)
                    updateTheme()
                }
            }
            updateThemeText()
        }
    }

    private fun saveTheme(theme: Int) {
        val sharedPref = getSharedPreferences(THEME_KEY, Context.MODE_PRIVATE) ?: return
        with (sharedPref.edit()) {
            putInt(THEME_KEY, theme)
            apply()
        }
    }

    private fun getMyTheme(): Int {
        val sharedPref = getSharedPreferences(THEME_KEY, Context.MODE_PRIVATE) ?: return 2
        return sharedPref.getInt(THEME_KEY, 2)
    }

    private fun showConfirmDeleteDialog() {
        MaterialAlertDialogBuilder(this)
            .setTitle(resources.getString(R.string.delete_database_title))
            .setMessage(resources.getString(R.string.delete_database_des))
            .setPositiveButton(resources.getString(R.string.delete)) { _, _ ->
                clearAppData()
                updateStorageText()
            }
            .show()
    }

    private fun updateTheme() {
        startActivity(Intent(this, ChangeThemeActivity::class.java))
    }

}
