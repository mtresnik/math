package com.resnik.math.symbo.algebra.operation.base

import com.resnik.math.symbo.algebra.ComplexNumber
import com.resnik.math.symbo.algebra.operation.Constant
import com.resnik.math.symbo.algebra.operation.Operation
import com.resnik.math.symbo.algebra.operation.Variable


class Multiplication(values: Array<Operation>) : Operation(*values) {

    constructor(o1: Operation, o2: Operation, vararg oN: Operation) : this(require2(o1, o2, *oN))

    override fun toConstant(): Constant {
        if (!isConstant()) {
            return Constant.NaN
        }
        var ret: Constant = Constant.ONE
        this.values.indices.forEach {
            ret = Constant(ret.value * this.values[it].toConstant().value)
        }
        return ret
    }

    override fun toNonConstantString(): String {
        var retString = ""
        var remaining: MutableList<Operation> = mutableListOf()
        var coefficient = Constant.ONE
        for (o in values) {
            if (o is Constant || o.isConstant()) {
                val oRep: Constant = o.toConstant()
                if (oRep.value == ComplexNumber.ZERO) {
                    return Constant.ZERO.toString()
                }
                val newVal: ComplexNumber = coefficient.value.times(oRep.value)
                coefficient = Constant(newVal)
            } else {
                remaining.add(o)
            }
        }
        if (coefficient.value == ComplexNumber.ZERO) {
            return Constant.ZERO.toString()
        }
        if (coefficient.value != ComplexNumber.ONE) {
            val tempList: MutableList<Operation> = mutableListOf()
            tempList.add(coefficient)
            tempList.addAll(remaining)
            remaining = tempList
        }
        var doAdd = ""
        for (i in remaining.indices) {
            val currElemStr = remaining[i].toString()
            retString += if (currElemStr == Constant.ONE.toString() || currElemStr.isEmpty()) {
                continue
            } else {
                doAdd
            }
            retString += remaining[i].toString()
            if (i < remaining.size - 1) {
                doAdd = " * "
            }
        }
        return retString
    }

    override fun generate(values: Array<Operation>): Operation = Multiplication(values)
    override fun getDerivative(respectTo: Variable): Operation {
        val products: Array<Operation>
        val productList: MutableList<Multiplication> = mutableListOf()
        for (elem in values.indices) {
            val valuesCopy = Array<Operation>(values.size) { Constant.ZERO }
            for (subElem in values.indices) {
                if (subElem == elem) {
                    valuesCopy[subElem] = values[subElem].getDerivative(respectTo)
                } else {
                    valuesCopy[subElem] = values[subElem]
                }
            }
            val currProduct = Multiplication(valuesCopy)
            if (currProduct.toString() != Constant.ZERO.toString()) {
                productList.add(currProduct)
            }
        }
        products = productList.toTypedArray()
        return Addition(products)
    }
}