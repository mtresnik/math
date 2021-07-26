package com.resnik.math.linear.array.geometry

import com.resnik.math.linear.array.ArrayPoint

class BoundingBox(vararg val points : ArrayPoint) : Shape2d {

    val minDim : Int = points.minBy { it.dim }!!.dim
    val minPoint : ArrayPoint
    val maxPoint : ArrayPoint

    init {
        minPoint = ArrayPoint(*DoubleArray(minDim){index -> points.minBy { it[index] }!![index]})
        maxPoint = ArrayPoint(*DoubleArray(minDim){index -> points.maxBy { it[index] }!![index]})
    }

    fun minX() : Double = minPoint.x()
    fun maxX() : Double = maxPoint.x()

    fun minY() : Double = minPoint.y()
    fun maxY() : Double = maxPoint.y()

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

    override fun getPoints(): List<ArrayPoint> = points.toMutableList()
}