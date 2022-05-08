package com.resnik.math.plot

import com.resnik.math.linear.array.ArrayPoint2d
import com.resnik.math.linear.array.geometry.BoundingBox
import java.awt.Color
import java.awt.Graphics2D
import java.awt.image.BufferedImage

class Plot2d(
    private val colorMap: MutableMap<MutableList<ArrayPoint2d>, Color> = mutableMapOf(),
    width: Int = 500,
    height: Int = 500,
    padding: Int = 100,
    background: Color = Color.WHITE,
    axisColor: Color = Color.BLACK,
    lineColor: Color = Color.LIGHT_GRAY,
    axisThickness: Float = 5.0f,
    dataLineThickness: Float = 2.0f
) : Chart(width, height, padding, background, axisColor, lineColor, axisThickness, dataLineThickness) {


    var boundingBox: BoundingBox? = null

    fun add(points: MutableList<ArrayPoint2d>, color: Color = Color.BLACK) {
        colorMap[points] = color
        boundingBox = null
    }

    fun boundingBox(): BoundingBox = boundingBox ?: BoundingBox(*colorMap.keys.flatten().toTypedArray())

    fun getX(xVal: Double): Int =
        (width * (xVal - boundingBox().minX()) / (boundingBox().maxX() - boundingBox().minX())).toInt() + padding

    fun getY(yVal: Double): Int =
        height - (height * (yVal - boundingBox().minY()) / (boundingBox().maxY() - boundingBox().minY())).toInt() + padding

    fun getPoint(doublePoint: ArrayPoint2d): Pair<Int, Int> = Pair(getX(doublePoint.x()), getY(doublePoint.y()))

    fun drawLine(pt1: ArrayPoint2d, pt2: ArrayPoint2d, graphics2D: Graphics2D) {
        val px1 = getPoint(pt1)
        val px2 = getPoint(pt2)
        graphics2D.drawLine(px1.first, px1.second, px2.first, px2.second)
    }

    fun drawLines(points: MutableList<ArrayPoint2d>, color: Color, graphics2D: Graphics2D) {
        graphics2D.paint = color
        points.zipWithNext { pt1: ArrayPoint2d, pt2: ArrayPoint2d -> drawLine(pt1, pt2, graphics2D) }
    }

    fun drawPoint(pt1: ArrayPoint2d, graphics2D: Graphics2D) {
        val px1 = getPoint(pt1)
        graphics2D.drawOval(px1.first, px1.second, 5, 5)
    }

    fun drawPoints(points: MutableList<ArrayPoint2d>, color: Color, graphics2D: Graphics2D) {
        graphics2D.paint = color
        points.forEach { drawPoint(it, graphics2D) }
    }

    fun plot(drawLines: Boolean = true, drawPoints: Boolean = true): BufferedImage {
        val pair = renderAxes()
        val bufferedImage = pair.first
        val graphics2D = pair.second
        colorMap.forEach { (points, color) ->
            if (drawLines) drawLines(points, color, graphics2D)
            if (drawPoints) drawPoints(points, color, graphics2D)
        }
        return bufferedImage
    }

}