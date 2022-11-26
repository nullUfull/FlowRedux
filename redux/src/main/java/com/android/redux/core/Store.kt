package com.android.redux.core

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

interface Action

interface State

typealias Reducer<State, Action> = (preState: State, action: Action) -> State

typealias Dispatcher<Action> = (Action) -> Any

typealias StoreCreator<State, Action> = (
    reducer: Reducer<State, Action>,
    initialState: State,
    enhancer: Any?
) -> Store<State, Action>

typealias StoreEnhancer<State, Action> = (StoreCreator<State, Action>) -> StoreCreator<State, Action>

typealias Middleware<State, Action> = (store: Store<State, Action>) -> (next: Dispatcher<Action>) -> Dispatcher<Action>

interface Store<S : State, A : Action> {

    val state: StateFlow<S>

    val dispatch: Dispatcher<A>
}

internal class InternalStore<S : State, A : Action>(reducer: Reducer<S, A>, initialState: S) : Store<S, A> {

    private val _state = MutableStateFlow(initialState)
    override val state = _state.asStateFlow()

    override val dispatch: Dispatcher<A> = { action ->
        _state.update { reducer(it, action) }
        action
    }
}

inline fun <reified S : State, A : Action> middleware(
    crossinline dispatch: (store: Store<S, A>, next: Dispatcher<A>, action: A) -> Any
): Middleware<S, A> =
    { store ->
        { next ->
            { action ->
                dispatch(store, next, action)
            }
        }
    }

inline fun <reified S : State, A : Action> combineReducers(vararg reducers: Reducer<S, A>): Reducer<S, A> =
    { state, action ->
        reducers.fold(state) { s, reducer -> reducer(s, action) }
    }

inline fun <reified S : State, A : Action> applyMiddleware(vararg middlewares: Middleware<S, A>): StoreEnhancer<S, A> =
    { storeCreator ->
        { reducer, initialState, enhancer ->
            val store = storeCreator(reducer, initialState, enhancer)

            object : Store<S, A> {
                override val state = store.state
                override val dispatch =
                    middlewares.foldRight(store.dispatch) { middleware, next ->
                        middleware(this)(next)
                    }
            }
        }
    }

fun <S : State, A : Action> createStore(
    reducer: Reducer<S, A>,
    initState: S,
    enhancer: StoreEnhancer<S, A>? = null
): Store<S, A> = if (enhancer != null) {
    enhancer { r, initialState, _ -> createStore(r, initialState) }(reducer, initState, null)
} else {
    InternalStore(reducer, initState)
}