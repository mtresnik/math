package com.resnik.math.stats.regression

import com.resnik.math.linear.array.ArrayPoint2d
import com.resnik.math.plot.Plot2d
import org.junit.Ignore
import org.junit.Test
import java.awt.Color
import javax.swing.ImageIcon
import javax.swing.JLabel
import javax.swing.JOptionPane

class TestPolynomialRegression {


    @Test
    @Ignore
    fun testPolynomialRegression(){
        val pointList = mutableListOf<ArrayPoint2d>()
        val numPoints = 50
        val m = 0.5
        val b = 1
        repeat(numPoints) {
            val x = Math.random() * 10
            val y = 0.1 * (x - 2) * (x - 5) * (x - 10) + Math.random()
            pointList.add(ArrayPoint2d(x, y))
        }
        val minX = pointList.minOf { point -> point.x() }
        val maxX = pointList.maxOf { point -> point.x() }
        val numDottedPoints = 80
        val regression = PolynomialRegression(pointList, 3)
        println("Generated regression")
        val polynomial = regression.polynomial
        println(polynomial)
        val regressionLine = mutableListOf<ArrayPoint2d>()
        repeat(numDottedPoints) {
            val t = it.toDouble() / numDottedPoints
            val x = (maxX - minX) * t + minX
            val y = regression(x)
            regressionLine.add(ArrayPoint2d(x, y))
        }

        val plot2d = Plot2d()
        plot2d.add(pointList, Color.BLUE)
        plot2d.add(regressionLine, Color.RED)
        val image = plot2d.plot(connectPoints = false, drawPoints = true)
        val icon = ImageIcon(image)
        val label = JLabel(icon)
        JOptionPane.showMessageDialog(null, label)
    }

}