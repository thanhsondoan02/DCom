package com.example.dcom.presentation.widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.RelativeLayout
import androidx.core.view.updateLayoutParams
import androidx.recyclerview.widget.RecyclerView
import com.example.dcom.R

class CustomEditText @JvmOverloads constructor(
    ctx: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : RelativeLayout(ctx, attrs, defStyleAttr) {

    companion object {
        const val DEFAULT_HEIGHT = 600
        const val HIDE_HEIGHT = 1
    }

    var listener: IListener? = null

    private val inflateView: View
    private val edtInput: EditText
    private val btnClear: Button
    private val btnSpeak: Button
    private val rvSuggestion: RecyclerView

    init {
        inflateView = LayoutInflater.from(context).inflate(R.layout.custom_edit_text, this, true)
        edtInput = inflateView.findViewById(R.id.edtCustomEditTextInput)
        btnClear = inflateView.findViewById(R.id.btnCustomEditTextClear)
        btnSpeak = inflateView.findViewById(R.id.btnCustomEditTextSpeak)
        rvSuggestion = inflateView.findViewById(R.id.rvCustomEditSuggestions)

        btnClear.setOnClickListener { edtInput.setText("") }
        btnSpeak.setOnClickListener { listener?.onSpeak() }
    }

    fun addSuggestText(text: String, tempText: String) {
        // check if edittext contains text
        if (edtInput.text.toString().isNotEmpty()) {
            // check if edittext contains tempText
            if (edtInput.text.toString().contains(tempText)) {
                // get index of tempText
                val index = edtInput.text.toString().indexOf(tempText)
                // get text before tempText
                val beforeText = edtInput.text.toString().substring(0, index)
                // get text after tempText
                val afterText = edtInput.text.toString().substring(index + tempText.length)
                // set text to edittext
                edtInput.setText("$beforeText$text$afterText")
                // set cursor to end of text
                edtInput.setSelection(beforeText.length + text.length)
            }
        }
        edtInput.text.append(" $text")
    }

    fun getText(): String = edtInput.text.toString()

    fun mRequestFocus(context: Context) {
        edtInput.requestFocus()
    }

    fun changeHeight(newHeight: Int) {
        updateLayoutParams {
            height = newHeight
        }
    }

    fun getRecyclerView(): RecyclerView = rvSuggestion

    fun getEditText(): EditText = edtInput

    interface IListener {
        fun onHeightChange(height: Int)
        fun onSpeak()
    }

}
