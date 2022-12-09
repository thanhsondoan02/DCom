package com.example.dcom.presentation.main.communication

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.View.OnTouchListener
import android.widget.Button
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.updateLayoutParams
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.dcom.R
import com.example.dcom.database.message.Message
import com.example.dcom.extension.*
import com.example.dcom.presentation.common.BaseFragment
import com.example.dcom.presentation.main.MainActivity
import com.example.dcom.presentation.main.history.conversation.ConversationAdapter
import com.example.dcom.presentation.widget.CustomEditText
import com.example.dcom.thread.CommunicationViewModel
import com.google.android.material.materialswitch.MaterialSwitch


class CommunicationFragment : BaseFragment(R.layout.communication_fragment) {

    private lateinit var btnKeyboard: Button
    private lateinit var cedtInput: CustomEditText
    private lateinit var rvConversation: RecyclerView
    private lateinit var constRoot: ConstraintLayout

    private val communicationAdapter by lazy {
        ConversationAdapter()
    }

    private var state: STATE = STATE.DEFAULT
    private val viewModel by viewModels<CommunicationViewModel>()
    private var isListen: Boolean = true
    private var isTouching: Boolean = false
    private val recognitionManager: RecognitionManager by lazy {
        RecognitionManager(
            requireContext(),
            shouldMute = false,
            callback = object : RecognitionCallback {
                override fun onResults(results: String) {
//                    println("onResults callback: $results")
                    addOtherMessage(results)
                }

                override fun onPrepared(status: RecognitionStatus) {
                    when (status) {
                        RecognitionStatus.SUCCESS -> {
                            Toast.makeText(requireContext(), "Recognition is ready", Toast.LENGTH_SHORT).show()
                        }
                        RecognitionStatus.UNAVAILABLE -> {
                            Toast.makeText(requireContext(), "Recognition is unavailable", Toast.LENGTH_SHORT).show()
                        }
                    }
                }

                override fun onBeginningOfSpeech() {

                }

                override fun onKeywordDetected() {

                }

                override fun onReadyForSpeech(params: Bundle) {

                }

                override fun onBufferReceived(buffer: ByteArray) {

                }

                override fun onRmsChanged(rmsdB: Float) {

                }

                override fun onPartialResults(results: List<String>) {

                }

                override fun onError(errorCode: Int) {
                    Log.d("Duc", "onErrorcallback: $errorCode")

                }

                override fun onEvent(eventType: Int, params: Bundle) {

                }

                override fun onEndOfSpeech() {

                }
            }

        )
    }


    override fun onInitView() {
        super.onInitView()

        setUpVariables()
        setUpOnClick()
        setUpRecyclerView()
        setState(STATE.DEFAULT)
        recognitionManager.createRecognizer()

        if (isListen) {
            recognitionManager.startRecognition()
            viewModel.speechToText(requireContext())
        } else {
            recognitionManager.stopRecognition()
        }
    }

    @JvmName("getRecognitionManager1")
    fun getRecognitionManager(): RecognitionManager {
        return recognitionManager
    }

    override fun onObserverViewModel() {
        super.onObserverViewModel()

        lifecycleScope.launchWhenCreated {
            viewModel.textToSpeechState.collect {
                handleUiState(it, object : IViewListener {
                    override fun onSuccess() {
                        Toast.makeText(requireContext(), "Speak success", Toast.LENGTH_SHORT).show()
                    }
                })
            }
        }

//        lifecycleScope.launchWhenCreated {
//            viewModel.speechToTextState.collect {
//                handleUiState(it, object : IViewListener {
//                    override fun onSuccess() {
//                        if (isListen) {
//                            addOtherMessage(it.data)
//                            viewModel.speechToText(requireContext())
//                        }
//                    }
//                })
//            }
//        }
    }


    private fun setUpVariables() {
        btnKeyboard = requireView().findViewById(R.id.btnCommunicationKeyboard)
        cedtInput = requireView().findViewById(R.id.cedtCommunicationInput)
        rvConversation = requireView().findViewById(R.id.rvCommunicationConversation)
        constRoot = requireView().findViewById(R.id.constCommunicationRoot)
    }

    private fun setUpOnClick() {
        btnKeyboard.setOnClickListener {
            setState(STATE.INPUT)
        }

        cedtInput.listener = object : CustomEditText.IListener {
            override fun onHeightChange(height: Int) {
            }

            override fun onSpeak() {
                val inputText = getInputText()
                addMineMessage(inputText)
                recognitionManager.stopRecognition()
                viewModel.textToSpeech(inputText, requireContext(), this@CommunicationFragment)
            }
        }

        val switch = activity?.findViewById<MaterialSwitch>(R.id.btnMainRight3)
//        if (!(activity as MainActivity).isRecordGranted) {
//            switch?.isChecked = false
//            isListen = false
//            switch?.setOnClickListener {
//                switch.isChecked = false
//                Toast.makeText(requireContext(), "Bạn cần cấp quyền ghi âm để sử dụng chức năng này", Toast.LENGTH_SHORT).show()
//            }
//        } else {
        switch?.setOnCheckedChangeListener { _, isChecked ->
            isListen = isChecked
            if (isListen) {
                recognitionManager.startRecognition()
                viewModel.speechToText(requireContext())
            } else {
                recognitionManager.stopRecognition()
            }
        }
//        }

        rvConversation.setOnTouchListener(RVClickHandler(rvConversation))
        rvConversation.setOnClickListener {
            setState(STATE.DEFAULT)
        }

        activity?.findViewById<Button>(R.id.btnMainRight)?.setOnClickListener {
            communicationAdapter.clear()
        }
    }

    private fun setUpRecyclerView() {
        rvConversation.layoutManager = LinearLayoutManager(requireContext())
        setAdapterListener()
        rvConversation.adapter = communicationAdapter
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
        rvConversation.scrollToBot()
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

    private fun addMineMessage(message: String?) {
        message?.let {
            if (!isTouching) scrollRecyclerViewToLastItem()
            communicationAdapter.add(Message(true, it, getCurrentTime()))
        }
    }

    private fun addOtherMessage(message: String?) {
        message?.let {
            if (!isTouching) scrollRecyclerViewToLastItem()
            communicationAdapter.add(Message(false, it, getCurrentTime()))
        }
    }

    private fun getCurrentTime(): Long {
        return System.currentTimeMillis()
    }

    private fun setState(state: STATE) {
        when (state) {
            STATE.DEFAULT -> {
                if (this@CommunicationFragment.state == STATE.DEFAULT) return
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

    inner class RVClickHandler(private val mRecyclerView: RecyclerView) : OnTouchListener {
        private var mStartX = 0f
        private var mStartY = 0f
        override fun onTouch(v: View?, event: MotionEvent): Boolean {
            var isConsumed = false
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    mStartX = event.x
                    mStartY = event.y
                }
                MotionEvent.ACTION_UP -> {
                    val endX = event.x
                    val endY = event.y
                    if (detectClick(mStartX, mStartY, endX, endY)) {
                        //Ideally it would never be called when a child View is clicked.
                        //I am not so sure about this.
                        val itemView: View? = mRecyclerView.findChildViewUnder(endX, endY)
                        if (itemView == null) {
                            //RecyclerView clicked
                            mRecyclerView.performClick()
                            isConsumed = true
                        }
                    }
                }
            }
            return isConsumed
        }

        private fun detectClick(startX: Float, startY: Float, endX: Float, endY: Float): Boolean {
            return Math.abs(startX - endX) < 3.0 && Math.abs(startY - endY) < 3.0
        }

    }

}
