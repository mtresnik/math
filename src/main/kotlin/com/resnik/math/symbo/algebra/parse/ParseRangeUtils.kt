package com.resnik.math.symbo.algebra.parse

class ParseRangeUtils {

    companion object {
        fun inRange(query: Double, x1: Double, x2: Double): Boolean {
            val max = x1.coerceAtLeast(x2)
            val min = x1.coerceAtMost(x2)
            return query in min..max
        }

        fun inRange(query: Int, x1: Int, x2: Int): Boolean {
            val max = x1.coerceAtLeast(x2)
            val min = x1.coerceAtMost(x2)
            return query in min..max
        }

        fun inBox(qx: Double, qy: Double, x1: Double, y1: Double, x2: Double, y2: Double): Boolean {
            return inRange(qx, x1, x2) && inRange(qy, y1, y2)
        }
    }

}