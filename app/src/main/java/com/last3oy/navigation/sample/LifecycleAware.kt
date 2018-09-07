package com.last3oy.navigation.sample

import android.view.View
import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import androidx.lifecycle.GenericLifecycleObserver
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import kotlin.reflect.KProperty

/**
 * Copy from Lazy.kt
 */
interface LifecycleAwareFindView<out T : View> {

    val value: T

    fun isInitialized(): Boolean
}

fun <T> Fragment.findView(@IdRes viewId: Int): LifecycleAwareFindView<T>
        where T : View = FindFragmentViewImpl(this, viewId)

private object UNINITIALIZED_VALUE

private class FindFragmentViewImpl<out T, out U>(fragment: U, val viewId: Int)
    : LifecycleAwareFindView<T>, GenericLifecycleObserver where T : View, U : Fragment, U : LifecycleOwner {

    override fun onStateChanged(source: LifecycleOwner?, event: Lifecycle.Event?) {
        when (event) {
            Lifecycle.Event.ON_STOP -> _value = UNINITIALIZED_VALUE
            Lifecycle.Event.ON_DESTROY -> if (fragment?.retainInstance == false) fragment = null
            else -> return
        }
    }

    init {
        fragment.lifecycle.addObserver(this)
    }

    private var fragment: U? = fragment
    private var _value: Any? = UNINITIALIZED_VALUE

    override val value: T
        get() {
            if (_value === UNINITIALIZED_VALUE) {
                _value = fragment!!.view!!.findViewById(viewId)
            }

            @Suppress("UNCHECKED_CAST")
            return _value as T
        }

    override fun isInitialized(): Boolean = _value !== UNINITIALIZED_VALUE

    override fun toString(): String = if (isInitialized()) value.toString() else "LifecycleAwareLazy value not initialized yet."
}

/**
 * extension for delegate.
 */
operator fun <T : View> LifecycleAwareFindView<T>.getValue(thisRef: Any?, property: KProperty<*>): T = value