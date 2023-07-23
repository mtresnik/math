package com.resnik.math.symbo.logic.operation

import com.resnik.math.symbo.logic.operation.base.And
import com.resnik.math.symbo.logic.operation.base.Not
import com.resnik.math.symbo.logic.operation.base.Or
import org.junit.Test

class TestLogicalOperation {

    @Test
    fun testOr1(){
        val p = LogicalVariable.P
        val q = LogicalVariable.Q
        val or = Or(p, q)
        println(or)
    }

    @Test
    fun testOr2(){
        val p = LogicalVariable.P
        val or = Or(p, LogicalConstant.FALSE)
        println(or)
    }

    @Test
    fun testOr3(){
        val p = LogicalVariable.P
        val or = Or(p, LogicalConstant.TRUE)
        println(or)
    }

    @Test
    fun testOr4(){
        val p = LogicalVariable.P
        val or = p or LogicalConstant.FALSE
        println(or)
    }

    @Test
    fun testAnd1(){
        val p = LogicalVariable.P
        val q = LogicalVariable.Q
        val and = And(p, q)
        println(and)
    }

    @Test
    fun testAnd2(){
        val p = LogicalVariable.P
        val and = And(p, LogicalConstant.TRUE)
        println(and)
    }

    @Test
    fun testAnd3() {
        val p = LogicalVariable.P
        val and = And(p, LogicalConstant.FALSE)
        println(and)
    }

    @Test
    fun testNot1() {
        val p = LogicalVariable.P
        val not = Not(p)
        println(not)
    }


    @Test
    fun testNot2() {
        val not = Not(LogicalConstant.TRUE)
        println(not)
    }

    @Test
    fun testNot3() {
        val p = LogicalVariable.P
        val q = LogicalVariable.Q
        val and = And(p, q)
        println(and)
        println(and.not().distribute())
    }


    @Test
    fun testImplies1() {
        val p = LogicalVariable.P
        val q = LogicalVariable.Q
        println(p implies q)
    }

    @Test
    fun testImplies2() {
        println(LogicalVariable.P implies false)
    }

    @Test
    fun testImplies3() {
        println(true implies LogicalVariable.P)
    }

    @Test
    fun testImplies4() {
        println(LogicalVariable.P implies true)
    }

    @Test
    fun testImplies5() {
        println(false implies LogicalVariable.P)
    }

    @Test
    fun testIff1() {
        val p = LogicalVariable.P
        val q = LogicalVariable.Q
        println(p iff q)
    }

    @Test
    fun testIff2() {
        println(LogicalVariable.P iff false)
    }

    @Test
    fun testIff3() {
        println(true iff LogicalVariable.P)
    }

}