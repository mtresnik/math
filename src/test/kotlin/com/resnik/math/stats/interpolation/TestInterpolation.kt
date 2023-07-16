package com.resnik.math.stats.interpolation

import com.resnik.math.linear.array.ArrayPoint2d
import com.resnik.math.plot.Plot2d
import org.junit.Ignore
import org.junit.Test
import javax.swing.ImageIcon
import javax.swing.JLabel
import javax.swing.JOptionPane

class TestInterpolation {

    @Test
    fun testLinear(){
        val p1 = ArrayPoint2d(-1.0,5.0)
        val p2 = ArrayPoint2d(5.0, 3.0)
        val linear = LinearInterpolation(p1, p2)
        println(linear.coeff())
    }

    @Test
    fun testQuadratic1(){
        val p1 = ArrayPoint2d(-1.0,5.0)
        val p2 = ArrayPoint2d(5.0, 3.0)
        val p3 = ArrayPoint2d(4.0, 4.0)
        val quadratic = QuadraticInterpolation(p1,p2,p3)
        println(quadratic.coeff)
        println(quadratic.det)
    }

    @Test
    fun testQuadratic2(){
        val p1 = ArrayPoint2d(10.0,3.0)
        val p2 = ArrayPoint2d(8.0, 6.0)
        val p3 = ArrayPoint2d(9.0, 3.0)
        val quadratic = QuadraticInterpolation(p1,p2,p3)
        println(quadratic.coeff)
        println(quadratic.det)
    }

    @Test
    @Ignore
    fun testQuadraticGraph(){
        val p1 = ArrayPoint2d(8.0, 6.0)
        val p2 = ArrayPoint2d(9.0, 3.0)
        val p3 = ArrayPoint2d(10.0,3.0)
        val quadratic = QuadraticInterpolation(p1,p2,p3)
        val coeff = quadratic.coeff
        val steps = 9.0
        val dt = (p3.x() - p1.x()) / steps
        val pointList = mutableListOf<ArrayPoint2d>()
        repeat(steps.toInt()) {
            val x = p1.x() + dt * it
            val y = coeff.values[0] + coeff.values[1] * x + coeff.values[2] * x * x
            pointList.add(ArrayPoint2d(x, y))
        }
        val plot2d = Plot2d()
        plot2d.add(pointList)
        val image = plot2d.plot(drawPoints = false)
        val icon = ImageIcon(image)
        val label = JLabel(icon)
        JOptionPane.showMessageDialog(null, label)
    }

}