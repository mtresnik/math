package com.resnik.math.symbo.logic.operation.base

import com.resnik.math.symbo.logic.operation.LogicalConstant
import com.resnik.math.symbo.logic.operation.LogicalOperation

class Iff(val left: LogicalOperation, val right: LogicalOperation): LogicalOperation(left, right) {

    override fun toConstant(): LogicalConstant {
        if (isTrue()) return LogicalConstant.TRUE
        if (!super.isConstant()) throw CONSTANT_INFERENCE_ERROR
        return LogicalConstant.FALSE
    }

    override fun toNonConstantString(): String {
        return "$left <-> $right"
    }

    private fun isTrue(): Boolean {
        return ((left.isConstant() && !left.toConstant().value)
                || (right.isConstant() && right.toConstant().value)) &&
                ((left.isConstant() && left.toConstant().value)
                || (right.isConstant() && !right.toConstant().value))
    }

    override fun isConstant(): Boolean {
        return super.isConstant() || isTrue()
    }

    override fun generate(ops: Array<LogicalOperation>): Iff {
        return Iff(ops[0],ops[1])
    }

}