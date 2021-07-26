package com.resnik.math.symbo.algebra

import org.junit.Test

class TestComplex {

    @Test
    fun testComplex1(){
        val val1 = ComplexNumber(1.0, 1.0)
        println(val1)
        val val2 = ComplexNumber(1.0, -2.0)
        println(val2)
        println(val1 + val2)
        println(val1 - val2)
        println(ComplexNumber.NaN)
    }

    @Test
    fun testComplex2(){
        val val1 = ComplexNumber(4.0)
        val val2 = ComplexNumber(2.0)
        println(val1.pow(val2))
        println(ComplexNumber.expI(Math.PI))
    }
}