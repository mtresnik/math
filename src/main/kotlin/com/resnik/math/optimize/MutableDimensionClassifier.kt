package com.resnik.math.optimize

class MutableDimensionClassifier(var min : Double, var max : Double) {

    fun toImmutableDimensionClassifier() : ImmutableDimensionClassifier = ImmutableDimensionClassifier(min, max)

    override fun toString(): String = "[$min, $max]"

}