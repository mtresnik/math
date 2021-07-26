package com.resnik.math.symbo.algebra.operation.base

import com.resnik.math.symbo.algebra.ComplexNumber
import com.resnik.math.symbo.algebra.operation.Constant
import com.resnik.math.symbo.algebra.operation.Operation

class Power(values : Array<Operation>) : Operation(*values){

    val base : Operation = values[0]
    val exponent : Operation = values[1]

    constructor(base : Operation, exponent : Operation) : this(mutableListOf<Operation>(base, exponent).toTypedArray())

    override fun isConstant(): Boolean {
        if(exponent.isConstant() && exponent.toConstant() == Constant.ZERO){
            return true
        }
        return base.isConstant() && exponent.isConstant()
    }

    override fun toConstant(): Constant {
        if (!isConstant()) {
            return Constant.NaN
        }
        if (exponent.toConstant() == Constant.ZERO) {
            return Constant.ONE
        }
        return Constant(base.toConstant().value.pow(exponent.toConstant().value))
    }

    override fun toNonConstantString(): String {
        if (exponent is Constant || exponent.isConstant()) {
            val exponentConstant: Constant = exponent.toConstant()
            if (exponentConstant.value == ComplexNumber.ONE) {
                return base.toString()
            }
            if (exponentConstant.value == ComplexNumber.ZERO) {
                return Constant.ONE.toString()
            }
        }
        return "($base)^($exponent)"
    }

    override fun generate(values: Array<Operation>): Operation = Power(values)

}