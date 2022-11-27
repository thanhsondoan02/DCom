package com.example.dcom.presentation.main.favorite

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

    private val mData = mutableListOf<Note>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseVH {
        return when (viewType) {
            SEARCH -> SearchVH(LayoutInflater.from(parent.context).inflate(R.layout.favorite_search_item, parent, false))
            NOTE -> NoteVH(LayoutInflater.from(parent.context).inflate(R.layout.favorite_note_item, parent, false))
            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun onBindViewHolder(holder: BaseVH, position: Int) {
        if (getItemViewType(position) == NOTE){
            holder.bind(mData[position-1])
        } else {
            holder.bind()
        }
    }

    override fun getItemCount(): Int {
        return mData.size + 1
    }

    override fun getItemViewType(position: Int): Int {
        return when (position) {
            0 -> SEARCH
            else -> NOTE
        }
    }

    fun update(position: Int, note: Note) {
        mData[position-1] = note
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
                listener?.onNoteClick(mData[adapterPosition-1].id, adapterPosition)
            }
        }

        override fun bind(data: Any?) {
            data as Note
            tvTitle.text = data.title
            tvContent.text = data.content
        }
    }

//    class Note {
//        var id: String? = null
//        var title: String? = null
//        var content: String? = null
//        var lastEdit: String? = null
//    }

    interface IListener {
        fun onNoteClick(noteId: Int, position: Int)
    }

}
