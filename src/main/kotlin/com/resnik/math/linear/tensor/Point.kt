package com.resnik.math.linear.tensor

import com.resnik.math.symbo.algebra.operation.Operation

class Point(vararg val values : Operation) {

    val dim = values.size

    fun x(): Operation = this[0]

    fun y(): Operation = this[1]

    fun z(): Operation = this[2]

    fun w(): Operation = this[3]

    operator fun minus(other: Point) : Vector = Vector(Array(dim){ this[it] - other[it]})

    operator fun plus(other: Vector) : Point = Point(*Array(dim){ this[it] + other[it].value})

    open fun distanceTo(other: Point): Scalar = (this - other).magnitude()

    fun distanceTo(p1: Point, p2: Point): Scalar = p1.distanceTo(p2)

    fun toVector() : Vector = Vector(Array(dim){ this[it]})

    fun cross(p1: Point, p2: Point) : Operation {
        val thisX = this.x()
        val thisY = this.y()
        val x1 = p1.x()
        val y1 = p1.y()
        val x2 = p2.x()
        var y2 = p2.y()
        return (x2 - x1) * (thisY - y1) - (y2 - y1) * (thisX - x1)
    }

    operator fun get(index: Int) : Operation = values[index]

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        if (values.contentEquals((other as Point).values)) return true
        return false
    }

    override fun hashCode(): Int = values.contentHashCode()

    override fun toString(): String = "(${values.contentToString()})"
}