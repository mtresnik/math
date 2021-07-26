package com.resnik.math.linear.array

import com.resnik.math.linear.array.ArrayVector

@FunctionalInterface
interface ArrayVectorFunction {

    fun apply(vector: ArrayVector) : ArrayVector

}