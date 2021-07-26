package com.resnik.math.linear.tensor

class TensorIndexIterator(val min : TensorIndex, val max: TensorIndex) : Iterator<TensorIndex> {

    constructor(range: TensorRange) : this(range.minIndex, range.maxIndex)

    var currIndex : Int = min[0]
    var next : TensorIndexIterator? = null

    init {
        if(min.hasNext() && max.hasNext()){
            next = TensorIndexIterator(min.next!!, max.next!!)
        }
    }

    override fun hasNext(): Boolean = curr() < max

    fun curr() : TensorIndex = TensorIndex(currIndex, next?.curr())

    fun reset() {
        currIndex = min[0]
        next?.reset()
    }

    override fun next(): TensorIndex {
        // 0 0 0 --> 1 2 3
        if(next == null){
            // Works for single digit
            currIndex++
        }else if(next != null && !next!!.hasNext()){
            // Reset next if next is at max
            next!!.reset()
            currIndex++
            return TensorIndex(currIndex, next?.curr())
        }
        return TensorIndex(currIndex, next?.next())
    }

}