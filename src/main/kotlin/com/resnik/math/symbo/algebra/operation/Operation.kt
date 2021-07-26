package com.resnik.math.symbo.algebra.operation

import com.resnik.math.symbo.algebra.Algebraic
import com.resnik.math.symbo.algebra.ComplexNumber
import com.resnik.math.symbo.algebra.asComplex
import com.resnik.math.symbo.algebra.operation.base.*
import java.util.*

abstract class Operation(vararg val values: Operation) : Algebraic<Operation> {

    abstract fun toConstant(): Constant

    abstract fun toNonConstantString(): String

    open fun isConstant(): Boolean {
        this.values.forEach {
            if (it !is Constant && !it.isConstant()) {
                return false
            }
        }
        return true
    }

    operator fun plus(other: Double) : Operation = this + Constant(other)

    override fun plus(other: Operation): Operation = Addition(this, other)

    operator fun minus(other: Double) : Operation = this - Constant(other)

    override fun minus(other: Operation): Operation = Subtraction(this, other)

    operator fun times(other: Double) : Operation = this * Constant(other)

    override fun times(other: Operation): Operation = Multiplication(this, other)

    operator fun div(other : Double) : Operation = this / Constant(other)

    override fun div(other: Operation): Operation = Division(this, other)

    fun pow(other : Double) : Operation = this.pow(Constant(other))

    override fun pow(other: Operation): Operation = Power(this, other)

    override fun sqrt(): Operation = pow(Constant.ONE_HALF)

    operator fun contains(variable: Variable): Boolean {
        if (this is Variable) {
            return this == variable
        }
        return varOperations(variable).isNotEmpty()
    }

    fun varOperations(variable: Variable): Array<Operation> {
        val validOperationsList = mutableListOf<Operation>()
        this.values.forEach { curr ->
            if (curr !is Constant && !curr.isConstant()) {
                if (variable in curr) {
                    validOperationsList.add(curr)
                }
            }
        }
        return validOperationsList.toTypedArray()
    }

    open fun getVariables(): Array<Variable> {
        val retArray: Array<Variable>
        val varList: MutableList<Variable> = mutableListOf()
        for (i in values.indices) {
            val currOp: Operation = values[i]
            if (currOp.isConstant()) {
                continue
            }
            val allVars: Array<Variable> = currOp.getVariables()
            for (currVar in allVars) {
                if (!varList.contains(currVar)) {
                    varList.add(currVar)
                }
            }
        }
        retArray = Array(varList.size){varList[it]}
        Arrays.sort(retArray) { t, t1 -> t.name.compareTo(t1.name) }
        return retArray
    }

    fun evaluate(t: Operation): Operation = evaluate(Variable.X, t)

    fun evaluate(variable: Variable, value: Operation): Operation {
        if(variable !in this){
            return this
        }
        if(this is Variable && this == variable){
            return value
        }
        val newValues = Array<Operation>(this.values.size){
            this.values[it].evaluate(variable, value)
        }
        return generate(newValues)
    }

    fun evaluate(variable: Variable, value: ComplexNumber): Operation {
        return evaluate(variable, Constant(value))
    }

    fun evaluate(variable: Variable, value: Double): Operation {
        return evaluate(variable, value.asComplex())
    }

    operator fun set(variable: Variable, value : Double) : Operation = evaluate(variable, value)

    operator fun set(variable: Variable, value: ComplexNumber) : Operation = evaluate(variable, value)

    operator fun set(variable: Variable, value: Operation) : Operation = evaluate(variable, value)

    fun evaluateReal(real : Double) : Operation = this.evaluate(Constant(ComplexNumber(real)))

    abstract fun generate(values: Array<Operation>): Operation

    fun wrap() : Parentheses = Parentheses(this)

    override fun toString(): String {
        if(this.isConstant()){
            return this.toConstant().toString()
        }
        return this.toNonConstantString()
    }

    companion object {

        fun require1(o1: Operation, vararg oN: Operation): Array<Operation> {
            return Array(1 + oN.size) {
                if (it == 0) {
                    o1
                } else {
                    oN[it - 2]
                }
            }
        }

        fun require2(o1: Operation, o2: Operation, vararg oN: Operation): Array<Operation> {
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