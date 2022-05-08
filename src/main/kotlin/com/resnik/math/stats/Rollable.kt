package com.resnik.math.stats

import com.resnik.math.util.CountList

abstract class Rollable {

    abstract fun roll(): Int

    fun roll(n: Int): CountList<Int> {
        val ret = CountList<Int>()
        repeat(n) {
            ret + roll()
        }
        return ret
    }

}