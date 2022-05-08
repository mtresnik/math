package com.resnik.math.optimize

import com.resnik.math.linear.array.ArrayPoint
import com.resnik.math.linear.array.ArrayVector

class UnboundedMinimizer(
    val numDimensions: Int,
    val numDirections: Int = numDimensions,
    val numIterations: Int = 100,
    val stepSize: Double = 10.0,
    val numSteps: Int = 10
) : Minimizer {

    fun genDirections(): MutableList<ArrayVector> {
        // Generate I number of directions to choose from
        // Complexity: I * D
        return MutableList(numDirections) { ArrayVector.randomGaussian(numDimensions) }
    }

    fun genSeed(): ArrayPoint = ArrayPoint.randomGaussian(numDimensions)

    fun getNextPoint(
        currPoint: ArrayPoint,
        directions: MutableList<ArrayVector>,
        func1: (list: ArrayPoint) -> Double
    ): ArrayPoint {
        // Clone directions into a step size oriented set, for big and small hops to see which has lower value
        var stepSizeOriented = mutableListOf<ArrayVector>()
        repeat(numSteps) {
            val size = stepSize * (it + 1) / numSteps
            directions.forEach { vector ->
                stepSizeOriented.add(vector * size)
            }
        }
        stepSizeOriented.sortBy { func1(it + currPoint) }
        directions.clear()
        directions.addAll(stepSizeOriented.take(numDirections))
        return stepSizeOriented.first() + currPoint
    }

    override fun minimize(func1: (list: ArrayPoint) -> Double): ArrayPoint {
        val seedPoint = genSeed()
        var currPoint = seedPoint
        val previousDirections = genDirections()
        currPoint = getNextPoint(currPoint = currPoint, directions = previousDirections, func1 = func1)
        repeat(numIterations) {
            previousDirections.addAll(genDirections())
            currPoint = getNextPoint(currPoint = currPoint, directions = previousDirections, func1 = func1)
        }
        return currPoint
    }

}