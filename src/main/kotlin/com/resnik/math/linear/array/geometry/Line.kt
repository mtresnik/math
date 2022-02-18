package com.resnik.math.linear.array.geometry

import com.resnik.math.linear.array.ArrayPoint

class Line(val from : ArrayPoint, val to : ArrayPoint) {

    val length = from.distanceTo(to)

    fun split(n : Int) : List<ArrayPoint> {
        if(n <= 0) return listOf()
        if(n == 1) return listOf(from)
        if(n == 2) return listOf(from, to)
        val dV = (to - from) / n
        /*
        * n = 3
        * 0 --> 1 --> 2
        * s --> m --> end
        *
        * n = 4
        * 0 --> 1 --> 2 --> 3
        * s --> x --> y --> end
        * */
        val retList = mutableListOf<ArrayPoint>()
        repeat(n) { retList.add(dV * it  + from) }
        return retList
    }

}