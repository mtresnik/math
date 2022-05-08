package com.resnik.math.symbo.logic.operation

abstract class LogicalOperation(vararg values: LogicalOperation) : LogicalInterface<LogicalOperation> {

    override fun or(other: Boolean): LogicalOperation {
        if (other) return LogicalConstant(true)
        return this
    }

    override fun or(other: LogicalOperation): LogicalOperation {
        TODO("Not yet implemented")
    }

    override fun and(other: LogicalOperation): LogicalOperation {
        TODO("Not yet implemented")
    }

    override fun not(): LogicalOperation {
        TODO("Not yet implemented")
    }

    override fun implies(other: LogicalOperation): LogicalOperation {
        TODO("Not yet implemented")
    }

    override fun iff(other: LogicalOperation): LogicalOperation {
        TODO("Not yet implemented")
    }


}