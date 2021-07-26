package com.resnik.math.symbo.algebra.operation

import com.resnik.math.symbo.algebra.operation.Constant.Companion.toConstant
import com.resnik.math.symbo.algebra.operation.base.Addition
import org.junit.Test

class TestOperation {

    @Test
    fun testOperation1(){
        val op1 = Addition(Variable.X, (3.0).toConstant())
        println(op1.toNonConstantString())
        val op2 = op1.evaluate(Variable.X, Variable.Y)
        println(op2)
        val op3 = Variable.X.pow(Variable.Y)
        println(op3)
        println(op3.evaluate(Variable.Y, Constant(5.0)))
        println(op3.evaluate(Variable.Y, Constant(5.0)).evaluate(Variable.X, Constant(2.0)))

        val op4 = Variable.X + Variable.Y * 3.0
        println(op4)

        val op5 = (5.0 + 3.0 + Variable.X)
        println(op5)
    }

    @Test
    fun testOperation2(){
        val x = Variable.X
        val y = Variable.Y

        println(x + y + 3.0 + 10.0 + x*x + x*10.0)
        val op = x + y + 3.0 + 10.0 + x*x + x*10.0
        println(op.evaluate(x, 5.0))
        val op2 = op.evaluate(x, 5.0).evaluate(y, 10.0)
        println(op2)
    }

}
