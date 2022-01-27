package com.resnik.math.linear.array

@FunctionalInterface
interface ArrayVectorFunction {

    fun apply(vector: ArrayVector) : ArrayVector

}