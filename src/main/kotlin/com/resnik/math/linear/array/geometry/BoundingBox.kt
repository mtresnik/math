package com.resnik.math.linear.array.geometry

import com.resnik.math.linear.array.ArrayPoint

open class BoundingBox(vararg points : ArrayPoint) : Shape2d<ConcaveHull> {

    val minDim : Int = points.minBy { it.dim }!!.dim
    val minPoint : ArrayPoint
    val maxPoint : ArrayPoint
    val _points : MutableList<ArrayPoint> = mutableListOf()

    init {
        minPoint = ArrayPoint(*DoubleArray(minDim){index -> points.minBy { it[index] }!![index]})
        maxPoint = ArrayPoint(*DoubleArray(minDim){index -> points.maxBy { it[index] }!![index]})
        _points.add(ArrayPoint(minX(), minY()))
        _points.add(ArrayPoint(minX(), maxY()))
        _points.add(ArrayPoint(maxX(), maxY()))
        _points.add(ArrayPoint(maxX(), minY()))
    }

    open fun minX() : Double = minPoint.x()
    open fun maxX() : Double = maxPoint.x()

    open fun minY() : Double = minPoint.y()
    open fun maxY() : Double = maxPoint.y()

    override fun area(): Double {
        var ret = 1.0
        repeat(minDim){
            ret *= (maxPoint[it] - minPoint[it])
        }
        return ret
    }

    override fun contains(point: ArrayPoint): Boolean {
        if(point.dim < minDim)
            return false
        repeat(minDim){
            if(point[it] < minPoint[it])
                return false
            if(point[it] > maxPoint[it])
                return false
        }
        return true
    }

    override fun getPoints(): List<ArrayPoint> = _points.toMutableList()

    override fun generate(points: List<ArrayPoint>): ConcaveHull {
        return ConcaveHull(*points.toTypedArray())
    }
}