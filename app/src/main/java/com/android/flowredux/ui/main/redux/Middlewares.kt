package com.android.flowredux.ui.main.redux

import com.android.flowredux.ui.main.repository.BookRepository
import com.android.redux.core.Middleware
import com.android.redux.core.middleware
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

fun fetchData(
    coroutineScope: CoroutineScope,
    repository: BookRepository
): Middleware<MainUiState, MainAction> = middleware { store, next, action ->
    if (action is MainAction.FetchData) {
        coroutineScope.launch {
            store.dispatch(MainAction.OnFetchSuccess(repository.fetchBooks()))
        }
    }
    next(action)
}