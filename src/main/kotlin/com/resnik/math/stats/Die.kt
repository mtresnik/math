package com.resnik.math.stats

class Die(val size : Int = 6) : Rollable() {

    private val sides = (1 .. size)

    override fun roll() : Int = sides.random()

}