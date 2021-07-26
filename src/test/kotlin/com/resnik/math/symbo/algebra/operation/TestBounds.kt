package com.resnik.math.symbo.algebra.operation

import com.resnik.math.symbo.algebra.Bounds
import org.junit.Test

class TestBounds {

    @Test
    fun testBounds1(){
        val bounds = Bounds.DEFAULT_10
        println(10.0 in bounds)
        println(20.0 in bounds)
        val smallBounds = Bounds(-1.0, 1.0)
        println(smallBounds in bounds)
        val outOfBounds = Bounds(-1.0, 20.0)
        println(outOfBounds in bounds)
    }

    @Test
    fun testBounds2(){
        val bounds1 = Bounds.DEFAULT_10
        println("Bounds1: $bounds1")
        val bounds2 = Bounds(-1.0, 20.0)
        println("Bounds2: $bounds2")
        val bounds3 = bounds1.union(bounds2)
        println("Union: $bounds3")
        val intesection = bounds2.intersect(bounds1)
        println("Intersection: $intesection")
    }

    @Test
    fun testBounds3(){
        val bounds1 = Bounds.DEFAULT_10
        println("Bounds1: $bounds1")
        val bounds2 = Bounds(11.0, 20.0)
        println("Bounds2: $bounds2")
        val bounds3 = bounds1.union(bounds2)
        println("Union: $bounds3")
        val intesection : Bounds? = bounds2.intersect(bounds1)
        println("Intersection: $intesection")
    }

    @Test
    fun testBounds4(){
        val bounds1 = Bounds.DEFAULT_10
        println("Bounds1: $bounds1")
        val bounds2 = Bounds(11.0, 20.0)
        println("Bounds2: $bounds2")
        val bounds3 = bounds1.union(bounds2)
        println("Union: $bounds3")
        val gap : Bounds? = bounds2.findGap(bounds1)
        println("Hole: $gap")
    }

}