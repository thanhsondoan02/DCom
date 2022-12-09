package com.example.dcom.presentation.main.history

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.dcom.R
import com.example.dcom.database.conversation.Conversation
import com.example.dcom.database.message.Message
import com.example.dcom.presentation.common.recyclerview.BaseVH
import com.google.android.material.card.MaterialCardView

@Suppress("UNCHECKED_CAST")
class HistoryAdapter : RecyclerView.Adapter<BaseVH>() {

    interface IListener {
        fun onClickConversation(id: Int?, name: String?)
    }

    companion object {
        const val COMMON = 0
        const val CONVERSATION = 1
    }

    var listener: IListener? = null

    private val mData = mutableListOf<ConversationDisplay>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseVH {
//        return when (viewType) {
//            COMMON -> CommonVH(LayoutInflater.from(parent.context).inflate(R.layout.history_common_item, parent, false))
//            CONVERSATION -> ConversationVH(LayoutInflater.from(parent.context).inflate(R.layout.history_conversation_item, parent, false))
//            else -> throw IllegalArgumentException("Invalid view type")
//        }
        return ConversationVH(LayoutInflater.from(parent.context).inflate(R.layout.history_conversation_item, parent, false))
    }

    override fun onBindViewHolder(holder: BaseVH, position: Int) {
        holder.bind(mData[position])
    }

    override fun getItemCount(): Int {
        return mData.size
    }

    override fun getItemViewType(position: Int): Int {
//        return when (position) {
//            0 -> COMMON
//            else -> CONVERSATION
//        }
        return CONVERSATION
    }

    fun addItems(data: List<ConversationDisplay>) {
        mData.addAll(data)
        notifyItemRangeInserted(mData.size, data.size)
    }

    fun addItem(data: ConversationDisplay) {
        mData.add(data)
        notifyItemInserted(mData.size)
    }

//    inner class CommonVH(itemView: View): BaseVH(itemView) {
//
//        private val rvTopConversations: RecyclerView = itemView.findViewById(R.id.rvHistoryCommonTopConversation)
//
//        init {
//            rvTopConversations.layoutManager = LinearLayoutManager(itemView.context, RecyclerView.HORIZONTAL, false)
//            rvTopConversations.adapter = CommonAdapter()
//        }
//
//        @SuppressLint("NotifyDataSetChanged")
//        override fun bind(data: Any?) {
//            rvTopConversations.adapter?.notifyDataSetChanged()
//        }
//    }

    inner class ConversationVH(itemView: View): BaseVH(itemView) {

        private val title: TextView = itemView.findViewById(R.id.tvHistoryConversationTitle)
        private val latestMessage: TextView = itemView.findViewById(R.id.tvHistoryConversationLatestMessage)
        private val mcvConversation: MaterialCardView = itemView.findViewById(R.id.mcvHistoryConversationCard)

        init {
            mcvConversation.setOnClickListener {
                listener?.onClickConversation(mData[adapterPosition].conversation?.id, mData[adapterPosition].conversation?.name)
            }
        }

        override fun bind(data: Any?) {
            data as ConversationDisplay
            title.text = data.conversation?.name
            latestMessage.text = data.latestMessage?.content
        }
    }

    class ConversationDisplay {
        var conversation: Conversation? = null
        var latestMessage: Message? = null

        constructor(conversation: Conversation?, latestMessage: Message? = null) {
            this.conversation = conversation
            this.latestMessage = latestMessage
        }
    }

//    inner class CommonAdapter: RecyclerView.Adapter<BaseVH>() {
//
//        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseVH {
//            return CommonConversationVH(LayoutInflater.from(parent.context).inflate(R.layout.history_common_conversation_item, parent, false))
//        }
//
//        override fun onBindViewHolder(holder: BaseVH, position: Int) {
//            holder.bind((mData[0] as List<ConversationData>)[position])
//        }
//
//        override fun getItemCount(): Int {
//            return (mData[0] as List<ConversationData>).size
//        }
//
//        inner class CommonConversationVH(itemView: View): BaseVH(itemView) {
//
//            private val avatar: ImageView = itemView.findViewById(R.id.ivHistoryCommonConversationAvatar)
//            private val title: TextView = itemView.findViewById(R.id.tvHistoryCommonConversationTitle)
//
//            init {
//                itemView.setOnClickListener {
//                    listener?.onClickConversation((mData[0] as List<ConversationData>)[adapterPosition].id)
//                }
//            }
//
//            override fun bind(data: Any?) {
//                data as ConversationData
//                avatar.setImageDrawable(data.avatar)
//                title.text = data.title
//            }
//        }
//    }
}
