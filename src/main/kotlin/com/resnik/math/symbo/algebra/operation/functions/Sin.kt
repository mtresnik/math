package com.resnik.math.symbo.algebra.operation.functions

import com.resnik.math.symbo.algebra.ComplexNumber
import com.resnik.math.symbo.algebra.operation.Constant
import com.resnik.math.symbo.algebra.operation.Operation
import com.resnik.math.symbo.algebra.operation.Variable
import com.resnik.math.symbo.algebra.operation.base.Multiplication




class Sin(val inner: Operation) : Operation(inner) {

    override fun toConstant(): Constant = Constant(ComplexNumber.sin(inner.toConstant().value))

    override fun toNonConstantString(): String = "sin($inner)"

    override fun generate(values: Array<Operation>): Operation {
        if (values.isEmpty()) {
            return Sin(Variable.X)
        }
        return Sin(values[0])
    }

    override fun getDerivative(respectTo: Variable): Operation {
        val firstTerm: Operation = Cos(inner)
        val secondTerm: Operation = inner.getDerivative(respectTo)
        return Multiplication(firstTerm, secondTerm)
    }

}