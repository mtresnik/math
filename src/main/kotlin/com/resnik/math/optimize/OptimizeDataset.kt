package com.resnik.math.optimize

interface OptimizeDataset<DATA, DIR> {

    fun genSeed() : DATA

    fun genDirection() : DIR

    fun getSize(x : DATA) : Int

    fun scale(y : DIR, scale : Double) : DIR

    fun add(x : DATA, y : DIR) : DATA

}