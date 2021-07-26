package com.resnik.math.linear.array.geometry

import com.resnik.math.linear.array.ArrayPoint
import com.resnik.math.linear.array.ArrayPoint2d
import java.awt.BasicStroke
import java.awt.Color
import java.awt.Graphics2D
import java.awt.image.BufferedImage
import java.lang.RuntimeException
import javax.swing.ImageIcon
import javax.swing.JLabel
import javax.swing.JOptionPane
import kotlin.math.floor

class ShapeCollection(val width : Int = 1000,
                      val height : Int = 1000,
                      val background : Color = Color.WHITE,
                      val pointRadius : Int = 20
) {

    val shapes : MutableMap<Shape, Color> = mutableMapOf()
    val points : MutableMap<ArrayPoint2d, Color> = mutableMapOf()
    lateinit var boundingBox : BoundingBox

    fun addShape(shape : Shape, color : Color = Color.BLACK){
        this.shapes[shape] = color
    }

    fun addPoint(point : ArrayPoint2d, color : Color = Color.BLACK){
        this.points[point] = color
    }

    fun drawCenteredCircle(g: Graphics2D, x: Int, y: Int, r: Int) = g.fillOval(x - r / 2, y - r / 2, r, r)

    fun convertPixels(point: ArrayPoint2d) : Pair<Int, Int> {
        if(point !in boundingBox)
            throw RuntimeException("Point is outside current bounds of image.")
        val x = point.x
        val y = point.y
        val relX = (x - boundingBox.minX()) / (boundingBox.maxX() - boundingBox.minX())
        val relY = (y - boundingBox.minY()) / (boundingBox.maxY() - boundingBox.minY())
        return Pair(floor(relX*width).toInt(), floor(relY*height).toInt())
    }

    fun drawPoint(color : Color, point : ArrayPoint2d, graphics2D: Graphics2D){
        val coordinate = convertPixels(point)
        graphics2D.paint = color
        drawCenteredCircle(graphics2D, coordinate.first, coordinate.second, pointRadius)
    }

    fun drawLine(color: Color, p1 : ArrayPoint2d, p2 : ArrayPoint2d, graphics2D: Graphics2D, stroke: Float = 5f){
        val from = convertPixels(p1)
        val to = convertPixels(p2)
        graphics2D.paint = color
        graphics2D.stroke = BasicStroke(stroke)
        graphics2D.drawLine(from.first, from.second, to.first, to.second)
    }


    fun build() : BufferedImage {
        val image = BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB)
        val graphics: Graphics2D = image.createGraphics()
        graphics.background = background
        graphics.clearRect(0,0, width, height)

        // build bounding box
        val pointList = mutableListOf<ArrayPoint>()
        points.forEach{pointList.add(it.key)}
        shapes.forEach{pointList.addAll(it.key.getPoints())}
        boundingBox = BoundingBox(*pointList.toTypedArray())


        shapes.forEach{shapePair ->
            val shapePoints = shapePair.key.getPoints()
            shapePoints.forEachIndexed{index, shapePoint ->
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
        points.forEach{drawPoint(it.value, it.key, graphics)}
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