package com.resnik.math.symbo.logic.operation

class LogicalVariable(val name: String): LogicalOperation() {
    override fun toConstant(): LogicalConstant = throw CONSTANT_INFERENCE_ERROR

    override fun toNonConstantString(): String = this.name

    override fun isConstant(): Boolean = false

    override fun generate(ops: Array<LogicalOperation>): LogicalOperation {
        return LogicalVariable(name)
    }

    companion object {
        val P = LogicalVariable("p")
        val Q = LogicalVariable("q")
    }
}