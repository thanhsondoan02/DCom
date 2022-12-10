package com.example.dcom.presentation.main.communication

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.View.OnTouchListener
import android.view.WindowManager
import android.widget.Button
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.updateLayoutParams
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.dcom.R
import com.example.dcom.database.AppDatabase
import com.example.dcom.database.message.Message
import com.example.dcom.extension.*
import com.example.dcom.presentation.common.BaseFragment
import com.example.dcom.presentation.common.convertTime
import com.example.dcom.presentation.conversation.ConversationAdapter
import com.example.dcom.presentation.main.MainActivity
import com.example.dcom.presentation.widget.CustomEditText
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.util.*

class CommunicationFragment : BaseFragment(R.layout.communication_fragment) {
    companion object {
        val frequentlyMap = HashMap<String, Int>()
    }

    private lateinit var btnKeyboard: Button
    private lateinit var btnDelete: Button
    private lateinit var btnSave: Button
    private lateinit var cedtInput: CustomEditText
    private lateinit var rvConversation: RecyclerView
    private lateinit var constRoot: ConstraintLayout
    private var isRecognitionActivated: Boolean = false

    private val communicationAdapter by lazy {
        ConversationAdapter()
    }

    private val suggestingAdapter by lazy {
        SuggestingAdapter()
    }

    private var tempText: String = ""
    private var state: STATE = STATE.DEFAULT
    private val viewModel by viewModels<CommunicationViewModel>()
    private var isTouching: Boolean = false
    private val frequentlyFileDirPath by lazy {
        requireContext().getExternalFilesDir(null).toString() + "/Frequently/frequently.properties"
    }
    private val properties = Properties()
    private val speechRecognizer by lazy {
        SpeechRecognizer.createSpeechRecognizer(requireContext())
    }
    private val speechRecognizerIntent by lazy {
        Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
            putExtra(
                RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
            )
            putExtra(RecognizerIntent.EXTRA_LANGUAGE, "vi-VN")
            putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, context?.packageName)
            putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 1)
        }
    }

    override fun onInitView() {
        super.onInitView()

        setUpVariables()
        setUpOnClick()
        setUpRecyclerView()
        setUpCustomEditText()
        setState(STATE.DEFAULT)
        setUpSuggesting()
        createRecognizer()
        startRecognition()

        viewModel.speechToText(requireContext())

        (activity as MainActivity).listenerCommunication = object: MainActivity.IListenerCommunication {
            override fun onPageChangeCommunication(position: Int) {
                if (position == 0) {
                    startRecognition()
                } else {
                    stopRecognition()
                }
            }
        }
    }

    private fun createRecognizer() {
        speechRecognizer.setRecognitionListener(object : RecognitionListener {
            override fun onReadyForSpeech(bundle: Bundle) {}

            override fun onBeginningOfSpeech() {}

            override fun onRmsChanged(v: Float) {}

            override fun onBufferReceived(bytes: ByteArray) {}

            override fun onEndOfSpeech() {}

            override fun onError(i: Int) {
                if (isRecognitionActivated) {
                    startRecognition()
                }
            }

            override fun onResults(bundle: Bundle) {
                val matches = bundle.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
                if (matches != null) {
                    addOtherMessage(matches[0])
                }
                if (isRecognitionActivated) {
                    startRecognition()
                }
            }

            override fun onPartialResults(bundle: Bundle) {}

            override fun onEvent(i: Int, bundle: Bundle) {}
        })
    }

    fun startRecognition() {
        isRecognitionActivated = true
        speechRecognizer.startListening(speechRecognizerIntent)
    }

    fun stopRecognition() {
        isRecognitionActivated = false
        speechRecognizer.stopListening()
    }

    override fun onObserverViewModel() {
        super.onObserverViewModel()

        lifecycleScope.launchWhenCreated {
            viewModel.textToSpeechState.collect {
                handleUiState(it, object : IViewListener {
                    override fun onSuccess() {
                        if (viewModel.createdTime == null) {
                            viewModel.createdTime = System.currentTimeMillis()
                        }
                        Toast.makeText(
                            requireContext(),
                            getString(R.string.speak_success),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                })
            }
        }

        lifecycleScope.launchWhenCreated {
            viewModel.speechToTextState.collect {
                handleUiState(it, object : IViewListener {
                    override fun onSuccess() {
                        if (viewModel.createdTime == null) {
                            viewModel.createdTime = System.currentTimeMillis()
                        }
//                        addOtherMessage(it.data)
                        viewModel.speechToText(requireContext())
                    }
                })
            }
        }
    }

    private fun setUpSuggesting() {
        val frequentlyFile = requireContext().getExternalFilesDir(null).toString() + "/Frequently"
        val frequentlyFileDir = File(frequentlyFile)
        if (!frequentlyFileDir.exists()) {
            frequentlyFileDir.mkdir()
        }
        val frequentlyFilePath = "$frequentlyFile/frequently.properties"
        val frequentlyFileDirPath = File(frequentlyFilePath)
        if (!frequentlyFileDirPath.exists()) {
            frequentlyFileDirPath.createNewFile()
        }
        properties.load(FileInputStream(frequentlyFileDirPath))
        for (key in properties.stringPropertyNames()) {
            frequentlyMap[key] = properties.getProperty(key).toInt()
        }
    }

    override fun onPause() {
        super.onPause()
        saveFrequency()
        saveTempConversation()
        stopRecognition()
    }

    override fun onResume() {
        super.onResume()
        if ((activity as MainActivity).getViewPager().currentItem == 0) {
            startRecognition()
        }
    }

    private fun saveTempConversation() {
        val list = communicationAdapter.getAllData()
        if (list.isNotEmpty()) {
            viewModel.saveTempConversation(list)
        }
    }

    private fun saveFrequency() {
        properties.clear()
        for (key in frequentlyMap.keys) {
            properties.setProperty(key, frequentlyMap[key].toString())
        }
        properties.store(FileOutputStream(frequentlyFileDirPath), null)
    }

    private fun setUpVariables() {
        btnKeyboard = requireView().findViewById(R.id.btnCommunicationKeyboard)
        btnDelete = requireView().findViewById(R.id.btnCommunicationDelete)
        btnSave = requireView().findViewById(R.id.btnCommunicationSave)
        cedtInput = requireView().findViewById(R.id.cedtCommunicationInput)
        rvConversation = requireView().findViewById(R.id.rvCommunicationConversation)
        constRoot = requireView().findViewById(R.id.constCommunicationRoot)

        viewModel.database = AppDatabase.getInstance(requireContext())
    }

    private fun setUpOnClick() {
        btnKeyboard.setOnClickListener {
            setState(STATE.INPUT)
        }

        cedtInput.listener = object : CustomEditText.IListener {
            override fun onHeightChange(height: Int) {
            }

            override fun onSpeak() {
                val inputText = getInputText().trim()
                stopRecognition()
                if (inputText.isEmpty()) {
                    Toast.makeText(
                        requireContext(),
                        getString(R.string.input_empty),
                        Toast.LENGTH_SHORT
                    ).show()
                    return
                }
                frequentlyMap[inputText] = frequentlyMap[inputText]?.plus(1) ?: 1
                addMineMessage(inputText)
                viewModel.textToSpeech(inputText, requireContext(), this@CommunicationFragment)
                cedtInput.getEditText().setText("")
            }
        }

        rvConversation.setOnTouchListener(RVClickHandler(rvConversation))
        rvConversation.setOnClickListener {
            setState(STATE.DEFAULT)
        }

        btnDelete.setOnClickListener {
            showConfirmDeleteDialog()
        }

        btnSave.setOnClickListener {
            showSaveDialog()
        }
    }

    private fun setUpRecyclerView() {
        rvConversation.layoutManager = LinearLayoutManager(requireContext())
        setAdapterListener()
        rvConversation.adapter = communicationAdapter

        communicationAdapter.addList(viewModel.getTempConversation())
    }

    private fun setUpCustomEditText() {
        // init suggest recycler view
        cedtInput.apply {
            getRecyclerView().layoutManager =
                LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)
            getRecyclerView().adapter = suggestingAdapter.apply {
                listener = object : SuggestingAdapter.IListener {
                    override fun onNoteClick(text: String) {
                        cedtInput.addSuggestText(text, tempText)
                    }
                }
            }

            // first show all notes
            suggestingAdapter.clear()
            suggestingAdapter.addItems(viewModel.searchNote(""))

            // search note
            getEditText().doOnTextChanged { text, start, before, count ->
                tempText = text.toString().substring(start, start + count)
                suggestingAdapter.clear()
                suggestingAdapter.addItems(viewModel.searchNote(tempText))
            }
        }
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
            communicationAdapter.addAndNotify(Message(-1, true, it, getCurrentTime()))
        }
    }

    private fun addOtherMessage(message: String?) {
        message?.let {
            if (!isTouching) scrollRecyclerViewToLastItem()
            communicationAdapter.addAndNotify(Message(-1, false, it, getCurrentTime()))
        }
    }

    private fun getCurrentTime(): Long {
        return System.currentTimeMillis()
    }

    private fun setState(state: STATE) {
        when (state) {
            STATE.DEFAULT -> {
                if (this@CommunicationFragment.state == STATE.DEFAULT) return
                hideCustomEditText()
            }
            STATE.INPUT -> {
                showCustomEditText()
            }
            STATE.FULL -> { // deprecated
            }
        }
    }

    private fun hideCustomEditText() {

        // hide custom edit text
        hideKeyboard(requireView())
        cedtInput.changeHeight(CustomEditText.HIDE_HEIGHT)
        cedtInput.hide()

        // show other views
        (activity as MainActivity).showBottomNav()
        btnKeyboard.show()
        btnSave.show()
        btnDelete.show()

        // update state
        this@CommunicationFragment.state = STATE.DEFAULT
    }

    private fun showCustomEditText() {

        // hide other views
        (activity as MainActivity).hideBottomNav()
        btnKeyboard.hide()
        btnSave.hide()
        btnDelete.hide()

        // show custom edit text
        cedtInput.changeHeight(CustomEditText.DEFAULT_HEIGHT)
        cedtInput.show()
        suggestingAdapter.clear()
        suggestingAdapter.addItems(viewModel.searchNote(""))

        // update state
        this@CommunicationFragment.state = STATE.INPUT
    }

    private fun showConfirmDeleteDialog() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(resources.getString(R.string.delete_conversation_title))
            .setMessage(resources.getString(R.string.delete_conversation_des))
            .setPositiveButton(resources.getString(R.string.delete)) { _, _ -> tempDelete() }
            .setNegativeButton(resources.getString(R.string.save)) { _, _ -> showSaveDialog() }
            .show()
    }

    private fun tempDelete() {
        val deletedMessages = mutableListOf<Message>()
        deletedMessages.addAll(communicationAdapter.getAllData())
        communicationAdapter.clear()
        Snackbar.make(constRoot, getString(R.string.deleted), Snackbar.LENGTH_LONG)
            .setAction(getString(R.string.undo)) {
                communicationAdapter.addList(deletedMessages, 0)
            }
            .show()
        viewModel.deleteTempConversation()
    }

    @SuppressLint("SetTextI18n")
    private fun showSaveDialog() {
        activity?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        val view = LayoutInflater.from(requireContext())
            .inflate(R.layout.save_conversation_dialog, null, false)
        val edtTitle = view.findViewById<TextInputEditText>(R.id.edtSaveConversationTitle)

        // set current time
        viewModel.setCreateTime()
        edtTitle.setText(
            getString(R.string.conversation_title_example) + " " + convertTime(
                viewModel.createdTime
            )
        )

        var isInit = true
        edtTitle.setOnFocusChangeListener { v, hasFocus ->
            if (hasFocus && isInit) {
                edtTitle.setText("")
                isInit = false
            }
        }
        MaterialAlertDialogBuilder(requireContext()).setView(view)
            .setTitle(resources.getString(R.string.save_conversation_title))
            .setPositiveButton(resources.getString(R.string.save)) { _, _ -> save(edtTitle.text.toString()) }
            .show()
    }

    private fun save(title: String) {
        val list = communicationAdapter.getAllData()
        viewModel.saveNewConversation(list, title)

        properties.clear()
        for (key in frequentlyMap.keys) {
            properties.setProperty(key, frequentlyMap[key].toString())
        }
        properties.store(FileOutputStream(frequentlyFileDirPath), null)
        communicationAdapter.clear()
        Toast.makeText(requireContext(), getString(R.string.saved), Toast.LENGTH_SHORT).show()
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
