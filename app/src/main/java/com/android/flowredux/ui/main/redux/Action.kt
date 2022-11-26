package com.android.flowredux.ui.main.redux

import com.android.redux.core.Action

sealed interface MainAction:Action {
    object FetchData : MainAction
    data class OnFetchSuccess(val books: List<Book>) : MainAction
}