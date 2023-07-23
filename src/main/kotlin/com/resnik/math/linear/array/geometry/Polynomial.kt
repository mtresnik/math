package com.resnik.math.linear.array.geometry

import kotlin.math.pow

class Polynomial(vararg val coefficients: Double) : Function1<Double, Double> {

    override fun invoke(p1: Double): Double {
        return (0 until this.coefficients.size).sumOf { power ->
            val coefficient = this.coefficients[power]
            coefficient * p1.pow(power)
        }
    }

    override fun toString(): String {
        return "Polynomial(coefficients=${coefficients.contentToString()})"
    }


}