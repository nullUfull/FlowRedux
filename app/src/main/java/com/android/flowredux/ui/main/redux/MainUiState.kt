package com.android.flowredux.ui.main.redux

import com.android.redux.core.State

sealed class MainUiState:State {
    data class HasData(val books: List<Book>) : MainUiState()
    object NoData : MainUiState() {
    }
}

data class Book(
    val name: String,
    val author: String,
    val url: String,
)
