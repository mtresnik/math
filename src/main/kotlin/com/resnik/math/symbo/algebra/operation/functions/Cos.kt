package com.resnik.math.symbo.algebra.operation.functions

import com.resnik.math.symbo.algebra.ComplexNumber
import com.resnik.math.symbo.algebra.operation.Constant
import com.resnik.math.symbo.algebra.operation.Operation
import com.resnik.math.symbo.algebra.operation.Variable
import com.resnik.math.symbo.algebra.operation.base.Multiplication
import com.resnik.math.symbo.algebra.operation.base.Negation


class Cos(val inner: Operation) : Operation(inner) {

    override fun toConstant(): Constant = Constant(ComplexNumber.cos(inner.toConstant().value))

    override fun toNonConstantString(): String = "cos($inner)"

    override fun generate(values: Array<Operation>): Operation {
        if (values.isEmpty()) {
            return Cos(Variable.X)
        }
        return Cos(values[0])
    }

    override fun getDerivative(respectTo: Variable): Operation {
        val firstTerm: Operation = Negation(Sin(inner))
        val secondTerm: Operation = inner.getDerivative(respectTo)
        return Multiplication(firstTerm, secondTerm)
    }

}