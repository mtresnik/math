package com.resnik.math.linear.tensor

import java.lang.IllegalStateException

class TensorIndex {

    val value : Int
    val next : TensorIndex?

    constructor(value : Int, next: TensorIndex?){
        this.value = value;
        this.next = next;
    }

    constructor(vararg values : Int) : this(values.first(), if(values.size > 1) { TensorIndex(*values.copyOfRange(1, values.size)) } else null)

    fun size() : Int = if(next == null) { 1 } else next.size() + 1

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

    fun to(other: TensorIndex) : TensorIndexIterator = TensorIndexIterator(TensorRange(this, other))

    fun from(other: TensorIndex) : TensorIndexIterator = other.to(this)

    fun toSequenceString() : String = "$value${next?.toSequenceString() ?: ""}"

    override fun toString(): String = toSequenceString()

    operator fun compareTo(other: TensorIndex) : Int = toSequenceString().compareTo(other.toSequenceString())

}