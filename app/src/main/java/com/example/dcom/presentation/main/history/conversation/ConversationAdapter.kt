package com.example.dcom.presentation.main.history.conversation

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.dcom.R
import com.example.dcom.database.message.Message
import com.example.dcom.presentation.common.recyclerview.BaseVH

class ConversationAdapter : RecyclerView.Adapter<BaseVH>() {

    companion object {
        const val MINE_MESSAGE = 0
        const val OTHER_MESSAGE = 1
    }

    var listener: IListener? = null
    
    private val mData = mutableListOf<Message>()
    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseVH {
        return when (viewType){
            MINE_MESSAGE -> {
                MineMessageVH(LayoutInflater.from(parent.context).inflate(R.layout.mine_message_item, parent, false))
            }
            OTHER_MESSAGE -> {
                OtherMessageVH(LayoutInflater.from(parent.context).inflate(R.layout.other_message_item, parent, false))
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
        return if (mData[position].isMine) {
            MINE_MESSAGE
        } else {
            OTHER_MESSAGE
        }
    }

    fun update(position: Int, note: Message) {
        mData[position] = note
        notifyItemChanged(position)
    }

    fun add(note: Message) {
        mData.add(note)
        notifyItemInserted(mData.size)
    }

    fun addList(data: List<Message>) {
        mData.addAll(data)
        notifyItemRangeInserted(mData.size, data.size)
    }

    fun remove(position: Int) {
        mData.removeAt(position-1)
        notifyItemRemoved(position)
    }

    inner class MineMessageVH(itemView: View): BaseVH(itemView) {

        private val tvMessage = itemView.findViewById<TextView>(R.id.tvMineMessage)
        private val rlContainer = itemView.findViewById<View>(R.id.rlMineMessageContainer)

        init {
            itemView.setOnClickListener {
                listener?.onRecyclerViewClick()
            }
        }

        override fun bind(data: Any?) {
            val mData = data as Message
            tvMessage.text = mData.content
            rlContainer.setBackgroundResource(getMessageBackground())
        }

        private fun getMessageBackground(): Int {
            return if (adapterPosition == 0 || !mData[adapterPosition-1].isMine) {
                if (adapterPosition == mData.size-1 || !mData[adapterPosition+1].isMine) {
                    R.drawable.mine_message_full
                } else {
                    R.drawable.mine_message_full_except_bot_right
                }
            } else {
                if (adapterPosition == mData.size-1 || !mData[adapterPosition+1].isMine) {
                    R.drawable.mine_message_full_except_top_right
                } else {
                    R.drawable.mine_message_left
                }
            }
        }

    }

    inner class OtherMessageVH(itemView: View): BaseVH(itemView) {

        private val tvMessage = itemView.findViewById<TextView>(R.id.tvOtherMessage)
        private val rlContainer = itemView.findViewById<View>(R.id.rlOtherMessageContainer)

        init {
            itemView.setOnClickListener {
                listener?.onRecyclerViewClick()
            }
        }

        override fun bind(data: Any?) {
            val mData = data as Message
            tvMessage.text = mData.content
            rlContainer.setBackgroundResource(getMessageBackground())
        }

        private fun getMessageBackground(): Int {
            return if (adapterPosition == 0 || mData[adapterPosition-1].isMine) {
                if (adapterPosition == mData.size-1 || mData[adapterPosition+1].isMine) {
                    R.drawable.other_message_full
                } else {
                    R.drawable.other_message_full_except_bot_left
                }
            } else {
                if (adapterPosition == mData.size-1 || mData[adapterPosition+1].isMine) {
                    R.drawable.other_message_full_except_top_left
                } else {
                    R.drawable.other_message_right
                }
            }
        }
    }
    
    interface IListener {
        fun onRecyclerViewClick()
    }

}

