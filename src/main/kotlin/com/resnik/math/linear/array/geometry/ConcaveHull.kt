package com.resnik.math.linear.array.geometry

import com.resnik.math.linear.array.ArrayPoint

class ConcaveHull(vararg val points : ArrayPoint) : Shape2d<ConcaveHull> {

    val boundingBox : BoundingBox = BoundingBox(*Array<ArrayPoint>(points.size){points[it]})

    override fun contains(point : ArrayPoint): Boolean {
        if(!boundingBox.contains(point)){
            return false
        }
        val x = point.x()
        val y = point.y()
        // https://wrf.ecse.rpi.edu/Research/Short_Notes/pnpoly.html
        var inside = false
        var i = 0
        var j = points.size - 1
        while (i < points.size) {
            if (points[i].y() > y != points[j].y() > y &&
                    x < (points[j].x() - points[i].x()) * (y - points[i].y()) / (points[j].y() - points[i].y()) + points[i].x()) {
                inside = !inside
            }
            j = i++
        }
        return inside
    }

    override fun getPoints(): List<ArrayPoint> = points.asList()

    override fun generate(points: List<ArrayPoint>): ConcaveHull = ConcaveHull(*points.toTypedArray())

    override fun area(): Double = 0.0

    // TODO : Add get neighbors function similar to Rect#getNeighbors(list<rect>) : list<int>

}