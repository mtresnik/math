package com.resnik.math.symbo.algebra

import kotlin.math.pow

fun Pair<Double, Double>.asComplex() : ComplexNumber = ComplexNumber(this.first, this.second)

fun Double.asComplex() : ComplexNumber = ComplexNumber(this)

operator fun Double.times(other: ComplexNumber) : ComplexNumber = other * this

fun Double.pow(other: ComplexNumber) : ComplexNumber =
        ComplexNumber.realI(this, other.real) * this.pow(-1.0 * other.imaginary)