package com.resnik.math.linear.array.geometry

import com.resnik.math.linear.array.ArrayPoint

interface Shape {

    operator fun contains(point : ArrayPoint) : Boolean

    fun getPoints() : List<ArrayPoint>

}