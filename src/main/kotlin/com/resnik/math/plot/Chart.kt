package com.resnik.math.plot

import java.awt.BasicStroke
import java.awt.Color
import java.awt.Graphics2D
import java.awt.image.BufferedImage

abstract class Chart(
    val width: Int = 500,
    val height: Int = 500,
    val padding: Int = 100,
    val background: Color = Color.WHITE,
    val axisColor: Color = Color.BLACK,
    val lineColor: Color = Color.LIGHT_GRAY,
    val axisThickness: Float = 5.0f,
    val dataLineThickness: Float = 2.0f
) {

    private fun drawAxes(graphics2D: Graphics2D) {
        graphics2D.paint = axisColor
        graphics2D.stroke = BasicStroke(5.0f)
        graphics2D.drawLine(0, 0, width + 2 * padding, 0)
        graphics2D.drawLine(width + 2 * padding, 0, width + 2 * padding, height + 2 * padding)
        graphics2D.drawLine(0, height + 2 * padding, width + 2 * padding, height + 2 * padding)
        graphics2D.drawLine(0, 0, 0, height + 2 * padding)
    }

    protected fun renderAxes(): Pair<BufferedImage, Graphics2D> {
        // Draw axes
        val bufferedImage = BufferedImage(width + 2 * padding, height + 2 * padding, BufferedImage.TYPE_INT_ARGB)
        val graphics2D = bufferedImage.createGraphics()
        graphics2D.background = background
        graphics2D.clearRect(0, 0, width + 2 * padding, height + 2 * padding)
        drawAxes(graphics2D)
        return Pair(bufferedImage, graphics2D)
    }

}