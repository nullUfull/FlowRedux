package com.android.flowredux.ui.main.redux

fun reducer(state: MainUiState, action: MainAction): MainUiState {
    return when (action) {
        is MainAction.OnFetchSuccess -> onFetchSuccessReducer(state, action)
        else -> state
    }
}

private val onFetchSuccessReducer = { state: MainUiState, action: MainAction.OnFetchSuccess ->
    when (state) {
        is MainUiState.HasData -> {
            state.copy(books = action.books)
        }
        is MainUiState.NoData -> {
            MainUiState.HasData(books = action.books)
        }
    }
}