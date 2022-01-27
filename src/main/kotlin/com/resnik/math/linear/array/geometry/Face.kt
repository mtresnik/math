package com.resnik.math.linear.array.geometry

import com.resnik.math.linear.array.ArrayPoint
import com.resnik.math.linear.array.ArrayVector

class Face(p1 : ArrayPoint, p2 : ArrayPoint, p3 : ArrayPoint) : Triangle(p1, p2, p3) {

    fun normal() : ArrayVector {
        val v1 = p2 - p1
        val v2 = p3 - p1
        return v1.cross(v2).normalized()
    }

}