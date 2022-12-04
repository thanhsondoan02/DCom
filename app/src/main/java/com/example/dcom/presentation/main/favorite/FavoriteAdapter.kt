package com.example.dcom.presentation.main.favorite

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.dcom.R
import com.example.dcom.database.note.Note
import com.example.dcom.presentation.common.recyclerview.BaseVH

class FavoriteAdapter : RecyclerView.Adapter<BaseVH>() {

    companion object {
        const val SEARCH = 0
        const val NOTE = 1
    }

    var listener: IListener? = null

    private val mData = mutableListOf<Any>()

    init {
//        mData.add(Unit)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseVH {
        return when (viewType) {
            SEARCH -> SearchVH(LayoutInflater.from(parent.context).inflate(R.layout.favorite_search_item, parent, false))
            NOTE -> NoteVH(LayoutInflater.from(parent.context).inflate(R.layout.favorite_note_item, parent, false))
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
            is Unit -> SEARCH
            is Note -> NOTE
            else -> throw IllegalArgumentException("Invalid data type")
        }
    }

    fun update(position: Int, note: Note) {
        mData[position] = note
        notifyItemChanged(position)
    }

    fun add(note: Note) {
        mData.add(note)
        notifyItemInserted(mData.size)
    }

    fun addData(data: List<Note>) {
        mData.addAll(data)
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

    inner class SearchVH(itemView: View): BaseVH(itemView) {

        init {
        }

        override fun bind(data: Any?) {
        }
    }

    inner class NoteVH(itemView: View): BaseVH(itemView) {

        private val tvTitle: TextView = itemView.findViewById(R.id.tvFavoriteNoteTitle)
        private val tvContent: TextView = itemView.findViewById(R.id.tvFavoriteNoteContent)

        init {
            itemView.setOnClickListener {
                listener?.onNoteClick((mData[adapterPosition] as? Note)?.id, adapterPosition)
            }
        }

        override fun bind(data: Any?) {
            data as Note
            tvTitle.text = data.title
            tvContent.text = data.content
        }
    }

    interface IListener {
        fun onNoteClick(noteId: Int?, position: Int)
    }

}
