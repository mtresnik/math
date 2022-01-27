package com.resnik.math.optimize

import com.resnik.math.linear.array.ArrayPoint

class DataPoint (val input : ArrayPoint, val result : Double) {
    override fun toString(): String = "($input,$result)"
}