package com.example.dcom.ui.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.dcom.R
import com.example.dcom.base.BaseVH

class HomeAdapter(val activity: HomeActivity) : RecyclerView.Adapter<BaseVH>() {

    private val dataList: List<FeatureData> = getDataList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseVH {
        return FeatureVH(LayoutInflater.from(parent.context).inflate(R.layout.home_feature_item, parent, false))
    }

    override fun onBindViewHolder(holder: BaseVH, position: Int) {
        holder.bind(dataList[position])
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    private fun getDataList(): List<FeatureData> {
        val list: MutableList<FeatureData> = mutableListOf()
        list.add(FeatureData().apply {
            title = activity.getString(R.string.speech_to_text)
            description = activity.getString(R.string.speech_to_text_des)
            background = R.drawable.home_feature_gradient1
            icon = R.drawable.home_ic_maths
        })
        list.add(FeatureData().apply {
            title = activity.getString(R.string.text_to_speech)
            description = activity.getString(R.string.text_to_speech_des)
            background = R.drawable.home_feature_gradient2
            icon = R.drawable.home_ic_biology
        })
        list.add(FeatureData().apply {
            title = activity.getString(R.string.conversation_history)
            description = activity.getString(R.string.conversation_history_des)
            background = R.drawable.home_feature_gradient3
            icon = R.drawable.home_ic_chemistry
        })
        list.add(FeatureData().apply {
            title = activity.getString(R.string.fast_communication)
            description = activity.getString(R.string.fast_communication_des)
            background = R.drawable.home_feature_gradient4
            icon = R.drawable.home_ic_physics
        })
        list.add(FeatureData().apply {
            title = activity.getString(R.string.emergency_signal)
            description = activity.getString(R.string.emergency_signal_des)
            background = R.drawable.home_feature_gradient5
            icon = R.drawable.home_ic_geography
        })
        return list
    }

    inner class FeatureVH(itemView: View): BaseVH(itemView) {

        private val rlContainer: RelativeLayout = itemView.findViewById(R.id.rlFeatureContainer)
        private val tvTitle: TextView = itemView.findViewById(R.id.tvFeatureTitle)
        private val tvDes: TextView = itemView.findViewById(R.id.tvFeatureDes)
        private val ivIcon: ImageView = itemView.findViewById(R.id.ivFeatureIcon)

        init {
            rlContainer.setOnClickListener{
                val position = adapterPosition
                when(dataList[position].title) {
                    activity.getString(R.string.text_to_speech) -> {
                        activity.listener.onTextToSpeechClick()
                    }
                    activity.getString(R.string.speech_to_text) -> {
                        activity.listener.onSpeechToTextClick()
                    }
                    activity.getString(R.string.conversation_history) -> {
                        activity.listener.onConversationHistoryClick()
                    }
                    activity.getString(R.string.fast_communication) -> {
                        activity.listener.onFastCommunicationClick()
                    }
                }
            }
        }

        override fun bind(data: Any?) {
            val mData = data as FeatureData
            rlContainer.setBackgroundResource(mData.background?: R.drawable.ic_launcher_background)
            tvTitle.text = mData.title
            tvDes.text = mData.description
            ivIcon.setImageResource(mData.icon?: R.drawable.ic_launcher_background)
        }
    }

    class FeatureData {
        var title: String? = null
        var description: String? = null
        var background: Int? = null
        var icon: Int? = null
    }


}
