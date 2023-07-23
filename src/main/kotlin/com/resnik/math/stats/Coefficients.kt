package com.resnik.math.stats

import kotlin.math.pow
import kotlin.math.sqrt

object Coefficients {


    fun pearsonCorrelation(x: DoubleArray, y: DoubleArray): Double {
        return pearsonCorrelation(x, x.mean(), y, y.mean())
    }

    fun pearsonCorrelation(x: DoubleArray, xBar: Double, y: DoubleArray, yBar: Double): Double {
        val numerator = x.indices.sumOf { index -> (x[index] - xBar) * (y[index] - yBar) }
        val denominator = sqrt(x.indices.sumOf { index -> (x[index] - xBar).pow(2) }) * sqrt(x.indices.sumOf { index -> (y[index] - yBar).pow(2) })
        return numerator / denominator
    }

}