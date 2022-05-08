package com.resnik.math.linear.tensor

import com.resnik.math.symbo.algebra.operation.Operation
import com.resnik.math.symbo.algebra.operation.Variable

open class Tensor(val dims: TensorDimension, val init: (Int) -> Scalar = { Scalar() }) {

    var inner: Array<Tensor>

    init {
        inner = when {
            this.isScalar() -> {
                arrayOf()
            }
            this.isVector() -> {
                Array(dims.value) { init(it) }
            }
            this.isMatrix() -> {
                Array(dims[0]) { Vector(dims[1]) }
            }
            else -> {
                Array(dims.value) { Tensor(dims.next!!) }
            }
        }
    }

    fun isScalar(): Boolean = this.rank() == 0

    fun toScalar(): Scalar {
        if (!this.isScalar()) {
            throw IllegalStateException("Not a Scalar")
        }
        return Scalar()
    }

    fun isVector(): Boolean = this.rank() == 1

    fun toVector(): Vector {
        if (!this.isVector()) {
            throw IllegalStateException("Not a Vector")
        }
        val ret = Vector(dims.value)
        this.inner.indices.forEach { ret.inner[it] = this.inner[it] }
        return ret
    }

    fun isMatrix(): Boolean = this.rank() == 2

    fun toMatrix(): Matrix {
        if (!this.isMatrix()) {
            throw IllegalStateException("Not a Matrix")
        }
        val ret = Matrix(dims[0], dims[1])
        ret.inner = this.inner.map { it.toVector() }.toTypedArray()
        return ret
    }

    operator fun get(vararg intArray: Int): Tensor = get(TensorIndex(*intArray))

    operator fun get(tensorIndex: TensorIndex): Tensor {
        if (tensorIndex !in dims) {
            throw ArrayIndexOutOfBoundsException()
        }
        var retTensor = this
        var currIndex = tensorIndex
        while (currIndex.hasNext()) {
            retTensor = retTensor.inner[currIndex.value]
            currIndex = tensorIndex.next!!
        }
        retTensor = retTensor.inner[currIndex.value]
        return retTensor
    }

    operator fun set(tensorIndex: TensorIndex, value: Tensor) {
        if (tensorIndex !in dims) {
            throw ArrayIndexOutOfBoundsException()
        }
        var retTensor = this
        var currIndex = tensorIndex
        while (currIndex.hasNext()) {
            retTensor = retTensor.inner[currIndex.value]
            currIndex = tensorIndex.next!!
        }
        retTensor.inner[currIndex.value] = value
    }

    operator fun set(vararg intArray: Int, value: Tensor) = set(TensorIndex(*intArray), value)

    operator fun set(vararg intArray: Int, value: Double) = set(TensorIndex(*intArray), Scalar(value))

    operator fun set(vararg intArray: Int, value: Operation) = set(TensorIndex(*intArray), Scalar(value))

    fun checkDims(other: Tensor) {
        if (dims != other.dims) {
            throw IllegalArgumentException()
        }
    }

    fun rank(): Int = dims.values.size

    open fun sum(): Tensor {
        var ret: Tensor = inner[0]
        (1 until inner.size).forEach {
            ret += ret.inner[it]
        }
        return ret
    }

    open operator fun plus(other: Tensor): Tensor {
        checkDims(other)
        var ret = Tensor(dims)
        repeat(dims.value) {
            ret += this[it]
        }
        return ret
    }

    open fun evaluate(variable: Variable, operation: Operation): Tensor {
        val retTensor = Tensor(this.dims)
        this.inner.indices.forEach { retTensor.inner[it] = inner[it].evaluate(variable, operation) }
        return retTensor
    }

    override fun toString(): String {
        return inner.contentToString().replace("{", "[").replace("}", "}")
    }


}