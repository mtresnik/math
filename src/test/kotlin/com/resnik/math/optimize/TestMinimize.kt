package com.resnik.math.optimize

import com.resnik.math.linear.array.ArrayMatrix
import com.resnik.math.linear.array.ArrayPoint
import com.resnik.math.linear.array.ArrayVector
import org.junit.Test
import java.util.*
import kotlin.math.abs

class TestMinimize {

    @Test
    fun testMinimize1() {
        val func1 : (list : ArrayPoint) -> Double = {
            val x = it.x()
            x * (x - 2) * (x + 1) * (x - 2)
        }
        val minimizer = RangedMinimizer(listOf(ImmutableDimensionClassifier.STANDARD), subSplineSize = 500)
        val min = minimizer.minimize(func1)
        println(min)
    }

    @Test
    fun testMinimize2(){
        val func1 : (list : ArrayPoint) -> Double = {
            val x = it.x()
            x * (x - 2) * (x + 1) * (x - 2)
        }
        val minimizer = BruteForceMinimizer(listOf(ImmutableDimensionClassifier.STANDARD))
        val min = minimizer.minimize(func1)
        println(min)
    }

    @Test
    fun testMinimize3(){
        val func1 : (list : ArrayPoint) -> Double = {
            val x = it.x()
            val y = it.y()
            x * x + y * (y - 2)
        }
        val minimizer = BruteForceMinimizer(listOf(ImmutableDimensionClassifier.STANDARD, ImmutableDimensionClassifier.STANDARD))
        val min = minimizer.minimize(func1)
        println(min)
    }

    @Test
    fun testMinimize10() {
        val func1 : (list : ArrayPoint) -> Double = { point->
            point.values.sumByDouble { abs(it) }
        }
        val minList = mutableListOf<ImmutableDimensionClassifier>()
        repeat(5){
            minList.add(ImmutableDimensionClassifier.STANDARD)
        }
        val minimizer = BruteForceMinimizer(minList, iterSize = 20)
        val min = minimizer.minimize(func1)
        println(min)
    }

    @Test
    fun testUnboundedMinimizer() {
        val func1 : (list : ArrayPoint) -> Double = {
            val x = it.x()
            val y = it.y()
            x * x + y * (y - 50)
        }
        val minimizer = UnboundedMinimizer(2, numIterations = 1000, stepSize = 1.0)
        val min = minimizer.minimize(func1)
        println(min)
    }



}