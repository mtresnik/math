package com.resnik.math.linear.array

import com.resnik.math.getOrDefault
import com.resnik.math.plus
import java.util.*

class ArrayTensorRegionIterator : Iterator<ArrayTensor> {

    lateinit var tensor : ArrayTensor
    lateinit var regionSizes : IntArray
    lateinit var bottomCorner : IntArray
    lateinit var topCorner : IntArray

    var coordIterator: ArrayTensorCoordIteratorInterface

    constructor(tensor: ArrayTensor, regionSizes : IntArray, padding: IntArray = IntArray(0)) {
        init(tensor, regionSizes, padding)
        this.coordIterator = ArrayTensorCoordIterator(
            ArrayTensorRegion(
                ArrayTensorIndex(*bottomCorner),
                ArrayTensorIndex(*topCorner)
            )
        )
    }

    constructor(tensor: ArrayTensor, regionSizes : IntArray, padding: IntArray = IntArray(0), strides : IntArray = IntArray(0)){
        init(tensor, regionSizes, padding)
        this.coordIterator = ArrayTensorStrideCoordIterator(
            ArrayTensorRegion(
                ArrayTensorIndex(
                    *bottomCorner
                ), ArrayTensorIndex(*topCorner)
            ), strides
        )
    }

    fun init(tensor: ArrayTensor, regionSizes : IntArray, padding: IntArray = IntArray(0)){
        this.tensor = tensor
        var regionSizesCopy = regionSizes
        if(regionSizesCopy.size < tensor.dim.size()){
            regionSizesCopy = regionSizes.copyOf(tensor.dim.size())
            Arrays.fill(regionSizesCopy, regionSizes.size, regionSizesCopy.size, 1)
        }
        regionSizesCopy.indices.forEach { regionSizesCopy[it] -- }
        this.regionSizes = regionSizesCopy
        this.bottomCorner = IntArray(tensor.dim.size()){-padding.getOrDefault(it, 0)}
        println("TensorDim: ${tensor.dim} \t RegionDim: ${regionSizes.contentToString()}")
        this.topCorner = IntArray(tensor.dim.size()){tensor.dim.values.getOrDefault(it, 0) - regionSizes.getOrDefault(it, 0) + padding.getOrDefault(it, 0)}
    }

    fun numTraversed() : Int = coordIterator.numTraversed()

    fun coords() : IntArray = coordIterator.coords()

    override fun hasNext(): Boolean = coordIterator.hasNext()

    override fun next(): ArrayTensor {
        val regionBottom = coordIterator.next()
        val regionTop = regionBottom + regionSizes
        return tensor.getRegion(regionBottom, regionTop)
    }
}