package com.example.dcom.presentation.main.communication

import android.widget.Button
import android.widget.Toast
import androidx.core.view.updateLayoutParams
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.dcom.R
import com.example.dcom.extension.hide
import com.example.dcom.extension.hideKeyboard
import com.example.dcom.extension.show
import com.example.dcom.presentation.common.BaseFragment
import com.example.dcom.presentation.main.MainActivity
import com.example.dcom.presentation.widget.CustomEditText

class CommunicationFragment: BaseFragment(R.layout.communication_fragment) {

    private lateinit var btnKeyboard: Button
    private lateinit var cedtInput: CustomEditText
    private lateinit var rvConversation: RecyclerView

    private val communicationAdapter by lazy {
        CommunicationAdapter()
    }

    private var state: STATE = STATE.DEFAULT

    override fun onInitView() {
        super.onInitView()

        setUpVariables()
        setUpOnClick()
        setUpRecyclerView()

        // fake data
        communicationAdapter.addData(mockConversationData())

        setState(STATE.DEFAULT)
    }

    private fun setUpVariables() {
        btnKeyboard = requireView().findViewById(R.id.btnCommunicationKeyboard)
        cedtInput = requireView().findViewById(R.id.cedtCommunicationInput)
        rvConversation = requireView().findViewById(R.id.rvCommunicationConversation)
    }

    private fun setUpOnClick() {
        btnKeyboard.setOnClickListener {
//            showInputBox()
            setState(STATE.INPUT)
        }

        cedtInput.listener = object : CustomEditText.IListener {
            override fun onHeightChange(height: Int) {
//                ivBottomPlaceHolder.updateLayoutParams {
//                    this.height = height
//                }
            }

            override fun onSpeak() {
                Toast.makeText(requireContext(), "Gáy: " + getInputText(), Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setUpRecyclerView() {
        rvConversation.layoutManager = LinearLayoutManager(requireContext())
        setAdapterListener()
        rvConversation.adapter = communicationAdapter
    }

    private fun setAdapterListener() {
        communicationAdapter.listener = object : CommunicationAdapter.IListener {
            override fun onClickItem(index: Int) {
                when (state) {
                    STATE.INPUT -> {
                        setState(STATE.DEFAULT)
                    }
                    STATE.FULL -> {
                    }
                    STATE.DEFAULT -> {
                    }
                }
            }
        }
    }

    private fun scrollRecyclerViewToLastItem() {
        rvConversation.postDelayed({
            rvConversation.scrollToPosition(rvConversation.adapter!!.itemCount - 1)
        }, 1000)
    }

    private fun mockConversationData(): MutableList<Any> {
        val ans = mutableListOf<Any>()

        val list = mutableListOf(
            getString(R.string.speech_to_text_des),
            getString(R.string.emergency_signal_des),
            getString(R.string.text_to_speech_des),
            getString(R.string.fast_communication_des),
            getString(R.string.example_paragraph),
            getString(R.string.example_paragraph2),
        )

        val listNumber = listOf(0, 0, 0, 0, 1)
        var count = 0

        for (i in 0 until 20) {
            if (i == 0 || listNumber.random() == 1) {
                ans.add(CommunicationAdapter.TimeDateData().apply {
                    timeDate = "11:28 AM $count"
                })
                count++
            }
            ans.add(CommunicationAdapter.MessageData().apply {
                message = list.random()
            })
        }
        return ans
    }

    private fun changeInputBoxHeight() {
        cedtInput.updateLayoutParams {
            height = 100
        }
    }

    private fun getInputText(): String {
        return cedtInput.getText()
    }

    private fun setState(state: STATE) {
        when (state) {
            STATE.DEFAULT -> {
                hideKeyboard(requireView())
                cedtInput.changeHeight(CustomEditText.HIDE_HEIGHT)
                (activity as MainActivity).showBottomNav()
                btnKeyboard.show()
                this@CommunicationFragment.state = STATE.DEFAULT
            }
            STATE.INPUT -> {
                (activity as MainActivity).hideBottomNav()
                btnKeyboard.hide()
                cedtInput.changeHeight(CustomEditText.DEFAULT_HEIGHT)
                this@CommunicationFragment.state = STATE.INPUT
            }
            STATE.FULL -> { // hide all
                hideKeyboard(requireView())
                cedtInput.changeHeight(CustomEditText.HIDE_HEIGHT)
                (activity as MainActivity).hideBottomNav()
                btnKeyboard.hide()
                this@CommunicationFragment.state = STATE.FULL
            }
        }
    }

    enum class STATE {
        INPUT, DEFAULT, FULL
    }
}
