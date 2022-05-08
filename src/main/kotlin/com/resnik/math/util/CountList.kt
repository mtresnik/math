package com.resnik.math.util

import java.util.*

open class CountList<T> : Iterable<Map.Entry<T, Int>> {

    private val internalMap = TreeMap<T, Int>()

    operator fun get(obj: T): Int = internalMap.getOrDefault(obj, 0)

    fun addAll(collection: Collection<T>) {
        collection.forEach { this.add(it) }
    }

    fun add(obj: T): CountList<T> {
        internalMap[obj] = get(obj) + 1
        return this
    }

    operator fun plus(obj: T): CountList<T> = add(obj)

    operator fun contains(obj: T): Boolean = internalMap.contains(obj)

    fun Collection<T>.toCountList(): CountList<T> {
        val ret = CountList<T>()
        ret.addAll(this)
        return ret
    }

    fun toMap(): Map<T, Int> = TreeMap(internalMap)

    override fun toString(): String {
        return internalMap.toString()
    }

    override fun iterator(): Iterator<Map.Entry<T, Int>> {
        return internalMap.toMap().iterator()
    }

    fun max(): Int = internalMap.maxByOrNull { it.value }?.value ?: -1


}