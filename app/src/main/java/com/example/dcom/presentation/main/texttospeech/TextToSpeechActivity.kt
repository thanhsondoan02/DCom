package com.example.dcom.presentation.main.texttospeech

import android.annotation.SuppressLint
import android.app.Dialog
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.dcom.R
import com.example.dcom.presentation.widget.VolumePicker

class TextToSpeechActivity : AppCompatActivity() {

    private lateinit var tvActionBarTitle: TextView
    private lateinit var btnGoBack: ImageButton
    private lateinit var btnMenu: ImageButton

    private lateinit var dialogInflateView: View
    private lateinit var volume: VolumePicker
    private lateinit var pitch: VolumePicker
    private lateinit var speed: VolumePicker

    private var menuDialog: Dialog? = null

    private var pitchPercent = 100
    private var speedPercent = 100
    private var volumePercent = 100

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.text_to_speech_activity)

        initView()

        btnGoBack.setOnClickListener { onBackPressed() }
        btnMenu.setOnClickListener { showMenu() }
    }

    private fun initView() {
        tvActionBarTitle = findViewById(R.id.tvActionBarTitle)
        btnGoBack = findViewById(R.id.btnActionBarGoBack)
        btnMenu = findViewById(R.id.btnTextToSpeechMenu)

        tvActionBarTitle.text = getString(R.string.text_to_speech)
    }

    @SuppressLint("InflateParams")
    private fun menuDialog(): Dialog {
        val dialog = Dialog(this@TextToSpeechActivity, R.style.DialogTheme)

        dialogInflateView = layoutInflater.inflate(R.layout.text_to_speech_menu_layout, null)
        volume = dialogInflateView.findViewById(R.id.vpTextToSpeechVolume)
        pitch = dialogInflateView.findViewById(R.id.vpTextToSpeechPitch)
        speed = dialogInflateView.findViewById(R.id.vpTextToSpeechSpeed)

        dialog.setContentView(dialogInflateView)

        dialog.window?.apply {
            setGravity(Gravity.BOTTOM)
            setLayout(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT
            )
        }

        return dialog
    }

    private fun showMenu() {
        if (menuDialog == null) {
            menuDialog = menuDialog()
        } else {
            volume.setPickerLength(volumePercent)
            pitch.setPickerLength(pitchPercent)
            speed.setPickerLength(speedPercent)
        }

        menuDialog!!.show()
    }

}
