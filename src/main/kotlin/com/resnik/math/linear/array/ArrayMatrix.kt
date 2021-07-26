package com.resnik.math.linear.array

import java.lang.IllegalArgumentException
import kotlin.math.pow

class ArrayMatrix {

    val values: Array<DoubleArray>
    val width: Int
    val height: Int

    constructor(size: Int, init: (Int) -> Double = {0.0}) : this(size, size, init)

    constructor(height: Int, width: Int, init: (Int) -> Double = {0.0}){
        values = Array(height){ DoubleArray(width){init(it)} }
        this.height = height
        this.width = width
    }

    constructor(values: Array<DoubleArray>){
        this.values = values
        this.height = values.size
        this.width = values[0].size
    }

    fun rowVectors(): Array<ArrayVector> = values.map { ArrayVector(*it) }.toTypedArray()

    fun columnVectors(): Array<ArrayVector> {
        val ret: Array<ArrayVector> = Array(width){ ArrayVector() }
        for(i in 0 until width){
            val colVec = ArrayVector(height)
            for(j in 0 until height){
                colVec[j] = values[j][i]
            }
            ret[i] = colVec
        }
        return ret
    }

    fun toVector() : ArrayVector? = if(height == 1) rowVectors()[0] else if(width == 1) columnVectors()[0] else null

    fun isVector() : Boolean = height == 1 || width == 1

    fun sum() : Double = values.sumByDouble { it.sum() }

    operator fun plus(other: ArrayMatrix): ArrayMatrix = ArrayMatrix(values.zip(other.values) { col1, col2 -> col1.zip(col2) { x, y -> x + y }.toDoubleArray() }.toTypedArray())

    operator fun minus(other: ArrayMatrix): ArrayMatrix = ArrayMatrix(values.zip(other.values) { col1, col2 -> col1.zip(col2) { x, y -> x - y }.toDoubleArray() }.toTypedArray())

    operator fun times(other: ArrayMatrix): ArrayMatrix {
        if(this.width != other.height){
            throw IllegalArgumentException("Invalid dim, cannot multiply ${dimString()} by ${other.dimString()}")
        }
        val ret = ArrayMatrix(height, other.width)
        for(i in 0 until height){
            val currRow = rowVectors()[i]
            for(j in 0 until other.width){
                val currCol = other.columnVectors()[j]
                ret.values[i][j] = currRow * currCol
            }
        }
        return ret
    }

    operator fun times(other: Double): ArrayMatrix = apply { it * other }

    operator fun div(other: Double): ArrayMatrix = apply { it / other }

    operator fun div(other: Int): ArrayMatrix = apply { it / other }

    operator fun Double.times(other: ArrayMatrix) : ArrayMatrix = other.times(this)

    operator fun plus(other: Double): ArrayMatrix = apply { it + other }

    fun pow(other : Double) : ArrayMatrix = apply { it.pow(other) }

    fun pow(other : Int) : ArrayMatrix = apply { it.pow(other) }

    operator fun get(index: Int): DoubleArray = this.values[index]

    fun setFrom(other: ArrayMatrix) = values.indices.forEach { row -> values[row].indices.forEach { col -> values[row][col] = other.values[row][col] } }

    fun addColumnVector(toAdd : ArrayVector) : ArrayMatrix {
        val ret : ArrayMatrix = ArrayMatrix(height, width + 1) { 0.0 }
        ret.setFrom(this)
        repeat(height){row -> ret.values[row][width] = toAdd[row]}
        return ret
    }

    fun hadamard(other: ArrayMatrix) : ArrayMatrix = ArrayMatrix(values.zip(other.values) { col1, col2 -> col1.zip(col2) { x, y -> x * y }.toDoubleArray() }.toTypedArray())

    fun apply(func : Function1<Double, Double>) : ArrayMatrix = ArrayMatrix(values.map { it.map(func).toDoubleArray() }.toTypedArray())

    fun transpose() : ArrayMatrix {
        val ret = ArrayMatrix(width, height)
        repeat(height){i -> repeat(width){j -> ret[j][i] = this[i][j]} }
        return ret
    }

    fun dimString() : String = "($height x $width)"

    fun numElements() : Int = height * width

    fun avg() : Double = this.sum() / numElements()

    fun flip() : ArrayMatrix {
        val ret : ArrayMatrix = ArrayMatrix(this.height, this.width);
        repeat(this.height){row ->
            repeat(this.width){col ->
                ret[row][col] = this[this.height - row - 1][this.width - col - 1];
            }
        }
        return ret;
    }

    override fun toString(): String {
        var ret = "["
        for(row in values.indices){
            for(col in values[row].indices){
                ret += "${values[row][col]}"
                if(col < values[row].lastIndex){
                    ret += ","
                }
            }
            if(row < values.lastIndex){
                ret += "\n"
            }
        }
        return "$ret]"
    }

}