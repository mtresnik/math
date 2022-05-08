package com.resnik.math.linear.array.geometry

import com.resnik.math.linear.array.ArrayPoint
import kotlin.math.absoluteValue
import kotlin.math.pow

class Sphere(val center: ArrayPoint, radius: Double) : Shape3d<Sphere> {

    val radius: Double = radius.absoluteValue

    override fun volume(): Double = (4.0 / 3.0) * Math.PI * radius.pow(3)

    override fun contains(point: ArrayPoint): Boolean = Sphere(center, point.distanceTo(center)).volume() <= volume()

    override fun getPoints(): List<ArrayPoint> = mutableListOf(center)

    override fun generate(points: List<ArrayPoint>): Sphere = Sphere(points.first(), radius)
}