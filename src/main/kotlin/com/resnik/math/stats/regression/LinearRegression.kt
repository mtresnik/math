package com.resnik.math.stats.regression

import com.resnik.math.linear.array.ArrayPoint
import com.resnik.math.stats.Coefficients
import com.resnik.math.stats.mean
import com.resnik.math.stats.stddev
import kotlin.math.pow

class LinearRegression(private val x: DoubleArray, private val y: DoubleArray) : Function1<Double, Double> {

    private val xBar: Double = x.mean()
    private val xStdDev: Double = x.stddev(xBar)
    private val yBar: Double = y.mean()
    private val yStdDev: Double = y.stddev(yBar)
    private val r: Double = Coefficients.pearsonCorrelation(x, xBar, y, yBar)
    private val b = r * yStdDev / xStdDev
    private val a = yBar - b * xBar
    val rSquared: Double = 1.0 -
            x.indices.sumOf { index -> (this(x[index]) - yBar).pow(2) } /
            x.indices.sumOf { index -> (y[index] - yBar).pow(2) }



    constructor(points: Collection<ArrayPoint>) : this(
        points.map { pt -> pt.x() }.toDoubleArray(),
        points.map { pt -> pt.y() }.toDoubleArray()
    )

    override fun invoke(p1: Double): Double {
        return a + b * p1
    }


}