package com.resnik.math.linear.tensor

import com.resnik.math.symbo.algebra.operation.Variable
import org.junit.Test

class TestMatrix {

    @Test
    fun testDet2(){
        val matrix = Matrix.generate(arrayOf(
            arrayOf(Variable("a"), Variable("b")),
            arrayOf(Variable("c"), Variable("d"))
        ))
        println(matrix)
        val det = matrix.det()
        println(det)
    }

    @Test
    fun testDet3(){
        val matrix = Matrix.generate(arrayOf(
            arrayOf(Variable("a"), Variable("b"), Variable("c")),
            arrayOf(Variable("d"), Variable("e"), Variable("f")),
            arrayOf(Variable("g"), Variable("h"), Variable("i")),
        ))
        println(matrix)
        val det = matrix.det()
        println(det)
    }

    @Test
    fun testInverse2Variables() {
        val matrix = Matrix.generate(arrayOf(
            arrayOf(Variable("a"), Variable("b")),
            arrayOf(Variable("c"), Variable("d"))
        ))
        println(matrix)
        val inverse = matrix.inverse()
        println(inverse)
    }

    @Test
    fun testInverse2() {
        val matrix = Matrix.generate(arrayOf(
            doubleArrayOf(1.0 , 2.0),
            doubleArrayOf(3.0, 4.0)
        ))
        println(matrix)
        val inverse = matrix.inverse()
        println(inverse)
        println(inverse.dot(matrix))
    }


}