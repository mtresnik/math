package com.resnik.math.linear.array

class ArrayTensorCoordIterator(region : ArrayTensorRegion) :
    ArrayTensorCoordIteratorInterface {

    val startCoord = IntArray(region.first.size())
    val currentCoord = IntArray(region.first.size())
    val finalCoord = IntArray(region.second.size())
    var currentCount = 0
    var finalIndex = 1

    init {
        region.first.values.indices.forEach {
            startCoord[it] = region.first[it].coerceAtMost(region.second[it])
            currentCoord[it] = startCoord[it]
            finalCoord[it] = region.first[it].coerceAtLeast(region.second[it]) - 1

            finalIndex *= (finalCoord[it] - startCoord[it]) + 2
        }
        currentCoord[0]--
    }

    override fun numTraversed(): Int = currentCount

    override fun coords(): IntArray  = currentCoord

    override fun hasNext(): Boolean = currentCount < finalIndex

    override fun next(): IntArray {
        currentCoord.indices.forEach {
            if(currentCoord[it] > finalCoord[it]){
                currentCount += (currentCoord[it] - finalCoord[it] - 1)
                currentCoord[it] = startCoord[it]
            }else{
                currentCoord[it] = currentCoord[it] + 1
                currentCount++
                return currentCoord
            }
        }
        throw IndexOutOfBoundsException("Please use hasNext().")
    }
}