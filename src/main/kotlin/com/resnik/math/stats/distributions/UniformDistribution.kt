package com.resnik.math.stats.distributions

class UniformDistribution<Double>(private val value : Double) : Distribution<Double> {

    override fun next(): Double {
        return value
    }

}