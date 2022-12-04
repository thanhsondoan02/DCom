package com.example.dcom.presentation.main.communication

import android.widget.Button
import android.widget.Toast
import androidx.core.view.updateLayoutParams
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.dcom.R
import com.example.dcom.database.message.Message
import com.example.dcom.extension.hide
import com.example.dcom.extension.hideKeyboard
import com.example.dcom.extension.show
import com.example.dcom.presentation.common.BaseFragment
import com.example.dcom.presentation.main.MainActivity
import com.example.dcom.presentation.main.history.conversation.ConversationAdapter
import com.example.dcom.presentation.widget.CustomEditText

class CommunicationFragment: BaseFragment(R.layout.communication_fragment) {

    private lateinit var btnKeyboard: Button
    private lateinit var cedtInput: CustomEditText
    private lateinit var rvConversation: RecyclerView

    private val communicationAdapter by lazy {
        ConversationAdapter()
    }

    private var state: STATE = STATE.DEFAULT

    override fun onInitView() {
        super.onInitView()

        setUpVariables()
        setUpOnClick()
        setUpRecyclerView()
        setState(STATE.DEFAULT)
    }

    private fun setUpVariables() {
        btnKeyboard = requireView().findViewById(R.id.btnCommunicationKeyboard)
        cedtInput = requireView().findViewById(R.id.cedtCommunicationInput)
        rvConversation = requireView().findViewById(R.id.rvCommunicationConversation)
    }

    private fun setUpOnClick() {
        btnKeyboard.setOnClickListener {
            setState(STATE.INPUT)
        }

        cedtInput.listener = object : CustomEditText.IListener {
            override fun onHeightChange(height: Int) {
            }

            override fun onSpeak() {
                Toast.makeText(requireContext(), "Gáy: " + getInputText(), Toast.LENGTH_SHORT).show()
                addMineMessage(getInputText())
            }
        }
    }

    private fun setUpRecyclerView() {
        rvConversation.layoutManager = LinearLayoutManager(requireContext())
        setAdapterListener()
        rvConversation.adapter = communicationAdapter

        communicationAdapter.addList(mockConversationData())
        scrollRecyclerViewToLastItem()
    }

    private fun setAdapterListener() {
        communicationAdapter.listener = object : ConversationAdapter.IListener {
            override fun onRecyclerViewClick() {
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

    private fun mockConversationData(): MutableList<Message> {
        val ans = mutableListOf<Message>()

        val list = mutableListOf(
            getString(R.string.speech_to_text_des),
            getString(R.string.emergency_signal_des),
            getString(R.string.text_to_speech_des),
            getString(R.string.fast_communication_des),
            getString(R.string.example_paragraph),
            getString(R.string.example_paragraph2),
        )

        val isMine = mutableListOf(true, false)

        for (i in 0 until 100) {
            ans.add(Message(isMine.random(), list.random(), 1000L))
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

    private fun addMineMessage(message: String) {
        scrollRecyclerViewToLastItem()
        communicationAdapter.add(Message(true, message, getCurrentTime()))
    }

    private fun addOtherMessage(message: Message) {
        scrollRecyclerViewToLastItem()
        communicationAdapter.add(message)
    }

    private fun getCurrentTime(): Long {
        return System.currentTimeMillis()
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
