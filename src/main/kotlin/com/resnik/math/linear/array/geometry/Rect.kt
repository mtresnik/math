package com.resnik.math.linear.array.geometry

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

    companion object {

        fun minimize(rects : Collection<Rect>) : Pair<Double, Double> = rects.map { it.width }.min()!! to rects.map { it.height }.min()!!

        fun maximize(rects : Collection<Rect>) : Pair<Double, Double> = rects.map { it.width }.max()!! to rects.map { it.height }.max()!!

    }

    fun getNeighborIndices(rectList : List<Rect>, minWidth : Double = 0.001, minHeight : Double = 0.001) : List<Int> {
        // Check if boundary points are within other rects
        /*
        * x: not neighbor
        * o: neighbor
        * c: center
        *
        * [x][o][x]
        * [o][c][o]
        * [x][o][x]
        *
        * */
        val numX = (2 * width / minWidth).coerceAtLeast(3.0).toInt()
        val dx = width / numX
        val numY = (2 * height / minHeight).coerceAtLeast(3.0).toInt()
        val dy = height / numY
        val testPoints = mutableListOf<ArrayPoint2d>()
        // Top and bottom rect(s)
        repeat(numX) { col ->
            val tx = this.x + col*dx
            testPoints.add(ArrayPoint2d(tx, this.y - dy))
            testPoints.add(ArrayPoint2d(tx, this.y + height + dy))
        }
        repeat(numY) { row ->
            val ty = this.y + row*dy
            testPoints.add(ArrayPoint2d(this.x - dx, ty))
            testPoints.add(ArrayPoint2d(this.x + width + dx, ty))
        }
        return rectList.indices.filter { index -> rectList[index] != this && testPoints.any { point -> point in rectList[index] } }
    }

    override fun toString(): String = "($x,$y)"


}