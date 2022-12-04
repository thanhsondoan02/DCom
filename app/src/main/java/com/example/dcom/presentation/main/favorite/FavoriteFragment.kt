package com.example.dcom.presentation.main.favorite

import android.content.Intent
import android.widget.EditText
import android.widget.ImageView
import androidx.core.widget.doOnTextChanged
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.dcom.R
import com.example.dcom.base.event.EventBusManager
import com.example.dcom.base.event.IEvent
import com.example.dcom.base.event.IEventHandler
import com.example.dcom.base.event.NoteEvent
import com.example.dcom.database.AppDatabase
import com.example.dcom.database.note.Note
import com.example.dcom.presentation.common.BaseFragment
import com.example.dcom.presentation.main.favorite.note.NoteActivity
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class FavoriteFragment : BaseFragment(R.layout.favorite_fragment), IEventHandler {

    private lateinit var rvContent: RecyclerView
    private lateinit var btnAdd: ImageView
    private lateinit var btnFastGen: ImageView
    private lateinit var edtSearch: EditText

    private val favoriteAdapter by lazy { FavoriteAdapter() }
    private lateinit var database: AppDatabase

    override fun onStart() {
        super.onStart()
        EventBusManager.instance?.register(this)
    }

    override fun onStop() {
        super.onStop()
        EventBusManager.instance?.unregister(this)
    }

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    override fun onEvent(event: IEvent) {
        when (event) {
            is NoteEvent -> {
                when (event.status) {
                    NoteEvent.STATUS.ADD -> {
                        favoriteAdapter.add(database.iNoteDao().get(event.noteId!!))
                    }
                    NoteEvent.STATUS.EDIT -> {
                        favoriteAdapter.update(event.position!!, database.iNoteDao().get(event.noteId!!))
                    }
                    NoteEvent.STATUS.DELETE -> {
                        favoriteAdapter.remove(event.position!!)
                    }
                }
                EventBusManager.instance?.removeSticky(event)
            }
        }
    }

    override fun onPrepareInitView() {
        super.onPrepareInitView()
        database = AppDatabase.getInstance(requireContext())
    }

    override fun onInitView() {
        super.onInitView()

        setUpVariables()
        setUpOnClick()
        setUpRecyclerView()

        favoriteAdapter.addData(database.iNoteDao().getAll())
    }

    private fun setUpVariables() {
        rvContent = requireView().findViewById(R.id.rvFavoriteContent)
        btnAdd = requireView().findViewById(R.id.btnFavoriteAdd)
        edtSearch = requireView().findViewById(R.id.edtFavoriteSearch)
        btnFastGen = requireView().findViewById(R.id.btnFavoriteFastGenerate)
    }

    private fun setUpOnClick() {
        favoriteAdapter.listener = object: FavoriteAdapter.IListener {
            override fun onNoteClick(noteId: Int?, position: Int) {
                val intent = Intent(requireContext(), NoteActivity::class.java)
                intent.putExtra(NoteActivity.NOTE_ID, noteId)
                intent.putExtra(NoteActivity.NOTE_POSITION, position)
                startActivity(intent)
            }
        }

        btnAdd.setOnClickListener {
            val intent = Intent(requireContext(), NoteActivity::class.java)
            startActivity(intent)
        }

        edtSearch.doOnTextChanged { text, start, before, count ->
            favoriteAdapter.clear()
            favoriteAdapter.addData(database.iNoteDao().search(text.toString()))
        }

        btnFastGen.setOnClickListener {
            fastGenerate(10)
        }
    }

    private fun fastGenerate(size: Int) {
        val list = getListOfRandomNote(size)
        database.iNoteDao().insertAll(list)
        favoriteAdapter.addData(database.iNoteDao().getLatestNotes(size))
    }

    private fun getListOfRandomNote(size: Int): List<Note> {
        val list: MutableList<Note> = mutableListOf()
        for (i in 0 until size) {
            list.add(
                Note(
                    getRandomTitle(5, 40),
                    getRandomTitle(20, 100)
                )
            )
        }
        return list
    }

    private fun getRandomTitle(lengthMin: Int, lengthMax: Int): String {
        val length = (lengthMin..lengthMax).random()
        val chars = ('a'..'z') + ('A'..'Z') + ('0'..'9') + ' '
        return (1..length)
            .map { chars.random() }
            .joinToString("")
    }

    private fun setUpRecyclerView() {
//        rvContent.layoutManager = object: CustomGridLayoutManager() {
//            override fun getMaxItemHorizontalByViewType(viewType: Int): Int {
//                return when (viewType) {
//                    FavoriteAdapter.SEARCH -> 1
//                    FavoriteAdapter.NOTE -> 2
//                    else -> throw IllegalArgumentException("Invalid view type")
//                }
//            }
//
//            override fun getItemViewType(adapterPosition: Int): Int {
//                return favoriteAdapter.getItemViewType(adapterPosition)
//            }
//        }.getGridLayoutManager(requireContext())

        rvContent.layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)

        rvContent.adapter = favoriteAdapter
    }

}
