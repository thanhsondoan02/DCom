package com.example.dcom.ui.texttospeech

import android.os.Bundle
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.dcom.R
import com.example.dcom.ui.customview.VolumePicker

class TextToSpeechActivity : AppCompatActivity() {

    private lateinit var tvActionBarTitle: TextView
    private lateinit var btnGoBack: ImageButton
    private lateinit var vpPitch: VolumePicker
    private lateinit var vpVolume: VolumePicker

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.text_to_speech_activity)

        initView()

        btnGoBack.setOnClickListener { onBackPressed() }
    }

    private fun initView() {
        tvActionBarTitle = findViewById(R.id.tvActionBarTitle)
        btnGoBack = findViewById(R.id.btnActionBarGoBack)
        vpPitch = findViewById(R.id.vpTextToSpeechPitch)
        vpVolume = findViewById(R.id.vpTextToSpeechVolume)

        tvActionBarTitle.text = getString(R.string.text_to_speech)
//        vpPitch.setPickerLength(30)
//        vpVolume.setPickerLength(80)
    }
}
