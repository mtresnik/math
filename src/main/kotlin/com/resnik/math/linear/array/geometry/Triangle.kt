package com.resnik.math.linear.array.geometry

import com.resnik.math.linear.array.ArrayPoint
import kotlin.math.sqrt

open class Triangle(val p1 : ArrayPoint, val p2 : ArrayPoint, val p3 : ArrayPoint) : Shape2d<Triangle> {

    override fun contains(point: ArrayPoint): Boolean {
        val area1 = Triangle(p1,p2,point).area()
        val area2 = Triangle(p2,p3,point).area()
        val area3 = Triangle(p3,p1,point).area()
        return (area1 + area2 + area3) == this.area()
    }

    override fun getPoints(): List<ArrayPoint> = mutableListOf(p1,p2,p3)

    override fun area(): Double {
        // Heron's Formula
        val a = p1.distanceTo(p2)
        val b = p2.distanceTo(p3)
        val c = p3.distanceTo(p1)
        val s = (a + b + c) / 2
        return sqrt(s * (s - a) * (s - b) * (s - c))
    }

    override fun generate(points: List<ArrayPoint>): Triangle = Triangle(points[0], points[1], points[2])

}