package com.resnik.math.symbo.algebra.operation.functions

import com.resnik.math.symbo.algebra.operation.Constant
import com.resnik.math.symbo.algebra.operation.Operation

class Log(val base: Operation, val inner: Operation) : Operation(base, inner) {

    override fun toConstant(): Constant {
        if (!isConstant()) {
            return Constant.NaN
        }
        val numerator = inner.toConstant().value.ln()
        val denominator = base.toConstant().value.ln()
        return Constant(numerator / denominator)
    }

    override fun toNonConstantString(): String = "log($base, $inner)"

    override fun generate(values: Array<Operation>): Operation {
        when (values.size) {
            0 -> {
                return Log(Constant.E, Constant.E)
            }
            1 -> {
                return Log(values.first(), values.first())
            }
            else -> {
                return Log(values.first(), values.second())
            }
        }
    }

    companion object {

        @JvmStatic
        fun ln(inner: Operation): Log = Log(Constant.E, inner)

    }

}
