package com.resnik.math.stats

class Dice(vararg val dice: Die) : Rollable() {

    constructor(num: Int, size: Int = 6) : this(*Array<Die>(num) { Die() })

    override fun roll(): Int {
        var sum = 0
        dice.forEach { sum += it.roll() }
        return sum
    }

}