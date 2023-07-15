package com.resnik.math.symbo.algebra.operation

import com.resnik.math.symbo.algebra.operation.base.Power
import org.junit.Test

class TestDerivatives {


    @Test
    fun testMultiplicationAddition(){
        val op1 = Variable.X * 2 + Constant.ONE
        println(op1)
        println(op1.getDerivative(Variable.X))
    }

    @Test
    fun testPow1() {
        val op = Power(Variable.X, Constant(2))
        println(op)
        println(op.getDerivative(Variable.X))
    }

    @Test
    fun testPow2() {
        val op = Power(Constant(2), Variable.X)
        println(op)
        println(op.getDerivative(Variable.X))
    }

    @Test
    fun testPow3() {
        val op = Power(Variable.X, Variable.X)
        println(op)
        println(op.getDerivative(Variable.X))
    }

    @Test
    fun testPow4() {

    }
}