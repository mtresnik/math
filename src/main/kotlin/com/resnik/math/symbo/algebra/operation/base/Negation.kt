package com.resnik.math.symbo.algebra.operation.base

import com.resnik.math.symbo.algebra.operation.Constant
import com.resnik.math.symbo.algebra.operation.Operation

class Negation(val inner: Operation) : Operation(inner) {

    override fun toConstant(): Constant {
        if (!isConstant()) {
            return Constant.NaN
        }
        return Constant(inner.toConstant().value * -1.0)
    }

    override fun toNonConstantString(): String = "(-1.0)*$inner"

    override fun generate(values: Array<Operation>): Operation {
        if (values.isEmpty()) {
            return Negation(Constant.ONE)
        }
        return Negation(values.first())
    }


}