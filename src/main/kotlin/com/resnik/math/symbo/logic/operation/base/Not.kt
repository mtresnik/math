package com.resnik.math.symbo.logic.operation.base

import com.resnik.math.symbo.logic.operation.LogicalConstant
import com.resnik.math.symbo.logic.operation.LogicalOperation

typealias LogicalNegation = Not

class Not(val inner: LogicalOperation) : LogicalOperation(inner) {
    override fun toConstant(): LogicalConstant {
        return LogicalConstant(!inner.toConstant().value)
    }

    override fun toNonConstantString(): String {
        return "¬($inner)"
    }

    override fun isConstant(): Boolean {
        return inner.isConstant()
    }

    override fun generate(ops: Array<LogicalOperation>): LogicalOperation {
        return Not(ops[0])
    }

    fun distribute(): LogicalOperation {
        if (inner is Not) {
            return inner.inner
        }
        if (inner is Or) {
            return And(inner.values.map { value -> value.not() }.toTypedArray())
        }
        if (inner is And) {
            return Or(inner.values.map { value -> value.not() }.toTypedArray())
        }
        return this
    }


}