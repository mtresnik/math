package com.resnik.math.stats.distributions

import kotlin.math.PI
import kotlin.math.exp
import kotlin.math.pow
import kotlin.math.sqrt

class NormalDistribution(variance: Double, mean: Double) :
    ContinuousDistribution(generatePdf(variance, mean), center = mean) {

    companion object {
        private fun generatePdf(variance: Double, mean: Double): Function1<Double, Double> {
            val denom = (variance * sqrt(2.0 * PI))
            // Normal distribution for stddev & mean
            return { x: Double ->
                exp(((x - mean) / variance).pow(2.0) * (-0.5)) / denom
            }
        }
    }

}