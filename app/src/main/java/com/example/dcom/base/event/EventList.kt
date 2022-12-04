package com.example.dcom.base.event

class NoteEvent(val status: STATUS, val position: Int?, val noteId: Int?) : IEvent {
    enum class STATUS {
        ADD, EDIT, DELETE
    }
}