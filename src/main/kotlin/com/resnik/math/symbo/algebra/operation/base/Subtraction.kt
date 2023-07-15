package com.resnik.math.symbo.algebra.operation.base

import com.resnik.math.symbo.algebra.ComplexNumber
import com.resnik.math.symbo.algebra.operation.Constant
import com.resnik.math.symbo.algebra.operation.Operation
import com.resnik.math.symbo.algebra.operation.Variable

class Subtraction(values: Array<Operation>) : Operation(*values) {

    constructor(o1: Operation, o2: Operation, vararg oN: Operation) : this(require2(o1, o2, *oN))

    override fun toConstant(): Constant {
        if (!isConstant()) {
            return Constant.NaN
        }
        var ret: Constant = this.values.first().toConstant()
        (1 .. this.values.lastIndex).forEach {
            ret = Constant(ret.value - this.values[it].toConstant().value)
        }
        return ret
    }

    override fun toNonConstantString(): String {
        var retString = "("
        val nonZeroList: MutableList<Operation> = mutableListOf()
        var allConstants = true
        for (o in values) {
            if (o is Constant) {
                if (o.value == ComplexNumber.ZERO) {
                    continue
                }
            } else {
                allConstants = false
            }
            nonZeroList.add(o)
        }
        if (nonZeroList.isEmpty()) {
            return Constant.ZERO.toString()
        }
        for (i in nonZeroList.indices) {
            retString += nonZeroList[i].toString()
            if (i < nonZeroList.size - 1) {
                retString += " - "
            }
        }
        if (allConstants) {
            retString = "" + evaluateReal(0.0)
        }
        return "$retString)"
    }

    override fun generate(values: Array<Operation>): Operation = Addition(values)
    override fun getDerivative(respectTo: Variable): Operation {
        return Subtraction(this.values.map { value -> value.getDerivative(respectTo) }.toTypedArray())
    }

    fun allValues(): List<Operation> {
        val retList: MutableList<Operation> = ArrayList()
        for (value in values) {
            var temp = value
            if (temp is Parentheses) {
                temp = temp.unwrap()
            }
            if (temp is Addition) {
                retList.addAll(temp.allValues())
            } else {
                retList.add(temp)
            }
        }
        return retList
    }

    override fun equals(other: Any?): Boolean {
        if (other === this) return true
        if (other !is Addition) return false
        val val1: List<Operation> = this.allValues()
        val val2: List<Operation> = other.allValues()
        if (val1.size != val2.size) return false
        for (op in val1) {
            if (!val2.contains(op)) {
                return false
            }
        }
        return true
    }
}