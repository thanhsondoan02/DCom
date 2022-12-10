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
import com.google.android.material.card.MaterialCardView

class FavoriteAdapter : RecyclerView.Adapter<BaseVH>() {

    companion object {
        const val SEARCH = 0
        const val NOTE = 1
        const val SELECT_PAYLOAD = "SELECT_PAYLOAD"
    }

    var listener: IListener? = null

    private val mData = mutableListOf<Any>()
    private var tempDelete = mutableListOf<Pair<NoteDisplay, Int>>()
    private var state = STATE.NORMAL
    private var countSelected: Int = 0

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
            is NoteDisplay -> NOTE
            else -> throw IllegalArgumentException("Invalid data type")
        }
    }

    fun selectAll() {
        mData.filterIsInstance<NoteDisplay>().forEach { it.isSelected = true }
        countSelected = mData.filterIsInstance<NoteDisplay>().size
        notifyItemRangeChanged(0, itemCount, SELECT_PAYLOAD)
        listener?.onChangeSelect(countSelected)
    }

    fun getSelectedNoteId() = mData.filterIsInstance<NoteDisplay>().filter { it.isSelected }.map { it.note.id }

    fun getSelectedPosition() = mData.filterIsInstance<NoteDisplay>().filter { it.isSelected }.map { mData.indexOf(it) }

    fun deleteSelected(): List<Note> {
        tempDelete.clear()
        val deletedNotes: MutableList<Note> = mutableListOf()
        for (i in mData.size - 1 downTo 0) {
            (mData[i] as? NoteDisplay)?.let {
                if (it.isSelected) {
                    deletedNotes.add(it.note)
                    remove(i)
                    tempDelete.add(Pair(it, i))
                }
            }
        }
        state = STATE.NORMAL
        listener?.onEndSelect()
        return deletedNotes
    }

    fun undoDeleted() {
        for (i in tempDelete.size - 1 downTo 0) {
            add(tempDelete[i].first.note, tempDelete[i].second)
        }
        tempDelete.clear()
    }

    fun unSelectAll() {
        mData.forEach {
            if (it is NoteDisplay) {
                it.isSelected = false
            }
        }
        countSelected = 0
        notifyItemRangeChanged(0, itemCount, SELECT_PAYLOAD)
        state = STATE.NORMAL
    }

    fun selectNone() {
        mData.forEach {
            if (it is NoteDisplay) {
                it.isSelected = false
            }
        }
        countSelected = 0
        notifyItemRangeChanged(0, itemCount, SELECT_PAYLOAD)
        listener?.onChangeSelect(countSelected)
    }

    fun update(position: Int, note: Note) {
        (mData[position] as NoteDisplay).note = note
        notifyItemChanged(position)
    }

    fun add(note: Note) {
        mData.add(NoteDisplay(note))
        notifyItemInserted(mData.size)
    }

    fun add(note: Note, position: Int) {
        mData.add(position, NoteDisplay(note))
        notifyItemInserted(position)
    }

    fun addItems(data: List<Note>) {
        data.forEach {
            mData.add(NoteDisplay(it))
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

    inner class SearchVH(itemView: View): BaseVH(itemView) {

        init {
        }

        override fun bind(data: Any?) {
        }
    }

    inner class NoteVH(itemView: View): BaseVH(itemView) {

        private val tvTitle: TextView = itemView.findViewById(R.id.tvFavoriteNoteTitle)
        private val tvContent: TextView = itemView.findViewById(R.id.tvFavoriteNoteContent)
        private val mtcNoteCard: MaterialCardView = itemView.findViewById(R.id.mtcFavoriteNoteCard)

        init {
            mtcNoteCard.setOnClickListener {
                when (state) {
                    STATE.NORMAL -> {
                        onSpeak()
                    }
                    STATE.SELECTING -> {
                        onSelect()
                    }
                }
            }

            mtcNoteCard.setOnLongClickListener {
                onSelect()
            }
        }

        override fun bind(data: Any?) {
            data as NoteDisplay
            tvTitle.text = data.note.title
            tvContent.text = data.note.content
            setSelectStatus(data.isSelected)
        }

        override fun bind(data: Any?, payloads: List<Any>) {
            if (payloads.contains(SELECT_PAYLOAD)) {
                setSelectStatus((data as NoteDisplay).isSelected)
            }
        }

        private fun setSelectStatus(isSelected: Boolean) {
            mtcNoteCard.isChecked = isSelected
        }

        private fun onSpeak() {
            listener?.onSpeak((mData[adapterPosition] as? NoteDisplay)?.note?.content)
        }

        private fun onSelect(): Boolean {
            mtcNoteCard.isChecked = !mtcNoteCard.isChecked
            if (mtcNoteCard.isChecked) {
                if (countSelected == 0) {
                    state = STATE.SELECTING
                    listener?.onInitSelect()
                }
                countSelected++
                (mData[adapterPosition] as? NoteDisplay)?.isSelected = true
            } else {
                countSelected--
                if (countSelected == 0) {
                    state = STATE.NORMAL
                    listener?.onEndSelect()
                }
                (mData[adapterPosition] as? NoteDisplay)?.isSelected = false
            }
            listener?.onChangeSelect(countSelected)
            return true
        }
    }

    class NoteDisplay(var note: Note, var isSelected: Boolean = false) {}

    enum class STATE {
        NORMAL, SELECTING
    }

    interface IListener {
        fun onSpeak(text: String?)
        fun onInitSelect()
        fun onChangeSelect(countSelected: Int)
        fun onEndSelect()
    }

}
