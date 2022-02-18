package com.resnik.math.linear.array

import com.resnik.math.plus
import java.util.*
import kotlin.math.pow

open class ArrayVector(vararg val values: Double) {

    constructor(size: Int) : this(*DoubleArray(size){Math.random()})

    constructor(size: Int, value: Double) : this(*DoubleArray(size) {value})

    operator fun plus(other: ArrayPoint) : ArrayPoint = ArrayPoint(*values.zip(other.values) { a, b -> a + b }.toDoubleArray())

    operator fun plus(other: ArrayVector): ArrayVector = ArrayVector(*values.zip(other.values) { a, b -> a + b }.toDoubleArray())

    operator fun minus(other: ArrayVector): ArrayVector = ArrayVector(*values.zip(other.values) { a, b -> a - b }.toDoubleArray())

    operator fun times(other: ArrayVector): Double = values.zip(other.values) { a, b -> a * b}.sum()

    fun dot(other: ArrayVector) : Double = this * other

    fun cross(other : ArrayVector) : ArrayVector = ArrayVector(
            this[1] * other[2] - other[1] * this[2],
            this[2] * other[0] - other[2] * this[0],
            this[0] * other[1] - other[0] * this[1]
    )

    operator fun plus(value: Double): ArrayVector = ArrayVector(*values.map { it + value }.toDoubleArray())

    operator fun minus(value: Double): ArrayVector = ArrayVector(*values.map { it - value }.toDoubleArray())

    operator fun times(value: Double): ArrayVector = ArrayVector(*values.map { it * value }.toDoubleArray())

    operator fun times(value: Int): ArrayVector = times(value.toDouble())

    operator fun Double.times(other: ArrayVector) : ArrayVector = other.times(this)

    operator fun Double.div(other : ArrayVector) : ArrayVector = other.div(this)

    fun append(other: ArrayVector) : ArrayVector = ArrayVector(*(this.values + other.values))

    operator fun div(value: Double): ArrayVector = ArrayVector(*values.map { it / value }.toDoubleArray())

    operator fun div(value: Int): ArrayVector = div(value.toDouble())

    fun limit(size: Int) : ArrayVector = ArrayVector(*this.values.copyOfRange(0, size))

    fun hadamard(other: ArrayVector) : ArrayVector = ArrayVector(*values.indices.map { values[it] * other.values[it] }.toDoubleArray())

    fun apply(func: Function1<Double, Double>): ArrayVector = ArrayVector(*values.map { func(it) }.toDoubleArray())

    operator fun get(index: Int) : Double = values[index]

    operator fun set(index: Int, value: Double) {
        values[index] = value
    }

    fun setFrom(other: ArrayVector) = values.indices.forEach { values[it] = other[it] }

    fun clone(): ArrayVector = ArrayVector(*values.copyOf(values.size))

    fun sum() : Double = values.sum()

    fun size() : Int = values.size

    fun toRowMatrix() : ArrayMatrix = ArrayMatrix(arrayOf(this.values))

    fun toColMatrix() : ArrayMatrix = ArrayMatrix(this.values.map { doubleArrayOf(it) }.toTypedArray())

    fun magnitude(): Double = values.map { v -> v.pow(2) }.sum().pow(0.5)

    fun normalized() : ArrayVector = ArrayVector(*(values.map { it / magnitude() }.toDoubleArray()))

    fun maxIndex() : Int = values.indices.maxBy { values[it] } ?: -1

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ArrayVector

        if (!values.contentEquals(other.values)) return false

        return true
    }

    override fun hashCode(): Int = values.contentHashCode()

    override fun toString(): String = "<${values.contentToString()}>"

    companion object {

        private val random : Random = Random()

        fun randomGaussian(size: Int) : ArrayVector {
            return ArrayVector(*DoubleArray(size){ random.nextGaussian()})
        }
    }

}