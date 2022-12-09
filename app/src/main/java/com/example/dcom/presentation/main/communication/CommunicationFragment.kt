package com.example.dcom.presentation.main.communication

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
import com.example.dcom.presentation.main.MainActivity
import com.example.dcom.presentation.main.history.conversation.ConversationAdapter
import com.example.dcom.presentation.widget.CustomEditText
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText

class CommunicationFragment: BaseFragment(R.layout.communication_fragment) {

    private lateinit var btnKeyboard: Button
    private lateinit var btnDelete: Button
    private lateinit var btnSave: Button
    private lateinit var cedtInput: CustomEditText
    private lateinit var rvConversation: RecyclerView
    private lateinit var constRoot: ConstraintLayout

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

    override fun onInitView() {
        super.onInitView()

        setUpVariables()
        setUpOnClick()
        setUpRecyclerView()
        setUpCustomEditText()
        setState(STATE.DEFAULT)

        viewModel.speechToText(requireContext())
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
                        Toast.makeText(requireContext(), getString(R.string.speak_success), Toast.LENGTH_SHORT).show()
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
                        addOtherMessage(it.data)
                        viewModel.speechToText(requireContext())
                    }
                })
            }
        }
    }

    override fun onPause() {
        super.onPause()
        saveTempConversation()
    }

    private fun saveTempConversation() {
        val list = communicationAdapter.getAllData()
        if (list.isNotEmpty()) {
            viewModel.saveTempConversation(list)
        }
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
                val inputText = getInputText()
                addMineMessage(inputText)
                viewModel.textToSpeech(inputText, requireContext())
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


    }

    private fun setUpCustomEditText() {
        // init suggest recycler view
        cedtInput.apply {
            getRecyclerView().layoutManager = LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)
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
    }

    private fun showSaveDialog() {
        activity?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        val view = LayoutInflater.from(requireContext()).inflate(R.layout.save_conversation_dialog, null, false)
        val edtTitle = view.findViewById<TextInputEditText>(R.id.edtSaveConversationTitle)
        MaterialAlertDialogBuilder(requireContext()).setView(view)
            .setTitle(resources.getString(R.string.save_conversation_title))
            .setPositiveButton(resources.getString(R.string.save)) { _, _ -> save(edtTitle.text.toString()) }
            .show()
    }

    private fun save(title: String) {
        val list = communicationAdapter.getAllData()
        viewModel.saveNewConversation(list, title)
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
