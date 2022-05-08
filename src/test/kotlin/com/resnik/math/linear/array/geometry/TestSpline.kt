package com.resnik.math.linear.array.geometry

import com.resnik.math.linear.array.ArrayPoint
import com.resnik.math.linear.array.ArrayPoint2d
import com.resnik.math.linear.array.geometry.Spline
import org.junit.Ignore
import org.junit.Test
import java.awt.Color

class TestSpline {

    @Test
    fun testSpline1(){
        val point1 = ArrayPoint(0.0, 0.5)
        val point2 = ArrayPoint(1.0, 2.0)
        val spline = Spline(point1, point2)
        println(spline)
        println(spline.tValues())
        val t = 0.5
        val point3 = spline[t]
        println(point3)
    }

    @Test
    fun testSpline2(){
        val point1 = ArrayPoint(0.0, 0.5)
        val point2 = ArrayPoint(1.0, 2.0)
        val point3 = ArrayPoint(1.5, 2.5)
        val spline = Spline(point1, point2, point3)
        println(spline)
        println(spline.tValues())
        val t = 0.8
        val point4 = spline[t]
        println(point4)
    }

    @Ignore
    @Test
    fun testSplineRender() {
        val startX = -2.0
        val endX = 3.0
        val numX = 10
        val stepX = (endX - startX) / numX
        val points = mutableListOf<ArrayPoint2d>()
        repeat(numX) {
            val x = it * stepX + startX
            val y = x * (x - 2) * (x + 1)
            points.add(ArrayPoint2d(x, y))
        }
        val spline = Spline2d(*points.toTypedArray())
        val shapeCollection = ShapeCollection(width = 500, height = 500, pointRadius = 10)
        shapeCollection.addSpline(spline)
        val startNum = -1.0
        val endNum = 2.0
        val steps = 20
        val stepSize = (endNum - startNum) / steps
        var currNum = startNum
        repeat(steps + 1){
            shapeCollection.addPoint(spline[currNum].to2d(), Color.RED)
            currNum += stepSize
        }
        shapeCollection.render()
    }

}