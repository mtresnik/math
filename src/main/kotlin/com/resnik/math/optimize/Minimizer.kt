package com.resnik.math.optimize

import com.resnik.math.linear.array.ArrayPoint

interface Minimizer {

    fun minimize(func1 : (list : ArrayPoint) -> Double) : ArrayPoint

}