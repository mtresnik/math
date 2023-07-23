package com.resnik.math.stats.regression

import com.resnik.math.linear.array.ArrayPoint
import com.resnik.math.linear.array.geometry.Polynomial
import com.resnik.math.linear.tensor.Matrix
import com.resnik.math.stats.mean
import kotlin.math.pow

class PolynomialRegression(private val x: DoubleArray, private val y: DoubleArray, val order: Int) : Function1<Double, Double> {

    private val yBar: Double = y.mean()
    val designMatrix = generateDesignMatrix()
    val forwardMatrix = (designMatrix.transpose().dot(designMatrix)).inverse().dot(designMatrix.transpose())
    val w = forwardMatrix.dot(Matrix.generate(y))
    val polynomial = generatePolynomial()
    val rSquared: Double = 1.0 -
            x.indices.sumOf { index -> (this(x[index]) - yBar).pow(2) } /
            x.indices.sumOf { index -> (y[index] - yBar).pow(2) }

    constructor(points: Collection<ArrayPoint>, power: Int) : this(
        points.map { pt -> pt.x() }.toDoubleArray(),
        points.map { pt -> pt.y() }.toDoubleArray(),
        power
    )

    private fun generateDesignMatrix(): Matrix {
        val retMatrix = Matrix(x.size, this.order + 1)
        this.x.indices.forEach { index ->
            val xValue = this.x[index]
            repeat(this.order + 1) {col ->
                retMatrix[index][col] = xValue.pow(col)
            }
        }
        return retMatrix
    }
    override fun invoke(p1: Double): Double {
        return polynomial(p1)
    }

    fun generatePolynomial() : Polynomial {
        return Polynomial(*(0 until (this.order + 1)).map { power ->
            this.w[power][0].toScalar().value.toConstant().value.real
        }.toDoubleArray())
    }


}