package com.resnik.math.optimize

import com.resnik.math.linear.array.ArrayPoint
import com.resnik.math.linear.array.geometry.Spline

class RangedMinimizer(val classifiers: List<ImmutableDimensionClassifier>, val subSplineSize: Int = 100) : Minimizer {

    private val UNIFORM_NUMBER = 100
    private val RANDOM_NUMBER = 20
    private val MOMENTUM = 2.0

    private fun uniformScaled(t: Double): ArrayPoint {
        val retList = mutableListOf<Double>()
        classifiers.forEach { retList.add(it.scaled(t)) }
        return ArrayPoint(*retList.toDoubleArray())
    }

    private fun random(): ArrayPoint {
        val retList = mutableListOf<Double>()
        classifiers.forEach { retList.add(it.random()) }
        return ArrayPoint(*retList.toDoubleArray())
    }

    private fun nextPoint(point1: ArrayPoint, point2: ArrayPoint, scale: Double = 1.0): ArrayPoint {
        val retList = mutableListOf<Double>()
        point1.values.indices.forEach { retList.add(scale * (point1[it] - point2[it] + point2[it])) }
        return ArrayPoint(*retList.toDoubleArray())
    }

    private fun coerceIn(list1: ArrayPoint): ArrayPoint {
        return ArrayPoint(*list1.values.mapIndexed { index: Int, d: Double -> classifiers[index].coerceIn(d) }
            .toDoubleArray())
    }

    private fun generateNext(func1: (point: ArrayPoint) -> Double, previous: List<DataPoint>): MutableList<DataPoint> {
        // Find direction from first and second, add result point to next list
        // Remove first element until new list is made, sort again
        val nextPoints = previous.zipWithNext { a: DataPoint, b: DataPoint ->
            // Generate set between the two minpoints of average between random and uniform
            val deltaResult = a.result - b.result
            val possibleNexts = mutableListOf<DataPoint>()
            val MAX = (UNIFORM_NUMBER + RANDOM_NUMBER) / 2
            repeat(MAX) {
                val t = 2.0 * (it.toDouble() / (MAX - 1) - 0.5)
                val possibleNext = nextPoint(a.input, b.input, t * deltaResult * MOMENTUM)
                // Scale movement by how close the two are
                val actualNext = coerceIn(possibleNext)
                possibleNexts.add(DataPoint(actualNext, func1(actualNext)))
            }
            possibleNexts.sortBy { it.result }
            possibleNexts.first()
        }
        nextPoints.sortedBy { it.result }
        return nextPoints.toMutableList()
    }

    override fun minimize(func1: (list: ArrayPoint) -> Double): ArrayPoint {
        val savedDatapoints = mutableListOf<DataPoint>()
        // Diagonal range
        // Uniform scale some amount of values in range
        val uniformDatapoints = mutableListOf<DataPoint>()
        repeat(UNIFORM_NUMBER) {
            // t range from 0.0 to 1.0
            val t = it.toDouble() / (UNIFORM_NUMBER - 1)
            val input = uniformScaled(t)
            uniformDatapoints.add(DataPoint(input, func1(input)))
        }
        // Random range
        val randomDatapoints = mutableListOf<DataPoint>()
        repeat(RANDOM_NUMBER) {
            val input = random()
            randomDatapoints.add(DataPoint(input, func1(input)))
        }
        // Combine ranges into seed points
        uniformDatapoints.sortBy { it.result }
        randomDatapoints.sortBy { it.result }
        val combined = mutableListOf<DataPoint>()
        repeat(UNIFORM_NUMBER / 2) {
            combined.add(uniformDatapoints[it])
        }
        repeat(RANDOM_NUMBER / 2) {
            combined.add(randomDatapoints[it])
        }
        // Combined list is now a spline of points
        combined.sortBy { it.result }
        // Save some results from initial seeding
        savedDatapoints.add(combined[0])
        savedDatapoints.add(combined[1])

        val numNextPoint = combined.size / 2
        var currPoints = combined
        repeat(numNextPoint) {
            val nextPoints = generateNext(func1, currPoints)
            savedDatapoints.add(nextPoints[0])
            savedDatapoints.add(nextPoints[1])
            currPoints = nextPoints
        }
        savedDatapoints.sortBy { it.result }
        println(savedDatapoints)
        val dataSpline = Spline(*savedDatapoints.map { it.input }.toTypedArray())
        val subSpline = dataSpline.generateSubSpline(steps = subSplineSize)
        val retPoints = subSpline.getPoints().map { DataPoint(it, func1(it)) }.toMutableList()
        retPoints.sortBy { it.result }
        return retPoints.first().input
    }

}