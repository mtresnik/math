package com.resnik.math.symbo.algebra.operation

import com.resnik.math.symbo.algebra.ComplexNumber

class Constant(val value: ComplexNumber) : Operation() {

    constructor(value: Int): this(value.toDouble())

    constructor(value: Double) : this(ComplexNumber(value))

    constructor(real: Double, imaginary: Double) : this(ComplexNumber(real, imaginary))

    override fun toConstant(): Constant = this

    override fun toNonConstantString(): String {
        if (value == ComplexNumber.PI) {
            return "π"
        } else if (value == ComplexNumber.E) {
            return "e"
        }
        return value.toString()
    }

    override fun isConstant(): Boolean = true

    override fun generate(values: Array<Operation>): Operation = Constant(this.value)
    override fun getDerivative(respectTo: Variable): Operation {
        return ZERO
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Constant

        if (value != other.value) return false

        return true
    }

    override fun hashCode(): Int {
        return value.hashCode()
    }

    override fun toString(): String {
        return value.toString()
    }

    companion object {
        val ONE = ComplexNumber.ONE.toConstant()
        val NEGATIVE_ONE = ComplexNumber.NEGATIVE_ONE.toConstant()
        val TWO = ComplexNumber.TWO.toConstant()
        val ONE_HALF = ComplexNumber.ONE_HALF.toConstant()
        val ZERO = ComplexNumber.ZERO.toConstant()
        val TEN = ComplexNumber.TEN.toConstant()
        val I = ComplexNumber.I.toConstant()
        val PI = ComplexNumber.PI.toConstant()
        val E = ComplexNumber.E.toConstant()
        val NaN = ComplexNumber.NaN.toConstant()
        val INFINITY = ComplexNumber.INFINITY.toConstant()

        fun ComplexNumber.toConstant(): Constant = Constant(this)

        fun Double.toConstant(): Constant = ComplexNumber(this).toConstant()
    }


}