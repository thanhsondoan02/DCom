package com.example.dcom.presentation.main.communication

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.dcom.R
import com.example.dcom.database.note.Note
import com.example.dcom.presentation.common.recyclerview.BaseVH
import com.google.android.material.card.MaterialCardView

class SuggestingAdapter : RecyclerView.Adapter<BaseVH>() {

    companion object {
        const val NOTE = 1
        const val SELECT_PAYLOAD = "SELECT_PAYLOAD"
    }

    var listener: IListener? = null

    private val mData = mutableListOf<Note>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseVH {
        return when (viewType) {
            NOTE -> NoteVH(LayoutInflater.from(parent.context).inflate(R.layout.suggest_item, parent, false))
            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun onBindViewHolder(holder: BaseVH, position: Int) {
        holder.bind(getDataAtPosition(position))
    }

    override fun getItemCount(): Int {
        return mData.size
    }

    override fun getItemViewType(position: Int): Int {
        return NOTE
    }

    fun update(position: Int, note: Note) {
        mData[position] = note
        notifyItemChanged(position)
    }

    fun add(note: Note) {
        mData.add(note)
        notifyItemInserted(mData.size)
    }

    fun add(note: Note, position: Int) {
        mData.add(position, note)
        notifyItemInserted(position)
    }

    fun addItems(data: List<Note>) {
        data.forEach {
            mData.add(it)
        }
        notifyItemRangeInserted(mData.size, data.size)
    }

    fun remove(position: Int) {
        mData.removeAt(position)
        notifyItemRemoved(position)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun clear() {
        mData.clear()
        notifyDataSetChanged()
    }

    private fun getDataAtPosition(position: Int) = mData[position]

    inner class NoteVH(itemView: View): BaseVH(itemView) {

        private val tvTitle: TextView = itemView.findViewById(R.id.tvSuggestTitle)
        private val tvContent: TextView = itemView.findViewById(R.id.tvSuggestContent)
        private val mtcNoteCard: MaterialCardView = itemView.findViewById(R.id.mtcSuggestCard)

        init {
            mtcNoteCard.setOnClickListener {
                listener?.onNoteClick(getDataAtPosition(adapterPosition).content)
            }
        }

        override fun bind(data: Any?) {
            data as Note
            tvTitle.text = data.title
            tvContent.text = data.content
        }
    }

    interface IListener {
        fun onNoteClick(text: String)
    }

}
