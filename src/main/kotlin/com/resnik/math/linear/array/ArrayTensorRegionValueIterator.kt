package com.resnik.math.linear.array

class ArrayTensorRegionValueIterator(val tensor: ArrayTensor, regionSizes : IntArray, padding : IntArray) : Iterator<ArrayTensorValueIterator> {

    val iter : ArrayTensorRegionIterator =
        ArrayTensorRegionIterator(tensor, regionSizes, padding)

    override fun hasNext(): Boolean = iter.hasNext()

    override fun next(): ArrayTensorValueIterator {
        val bottomCorner = iter.coordIterator.next()
        val topCorner = IntArray(bottomCorner.size){bottomCorner[it] - iter.regionSizes[it]}
        return ArrayTensorValueIterator(
            tensor,
            ArrayTensorCoordIterator(
                ArrayTensorRegion(
                    ArrayTensorIndex(*bottomCorner),
                    ArrayTensorIndex(*topCorner)
                )
            )
        )
    }

}