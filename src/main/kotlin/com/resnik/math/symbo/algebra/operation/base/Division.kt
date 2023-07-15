package com.resnik.math.symbo.algebra.operation.base

import com.resnik.math.symbo.algebra.operation.Constant
import com.resnik.math.symbo.algebra.operation.Operation
import com.resnik.math.symbo.algebra.operation.Variable

open class Division(values: Array<Operation>) : Operation(*values) {

    var numerator: Operation = values[0]
    var denominator: Operation = values[1]
    var rem: Array<Operation> = popTwo(values)

    constructor(o1: Operation, o2: Operation, vararg oN: Operation) : this(require2(o1, o2, *oN))

    override fun toConstant(): Constant {
        if (!isConstant()) {
            return Constant.NaN
        }
        return Constant(numerator.toConstant().value / Multiplication(require1(denominator, *rem)).toConstant().value)
    }

    override fun toNonConstantString(): String =
        "((" + numerator.toString() + ") / (" + Multiplication(require1(denominator, *rem)).toString() + "))"

    override fun generate(values: Array<Operation>): Operation = Division(values)
    override fun getDerivative(respectTo: Variable): Operation {
        val fx = numerator
        val f1x = numerator.getDerivative(respectTo)
        val gx: Operation = Multiplication(formatSuper1(denominator, *rem))
        val g1x = gx.getDerivative(respectTo)

        val p1: Operation = Multiplication(f1x, gx)
        val p2: Operation = Multiplication(fx, g1x)
        val newNumerator: Operation = Subtraction(p1, p2)
        val newDenominator: Operation = Power(gx, Constant(2.0))
        return Division(newNumerator, newDenominator)
    }

    companion object {

        fun popTwo(ops: Array<out Operation>): Array<Operation> {
            if (ops.size <= 2) {
                return arrayOf()
            }
            return Array(ops.size - 2) {
                ops[it + 2]
            }
        }
    }
}