package com.resnik.math.linear.array.geometry

import com.resnik.math.linear.array.ArrayPoint
import com.resnik.math.linear.array.ArrayPoint2d

open class Rect(val x : Double, val y : Double, val width : Double, val height : Double) {

    constructor(topLeft : ArrayPoint2d, width: Double, height: Double) : this(topLeft.x(), topLeft.y(), width, height)

    operator fun contains(point2d: ArrayPoint2d) : Boolean {
        val x1 = point2d.x()
        val y1 = point2d.y()
        return (x1 >= x && x1 < x + width) && (y1 >= y && y1 < y + height)
    }

    fun split(x1 : Double, y1 : Double) : RectDivision? {
        // Splits a rect based on the point
        if(ArrayPoint2d(x1, y1) !in this)
            return null
        val leftWidth = x1 - x
        val rightWidth = x + width - x1
        val topHeight = y1 - y
        val bottomHeight = y + height - y1

        val topRight =      Rect(x1, y, rightWidth, topHeight)
        val topLeft =       Rect(x, y, leftWidth, topHeight)
        val bottomLeft =    Rect(x, y1, leftWidth, bottomHeight)
        val bottomRight =   Rect(x1, y1, rightWidth, bottomHeight)
        return RectDivision(topRight, topLeft, bottomLeft, bottomRight)
    }

    fun subDivide() : RectDivision {
        return split(x + width / 2, y + height / 2)!!
    }

    fun area() : Double = width * height

    fun toLines() : List<Line> {
        val ret = mutableListOf<Line>()
        val topLeft = ArrayPoint2d(x, y)
        val topRight = ArrayPoint2d(x + width, y)
        val bottomRight = ArrayPoint2d(x + width, y + height)
        val bottomLeft = ArrayPoint2d(x, y + height)
        ret.add(Line(topLeft, topRight))
        ret.add(Line(topRight, bottomRight))
        ret.add(Line(bottomRight, bottomLeft))
        ret.add(Line(bottomLeft, topLeft))
        return ret
    }

    class RectDivision(var topRight : Rect?, var topLeft : Rect?, var bottomLeft : Rect?, var bottomRight : Rect?)

}