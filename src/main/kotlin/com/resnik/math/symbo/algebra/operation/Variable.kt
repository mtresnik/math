package com.resnik.math.symbo.algebra.operation

import com.resnik.math.symbo.algebra.operation.base.Division
import com.resnik.math.symbo.algebra.operation.base.Multiplication
import com.resnik.math.symbo.algebra.operation.base.Power
import com.resnik.math.util.CountList


class Variable(val name: String) : Operation() {

    var derivativeOf: Operation? = null
    var respectTo: CountList<Variable> = CountList()

    override fun isConstant(): Boolean = false

    override fun generate(values: Array<Operation>): Operation = Variable(this.name)
    override fun getDerivative(respectTo: Variable): Operation {
        if (this == respectTo) {
            return Constant.ONE
        }
        val d = Variable("d")
        val derOf = (if (this.derivativeOf == null) this else this.derivativeOf) ?: Constant.ONE
        val newRespectTo: CountList<Variable> = this.respectTo.clone()
        var sum = 0
        if (derOf == respectTo) {
            sum = -1
        } else {
            newRespectTo.add(respectTo)
        }

        val denominatorArray = Array<Operation>(newRespectTo.internalMap.size) { Constant.ZERO }
        var i = 0
        for ((key, value) in newRespectTo.internalMap) {
            sum += value
            denominatorArray[i] = Multiplication(d, Power(key, Constant(value)))
            i++
        }
        val numerator = Multiplication(Power(d, Constant(sum)), derOf)
        val denominator = Multiplication(denominatorArray)
        val div = Division(numerator, denominator)
        val retVar = Variable(div.toString())
        retVar.derivativeOf = derOf
        retVar.respectTo = newRespectTo
        return retVar
    }

    override fun toConstant(): Constant = Constant.NaN

    override fun toNonConstantString(): String = this.name

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Variable

        if (name != other.name) return false

        return true
    }

    override fun hashCode(): Int {
        return name.hashCode()
    }

    override fun getVariables(): Array<Variable> = arrayOf(this)

    companion object {
        val X = Variable("x")
        val Y = Variable("y")
        val Z = Variable("z")
        val THETA = Variable("θ")
        val I = Variable("i")
        val E = Variable("e")
        val PI = Variable("pi")
    }

}