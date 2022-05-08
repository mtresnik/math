package com.resnik.math.optimize

import com.resnik.math.linear.array.ArrayPoint
import java.lang.Double.max
import java.lang.Double.min

class BruteForceMinimizer(
    val classifiers: List<ImmutableDimensionClassifier>,
    val iterSize: Int = 10,
    val numTake: Int = 10,
    val numSubMinimizers: Int = 5
) : Minimizer {

    private fun uniformScaled(t: Double): ArrayPoint {
        val retList = mutableListOf<Double>()
        classifiers.forEach { retList.add(it.scaled(t)) }
        return ArrayPoint(*retList.toDoubleArray())
    }

    private fun generateGrid(): List<ArrayPoint> {
        val numDimensions = classifiers.size
        var runningList = mutableListOf<MutableList<Double>>()
        repeat(iterSize) {
            // Manually do this for first dimension
            val t = it.toDouble() / (iterSize - 1)
            val first = classifiers.first().scaled(t)
            runningList.add(mutableListOf(first))
        }
        // Size of runningList is now iterSize
        (1 until numDimensions).forEach { dim ->
            val nextList = mutableListOf<MutableList<Double>>()
            runningList.forEach { point ->
                repeat(iterSize) {
                    val t = it.toDouble() / (iterSize - 1)
                    val scaled = classifiers[dim].scaled(t)
                    val cloned = mutableListOf<Double>()
                    cloned.addAll(point)
                    cloned.add(scaled)
                    nextList.add(cloned)
                }
            }
            runningList = nextList
        }
        // Map list of lists to list of points
        return runningList.map { ArrayPoint(*it.toDoubleArray()) }
    }

    fun generateClassifiers(points: List<ArrayPoint>): List<ImmutableDimensionClassifier> {
        val retList = mutableListOf<MutableDimensionClassifier>()
        repeat(classifiers.size) {
            // Set initial values of each min / max combo
            retList.add(MutableDimensionClassifier(Double.MAX_VALUE, -1.0 * Double.MAX_VALUE))
        }
        points.forEach { point ->
            // Iterate over dimensions, and bound on dimensions
            repeat(classifiers.size) { dim ->
                retList[dim].min = min(retList[dim].min, point[dim])
                retList[dim].max = max(retList[dim].max, point[dim])
            }
        }
        println(retList)
        return retList.map { it.toImmutableDimensionClassifier() }
    }

    override fun minimize(func1: (list: ArrayPoint) -> Double): ArrayPoint {
        // Minimize using bounding boxes of multiple dimensions
        // Make a grid in multiple dimensions
        // Then make a new classifier under this one using a generated list of classifiers (bounding box)
        val newPoints = generateGrid()
            .map { DataPoint(it, func1(it)) }
            .sortedBy { it.result }
            .take(numTake)
            .map { it.input }
        var currPoints = newPoints
        repeat(numSubMinimizers) {
            println(currPoints.first())
            val newClassifiers = generateClassifiers(currPoints)
            val subBruteForceMinimizer = BruteForceMinimizer(newClassifiers)
            currPoints = subBruteForceMinimizer.generateGrid()
                .map { DataPoint(it, func1(it)) }
                .sortedBy { it.result }.map { it.input }.take(numTake)
        }
        return currPoints.first()
    }

}