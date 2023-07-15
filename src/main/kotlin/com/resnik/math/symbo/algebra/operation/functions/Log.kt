package com.resnik.math.symbo.algebra.operation.functions

import com.resnik.math.symbo.algebra.operation.Constant
import com.resnik.math.symbo.algebra.operation.Operation
import com.resnik.math.symbo.algebra.operation.Variable
import com.resnik.math.symbo.algebra.operation.base.Division

import com.resnik.math.symbo.algebra.operation.base.Multiplication




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

    override fun getDerivative(respectTo: Variable): Operation {
        if (base is Constant) {
            val retDiv: Division
            val a1x: Operation = inner.getDerivative(respectTo)
            val lnB: Operation = ln(base)
            val den: Operation = Multiplication(lnB, inner)
            retDiv = Division(a1x, den)
            return retDiv
        }
        val num: Operation = ln(inner)
        val den: Operation = ln(base)
        return Division(num, den).getDerivative(respectTo)
    }

    companion object {

        @JvmStatic
        fun ln(inner: Operation): Log = Log(Constant.E, inner)

    }

}
