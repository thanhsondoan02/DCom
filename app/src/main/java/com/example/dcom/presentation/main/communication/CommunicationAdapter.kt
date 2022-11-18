package com.example.dcom.presentation.main.communication

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.dcom.R
import com.example.dcom.presentation.common.recyclerview.BaseVH

class CommunicationAdapter : RecyclerView.Adapter<BaseVH>() {

    interface IListener {
        fun onClickItem(index: Int)
    }

    companion object {
        const val TIME_DATE = 0
        const val MESSAGE = 1
    }

    var listener: IListener? = null

    private val mData = mutableListOf<Any>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseVH {
        return when (viewType) {
            TIME_DATE -> TimeDateVH(LayoutInflater.from(parent.context).inflate(R.layout.communication_time_date_item, parent, false))
            MESSAGE -> MessageVH(LayoutInflater.from(parent.context).inflate(R.layout.communication_message_item, parent, false))
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
        return when (mData[position]) {
            is TimeDateData -> TIME_DATE
            is MessageData -> MESSAGE
            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    fun addData(data: List<Any>) {
        mData.addAll(data)
        notifyItemRangeInserted(mData.size, data.size)
    }

    inner class TimeDateVH(itemView: View): BaseVH(itemView) {

        private val tvDateTime = itemView.findViewById<TextView>(R.id.tvCommunicationTimeDate)

        init {
            itemView.setOnClickListener {
                listener?.onClickItem(adapterPosition)
            }
        }

        override fun bind(data: Any?) {
            data as TimeDateData
            tvDateTime.text = data.timeDate
        }
    }

    inner class MessageVH(itemView: View): BaseVH(itemView) {

        private val tvMessage = itemView.findViewById<TextView>(R.id.tvCommunicationMessage)

        init {
            itemView.setOnClickListener {
                listener?.onClickItem(adapterPosition)
            }
        }

        override fun bind(data: Any?) {
            data as MessageData
            tvMessage.text = data.message
        }
    }

    class TimeDateData {
        var timeDate: String = ""
    }

    class MessageData {
        var message: String = ""
    }

}
