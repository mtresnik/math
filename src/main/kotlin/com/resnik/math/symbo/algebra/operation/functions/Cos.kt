package com.resnik.math.symbo.algebra.operation.functions

import com.resnik.math.symbo.algebra.ComplexNumber
import com.resnik.math.symbo.algebra.operation.Constant
import com.resnik.math.symbo.algebra.operation.Operation
import com.resnik.math.symbo.algebra.operation.Variable

class Cos(val inner: Operation) : Operation(inner) {

    override fun toConstant(): Constant = Constant(ComplexNumber.cos(inner.toConstant().value))

    override fun toNonConstantString(): String = "cos($inner)"

    override fun generate(values: Array<Operation>): Operation {
        if (values.isEmpty()) {
            return Cos(Variable.X)
        }
        return Cos(values[0])
    }

}