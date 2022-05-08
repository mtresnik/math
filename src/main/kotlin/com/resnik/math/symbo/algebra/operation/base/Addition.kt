package com.resnik.math.symbo.algebra.operation.base

import com.resnik.math.symbo.algebra.ComplexNumber
import com.resnik.math.symbo.algebra.operation.Constant
import com.resnik.math.symbo.algebra.operation.Operation

class Addition(values: Array<Operation>) : Operation(*values) {

    constructor(o1: Operation, o2: Operation, vararg oN: Operation) : this(require2(o1, o2, *oN))

    override fun toConstant(): Constant {
        if (!isConstant()) {
            return Constant.NaN
        }
        var ret: Constant = Constant.ZERO
        this.values.indices.forEach {
            ret = Constant(ret.value + this.values[it].toConstant().value)
        }
        return ret
    }

    override fun toNonConstantString(): String {
        var retString = "("
        val nonConstantList: MutableList<Operation> = mutableListOf()
        if (isConstant()) {
            return this.toConstant().toString()
        }
        val constantList: MutableList<Constant> = mutableListOf()
        for (o in values) {
            if (o.toString().equals(Constant.ZERO.toString())) {
                continue
            }
            if (o.isConstant() || o is Constant) {
                constantList.add(o.toConstant())
            } else {
                nonConstantList.add(o)
            }
        }
        if (nonConstantList.isEmpty() && constantList.isEmpty()) {
            return Constant.ZERO.toString()
        }
        for (i in nonConstantList.indices) {
            retString += nonConstantList[i].toString()
            if (i < nonConstantList.size - 1) {
                retString += " + "
            }
        }
        if (constantList.isNotEmpty()) {
            var sum: ComplexNumber = ComplexNumber.ZERO
            for (i in constantList.indices) {
                sum = sum.plus(constantList[i].value)
            }
            retString += " + " + Constant(sum)
        }

        retString += ")"
        return retString
    }

    override fun generate(values: Array<Operation>): Operation = Addition(values)

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