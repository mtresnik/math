package com.resnik.math.linear.array

import java.lang.IllegalArgumentException
import java.util.*
import java.util.stream.DoubleStream
import kotlin.math.abs

class ArrayTensor(val dim : ArrayDimension, init: (Int) -> Double = {0.0}) {

    val values : DoubleArray = DoubleArray(dim.dimProduct()){init(it)}

    operator fun get(at : ArrayTensorIndex) : Double {
        if(at.size() != dim.size()){
            throw IllegalArgumentException("Size of index must be size of dimensions.")
        }
        return values[at.toInt(dim)]
    }

    operator fun get(at : IntArray) : Double {
        if(at.size != dim.size()){
            throw IllegalArgumentException("Size of index must be size of dimensions.")
        }
        return try{
            var sum = 0
            var product = 1
            values.indices.forEach {
                sum += at[it] * product
                product *= dim[it]
            }
            values[sum]
        }catch (e : Exception){
            0.0
        }
    }

    fun getRegion(region: ArrayTensorRegion) : ArrayTensor = getRegion(region.first, region.second)

    fun getRegion(a : ArrayTensorIndex, b : ArrayTensorIndex) : ArrayTensor = getRegion(a.values, b.values)

    fun getRegion(a : IntArray, b : IntArray) : ArrayTensor {
        val newDimensions = IntArray(a.size){ abs(b[it] - a[it]) + 1 }
        val region = ArrayTensor(ArrayDimension(*newDimensions))
        val iter = ArrayTensorCoordIterator(
            ArrayTensorRegion(
                ArrayTensorIndex(*a),
                ArrayTensorIndex(*b)
            )
        )
        while(iter.hasNext()){
            val currentCoords = iter.next()
            region.values[iter.currentCount - 1] = this[currentCoords]
        }
        return region
    }

    fun append(other: ArrayTensor, newRank : Int) : ArrayTensor {
        val newDimSizes = dim.values.copyOf(newRank)
        (dim.size() until newDimSizes.lastIndex).forEach {
            newDimSizes[it] = 1
        }
        if(other.dim.size() >= newRank){
            newDimSizes[newDimSizes.lastIndex] += other.dim[other.dim.lastIndex()]
        }else{
            newDimSizes[newDimSizes.lastIndex]++
        }
        val newValues = DoubleStream.concat(Arrays.stream(this.values), Arrays.stream(other.values)).toArray()
        return ArrayTensor(ArrayDimension(*newDimSizes)) { newValues[it] }
    }

    fun clone(init: (Int) -> Double = {values[it]}) : ArrayTensor = ArrayTensor(this.dim) { init(it) }

    fun flip() : ArrayTensor {
        val ret = clone{0.0}
        val top = IntArray(dim.size()){dim[it] - 1}
        val region : ArrayTensorRegion =
            ArrayTensorRegion(
                ArrayTensorIndex(
                    0,
                    0,
                    0
                ), ArrayTensorIndex(*top)
            )
        val coordIterator = ArrayTensorCoordIterator(region)
        while(coordIterator.hasNext()){
            val coords = coordIterator.next()
            val flipped = IntArray(coords.size){this.dim[it] - 1 - coords[it]}
            ret[ArrayTensorIndex(*flipped)] = get(flipped)
        }
        return ret
    }

    fun dimCheck(other: ArrayTensor) {
        if(other.dim != this.dim){
            throw IllegalArgumentException("Dimension mismatch. ${this.dim} != ${other.dim}")
        }
    }

    operator fun set(at : ArrayTensorIndex, value : Double){
        if(at.size() != dim.size()){
            throw IllegalArgumentException("Size of index must be size of dimensions.")
        }
        // println("Index: $at \t Dim: $dim")
        values[at.toInt(dim)] = value
    }

    operator fun times(other : ArrayTensor) : ArrayTensor {
        dimCheck(other)
        return this.applyIndexed { this.values[it] * other.values[it] }
    }

    operator fun times(other : Double) : ArrayTensor {
        return this.apply { it * other }
    }

    fun innerProduct(other: ArrayTensor) : Double = (this * other).sum()

    operator fun plus(other : ArrayTensor) : ArrayTensor {
        dimCheck(other)
        return this.applyIndexed { this.values[it] + other.values[it] }
    }

    fun sum() : Double = this.values.sum()

    operator fun minus(other: ArrayTensor) : ArrayTensor {
        dimCheck(other)
        return this.applyIndexed { this.values[it] - other.values[it] }
    }

    fun minus(other: ArrayTensor, scale : Double) : ArrayTensor {
        dimCheck(other)
        return this.applyIndexed { this.values[it] - other.values[it] * scale }
    }

    fun applyIndexed(func : Function1<Int, Double>) : ArrayTensor {
        val ret = ArrayTensor(this.dim)
        ret.values.indices.forEach { ret.values[it] = func(it) }
        return ret
    }

    fun apply(func : Function1<Double, Double>) : ArrayTensor {
        val ret = ArrayTensor(this.dim)
        ret.values.indices.forEach { ret.values[it] = func(this.values[it]) }
        return ret
    }

    fun regionIterator(regionSizes : IntArray, padding: IntArray = IntArray(0)) : ArrayTensorRegionIterator
            = ArrayTensorRegionIterator(this, regionSizes, padding)

    fun regionIterator(regionSizes : IntArray, padding: IntArray = IntArray(0), strides : IntArray = IntArray(0)) : ArrayTensorRegionIterator
            =
        ArrayTensorRegionIterator(this, regionSizes, padding, strides)

    fun maxIndex() : Int = this.values.indices.maxBy { this.values[it] }!!

}