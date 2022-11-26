package com.android.flowredux.ui.main.repository

import com.android.flowredux.ui.main.redux.Book
import kotlinx.coroutines.delay

class BookRepository {
    suspend fun fetchBooks(): List<Book> {
        delay(2000)
        return listOf(
            Book("杀死一只知更鸟", "周杰伦", ""),
            Book("杀死一只知更鸟", "周杰伦", ""),
            Book("杀死一只知更鸟", "周杰伦", ""),
            Book("杀死一只知更鸟", "周杰伦", ""),
            )
    }
}