package com.resnik.math.stats

import kotlin.math.pow

fun Double.sqrt() : Double = this.pow(0.5)

fun DoubleArray.variance() : Double {
    val mean = this.average()
    return this.sumByDouble { (it - mean).pow(2) } / (this.size - 1)
}

fun DoubleArray.mean() : Double = this.average()

fun DoubleArray.stddev() : Double = this.variance().sqrt()

fun DoubleArray.covariance(other: DoubleArray) : Double {
    val n = this.size
    if(n <= 1)
        return 0.0
    val xMean = this.mean()
    val yMean = other.mean()
    val sum = this.indices.sumByDouble { (this[it] - xMean) * (other[it] - yMean) }
    return sum / (n - 1)
}

fun DoubleArray.correlation(other: DoubleArray) : Double = this.covariance(other) /
        kotlin.math.sqrt(this.variance() * other.variance())

fun DoubleArray.apply(func : Function1<Double, Double>) : DoubleArray = DoubleArray(this.size) {func(this[it])}

fun DoubleArray.normalize() : DoubleArray {
    val sum = this.sum()
    return this.apply { it / sum }
}