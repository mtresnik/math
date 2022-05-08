package com.resnik.math.linear.array

@FunctionalInterface
interface ArrayMatrixFunction {

    fun apply(matrix: ArrayMatrix): ArrayMatrix

}