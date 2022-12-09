package com.example.dcom.presentation.main.favorite

import android.content.Intent
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.dcom.R
import com.example.dcom.base.event.EventBusManager
import com.example.dcom.base.event.IEvent
import com.example.dcom.base.event.IEventHandler
import com.example.dcom.base.event.NoteEvent
import com.example.dcom.database.AppDatabase
import com.example.dcom.database.note.Note
import com.example.dcom.extension.IViewListener
import com.example.dcom.extension.handleUiState
import com.example.dcom.extension.hide
import com.example.dcom.extension.show
import com.example.dcom.presentation.common.BaseFragment
import com.example.dcom.presentation.main.MainActivity
import com.example.dcom.presentation.main.favorite.note.NoteActivity
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class FavoriteFragment : BaseFragment(R.layout.favorite_fragment), IEventHandler {

    private lateinit var rvContent: RecyclerView
    private lateinit var btnAdd: Button
    private lateinit var btnFastGen: Button
    private lateinit var edtSearch: EditText
    private lateinit var constRoot: ConstraintLayout

    private val favoriteAdapter by lazy { FavoriteAdapter() }
    private val viewModel by viewModels<FavoriteViewModel>()

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
                        favoriteAdapter.add(viewModel.database.iNoteDao().get(event.noteId!!))
                    }
                    NoteEvent.STATUS.EDIT -> {
                        favoriteAdapter.update(event.position!!, viewModel.database.iNoteDao().get(event.noteId!!))
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
        viewModel.database = AppDatabase.getInstance(requireContext())
    }

    override fun onInitView() {
        super.onInitView()

        setUpVariables()
        setUpOnClick()
        setUpRecyclerView()

        favoriteAdapter.addItems(viewModel.database.iNoteDao().getAll())
    }

    override fun onObserverViewModel() {
        super.onObserverViewModel()

        lifecycleScope.launchWhenCreated {
            viewModel.textToSpeechState.collect {
                handleUiState(it, object : IViewListener {
                    override fun onSuccess() {
                        Toast.makeText(requireContext(), getString(R.string.speak_success), Toast.LENGTH_SHORT).show()
                    }
                })
            }
        }
    }

    private fun setUpVariables() {
        rvContent = requireView().findViewById(R.id.rvFavoriteContent)
        btnAdd = requireView().findViewById(R.id.btnFavoriteAdd)
        edtSearch = requireView().findViewById(R.id.edtFavoriteSearch)
        btnFastGen = requireView().findViewById(R.id.btnFavoriteFastGenerate)
        constRoot = requireView().findViewById(R.id.constFavoriteRoot)
    }

    private fun setUpOnClick() {
        favoriteAdapter.listener = object: FavoriteAdapter.IListener {
            override fun onSpeak(text: String?) {
                if (text != null) {
                    viewModel.textToSpeech(text, requireContext())
                } else {
                    Toast.makeText(requireContext(), "Text is null", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onInitSelect() {
                initSelectBar()
            }

            override fun onChangeSelect(countSelected: Int) {
                changeSelectBar(countSelected)
            }

            override fun onEndSelect() {
                hideSelectBar()
            }
        }

        btnAdd.setOnClickListener {
            val intent = Intent(requireContext(), NoteActivity::class.java)
            startActivity(intent)
        }

        edtSearch.doOnTextChanged { text, start, before, count ->
            favoriteAdapter.clear()
            favoriteAdapter.addItems(viewModel.database.iNoteDao().search(text.toString()))
        }

        btnFastGen.setOnClickListener {
            fastGenerate(10)
        }
    }

    private fun getMainTopBar(): AppBarLayout? {
        return (activity as? MainActivity)?.getMainTopBar()
    }

    private fun getSelectBar(): MaterialToolbar? {
        return (activity as? MainActivity)?.getSelectBar()
    }

    private fun initSelectBar() {
        getSelectBar()?.apply {
            show()
//            getMainTopBar()?.hide()
            btnAdd.hide()
            btnFastGen.hide()
            setNavigationOnClickListener {
                hideSelectBar()
            }
            setOnMenuItemClickListener { menuItem ->
                when (menuItem.itemId) {
                    R.id.itmSelectDelete -> {
                        showConfirmDeleteDialog()
                        true
                    }
                    R.id.itmSelectChangeColor -> {
                        true
                    }
                    R.id.itmSelectEdit -> {
                        startActivity(Intent(requireContext(), NoteActivity::class.java).apply {
                            putExtra(NoteActivity.NOTE_ID, favoriteAdapter.getSelectedNoteId().first())
                            putExtra(NoteActivity.NOTE_POSITION, favoriteAdapter.getSelectedPosition().first())
                        })
                        hideSelectBar()
                        true
                    }
                    else -> false
                }
            }
        }
    }

    private fun showConfirmDeleteDialog() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(resources.getString(R.string.delete_note_title))
            .setMessage(resources.getString(R.string.delete_note_des))
            .setPositiveButton(resources.getString(R.string.delete)) { _, _ -> tempDelete() }
            .show()
    }

    private fun tempDelete() {
        val deletedNotes = favoriteAdapter.deleteSelected()
        viewModel.deleteNotes(deletedNotes)
        Snackbar.make(constRoot, getString(R.string.deleted), Snackbar.LENGTH_LONG)
            .setAction(getString(R.string.undo)) {
                favoriteAdapter.undoDeleted()
                viewModel.addNotes(deletedNotes)
            }
            .show()
    }

    private fun hideSelectBar() {
        getSelectBar()?.hide()
        btnAdd.show()
        btnFastGen.show()
        favoriteAdapter.unSelectAll()
    }

    private fun changeSelectBar(countSelect: Int) {
        getSelectBar()?.title = countSelect.toString()
    }

    private fun goToEditNoteActivity(noteId: Int?, position: Int) {
        val intent = Intent(requireContext(), NoteActivity::class.java)
        intent.putExtra(NoteActivity.NOTE_ID, noteId)
        intent.putExtra(NoteActivity.NOTE_POSITION, position)
        startActivity(intent)
    }

    private fun fastGenerate(size: Int) {
        val list = getListOfRandomNote(size)
        viewModel.database.iNoteDao().insertAll(list)
        favoriteAdapter.addItems(viewModel.database.iNoteDao().getLatestNotes(size))
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
