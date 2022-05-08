package com.resnik.math.linear.array.geometry

import com.resnik.math.linear.array.ArrayPoint

// Collection of points that can be interpolated
open class Spline(vararg val points: ArrayPoint) {

    private val TValues: DoubleArray

    init {
        if (points.size < 2)
            throw IllegalArgumentException("A spline requires at least two points.")
        TValues = DoubleArray(points.size) { 0.0 }
        // Align distance on spline with TValue equivalent
        val length = length()
        var sum = 0.0
        TValues[0] = sum
        var previous: ArrayPoint = points[0]
        points.forEachIndexed { index, arrayPoint ->
            val distance = previous.distanceTo(arrayPoint)
            sum += distance
            TValues[index] = sum / length
            previous = arrayPoint
        }
    }

    fun length(): Double = points.toList().zipWithNext { a: ArrayPoint, b: ArrayPoint -> a.distanceTo(b) }.sum()

    operator fun get(t: Double): ArrayPoint {
        // Interpolate point based on position in list
        // Anything < 0.0 will be interpolated from first two points
        // Anything > 1.0 will be interpolated from last two points

        // If point A maps to the 0.1th T value and point B maps to 0.2th T value, t=0.15 will average the two
        // Convert TValues to list find two indicies it's closest to

        // Find last index where t > T
        val first: Int = when (val lastIndex = TValues.indexOfLast { T -> t > T }) {
            -1 -> {
                0
            }
            TValues.lastIndex -> {
                TValues.lastIndex - 1
            }
            else -> {
                lastIndex
            }
        }
        val second = first + 1
        val tA = TValues[first]
        val pointA = points[first]
        val tB = TValues[second]
        val pointB = points[second]

        // Find distance between t and min
        val dt = tB - tA
        val dPoint = pointB - pointA
        // s is now between 0.0 and 1.0
        val s = (t - tA) / dt
        // Scale vector by s and add to original point
        return dPoint * s + pointA
    }

    fun generateSubSpline(min: Double = -1.0, max: Double = 2.0, steps: Int = 10): Spline {
        val dt = (max - min) / steps
        val points = mutableListOf<ArrayPoint>()
        repeat(steps) {
            points.add(get(min + it * dt))
        }
        return Spline(*points.toTypedArray())
    }

    fun getPoints(): List<ArrayPoint> = points.toList()

    override fun toString(): String = points.contentToString()

    fun tValues(): List<Double> = TValues.toList()

}