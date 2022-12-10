package com.example.dcom.presentation.search

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.dcom.R
import com.example.dcom.database.conversation.Conversation
import com.example.dcom.database.message.Message
import com.example.dcom.database.note.Note
import com.example.dcom.presentation.common.recyclerview.BaseVH

class SearchAdapter: RecyclerView.Adapter<BaseVH>() {

    companion object {
        const val NOTE = 0
        const val CONVERSATION = 1
    }

    var listener: IListener? = null

    private val mData = mutableListOf<SearchDisplay>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseVH {
        return when (viewType) {
            NOTE -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.search_note_item, parent, false)
                NoteVH(view)
            }
            CONVERSATION -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.search_conversation_item, parent, false)
                ConversationVH(view)
            }
            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun onBindViewHolder(holder: BaseVH, position: Int) {
        holder.bind(mData[position])
    }

    override fun getItemCount(): Int {
        return mData.size
    }

    override fun getItemViewType(position: Int): Int {
        mData[position].apply {
            return if (note != null) {
                NOTE
            } else if (conversation != null) {
                CONVERSATION
            } else {
                throw IllegalArgumentException("Invalid view type")
            }
        }
    }

    fun addItems(items: List<SearchDisplay>) {
        mData.addAll(items)
        notifyItemRangeInserted(mData.size - items.size, items.size)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun clearItems() {
        mData.clear()
        notifyDataSetChanged()
    }

    fun update(position: Int, item: SearchDisplay) {
        mData[position] = item
        notifyItemChanged(position)
    }

    fun removeById(id: Int) {
        mData.find { it.note?.id == id || it.conversation?.id == id }?.let {
            val position = mData.indexOf(it)
            mData.remove(it)
            notifyItemRemoved(position)
        }
    }

    fun updateById(id: Int, item: SearchDisplay) {
        mData.find { it.conversation?.id == id || it.note?.id == id }?.let {
            it.conversation = item.conversation
            it.note = item.note
            notifyItemChanged(mData.indexOf(it))
        }
    }

    fun remove(position: Int) {
        mData.removeAt(position)
        notifyItemRemoved(position)
    }

    inner class NoteVH(itemView: View): BaseVH(itemView) {

        private val tvName = itemView.findViewById<TextView>(R.id.tvSearchNoteName)

        init {
            itemView.setOnClickListener {
                mData.getOrNull(adapterPosition)?.note?.let { note ->
                    listener?.onNoteClick(note, adapterPosition)
                }
            }
        }

        override fun bind(data: Any?) {
            tvName.text = (data as? SearchDisplay)?.note?.content
        }
    }

    inner class ConversationVH(itemView: View): BaseVH(itemView) {

        private val tvName = itemView.findViewById<TextView>(R.id.tvSearchConversationName)

        init {
            itemView.setOnClickListener {
                mData.getOrNull(adapterPosition)?.conversation?.let { conversation ->
                    listener?.onConversationClick(conversation, adapterPosition)
                }
            }
        }

        override fun bind(data: Any?) {
            tvName.text = (data as? SearchDisplay)?.conversation?.name
        }
    }

    class SearchDisplay(
        var note: Note? = null,
        var message: Message? = null,
        var conversation: Conversation? = null)

    interface IListener {
        fun onConversationClick(conversation: Conversation, position: Int)
        fun onNoteClick(note: Note?, position: Int)
    }

}
