package com.resnik.math.symbo.logic.operation

import com.resnik.math.symbo.logic.operation.base.*
import java.util.*


abstract class LogicalOperation(vararg val values: LogicalOperation) : LogicalInterface<LogicalOperation> {

    override fun or(other: Boolean): LogicalOperation {
        if (other) return LogicalConstant(true)
        return this
    }

    override fun or(other: LogicalOperation): Or {
        return Or(this, other)
    }

    override fun and(other: LogicalOperation): And {
        return And(this, other)
    }

    override fun not(): Not {
        return Not(this)
    }

    override fun implies(other: LogicalOperation): Implies {
        return Implies(this, other)
    }

    override fun iff(other: LogicalOperation): Iff {
        return Iff(this, other)
    }

    abstract fun toConstant(): LogicalConstant

    abstract fun toNonConstantString(): String

    open fun isConstant(): Boolean {
        this.values.forEach {
            if (it !is LogicalConstant && !it.isConstant()) {
                return false
            }
        }
        return true
    }

    override fun toString(): String {
        if (this.isConstant()) {
            return this.toConstant().toString()
        }
        return this.toNonConstantString()
    }

    open fun getVariables(): Array<LogicalVariable> {
        val retArray: Array<LogicalVariable>
        val varList: MutableList<LogicalVariable> = mutableListOf()
        for (element in values) {
            val allVars = element.getVariables()
            for (currVar in allVars) {
                if (!varList.contains(currVar)) {
                    varList.add(currVar)
                }
            }
        }
        retArray = varList.toTypedArray<LogicalVariable>()
        Arrays.sort( retArray) { t, t1 -> t.name.compareTo(t1.name) }
        return retArray
    }

    abstract fun generate(ops: Array<LogicalOperation>): LogicalOperation

    companion object {

        val CONSTANT_INFERENCE_ERROR = IllegalStateException("Cannot infer a logical constant from a non-constant set.")

        fun require1(o1: LogicalOperation, vararg oN: LogicalOperation): Array<LogicalOperation> {
            return Array(1 + oN.size) {
                if (it == 0) {
                    o1
                } else {
                    oN[it - 2]
                }
            }
        }

        fun require2(o1: LogicalOperation, o2: LogicalOperation, vararg oN: LogicalOperation): Array<LogicalOperation> {
            return Array(2 + oN.size) {
                when (it) {
                    0 -> {
                        o1
                    }
                    1 -> {
                        o2
                    }
                    else -> {
                        oN[it - 2]
                    }
                }
            }
        }
    }


}