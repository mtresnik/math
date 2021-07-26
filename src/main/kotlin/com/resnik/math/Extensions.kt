package com.resnik.math

fun IntArray.product() : Int {
    var retProd = 1
    this.forEach { retProd *= it }
    return retProd
}

fun IntArray.sum() : Int = this.sumBy { it }

fun IntArray.getOrDefault(index : Int, default : Int) : Int {
    if(index < this.size){
        return this[index]
    }
    return default
}

operator fun IntArray.plus(other : IntArray) : IntArray = IntArray(this.size){this[it] + other[it]}
operator fun IntArray.minus(other : IntArray) : IntArray = IntArray(this.size){this[it] - other[it]}
operator fun IntArray.times(other : IntArray) : IntArray = IntArray(this.size){this[it] * other[it]}
operator fun IntArray.times(other : Int) : IntArray = IntArray(this.size){this[it] * other}
operator fun Int.times(other: IntArray) : IntArray = other.times(this)
operator fun IntArray.div(other : IntArray) : IntArray = IntArray(this.size){this[it] / other[it]}

operator fun DoubleArray.plus(other : DoubleArray) : DoubleArray = DoubleArray(this.size){this[it] + other[it]}
operator fun DoubleArray.minus(other : DoubleArray) : DoubleArray = DoubleArray(this.size){this[it] - other[it]}
operator fun DoubleArray.times(other : DoubleArray) : DoubleArray = DoubleArray(this.size){this[it] * other[it]}
operator fun DoubleArray.times(other : Double) : DoubleArray = DoubleArray(this.size){this[it] * other}
operator fun Double.times(other: DoubleArray) : DoubleArray = other.times(this)
operator fun DoubleArray.div(other : DoubleArray) : DoubleArray = DoubleArray(this.size){this[it] / other[it]}

