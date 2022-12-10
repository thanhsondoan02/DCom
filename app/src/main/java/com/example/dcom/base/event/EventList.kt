package com.example.dcom.base.event

class NoteEvent(val status: STATUS, val position: Int?, val noteId: Int?) : IEvent {
    enum class STATUS {
        ADD, EDIT, DELETE
    }
}

class ConversationEvent(val status: STATUS, val position: Int, val id: Int) : IEvent {
    enum class STATUS {
        ADD, EDIT, DELETE
    }
}

class DeleteDatabaseEvent(val isFavorite: Boolean) : IEvent {}
