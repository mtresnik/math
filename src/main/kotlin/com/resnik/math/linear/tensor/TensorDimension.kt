package com.resnik.math.linear.tensor

import java.lang.IllegalStateException

class TensorDimension(vararg val values : Int) {

    val value : Int = values.firstOrNull() ?: -1
    val next : TensorDimension? = if(values.size > 1) { TensorDimension(*values.copyOfRange(1, values.size)) } else null

    fun size() : Int = if(next == null) { 1 } else next.size() + 1

    fun product() : Int {
        var ret = 1
        values.forEach { ret *= it }
        return ret
    }

    fun hasNext() : Boolean = next != null

    operator fun get(index : Int) : Int {
        if(index < 0 || index >= size()){
            throw ArrayIndexOutOfBoundsException("Illegal index requested");
        }
        if(index == 0){
            return value
        }
        if(next == null){
            throw IllegalStateException("Next cannot be null for index > 0")
        }
        return next[index - 1]
    }

    operator fun contains(index : TensorIndex) : Boolean {
        if(index.size() > size()){
            return false
        }
        var currIndex = index
        var currDimension = this
        while(currIndex.hasNext() && currDimension.hasNext()){
            if(currIndex.value < 0 || currIndex.value >= currDimension.value){
                return false
            }
            currIndex = currIndex.next!!
            currDimension = currDimension.next!!
        }
        return true
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as TensorDimension

        if (!values.contentEquals(other.values)) return false
        if (value != other.value) return false

        return true
    }

    override fun hashCode(): Int {
        var result = values.contentHashCode()
        result = 31 * result + value
        return result
    }

}