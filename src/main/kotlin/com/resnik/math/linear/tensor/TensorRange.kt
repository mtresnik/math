package com.resnik.math.linear.tensor

import java.lang.IllegalArgumentException

class TensorRange(val first : TensorIndex, val second : TensorIndex) {

    val minIndex : TensorIndex
    val maxIndex : TensorIndex

    init {
        if(first.size() != second.size()){
            throw IllegalArgumentException()
        }
        minIndex = TensorIndex(*Array(first.size()){ first[it].coerceAtMost(second[it]) }.toIntArray())
        maxIndex = TensorIndex(*Array(first.size()){ first[it].coerceAtLeast(second[it]) }.toIntArray())
    }

}