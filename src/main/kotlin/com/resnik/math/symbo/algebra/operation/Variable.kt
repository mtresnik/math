package com.resnik.math.symbo.algebra.operation

class Variable(val name : String) : Operation() {

    override fun isConstant(): Boolean = false

    override fun generate(values: Array<Operation>): Operation = Variable(this.name)

    override fun toConstant(): Constant = Constant.NaN

    override fun toNonConstantString(): String = this.name

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Variable

        if (name != other.name) return false

        return true
    }

    override fun hashCode(): Int {
        return name.hashCode()
    }

    override fun getVariables(): Array<Variable> = arrayOf(this)

    companion object {
        val X = Variable("x")
        val Y = Variable("y")
        val Z = Variable("z")
        val THETA = Variable("θ")
        val I = Variable("i")
        val E = Variable("e")
        val PI = Variable("pi")
    }

}