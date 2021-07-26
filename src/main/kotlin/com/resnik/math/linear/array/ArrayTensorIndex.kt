package com.resnik.math.linear.array

class ArrayTensorIndex(vararg val values : Int) {

    operator fun get(index : Int) : Int = values[index]

    operator fun set(index : Int, value : Int) {
        values[index] = value
    }

    fun size() : Int = values.size

    fun toInt(dim: ArrayDimension) : Int {
        var sum = 0
        var product = 1
        values.indices.forEach {
            sum += values[it] * product
            product *= dim[it]
        }
        return sum
    }

    override fun toString(): String {
        var ret = "("
        values.indices.forEach {
            ret += values[it]
            if(it < values.lastIndex){
                ret += " , "
            }
        }
        return "$ret)"
    }

    companion object {

        @JvmStatic
        fun fromInt(index : Int, dim : ArrayDimension) : ArrayTensorIndex {
            var remainder = index
            val ret = IntArray(dim.size())
            ret.indices.forEach {
                ret[it] = remainder % dim[it]
                remainder = (remainder - ret[it]) / dim[it]
            }
            return ArrayTensorIndex(*ret)
        }
    }

}