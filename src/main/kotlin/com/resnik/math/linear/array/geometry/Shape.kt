package com.resnik.math.linear.array.geometry

import com.resnik.math.linear.Quaternion
import com.resnik.math.linear.array.ArrayPoint
import com.resnik.math.linear.array.ArrayVector

interface Shape<T : Shape<T>> {

    operator fun contains(point: ArrayPoint): Boolean

    fun getPoints(): List<ArrayPoint>

    fun generate(points: List<ArrayPoint>): T

    fun rotate(theta: Double, about: ArrayVector): T =
        generate(getPoints().map { Quaternion(v = it.toVector3d()).rotate(theta, about).toPoint() })

}