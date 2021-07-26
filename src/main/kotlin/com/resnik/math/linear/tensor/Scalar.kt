package com.resnik.math.linear.tensor

import com.resnik.math.symbo.algebra.operation.Constant
import com.resnik.math.symbo.algebra.operation.Constant.Companion.toConstant
import com.resnik.math.symbo.algebra.operation.Operation
import com.resnik.math.symbo.algebra.operation.Variable

class Scalar(val value : Operation = Constant.ZERO) : Tensor(TensorDimension()) {

    constructor(doubleVal : Double) : this(doubleVal.toConstant())

    operator fun plus(other: Scalar) : Scalar = Scalar(this.value + other.value)

    operator fun minus(other: Scalar) : Scalar = Scalar(this.value - other.value)

    operator fun times(other: Scalar) : Scalar = Scalar(this.value * other.value)

    operator fun div(other: Scalar) : Scalar = Scalar(this.value / other.value)

    fun Double.toScalar() : Scalar = Scalar(this.toConstant())

    override fun evaluate(variable: Variable, operation: Operation): Tensor {
        return Scalar(value.evaluate(variable, operation))
    }

    override fun toString(): String {
        return "$value"
    }

}