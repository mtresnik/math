package com.resnik.math.symbo.logic.operation.base

import com.resnik.math.symbo.logic.operation.LogicalConstant
import com.resnik.math.symbo.logic.operation.LogicalOperation

typealias Union = Or
class Or(values: Array<LogicalOperation>): LogicalOperation(*values) {

    constructor(o1: LogicalOperation, o2: LogicalOperation, vararg oN: LogicalOperation) : this(require2(o1, o2, *oN))

    override fun toConstant(): LogicalConstant {
        // Add wiggle room for if there is a true value among non-constants
        if (containsTrue()) return LogicalConstant.TRUE
        if (!super.isConstant()) throw CONSTANT_INFERENCE_ERROR
        return LogicalConstant.FALSE
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
                retString += " v "
            }
        }
        if (constantList.isNotEmpty()) {
            val containsFalse: Boolean = constantList.contains(LogicalConstant.FALSE)
            if (containsFalse) {
                retString += " v " + LogicalConstant.FALSE
            }
        }

        retString += ")"
        return retString
    }

    private fun containsTrue(): Boolean {
        return this.values.firstOrNull { value -> value.isConstant() && value.toConstant().value } != null
    }

    override fun isConstant(): Boolean {
        return super.isConstant() || containsTrue()
    }

    override fun generate(ops: Array<LogicalOperation>): Union {
        return Or(ops)
    }
}