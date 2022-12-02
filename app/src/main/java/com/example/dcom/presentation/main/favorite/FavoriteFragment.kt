package com.example.dcom.presentation.main.favorite

import android.content.Intent
import android.widget.ImageButton
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.dcom.R
import com.example.dcom.base.event.EventBusManager
import com.example.dcom.base.event.IEvent
import com.example.dcom.base.event.IEventHandler
import com.example.dcom.base.event.NoteEvent
import com.example.dcom.database.AppDatabase
import com.example.dcom.presentation.common.BaseFragment
import com.example.dcom.presentation.main.favorite.note.NoteActivity
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class FavoriteFragment : BaseFragment(R.layout.favorite_fragment), IEventHandler {

    private lateinit var rvContent: RecyclerView
    private lateinit var btnAdd: ImageButton

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
