package com.resnik.math.linear.array

interface ArrayTensorCoordIteratorInterface : Iterator<IntArray> {

    fun numTraversed() : Int

    fun coords() : IntArray

}