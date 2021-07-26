package com.resnik.math.linear.array

import com.resnik.math.linear.array.ArrayMatrix

@FunctionalInterface
interface ArrayMatrixFunction {

    fun apply(matrix: ArrayMatrix) : ArrayMatrix

}