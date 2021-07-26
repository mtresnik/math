package com.resnik.math.symbo.algebra.parse

import com.resnik.math.symbo.algebra.operation.Constant
import com.resnik.math.symbo.algebra.operation.Variable
import org.junit.Test

class TestParse {


    @Test
    fun testParse1(){
        val ops = "x*x"
        val syntaxAnalyzer = SymbolicSyntaxAnalyzer()
        val analyzed = syntaxAnalyzer.analyze(ops)
        println(analyzed)

        val res = analyzed!!.set(Variable.X, 3.0)
        println(res)
    }

    @Test
    fun testParse2(){
        val ops = "sin(x)"
        val syntaxAnalyzer = SymbolicSyntaxAnalyzer()
        val analyzed = syntaxAnalyzer.analyze(ops)
        println(analyzed)

        val res = analyzed!!.set(Variable.X, Constant.PI)
        println(res)
    }

    @Test
    fun testComplexParse(){
        val ops = "e^(pi * i)"
        val syntaxAnalyzer = SymbolicSyntaxAnalyzer()
        val analyzed = syntaxAnalyzer.analyze(ops)
        println(analyzed)

        val res = analyzed!!.set(Variable.E, Constant.E)
        println(res)
    }

    @Test
    fun testSubstitutions(){
        val ops = "pi"

        val syntaxAnalyzer = SymbolicSyntaxAnalyzer()
        val analyzed = syntaxAnalyzer.analyze(ops)
        println(analyzed)

        println(analyzed!!::class)
    }

}