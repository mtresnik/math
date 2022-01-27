package com.resnik.math.optimize

class UnboundedGeneralMinimizer<T, K>(val optimizeDataset: OptimizeDataset<T, K>,
                                val numDirections : Int = optimizeDataset.getSize(optimizeDataset.genSeed()),
                                val numIterations : Int = 100,
                                val stepSize : Double = 10.0,
                                val numSteps : Int = 10) {

    fun genDirections() : MutableList<K> {
        return MutableList(numDirections){optimizeDataset.genDirection()}
    }

    fun getNextPoint(currPoint : T, directions : MutableList<K>, function: (data: T) -> Double) : T {
        // Clone directions into a step size oriented set, for big and small hops to see which has lower value
        var stepSizeOriented = mutableListOf<K>()
        repeat(numSteps) {
            val size = stepSize * (it + 1) / numSteps
            directions.forEach { vector ->
                stepSizeOriented.add(optimizeDataset.scale(vector, size))
            }
        }
        stepSizeOriented.sortBy { function(optimizeDataset.add(currPoint, it)) }
        directions.clear()
        directions.addAll(stepSizeOriented.take(numDirections))
        return optimizeDataset.add(currPoint, stepSizeOriented.first())
    }

    fun minimize(function: (data: T) -> Double): T {
        val seedPoint = optimizeDataset.genSeed()
        var currPoint = seedPoint
        val previousDirections = genDirections()
        currPoint = getNextPoint(currPoint = currPoint, directions = previousDirections, function = function)
        repeat(numIterations) {
            previousDirections.addAll(genDirections())
            currPoint = getNextPoint(currPoint = currPoint, directions = previousDirections, function = function)
        }
        return currPoint
    }



}