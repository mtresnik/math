package com.resnik.math.symbo.algebra

import java.lang.Double.max
import java.lang.Double.min

class Bounds(a: ComplexNumber, b: ComplexNumber) {

    var min: ComplexNumber = ComplexNumber(min(a.real, b.real), min(a.imaginary, b.imaginary))
    var max: ComplexNumber = ComplexNumber(max(a.real, b.real), max(a.imaginary, b.imaginary))

    constructor(a: Double, b: Double) : this(a.asComplex(), b.asComplex())

    operator fun contains(other: ComplexNumber): Boolean {
        if (other.real > max.real || other.real < min.real)
            return false
        if (other.imaginary > max.imaginary || other.imaginary < min.imaginary)
            return false
        return true
    }

    operator fun contains(other: Double): Boolean = other.asComplex() in this

    operator fun contains(other: Bounds): Boolean = other.min in this && other.max in this

    fun intersects(other: Bounds): Boolean {
        if (this.min in other || this.max in other)
            return true
        if (other.min in this || other.max in this)
            return true
        return false
    }

    fun union(other: Bounds): Bounds = Bounds(Bounds(this.min, other.min).min, Bounds(this.max, other.max).max)

    fun intersect(other: Bounds): Bounds? {
        val ret = Bounds(Bounds(this.min, other.min).max, Bounds(this.max, other.max).min)
        if (ret in this && ret in other)
            return ret
        return null
    }

    fun findGap(other: Bounds): Bounds? {
        val ret = Bounds(Bounds(this.min, other.min).max, Bounds(this.max, other.max).min)
        if (ret in this && ret in other)
            return null
        return ret
    }

    fun toList(separations: Int): List<ComplexNumber> =
            Array(separations) { min + (max - min) * (it.toDouble() / separations) }.toList()

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Bounds

        if (min != other.min) return false
        if (max != other.max) return false

        return true
    }

    override fun hashCode(): Int {
        var result = min.hashCode()
        result = 31 * result + max.hashCode()
        return result
    }

    override fun toString(): String {
        return "[$min, $max]"
    }

    companion object {

        val DEFAULT_10 = Bounds(-10.0, 10.0)

    }

}