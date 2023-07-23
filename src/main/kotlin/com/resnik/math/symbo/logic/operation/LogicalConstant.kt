package com.resnik.math.symbo.logic.operation

class LogicalConstant(val value: Boolean) : LogicalOperation() {
    override fun toConstant(): LogicalConstant {
        return this
    }

    override fun toNonConstantString(): String = this.value.toString()

    override fun isConstant(): Boolean {
        return true
    }

    override fun toString(): String = value.toString()
    override fun generate(ops: Array<LogicalOperation>): LogicalConstant {
        return LogicalConstant(value)
    }

    companion object {
        val TRUE = LogicalConstant(true)
        val FALSE = LogicalConstant(false)
    }

}