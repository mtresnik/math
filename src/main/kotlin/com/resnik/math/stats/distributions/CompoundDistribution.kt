package com.resnik.math.stats.distributions

import com.resnik.math.stats.normalize

class CompoundDistribution(val scaleMap: Map<Distribution<Double>, Double>) : Distribution<Double> {

    private val scales = scaleMap.values.toDoubleArray().normalize()
    private val entryList = scaleMap.entries.toList()

    override fun next(): Double {
        return entryList.indices.sumOf { index ->
            entryList[index].key.next() * scales[index]
        }
    }

}