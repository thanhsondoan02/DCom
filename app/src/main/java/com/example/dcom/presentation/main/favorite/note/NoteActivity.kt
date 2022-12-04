package com.example.dcom.presentation.main.favorite.note

import android.annotation.SuppressLint
import android.app.Dialog
import android.os.Bundle
import android.view.Gravity
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import com.example.dcom.R
import com.example.dcom.base.event.EventBusManager
import com.example.dcom.base.event.NoteEvent
import com.example.dcom.database.AppDatabase
import com.example.dcom.database.note.Note
import com.example.dcom.extension.hideKeyboard
import com.example.dcom.extension.showKeyboard
import com.example.dcom.presentation.common.BaseView

class NoteActivity : AppCompatActivity(), BaseView {

    companion object {
        const val NOTE_ID = "NOTE_ID"
        const val NOTE_POSITION = "NOTE_POSITION"
    }

    private lateinit var btnBack: ImageButton
    private lateinit var edtTitle: EditText
    private lateinit var edtContent: EditText
    private lateinit var btnDelete: ImageButton

    private var state = STATE.VIEW
    private var noteId: Int = -1
    private var notePosition: Int? = null
    private lateinit var database: AppDatabase
    private var dialog: Dialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.note_activity)

        database = AppDatabase.getInstance(this)

        onPrepareInitView()
        onInitView()
    }

    override fun onPrepareInitView() {
        super.onPrepareInitView()
        noteId = intent.getIntExtra(NOTE_ID, -1)
        notePosition = intent.getIntExtra(NOTE_POSITION, -1)
    }

    override fun onInitView() {
        setUpVariables()
        setUpListener()

        if (noteId == -1) {
            edtContent.requestFocus()
            showKeyboard(window.decorView.rootView)
            setEditState()
        } else {
            database.iNoteDao().get(noteId).let {
                edtTitle.setText(it.title)
                edtContent.setText(it.content)
            }
        }
    }

    private fun setUpVariables() {
        btnBack = findViewById(R.id.btnNoteBack)
        edtTitle = findViewById(R.id.edtNoteTitle)
        edtContent = findViewById(R.id.edtNoteContent)
        btnDelete = findViewById(R.id.btnNoteDelete)
    }

    private fun setUpListener() {
        btnBack.setOnClickListener {
            onBackPressed()
        }

        edtTitle.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus && state == STATE.VIEW) {
                setEditState()
            }
        }

        edtContent.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus && state == STATE.VIEW) {
                setEditState()
            }
        }

        btnDelete.setOnClickListener {
            if (state == STATE.EDIT) {
                setViewState()
            } else {
                confirmDialog().show()
            }
        }

    }

    private fun setViewState() {
        btnDelete.setImageResource(R.drawable.ic_round_delete_24)
        edtContent.clearFocus()
        edtTitle.clearFocus()
        hideKeyboard(window.decorView.rootView)
        state = STATE.VIEW
    }

    private fun setEditState() {
        btnDelete.setImageResource(R.drawable.ic_round_done_24)
        state = STATE.EDIT
    }

    enum class STATE {
        EDIT, VIEW
    }

    private fun save() {
        if (noteId == -1) {
            addNote()
        } else {
            updateNote()
        }
    }

    private fun addNote() {
        val note = Note(edtTitle.text.toString(), edtContent.text.toString())
        database.iNoteDao().insert(note)
        noteId = database.iNoteDao().getLatestNoteId()
        EventBusManager.instance?.postPending(NoteEvent(NoteEvent.STATUS.ADD, null, noteId))
    }

    private fun updateNote() {
        database.iNoteDao().update(edtTitle.text.toString(), edtContent.text.toString(), noteId)
        EventBusManager.instance?.postPending(NoteEvent(NoteEvent.STATUS.EDIT, notePosition, noteId))
    }

    private fun deleteNote() {
        database.iNoteDao().deleteById(noteId)
        EventBusManager.instance?.postPending(NoteEvent(NoteEvent.STATUS.DELETE, notePosition, null))
    }

    @SuppressLint("InflateParams")
    private fun confirmDialog(): Dialog {
        if (dialog == null) {
            dialog = Dialog(this, R.style.DialogTheme)

            val inflateView = layoutInflater.inflate(R.layout.confirm_dialog_layout, null)

            val btnYes = inflateView.findViewById<Button>(R.id.btnConfirmDialogYes)
            btnYes.setOnClickListener {
                deleteNote()
                onBackPressed()
            }

            val btnNo = inflateView.findViewById<Button>(R.id.btnConfirmDialogNo)
            btnNo.setOnClickListener {
                dialog!!.dismiss()
            }

            dialog!!.setContentView(inflateView)

            dialog!!.window?.apply {
                setGravity(Gravity.BOTTOM)
                setLayout(
                    WindowManager.LayoutParams.MATCH_PARENT,
                    WindowManager.LayoutParams.WRAP_CONTENT
                )
            }
        }

        return dialog!!
    }
}
