package com.resnik.math.stats.interpolation

import com.resnik.math.linear.array.ArrayPoint2d
import com.resnik.math.linear.array.ArrayVector

class LinearInterpolation(val x1 : Double, val y1 : Double, val x2 : Double, val y2 : Double) : Function1<Double, Double> {

    constructor(p1 : ArrayPoint2d, p2 : ArrayPoint2d) : this(p1.x(), p1.y(), p2.x(), p2.y())

    override fun invoke(x: Double): Double {
        return (y2 - y1) / (x2 - x1) * (x - x1) + y1
    }

    fun coeff() : ArrayVector {
        val ret = ArrayVector(2)
        ret[0] = -x1 * (y2 - y1) / (x2 - x1) + y1
        ret[1] = (y2 - y1) / (x2 - x1)
        return ret
    }
}