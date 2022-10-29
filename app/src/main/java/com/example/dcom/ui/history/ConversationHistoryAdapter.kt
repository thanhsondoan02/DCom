package com.example.dcom.ui.history

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.dcom.R
import com.example.dcom.base.BaseVH

class ConversationHistoryAdapter(private val activity: ConversationHistoryActivity) : RecyclerView.Adapter<BaseVH>() {

    companion object {
        const val MINE_MESSAGE = 0
        const val OTHER_MESSAGE = 1
    }

    private val dataList: List<ConversationData> = getDataList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseVH {
        return when (viewType){
            MINE_MESSAGE -> {
                MineMessageVH(
                    LayoutInflater
                        .from(parent.context)
                        .inflate(R.layout.mine_message_item, parent, false))
            }
            OTHER_MESSAGE -> {
                OtherMessageVH(
                    LayoutInflater
                        .from(parent.context)
                        .inflate(R.layout.other_message_item, parent, false))
            }
            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun onBindViewHolder(holder: BaseVH, position: Int) {
        holder.bind(dataList[position])
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun getItemViewType(position: Int): Int {
        if (dataList[position].type != null) {
            return dataList[position].type!!
        } else {
            throw IllegalArgumentException("Message type is null at position $position")
        }
    }

    private fun getDataList(): List<ConversationData> {
        val list: MutableList<ConversationData> = mutableListOf()
        list.add(ConversationData().apply {
            type = OTHER_MESSAGE
            message = activity.getString(R.string.speech_to_text_des)
        })
        list.add(ConversationData().apply {
            type = OTHER_MESSAGE
            message = activity.getString(R.string.text_to_speech_des)
        })
        list.add(ConversationData().apply {
            type = OTHER_MESSAGE
            message = activity.getString(R.string.fast_communication_setting_des)
        })
        list.add(ConversationData().apply {
            type = MINE_MESSAGE
            message = activity.getString(R.string.conversation_history_des)
        })
        list.add(ConversationData().apply {
            type = MINE_MESSAGE
            message = activity.getString(R.string.emergency_signal_des)
        })
        list.add(ConversationData().apply {
            type = MINE_MESSAGE
            message = activity.getString(R.string.app_des)
        })
        list.add(ConversationData().apply {
            type = OTHER_MESSAGE
            message = activity.getString(R.string.speech_to_text_des)
        })
        list.add(ConversationData().apply {
            type = MINE_MESSAGE
            message = activity.getString(R.string.text_to_speech_des)
        })
        list.add(ConversationData().apply {
            type = OTHER_MESSAGE
            message = activity.getString(R.string.fast_communication_setting_des)
        })
        list.add(ConversationData().apply {
            type = OTHER_MESSAGE
            message = activity.getString(R.string.conversation_history_des)
        })
        list.add(ConversationData().apply {
            type = MINE_MESSAGE
            message = activity.getString(R.string.emergency_signal_des)
        })
        list.add(ConversationData().apply {
            type = MINE_MESSAGE
            message = activity.getString(R.string.app_des)
        })
        list.addAll(list)
        return list
    }

    inner class MineMessageVH(itemView: View): BaseVH(itemView) {

        private val tvMessage = itemView.findViewById<TextView>(R.id.tvMineMessage)
        private val rlContainer = itemView.findViewById<View>(R.id.rlMineMessageContainer)

        init {

        }

        override fun bind(data: Any?) {
            val mData = data as ConversationData
            tvMessage.text = mData.message
            rlContainer.setBackgroundResource(getMessageBackground())
        }

        private fun getMessageBackground(): Int {
            return if (adapterPosition == 0 || dataList[adapterPosition-1].type == OTHER_MESSAGE) {
                if (adapterPosition == dataList.size-1 || dataList[adapterPosition+1].type == OTHER_MESSAGE) {
                    R.drawable.mine_message_full
                } else {
                    R.drawable.mine_message_full_except_bot_right
                }
            } else {
                if (adapterPosition == dataList.size-1 || dataList[adapterPosition+1].type == OTHER_MESSAGE) {
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

        }

        override fun bind(data: Any?) {
            val mData = data as ConversationData
            tvMessage.text = mData.message
            rlContainer.setBackgroundResource(getMessageBackground())
        }

        private fun getMessageBackground(): Int {
            return if (adapterPosition == 0 || dataList[adapterPosition-1].type == MINE_MESSAGE) {
                if (adapterPosition == dataList.size-1 || dataList[adapterPosition+1].type == MINE_MESSAGE) {
                    R.drawable.other_message_full
                } else {
                    R.drawable.other_message_full_except_bot_left
                }
            } else {
                if (adapterPosition == dataList.size-1 || dataList[adapterPosition+1].type == MINE_MESSAGE) {
                    R.drawable.other_message_full_except_top_left
                } else {
                    R.drawable.other_message_right
                }
            }
        }
    }

    class ConversationData {
        var type: Int? = null
        var message: String? = null
    }


}

