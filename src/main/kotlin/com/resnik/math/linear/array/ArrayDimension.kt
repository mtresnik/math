package com.resnik.math.linear.array

import com.resnik.math.product

public class ArrayDimension(vararg val values : Int) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ArrayDimension

        if (!values.contentEquals(other.values)) return false

        return true
    }

    override fun hashCode(): Int {
        return values.contentHashCode()
    }

    fun stripLast() : ArrayDimension = clone(lastIndex())

    fun clone(size : Int) : ArrayDimension =
        ArrayDimension(*this.values.copyOf(size))

    operator fun get(index : Int) : Int = values[index]

    fun lastIndex() : Int = values.lastIndex

    fun size() : Int = values.size

    fun dimProduct() : Int = values.product()

    override fun toString(): String {
        var ret = "("
        values.indices.forEach {
            ret += values[it]
            if(it < values.lastIndex){
                ret += " x "
            }
        }
        return "$ret)"
    }
}