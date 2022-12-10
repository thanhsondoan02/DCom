package com.example.dcom.presentation.main.history

import android.content.Intent
import android.view.LayoutInflater
import android.view.WindowManager
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.dcom.R
import com.example.dcom.base.event.*
import com.example.dcom.database.AppDatabase
import com.example.dcom.database.conversation.Conversation
import com.example.dcom.database.message.Message
import com.example.dcom.extension.gone
import com.example.dcom.extension.show
import com.example.dcom.presentation.common.BaseFragment
import com.example.dcom.presentation.conversation.ConversationActivity
import com.example.dcom.presentation.main.MainActivity
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class HistoryFragment : BaseFragment(R.layout.history_fragment), IEventHandler {

    private lateinit var rvContent: RecyclerView
    private val historyAdapter by lazy { HistoryAdapter() }
    private val database by lazy {
        AppDatabase.getInstance(requireContext())
    }

    override fun onStart() {
        super.onStart()
        EventBusManager.instance?.register(this)
    }

    override fun onStop() {
        super.onStop()
        EventBusManager.instance?.unregister(this)
    }

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    override fun onEvent(event: IEvent) {
        when (event) {
            is ConversationEvent -> {
                when (event.status) {
                    ConversationEvent.STATUS.ADD -> {
                        historyAdapter.addItem(HistoryAdapter.ConversationDisplay(database.iConversationDao().getConversationById(event.id)))
                        EventBusManager.instance?.removeSticky(event)
                    }
                    ConversationEvent.STATUS.EDIT -> {
                        val newTitle = database.iConversationDao().getConversationById(event.id).name
                        historyAdapter.updateTitleById(event.id, newTitle)
                        EventBusManager.instance?.removeSticky(event)
                    }
                    ConversationEvent.STATUS.DELETE -> {
                        historyAdapter.removeById(event.id)
                        EventBusManager.instance?.removeSticky(event)
                    }
                }
                EventBusManager.instance?.removeSticky(event)
            }

            is DeleteDatabaseEvent -> {
                if (!event.isFavorite) {
                    historyAdapter.clear()
                    EventBusManager.instance?.removeSticky(event)
                }
            }
        }
    }

    override fun onInitView() {
        super.onInitView()

        setUpVariables()
        setUpOnClick()
        setUpRecyclerView()
    }

    private fun setUpVariables() {
        rvContent = requireView().findViewById(R.id.rvHistoryContent)
    }

    private fun setUpOnClick() {
        historyAdapter.listener = object: HistoryAdapter.IListener {
            override fun onClickConversation(id: Int?, name: String?) {
                if (id != null) {
                    startActivity(Intent(requireContext(), ConversationActivity::class.java).apply {
                        putExtra(ConversationActivity.CONVERSATION_ID, id)
                    })
                } else {
                    Toast.makeText(requireContext(), getString(R.string.cant_find_conversation), Toast.LENGTH_SHORT).show()
                }
            }

            override fun onInitSelect() {
                initSelectBar()
            }

            override fun onChangeSelect(countSelected: Int) {
                changeSelectBar(countSelected)
            }

            override fun onEndSelect() {
                hideSelectBar()
            }
        }
    }

    private fun initSelectBar() {
        getSelectBar()?.apply {
            show()
            setNavigationOnClickListener {
                hideSelectBar()
            }
            setOnMenuItemClickListener { menuItem ->
                when (menuItem.itemId) {
                    R.id.itmSelectDelete -> {
                        showConfirmDeleteDialog()
                        true
                    }
                    R.id.itmSelectAll -> {
                        historyAdapter.selectAll()
                        true
                    }
                    R.id.itmSelectEdit -> {
                        showEditTitleDialog()
                        true
                    }
                    else -> false
                }
            }
        }
    }

    private fun showEditTitleDialog() {
        activity?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        val view = LayoutInflater.from(requireContext()).inflate(R.layout.save_conversation_dialog, null, false)
        val edtTitle = view.findViewById<TextInputEditText>(R.id.edtSaveConversationTitle)
        edtTitle.setText(historyAdapter.getSelectedConversationTitle().first())
        MaterialAlertDialogBuilder(requireContext()).setView(view)
            .setTitle(resources.getString(R.string.update_conversation_title))
            .setPositiveButton(resources.getString(R.string.save)) { _, _ ->
                val position = historyAdapter.getSelectedPosition().first()
                val id = historyAdapter.getSelectedConversationId().first()
                save(edtTitle.text.toString(), position, id)
            }
            .show()
    }

    private fun hideSelectBar() {
        getSelectBar()?.gone()
        historyAdapter.unSelectAllAndChangeState()
    }

    private fun changeSelectBar(countSelect: Int) {
        getSelectBar()?.title = countSelect.toString()
        getSelectBar()?.menu?.findItem(R.id.itmSelectEdit)?.isVisible = countSelect == 1
    }

    private fun showButtonSelectAll() {
        getSelectBar()?.apply {
            menu.findItem(R.id.itmSelectAll).isVisible = true
            menu.findItem(R.id.itmSelectDelete).isVisible = true
            menu.findItem(R.id.itmSelectNone).isVisible = false
        }
    }

    private fun showButtonSelectNone() {
        getSelectBar()?.apply {
            menu.findItem(R.id.itmSelectAll).isVisible = false
            menu.findItem(R.id.itmSelectDelete).isVisible = false
            menu.findItem(R.id.itmSelectNone).isVisible = true
        }
    }

    private fun getSelectBar(): MaterialToolbar? {
        return (activity as? MainActivity)?.getSelectBar()
    }

    private fun showConfirmDeleteDialog() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(resources.getString(R.string.delete_conversation_title))
            .setMessage(resources.getString(R.string.delete_conversation_des2))
            .setPositiveButton(resources.getString(R.string.delete)) { _, _ -> tempDelete() }
            .show()
    }

    private fun tempDelete() {
        val deletedNotes = historyAdapter.deleteSelected()
        deleteConversations(deletedNotes.map { it.id })
        Snackbar.make(requireView(), getString(R.string.deleted), Snackbar.LENGTH_LONG)
            .setAction(getString(R.string.undo)) {
                historyAdapter.undoDeleted()
                undeleteConversation()
            }
            .show()
    }

    private var deletedConversationList = listOf<Conversation>()
    private var deletedMessageList = listOf<Message>()

    private fun deleteConversations(listConversationId: List<Int>) {
        // save to variables
        deletedConversationList = database.iConversationDao().getConversationByIds(listConversationId)
        deletedMessageList = database.iConversationDao().getMessagesByConversationIds(listConversationId)

        // delete from database
        database.iConversationDao().deleteMessageInConversations(listConversationId)
        database.iConversationDao().deleteConversationByIds(listConversationId)
    }

    private fun undeleteConversation() {
        // save to database
        database.iConversationDao().insertConversations(deletedConversationList)
        database.iConversationDao().insertMessages(deletedMessageList)
    }

    private fun save(title: String, position: Int, id: Int) {
        historyAdapter.updateTitle(position, title)
        database.iConversationDao().updateConversationName(id, title)
        hideSelectBar()
    }

    private fun setUpRecyclerView() {
        rvContent.layoutManager = LinearLayoutManager(requireContext())
        rvContent.adapter = historyAdapter

        getConversationFromDatabase()
    }

    private fun getConversationFromDatabase() {
        val listConversationDisplay = mutableListOf<HistoryAdapter.ConversationDisplay>()
        database.iConversationDao().getAll().forEach { conversation ->
            listConversationDisplay.add(HistoryAdapter.ConversationDisplay(conversation))
        }
        historyAdapter.addItems(listConversationDisplay)
    }

//    private fun checkData() {
//        val list = database.iConversationDao().getAll()
//        val listConversationDisplay = mutableListOf<HistoryAdapter.ConversationDisplay>()
//        if (list.size > historyAdapter.itemCount) {
//            // add new data
//            list.subList(historyAdapter.itemCount, list.size - 1).forEach { conversation ->
//                val message = database.iConversationDao().getLatestMessageInConversation(conversation.id)
//                listConversationDisplay.add(HistoryAdapter.ConversationDisplay(conversation, message))
//            }
//        }
//        historyAdapter.addItems(listConversationDisplay)
//    }

}
