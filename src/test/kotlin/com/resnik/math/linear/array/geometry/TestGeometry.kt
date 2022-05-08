package com.resnik.math.linear.array.geometry

import com.resnik.math.linear.array.ArrayPoint
import com.resnik.math.linear.array.ArrayPoint2d
import org.junit.Ignore
import org.junit.Test
import java.awt.Color

class TestGeometry {

    @Ignore
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

    @Test
    fun testRectNeighbors() {
        // Get all rect neighbors
        val width = 1.0
        val height = 1.0
        val m = 5
        val n = 4
        val rectTiles = Array(m){ row -> Array(n){ col -> Rect(col*width, row*height, width, height)} }
        println(rectTiles.contentDeepToString())
        // Assuming they aren't in a simple 2d-array structure...
        val allTiles = rectTiles.flatten()
        // Assuming we don't know they are all the same height / width
        val (minWidth, minHeight) = Rect.minimize(allTiles)
        val neighbors = Array<List<Int>>(allTiles.size){ mutableListOf() }
        allTiles.forEachIndexed { index, rect ->
            neighbors[index] = rect.getNeighborIndices(allTiles, minWidth, minHeight)
        }
        println(neighbors.contentDeepToString())
    }

    @Test
    fun testRectNeighbors2() {
        // Rects of abnormal widths / heights
        val allTiles = mutableListOf<Rect>(
            Rect(0.0, 0.0, 1.0, 1.0), Rect(1.0, 0.0, 2.0, 1.0), Rect(3.0, 0.0, 1.0, 1.0),
            Rect(0.0, 1.0, 1.0, 1.0), Rect(1.0, 1.0, 2.0, 2.0), Rect(3.0, 1.0, 1.0, 1.0),
            Rect(0.0, 2.0, 1.0, 1.0), Rect(3.0, 2.0, 1.0, 2.0),
            Rect(0.0, 3.0, 3.0, 1.0)
        )
        val (minWidth, minHeight) = Rect.minimize(allTiles)
        val neighbors = Array<List<Int>>(allTiles.size){ mutableListOf() }
        allTiles.forEachIndexed { index, rect ->
            neighbors[index] = rect.getNeighborIndices(allTiles, minWidth, minHeight)
        }
        println(neighbors.contentDeepToString())
    }

}