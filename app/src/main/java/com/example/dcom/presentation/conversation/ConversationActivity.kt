package com.example.dcom.presentation.conversation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.dcom.R
import com.example.dcom.base.event.ConversationEvent
import com.example.dcom.base.event.EventBusManager
import com.example.dcom.database.AppDatabase
import com.example.dcom.presentation.common.BaseView
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputEditText

class ConversationActivity : AppCompatActivity(), BaseView {

    companion object {
        const val CONVERSATION_ID = "conversation_id"
    }

    private lateinit var rvListMessage: RecyclerView
    private lateinit var mtbTopBar: MaterialToolbar

    private var id: Int? = null
    private val database: AppDatabase by lazy {
        AppDatabase.getInstance(this)
    }
    private val conversationAdapter by lazy {
        ConversationAdapter()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        onPrepareInitView()
        setContentView(R.layout.conversation_activity)
        onInitView()
    }

    override fun onPrepareInitView() {
        super.onPrepareInitView()
        id = intent.getIntExtra(CONVERSATION_ID, -1)
    }

    override fun onInitView() {
        setUpVariables()
        setUpOnClick()
        setUpRecyclerView()
    }

    private fun setUpVariables() {
        rvListMessage = findViewById(R.id.rvConversationHistory)
        mtbTopBar = findViewById(R.id.mtbConversationTopBar)
        if (id != null) mtbTopBar.title = database.iConversationDao().getConversationById(id!!).name
    }

    private fun setUpOnClick() {
        mtbTopBar.setNavigationOnClickListener {
            finish()
        }

        mtbTopBar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.itmConversationDelete -> {
                    showConfirmDeleteDialog()
                    true
                }
                R.id.itmConversationEdit -> {
                    showEditTitleDialog()
                    true
                }
                else -> false
            }
        }
    }

    private fun setUpRecyclerView() {
        rvListMessage.adapter = conversationAdapter
        rvListMessage.layoutManager = LinearLayoutManager(this)

        getConversationData()
    }

    private fun getConversationData() {
        if (id != null && id != -1) {
            conversationAdapter.addList(database.iConversationDao().getAllMessageInConversation(id!!))
        }
    }

    private fun showEditTitleDialog() {
        window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        val view = LayoutInflater.from(this@ConversationActivity).inflate(R.layout.save_conversation_dialog, null, false)
        val edtTitle = view.findViewById<TextInputEditText>(R.id.edtSaveConversationTitle)
        edtTitle.setText(database.iConversationDao().getConversationById(id!!).name)
        MaterialAlertDialogBuilder(this@ConversationActivity).setView(view)
            .setTitle(resources.getString(R.string.update_conversation_title))
            .setPositiveButton(resources.getString(R.string.save)) { _, _ ->
                save(edtTitle.text.toString(), -1, id)
            }
            .show()
    }

    private fun save(title: String, position: Int, id: Int?) {
        if (id == null) return
        mtbTopBar.title = title
        database.iConversationDao().updateConversationName(id, title)
        EventBusManager.instance?.postPending(ConversationEvent(ConversationEvent.STATUS.EDIT, -1, id))
    }

    private fun showConfirmDeleteDialog() {
        MaterialAlertDialogBuilder(this@ConversationActivity)
            .setTitle(resources.getString(R.string.delete_note_title))
            .setMessage(resources.getString(R.string.delete_note_des))
            .setPositiveButton(resources.getString(R.string.delete)) { _, _ -> tempDelete() }
            .show()
    }

    private fun tempDelete() {
        database.iConversationDao().deleteConversationById(id!!)
        EventBusManager.instance?.postPending(ConversationEvent(ConversationEvent.STATUS.DELETE, -1, id!!))
        finish()
    }

}
