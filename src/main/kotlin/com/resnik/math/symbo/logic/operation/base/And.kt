package com.resnik.math.symbo.logic.operation.base

import com.resnik.math.symbo.logic.operation.LogicalConstant
import com.resnik.math.symbo.logic.operation.LogicalOperation

typealias LogicalProduct = And

class And(values: Array<LogicalOperation>): LogicalOperation(*values) {

    constructor(o1: LogicalOperation, o2: LogicalOperation, vararg oN: LogicalOperation) : this(require2(o1, o2, *oN))

    override fun toConstant(): LogicalConstant {
        if (containsFalse()) return LogicalConstant.FALSE
        if (!super.isConstant()) throw CONSTANT_INFERENCE_ERROR
        return LogicalConstant.TRUE
    }

    override fun toNonConstantString(): String {
        var retString = "("
        val nonConstantList: MutableList<LogicalOperation> = mutableListOf()
        if (isConstant()) {
            return this.toConstant().toString()
        }
        val constantList: MutableList<LogicalConstant> = mutableListOf()
        for (o in values) {
            if (o.isConstant() || o is LogicalConstant) {
                constantList.add(o.toConstant())
            } else {
                nonConstantList.add(o)
            }
        }
        if (nonConstantList.isEmpty() && constantList.isEmpty()) {
            return LogicalConstant.FALSE.toString()
        }
        for (i in nonConstantList.indices) {
            retString += nonConstantList[i].toString()
            if (i < nonConstantList.size - 1) {
                retString += " ∧ "
            }
        }
        if (constantList.isNotEmpty()) {
            val containsTrue: Boolean = constantList.contains(LogicalConstant.TRUE)
            if (containsTrue) {
                retString += " ∧ " + LogicalConstant.TRUE
            }
        }

        retString += ")"
        return retString
    }

    private fun containsFalse(): Boolean {
        return this.values.firstOrNull { value -> value.isConstant() && !value.toConstant().value } != null
    }

    override fun isConstant(): Boolean {
        return super.isConstant() || containsFalse()
    }

    override fun generate(ops: Array<LogicalOperation>): And {
        return And(ops)
    }

}