package com.resnik.math.stats.interpolation

import com.resnik.math.linear.array.ArrayMatrix
import com.resnik.math.linear.array.ArrayPoint2d
import com.resnik.math.linear.array.ArrayVector
import kotlin.math.pow

class QuadraticInterpolation(
    private val x1: Double,
    private val y1: Double,
    private val x2: Double,
    private val y2: Double,
    private val x3: Double,
    private val y3: Double
) : Function1<Double, Double> {

    val det = det(1.0, x1, x1.pow(2.0), 1.0, x2, x2.pow(2.0), 1.0, x3, x3.pow(2.0))
    val yBar = ArrayVector(y1, y2, y3).toColMatrix()
    val inv = inv()
    val coeff = coeff()


    constructor(p1: ArrayPoint2d, p2: ArrayPoint2d, p3: ArrayPoint2d) :
            this(
                p1.x(), p1.y(),
                p2.x(), p2.y(),
                p3.x(), p3.y()
            )

    fun det(a: Double, b: Double, c: Double, d: Double): Double {
        return a * d - b * c
    }

    fun det(a: Double, b: Double, c: Double, d: Double, e: Double, f: Double, g: Double, h: Double, i: Double): Double {
        return a * det(e, f, h, i) - b * det(d, f, g, i) + c * det(d, e, g, h)
    }

    fun coeff(): ArrayVector {
        return (inv * yBar).toVector()!!
    }

    fun inv(): ArrayMatrix {
        return ArrayMatrix(inv(1.0, x1, x1.pow(2.0), 1.0, x2, x2.pow(2.0), 1.0, x3, x3.pow(2.0)))
    }

    fun inv(
        a: Double,
        b: Double,
        c: Double,
        d: Double,
        e: Double,
        f: Double,
        g: Double,
        h: Double,
        i: Double
    ): Array<DoubleArray> {
        val ret = Array<DoubleArray>(3) { DoubleArray(3) { 0.0 } }
        ret[0][0] = (e * i - f * h) / det
        ret[0][1] = (c * h - b * i) / det
        ret[0][2] = (b * f - c * e) / det

        ret[1][0] = (f * g - d * i) / det
        ret[1][1] = (a * i - c * g) / det
        ret[1][2] = (c * d - a * f) / det

        ret[2][0] = (d * h - e * g) / det
        ret[2][1] = (b * g - a * h) / det
        ret[2][2] = (a * e - b * d) / det
        return ret
    }

    override fun invoke(x: Double): Double {
        return coeff[0] + coeff[1] * x + coeff[2] * x.pow(2.0)
    }

}