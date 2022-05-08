package com.resnik.math.optimize

class ImmutableDimensionClassifier(val min: Double, val max: Double) {

    fun random(): Double = scaled(Math.random())

    fun scaled(t: Double) = (max - min) * t + min

    fun coerceIn(input: Double) = input.coerceIn(min, max)

    fun toMutableDimensionClassifier(): MutableDimensionClassifier = MutableDimensionClassifier(min, max)

    companion object {
        val STANDARD = ImmutableDimensionClassifier(-10.0, 10.0)
    }

}