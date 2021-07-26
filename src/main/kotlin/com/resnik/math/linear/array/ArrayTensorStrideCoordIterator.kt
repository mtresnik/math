package com.resnik.math.linear.array

import com.resnik.math.div
import com.resnik.math.minus
import com.resnik.math.plus
import com.resnik.math.times

class ArrayTensorStrideCoordIterator(region : ArrayTensorRegion, val strides : IntArray) :
    ArrayTensorCoordIteratorInterface {

    val corner1 = region.first.values
    val coordIterator = ArrayTensorCoordIterator(
        ArrayTensorRegion(
            ArrayTensorIndex(*IntArray(corner1.size)),
            ArrayTensorIndex(*((region.second.values - region.first.values) / strides))
        )
    )

    override fun numTraversed(): Int = coordIterator.numTraversed()

    override fun coords(): IntArray = coordIterator.coords()

    override fun hasNext(): Boolean = coordIterator.hasNext()

    override fun next(): IntArray = (coordIterator.next() * strides) + corner1

}