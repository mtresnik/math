package com.resnik.math.linear.array.geometry

import com.resnik.math.linear.array.ArrayPoint

class Line(val from : ArrayPoint, val to : ArrayPoint) {

    val length = from.distanceTo(to)

}