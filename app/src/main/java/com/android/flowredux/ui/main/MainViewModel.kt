package com.android.flowredux.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.flowredux.ui.main.redux.MainAction
import com.android.flowredux.ui.main.redux.MainUiState
import com.android.flowredux.ui.main.redux.fetchData
import com.android.flowredux.ui.main.redux.reducer
import com.android.flowredux.ui.main.repository.BookRepository
import com.android.redux.core.applyMiddleware
import com.android.redux.core.lazyStore

class MainViewModel : ViewModel() {
    private val bookRepository by lazy { BookRepository() }
    private val store by lazyStore(
        ::reducer, MainUiState.NoData, applyMiddleware(
            fetchData(viewModelScope, bookRepository)
        )
    )
    val state = store.state

    fun dispatch(action: MainAction) {
        store.dispatch(action)
    }
}