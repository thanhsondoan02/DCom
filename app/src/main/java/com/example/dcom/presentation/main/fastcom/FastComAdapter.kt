package com.example.dcom.presentation.main.fastcom

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.dcom.R
import com.example.dcom.presentation.common.recyclerview.BaseVH

class FastComAdapter(private val activity: FastComActivity) : RecyclerView.Adapter<BaseVH>() {

    private val dataList: List<ConversationData> = getDataList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseVH {
        return TextVH(LayoutInflater
            .from(parent.context)
            .inflate(R.layout.fast_com_item, parent, false))
    }

    override fun onBindViewHolder(holder: BaseVH, position: Int) {
        holder.bind(dataList[position])
    }

    override fun getItemCount(): Int {
        Log.d("Peswoc", "getItemCount: ${dataList.size}")
        return dataList.size
    }

    private fun getDataList(): List<ConversationData> {
        val list: MutableList<ConversationData> = mutableListOf()
        list.add(ConversationData().apply {
            title = activity.getString(R.string.app_name)
            content = activity.getString(R.string.speech_to_text_des)
        })
        list.add(ConversationData().apply {
            title = activity.getString(R.string.emergency_signal)
            content = activity.getString(R.string.emergency_signal_des)
        })
        list.add(ConversationData().apply {
            title = activity.getString(R.string.app_name)
            content = activity.getString(R.string.example_paragraph2)
        })
        list.add(ConversationData().apply {
            title = activity.getString(R.string.app_name)
            content = activity.getString(R.string.speech_to_text_des)
        })
        list.add(ConversationData().apply {
            title = activity.getString(R.string.app_name)
            content = activity.getString(R.string.speech_to_text_des)
        })
        list.add(ConversationData().apply {
            title = activity.getString(R.string.app_name)
            content = activity.getString(R.string.speech_to_text_des)
        })
        list.add(ConversationData().apply {
            title = activity.getString(R.string.app_name)
            content = activity.getString(R.string.speech_to_text_des)
        })
        list.add(ConversationData().apply {
            title = activity.getString(R.string.app_name)
            content = activity.getString(R.string.speech_to_text_des)
        })
        list.add(ConversationData().apply {
            title = activity.getString(R.string.app_name)
            content = activity.getString(R.string.speech_to_text_des)
        })
        list.add(ConversationData().apply {
            title = activity.getString(R.string.app_name)
            content = activity.getString(R.string.speech_to_text_des)
        })
        list.add(ConversationData().apply {
            title = activity.getString(R.string.app_name)
            content = activity.getString(R.string.speech_to_text_des)
        })
        list.addAll(list)
        list.addAll(list)
        list.addAll(list)
        return list
    }

    inner class TextVH(itemView: View): BaseVH(itemView) {

        private val tvTitle: TextView = itemView.findViewById(R.id.tvFastComTitle)
        private val tvContent: TextView =  itemView.findViewById(R.id.tvFastComContent)

        override fun bind(data: Any?) {
            val mData = data as ConversationData

            tvTitle.text = mData.title
            tvContent.text = mData.content
        }

    }

    class ConversationData {
        var title: String? = null
        var content: String? = null
    }

}
