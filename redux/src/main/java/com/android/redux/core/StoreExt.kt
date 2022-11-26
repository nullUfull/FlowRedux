package com.android.redux.core

class LazyCoroutineStore<S : State, A : Action>(
    private val reducer: Reducer<S, A>,
    private val initState: S,
    private val enhancer: StoreEnhancer<S, A>? = null,
) : Lazy<Store<S, A>> {
    private var _value: Store<S, A>? = null

    override val value: Store<S, A>
        get() = _value ?: createStore(reducer, initState, enhancer).also {
            _value = it
        }

    override fun isInitialized() = _value != null
}

inline fun <reified S : State, A : Action> lazyStore(
    noinline reducer: Reducer<S, A>,
    initState: S,
    noinline enhancer: StoreEnhancer<S, A>? = null
) = LazyCoroutineStore(reducer, initState, enhancer)