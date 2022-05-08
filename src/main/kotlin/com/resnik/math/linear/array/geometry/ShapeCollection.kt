package com.resnik.math.linear.array.geometry

import com.resnik.math.linear.array.ArrayPoint
import com.resnik.math.linear.array.ArrayPoint2d
import com.resnik.math.linear.array.ArrayVector
import java.awt.BasicStroke
import java.awt.Color
import java.awt.Graphics2D
import java.awt.image.BufferedImage
import javax.swing.ImageIcon
import javax.swing.JLabel
import javax.swing.JOptionPane
import kotlin.math.floor

class ShapeCollection(
    val width: Int = 1000,
    val height: Int = 1000,
    val background: Color = Color.WHITE,
    val pointRadius: Int = 20,
    val offset: Double = 1.0
) {

    val shapes: MutableMap<Shape<*>, Color> = mutableMapOf()
    val vectors: MutableMap<ArrayVector, Color> = mutableMapOf()
    val points: MutableMap<ArrayPoint2d, Color> = mutableMapOf()
    val lineSegments: MutableMap<LineSegment<ArrayPoint>, Color> = mutableMapOf()
    val splines: MutableMap<Spline2d, Color> = mutableMapOf()
    lateinit var boundingBox: BoundingBox

    fun addShape(shape: Shape<*>, color: Color = Color.BLACK) {
        this.shapes[shape] = color
    }

    fun addPoint(point: ArrayPoint2d, color: Color = Color.BLACK) {
        this.points[point] = color
    }

    fun addVector(vector: ArrayVector, color: Color = Color.BLACK) {
        this.vectors[vector] = color
    }

    fun addLineSegment(lineSegment: LineSegment<ArrayPoint>, color: Color = Color.BLACK) {
        this.lineSegments[lineSegment] = color
    }

    fun addSpline(spline: Spline2d, color: Color = Color.BLACK) {
        this.splines[spline] = color
    }

    fun drawCenteredCircle(g: Graphics2D, x: Int, y: Int, r: Int) = g.fillOval(x - r / 2, y - r / 2, r, r)

    fun convertPixels(point: ArrayPoint2d): Pair<Int, Int> {
        val x = point.x
        val y = point.y
        val relX = (x - boundingBox.minX()) / (boundingBox.maxX() - boundingBox.minX())
        val relY = (y - boundingBox.minY()) / (boundingBox.maxY() - boundingBox.minY())
        return Pair(floor(relX * width).toInt(), floor(relY * height).toInt())
    }

    fun drawPoint(color: Color, point: ArrayPoint2d, graphics2D: Graphics2D) {
        val coordinate = convertPixels(point)
        graphics2D.paint = color
        drawCenteredCircle(graphics2D, coordinate.first, coordinate.second, pointRadius)
    }

    fun drawLine(color: Color, p1: ArrayPoint2d, p2: ArrayPoint2d, graphics2D: Graphics2D, stroke: Float = 5f) {
        val from = convertPixels(p1)
        val to = convertPixels(p2)
        graphics2D.paint = color
        graphics2D.stroke = BasicStroke(stroke)
        graphics2D.drawLine(from.first, from.second, to.first, to.second)
    }

    fun build(): BufferedImage {
        val image = BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB)
        val graphics: Graphics2D = image.createGraphics()
        graphics.background = background
        graphics.clearRect(0, 0, width, height)

        // build bounding box
        val pointList = mutableListOf<ArrayPoint>()
        points.forEach { pointList.add(it.key) }
        shapes.forEach { pointList.addAll(it.key.getPoints()) }
        splines.forEach { pointList.addAll(it.key.getPoints()) }
        val shapeBoundingBox = BoundingBox(*pointList.toTypedArray())
        val minX = shapeBoundingBox.minX() - offset
        val minY = shapeBoundingBox.minY() - offset
        val maxX = shapeBoundingBox.maxX() + offset
        val maxY = shapeBoundingBox.maxY() + offset
        boundingBox = BoundingBox(ArrayPoint(minX, minY), ArrayPoint(maxX, maxY))


        shapes.forEach { shapePair ->
            val shapePoints = shapePair.key.getPoints()
            shapePoints.forEachIndexed { index, shapePoint ->
                drawPoint(shapePair.value, shapePoint.to2d(), graphics)
                val nextIndex = (index + 1) % shapePoints.size
                drawLine(shapePair.value, shapePoint.to2d(), shapePoints[nextIndex].to2d(), graphics)
            }
            graphics.paint = shapePair.value
            val xCoords = mutableListOf<Int>()
            val yCoords = mutableListOf<Int>()
            shapePoints.forEach {
                val coords = convertPixels(it.to2d())
                xCoords.add(coords.first)
                yCoords.add(coords.second)
            }
            graphics.fillPolygon(xCoords.toIntArray(), yCoords.toIntArray(), xCoords.size)
        }

        splines.forEach { pair ->
            val spline = pair.key
            val color = pair.value
            spline.getPoints().forEach { point ->
                drawPoint(color, point.to2d(), graphics)
            }
            spline.getPoints().zipWithNext { from: ArrayPoint, dest: ArrayPoint ->
                drawLine(color, from.to2d(), dest.to2d(), graphics)
            }
        }

        lineSegments.forEach { pair ->
            drawLine(pair.value, pair.key.from.to2d(), pair.key.to.to2d(), graphics)
        }
        points.forEach { drawPoint(it.value, it.key, graphics) }
        val origin = ArrayPoint2d(0.0, 0.0)
        vectors.forEach {
            val dest = origin + it.key
            drawLine(it.value, origin, dest.to2d(), graphics)
        }
        graphics.dispose()
        return image
    }

    fun render() {
        val image = build()
        val icon = ImageIcon(image)
        val label = JLabel(icon)
        JOptionPane.showMessageDialog(null, label)
    }

}