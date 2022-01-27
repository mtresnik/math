package com.resnik.math.stats.distributions

interface Distribution<T> {

    fun next() : T

    fun sample() : T = next()

}