package com.resnik.math.linear.array.geometry

import com.resnik.math.linear.array.ArrayPoint
import com.resnik.math.linear.array.ArrayPoint2d
import kotlin.math.*

class Circle(val center: ArrayPoint, radius: Double) : Shape2d<Circle> {

    val radius: Double = radius.absoluteValue

    override fun contains(point: ArrayPoint): Boolean = (center.distanceTo(point) <= radius)

    override fun area(): Double = Math.PI * radius.pow(2)

    override fun getPoints(): List<ArrayPoint> = getBorderPoints()

    fun getBorderPoints(n: Int = 500): List<ArrayPoint> {
        val retList = mutableListOf<ArrayPoint>()
        val dTheta = 2 * PI / n
        repeat(n) { it ->
            val theta = dTheta * it
            val x = cos(theta) * radius + center.x()
            val y = sin(theta) * radius + center.y()
            retList.add(ArrayPoint2d(x, y))
        }
        return retList
    }

    override fun generate(points: List<ArrayPoint>): Circle = Circle(points.first(), radius)

}