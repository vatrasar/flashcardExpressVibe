package com.example.flashcardexpress.core.domain.error

sealed class FlashcardAppError: Throwable() {
    class NameTakenError: FlashcardAppError()
}