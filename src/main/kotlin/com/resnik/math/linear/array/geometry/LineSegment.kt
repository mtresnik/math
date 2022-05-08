package com.resnik.math.linear.array.geometry

import com.resnik.math.linear.array.ArrayPoint
import kotlin.math.abs

open class LineSegment<T : ArrayPoint>(var from: T, open var to: T) {

    val length = from.distanceTo(to)

    fun split(n: Int): List<ArrayPoint> {
        if (n <= 0) return listOf()
        if (n == 1) return listOf(from)
        if (n == 2) return listOf(from, to)
        val dV = (to - from) / n
        /*
        * n = 3
        * 0 --> 1 --> 2
        * s --> m --> end
        *
        * n = 4
        * 0 --> 1 --> 2 --> 3
        * s --> x --> y --> end
        * */
        val retList = mutableListOf<ArrayPoint>()
        repeat(n) { retList.add(dV * it + from) }
        return retList
    }

    fun dx(): Double = this.to.x() - this.from.x()

    fun dy(): Double = this.to.y() - this.from.y()

    fun intersects(other: LineSegment<T>): Boolean {
        fun ABC(current: LineSegment<T>): List<Double> =
            listOf(current.dy(), current.dx(), current.dy() * current.from.x() + current.dx() * current.from.y())
        val (A1, B1, C1) = ABC(this)
        val (A2, B2, C2) = ABC(other)
        val denominator = A1 * B2 - A2 * B1
        if (denominator == 0.0) return false
        val x1 = from.x()
        val y1 = from.y()
        val x2 = to.x()
        val y2 = to.y()
        val x3 = (C1 * B2 - B1 * C2) / denominator
        val y3 = (A1 * C2 - A2 * C1) / denominator
        val dx = abs(x3 - x2)
        val dy = abs(y3 - y2)
        return abs(x3 - x1) + abs(x2 - x1) == dx && abs(y3 - y1) + abs(y2 - y1) == dy
    }

}