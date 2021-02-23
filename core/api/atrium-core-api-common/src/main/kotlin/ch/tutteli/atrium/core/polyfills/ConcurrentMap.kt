package ch.tutteli.atrium.core.polyfills

import ch.tutteli.atrium.core.polyfills.impl.ConcurrentMapImpl
import ch.tutteli.atrium.core.polyfills.impl.MutableConcurrentMapImpl

internal interface ConcurrentMap<K, out V : Any> {
    operator fun get(key: K): V?

    companion object {
        operator fun <K, V: Any> invoke(source: Map<K, V>): ConcurrentMap<K, V> =
            ConcurrentMapImpl(source)
    }
}

internal interface MutableConcurrentMap<K, V : Any> : ConcurrentMap<K, V> {

    /**
     * Atomic if possible on the corresponding platform
     */
    fun putIfAbsent(key: K, value: V): V

    companion object {
        operator fun <K, V: Any> invoke(): MutableConcurrentMap<K, V> =
            MutableConcurrentMapImpl()
    }
}

