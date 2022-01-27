package com.resnik.math.stats.distributions

import com.resnik.math.stats.normalize
import com.resnik.math.util.CountList
import java.lang.IllegalStateException

open class DiscreteDistribution<T>(valueMap : Map<T, Double>) : Distribution<T> {

    constructor(countList: CountList<T>) : this(countList.toMap().mapValues { it.value.toDouble() })

    private val keys = valueMap.keys.toList()
    private val values = valueMap.values.toDoubleArray().normalize()
    private val cdf = DoubleArray(values.size) { 0.0 }

    init {
        if(values.any { it < 0.0 })
            throw IllegalArgumentException("All values must be non-negative.")
        var valueSum = 0.0
        values.forEachIndexed { index, value ->
            cdf[index] = value + valueSum
            valueSum += value
        }
    }

    override fun next(): T {
        val random = Math.random()
        if(random < cdf.first())
            return keys.first()
        val index = keys.indices.firstOrNull { cdf[it] > random } ?: throw IllegalStateException("Cannot be null")
        return keys[index]
    }

    companion object {

        fun DoubleArray.toDiscreteDistribution() : DiscreteDistribution<Int> {
            val map = mutableMapOf<Int, Double>()
            this.forEachIndexed{index, value ->
                map[index] = value
            }
            return DiscreteDistribution(map)
        }

    }
}