package com.resnik.math.linear.tensor

import com.resnik.math.symbo.algebra.operation.Constant
import com.resnik.math.symbo.algebra.operation.Operation
import kotlin.math.acos

class Vector(val len : Int, init: (Int) -> Tensor = {Scalar()}) : Tensor(TensorDimension(len)) {

    constructor(vararg values : Double) : this(values.size){
        values.indices.forEach { inner[it] = Scalar(values[it]) }
    }

    constructor(values : Array<Operation>) : this(values.size){
        values.indices.forEach { inner[it] = Scalar(values[it]) }
    }

    init {
        inner.indices.forEach { inner[it] = init(it) }
    }

    operator fun get(index : Int) : Scalar = super.get(index) as Scalar

    operator fun plus(other: Vector) : Vector = Vector(len){this[it] + other[it]}

    override fun sum() : Tensor {
        var ret = Scalar(Constant.ZERO)
        this.inner.forEach { ret += it as Scalar }
        return ret
    }

    operator fun minus(other: Vector) : Vector = Vector(len){this[it] - other[it]}

    operator fun times(other: Vector) : Scalar = Vector(len){this[it] * other[it]}.sum() as Scalar

    operator fun div(other: Scalar) : Vector = Vector(len){this[it] / other}

    fun magnitude() : Scalar = Scalar((this * this).value.sqrt())

    fun norm() : Vector = this / magnitude()

    fun theta(other: Vector) : Scalar = Scalar(acos(((this * other) / (this.magnitude() * other.magnitude())).value.toConstant().value.real))

    override fun toString(): String {
        return inner.contentToString().replace("{","[").replace("}","}")
    }

}