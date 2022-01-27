package com.resnik.math.examples

import org.junit.Test
import java.math.BigInteger

class MagicSquare {

    fun isMagic(square : Array<IntArray>) : Boolean {
        val n = square.size
        // sum each row
        val rowSums = square.map { it.sum() }
        val expected = rowSums.distinct().toMutableSet()
        if(expected.size > 1){
            return false
        }
        expected.addAll(square.mapIndexed{ row, _ -> IntArray(n){ col -> square[row][col]}.sum()}.distinct())
        if(expected.size > 1){
            return false
        }
        val leftDiag = IntArray(n){square[it][it]}
        expected.add(leftDiag.sum())
        val rightDiag = IntArray(n){square[n - 1 - it][n - 1 - it]}
        expected.add(rightDiag.sum())
        return expected.distinct().size <= 1
    }

    @Test
    fun testMagic(){
        val testSquare = arrayOf(
            intArrayOf(2,7,6),
            intArrayOf(9,5,1),
            intArrayOf(4,3,8))
        println(isMagic(testSquare))
    }

    @Test
    fun testBulks(){
        var a = BigInteger("0")
        var b = BigInteger("0")
        var c = BigInteger("0")
        var d = BigInteger("0")
        var e = BigInteger("0")
        var f = BigInteger("0")
        var g = BigInteger("0")
        var h = BigInteger("0")
        var i = BigInteger("0")
        val array = arrayOf(
            arrayOf(a*a,b*b,c*c),
            arrayOf(d*d,e*e,f*f),
            arrayOf(g*g,h*h,i*i))
    }

}