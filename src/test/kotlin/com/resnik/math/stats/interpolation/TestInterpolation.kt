package com.resnik.math.stats.interpolation

import com.resnik.math.linear.array.ArrayPoint2d
import org.junit.Test

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

}