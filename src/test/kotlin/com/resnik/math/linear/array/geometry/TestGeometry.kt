package com.resnik.math.linear.array.geometry

import com.resnik.math.linear.array.ArrayPoint
import com.resnik.math.linear.array.ArrayPoint2d
import org.junit.Test
import java.awt.Color

class TestGeometry {

    @Test
    fun testShapeCollection(){
        val shapeCollection = ShapeCollection(width = 500, height = 500, pointRadius = 10)
        val triangle = Triangle(ArrayPoint(0.0,0.0), ArrayPoint(0.5, 0.5), ArrayPoint(-1.0, 1.0))
        val circle = Circle(ArrayPoint(2.0, 2.0), 1.0)
        val numRand = 100
        shapeCollection.addShape(triangle, Color.RED)
        shapeCollection.addShape(circle, Color.BLUE)
        val minX = -5
        val maxX = 5
        val minY = -5
        val maxY = 5
        repeat(numRand){
            shapeCollection.addPoint(ArrayPoint2d(
                    Math.random()*(maxX - minX) + minX,
                    Math.random()*(maxY - minY) + minY
            ), Color(Math.random().toFloat(), Math.random().toFloat(), Math.random().toFloat()))
        }
        shapeCollection.render()
    }

}