package com.resnik.math.symbo.algebra

import kotlin.math.*

class ComplexNumber(real: Double = 0.0, imaginary: Double = 0.0) : Algebraic<ComplexNumber> {

    var real: Double = 0.0
    var imaginary: Double = 0.0

    init {
        this.real = if (real.absoluteValue < PRECISION) 0.0 else real
        this.imaginary = if (imaginary.absoluteValue < PRECISION) 0.0 else imaginary
    }

    fun congugate(): ComplexNumber = ComplexNumber(this.real, this.imaginary * -1)

    fun rSquared(): Double = (this * congugate()).real

    fun r(): Double = sqrt(this.rSquared())

    override fun plus(other: ComplexNumber): ComplexNumber =
        ComplexNumber(this.real + other.real, this.imaginary + other.imaginary)

    override fun minus(other: ComplexNumber): ComplexNumber =
        ComplexNumber(this.real - other.real, this.imaginary - other.imaginary)

    operator fun times(other: Double): ComplexNumber =
        ComplexNumber(this.real * other, this.imaginary * other)

    override fun times(other: ComplexNumber): ComplexNumber =
        ComplexNumber(
            this.real * other.real - this.imaginary * other.imaginary,
            this.real * other.imaginary + this.imaginary * other.real
        )

    override fun div(other: ComplexNumber): ComplexNumber = (this * other.congugate()) / other.rSquared()

    operator fun div(other: Double): ComplexNumber = this * (1.0 / other)

    override fun pow(other: ComplexNumber): ComplexNumber {
        if (this == ZERO && other == ZERO) {
            return ONE
        }
        if (this == ZERO)
            return ZERO
        return exp(this.ln() * other)
    }

    override fun sqrt(): ComplexNumber = this.pow(ComplexNumber(0.5))

    fun ln(): ComplexNumber = ComplexNumber(kotlin.math.ln(r()), theta())

    fun theta(): Double {
        val r = this.r()
        if (r == 0.0)
            return 0.0

        if (real == r && imaginary == 0.0) {
            return 0.0
        }
        if (real == 0.0 && imaginary == r) {
            return Math.PI / 2
        }
        if (real == -1.0 * r && imaginary == 0.0) {
            return Math.PI
        }
        return if (real == 0.0 && imaginary == -1.0 * r) {
            3 * Math.PI / 2
        } else atan(imaginary / real)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ComplexNumber

        if (real != other.real) return false
        if (imaginary != other.imaginary) return false

        return true
    }

    override fun hashCode(): Int {
        var result = real.hashCode()
        result = 31 * result + imaginary.hashCode()
        return result
    }

    override fun toString(): String {
        if (this == ZERO)
            return 0.0.toString()
        if (real == 0.0)
            return "${imaginary}i"
        if (imaginary == 0.0)
            return real.toString()
        if (this == NaN)
            return "NaN"
        return "$real ${if (imaginary >= 0) "+" else "-"} ${imaginary.absoluteValue}i"
    }


    companion object {
        val PRECISION = "1.0E-12".toDouble()

        fun cos(other: ComplexNumber): ComplexNumber = (exp(other * I) + exp(other * I * -1.0)) / 2.0

        fun sin(z: ComplexNumber): ComplexNumber = (exp(z.times(I)) - exp(z.times(I) * (-1.0))) * (-0.5) * (I)

        fun arctan(other: ComplexNumber): ComplexNumber = (I / 2.0) * (((I + other) / (I - other)).ln())

        fun exp(other: ComplexNumber): ComplexNumber = expI(other.imaginary) * kotlin.math.exp(other.real)

        fun expI(theta: Double): ComplexNumber = ComplexNumber(cos(theta), sin(theta))

        fun realI(real: Double, theta: Double): ComplexNumber {
            if (real == 0.0) {
                if (theta == 0.0) {
                    return ONE
                } else {
                    return ZERO
                }
            }
            val complexLog = ComplexNumber(real).ln()
            return expI(complexLog.real) * kotlin.math.exp(-1.0 * complexLog.imaginary)
        }

        val ONE = ComplexNumber(1.0)
        val NEGATIVE_ONE = ComplexNumber(-1.0)
        val TWO = ComplexNumber(2.0)
        val ONE_HALF = ComplexNumber(0.5)
        val ZERO = ComplexNumber(0.0)
        val TEN = ComplexNumber(10.0)
        val I = ComplexNumber(imaginary = 1.0)
        val PI = ComplexNumber(Math.PI)
        val E = ComplexNumber(Math.E)
        val NaN = ComplexNumber(Double.NaN, Double.NaN)
        val INFINITY = ComplexNumber(Double.POSITIVE_INFINITY)
    }

}