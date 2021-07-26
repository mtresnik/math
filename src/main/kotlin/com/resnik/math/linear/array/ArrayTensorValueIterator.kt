package com.resnik.math.linear.array

class ArrayTensorValueIterator(val tensor : ArrayTensor, val coordIterator: ArrayTensorCoordIterator) : Iterator<Double> {

    override fun hasNext(): Boolean = coordIterator.hasNext()

    override fun next(): Double = tensor[coordIterator.next()]

    fun innerProduct(other : ArrayTensor) : Double = other.values.indices.sumByDouble { next() * other.values[it] }

}