package com.example.dcom.presentation.widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.inputmethod.EditorInfo
import android.widget.Button
import android.widget.EditText
import android.widget.RelativeLayout
import com.example.dcom.R

class CustomEditText @JvmOverloads constructor( //TODO padding edit text
    ctx: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : RelativeLayout(ctx, attrs, defStyleAttr) {

    interface IListener {
        fun onHeightChange(height: Int)
    }

    var listener: IListener? = null

    private val edtInput: EditText
    private val btnClear: Button

    init {
        val view = LayoutInflater.from(context).inflate(R.layout.custom_edit_text, this, true)
        edtInput = view.findViewById(R.id.edtCustomEditTextInput)
        btnClear = view.findViewById(R.id.btnCustomEditTextClear)

        btnClear.setOnClickListener { edtInput.setText("") }
    }

    fun hideKeyboard() {
        edtInput.inputType = EditorInfo.TYPE_NULL
    }

}
