package com.example.dcom.presentation.main.history

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.dcom.R
import com.example.dcom.database.conversation.Conversation
import com.example.dcom.presentation.common.recyclerview.BaseVH
import com.google.android.material.card.MaterialCardView

@Suppress("UNCHECKED_CAST")
class HistoryAdapter : RecyclerView.Adapter<BaseVH>() {

    companion object {
        const val CONVERSATION = 1
        const val SELECT_PAYLOAD = "SELECT_PAYLOAD"
    }

    var listener: IListener? = null

    private val mData = mutableListOf<ConversationDisplay>()
    private var tempDelete = mutableListOf<Pair<ConversationDisplay, Int>>()
    private var state = STATE.NORMAL
    private var countSelected: Int = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseVH {
        return ConversationVH(LayoutInflater.from(parent.context).inflate(R.layout.history_conversation_item, parent, false))
    }

    override fun onBindViewHolder(holder: BaseVH, position: Int) {
        holder.bind(mData[position])
    }

    override fun getItemCount(): Int {
        return mData.size
    }

    override fun getItemViewType(position: Int): Int {
        return CONVERSATION
    }

    fun selectAll() {
        mData.filterIsInstance<ConversationDisplay>().forEach { it.isSelected = true }
        countSelected = mData.filterIsInstance<ConversationDisplay>().size
        notifyItemRangeChanged(0, itemCount, SELECT_PAYLOAD)
        listener?.onChangeSelect(countSelected)
    }

    fun updateTitle(position: Int, title: String) {
        mData[position].conversation.name = title
        notifyItemChanged(position)
    }

    fun getSelectedConversation() = mData.filterIsInstance<ConversationDisplay>().filter { it.isSelected }

    fun getSelectedConversationTitle() = mData.filterIsInstance<ConversationDisplay>().filter { it.isSelected }.map { it.conversation.name }

    fun getSelectedConversationId() = mData.filterIsInstance<ConversationDisplay>().filter { it.isSelected }.map { it.conversation.id }

    fun getSelectedPosition(): List<Int> {
        val list = mutableListOf<Int>()
        print(mData)
        mData.forEachIndexed { index, conversationDisplay ->
            if (conversationDisplay.isSelected) {
                list.add(index)
            }
        }
        return list
    }

    fun deleteSelected(): List<Conversation> {
        tempDelete.clear()
        val deletedConversations: MutableList<Conversation> = mutableListOf()
        for (i in mData.size - 1 downTo 0) {
            (mData[i] as? ConversationDisplay)?.let {
                if (it.isSelected) {
                    deletedConversations.add(it.conversation)
                    remove(i)
                    tempDelete.add(Pair(it, i))
                }
            }
        }
        state = STATE.NORMAL
        listener?.onEndSelect()
        return deletedConversations
    }

    fun undoDeleted() {
        for (i in tempDelete.size - 1 downTo 0) {
            add(tempDelete[i].first.conversation, tempDelete[i].second)
        }
        tempDelete.clear()
    }

    fun unSelectAllAndChangeState() {
        mData.forEach {
            it.isSelected = false
        }
        countSelected = 0
        notifyItemRangeChanged(0, itemCount, SELECT_PAYLOAD)
        state = STATE.NORMAL
    }

    @Deprecated("")
    fun unSelectAll() {
        mData.forEach {
            it.isSelected = false
        }
        countSelected = 0
        notifyItemRangeChanged(0, itemCount, SELECT_PAYLOAD)
        listener?.onChangeSelect(countSelected)
    }

    fun add(conversation: Conversation, position: Int) {
        mData.add(position, ConversationDisplay(conversation))
        notifyItemInserted(position)
    }

    fun addItems(data: List<ConversationDisplay>) {
        mData.addAll(data)
        notifyItemRangeInserted(mData.size, data.size)
    }

    fun addItem(data: ConversationDisplay) {
        mData.add(data)
        notifyItemInserted(mData.size)
    }

    fun remove(position: Int) {
        mData.removeAt(position)
        notifyItemRemoved(position)
    }

    inner class ConversationVH(itemView: View): BaseVH(itemView) {

        private val title: TextView = itemView.findViewById(R.id.tvHistoryConversationTitle)
        private val latestMessage: TextView = itemView.findViewById(R.id.tvHistoryConversationLatestMessage)
        private val mcvConversation: MaterialCardView = itemView.findViewById(R.id.mcvHistoryConversationCard)

        init {
            mcvConversation.setOnClickListener {
                when (state) {
                    STATE.NORMAL -> {
                        onSpeak()
                    }
                    STATE.SELECTING -> {
                        onSelect()
                    }
                }
            }

            mcvConversation.setOnLongClickListener {
                onSelect()
            }
        }

        override fun bind(data: Any?) {
            data as ConversationDisplay
            title.text = data.conversation.name
            setSelectStatus(data.isSelected)
        }

        override fun bind(data: Any?, payloads: List<Any>) {
            if (payloads.contains(SELECT_PAYLOAD)) {
                setSelectStatus((data as ConversationDisplay).isSelected)
            }
        }

        private fun setSelectStatus(isSelected: Boolean) {
            mcvConversation.isChecked = isSelected
        }

        private fun onSpeak() {
            listener?.onClickConversation(mData[adapterPosition].conversation?.id, mData[adapterPosition].conversation?.name)
        }

        private fun onSelect(): Boolean {
            mcvConversation.isChecked = !mcvConversation.isChecked
            if (mcvConversation.isChecked) {
                if (countSelected == 0) {
                    state = STATE.SELECTING
                    listener?.onInitSelect()
                }
                countSelected++
                mData[adapterPosition].isSelected = true
            } else {
                countSelected--
                if (countSelected == 0) {
                    state = STATE.NORMAL
                    listener?.onEndSelect()
                }
                mData[adapterPosition].isSelected = false
            }
            listener?.onChangeSelect(countSelected)
            print(mData)
            return true
        }
    }

    class ConversationDisplay(var conversation: Conversation) {
        var isSelected: Boolean = false
    }

    enum class STATE {
        NORMAL, SELECTING
    }

    interface IListener {
        fun onClickConversation(id: Int?, name: String?)
        fun onInitSelect()
        fun onChangeSelect(countSelected: Int)
        fun onEndSelect()
    }
    
}
