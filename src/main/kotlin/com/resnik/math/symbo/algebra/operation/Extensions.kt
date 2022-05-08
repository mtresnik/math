package com.resnik.math.symbo.algebra.operation

import com.resnik.math.symbo.algebra.operation.base.Power

operator fun Double.plus(other: Operation): Operation = other + this

operator fun Double.minus(other: Operation): Operation = Constant(this) - other

operator fun Double.times(other: Operation): Operation = other * this

operator fun Double.div(other: Operation): Operation = Constant(this) / other

fun Double.pow(other: Operation): Operation = Power(Constant(this), other)

operator fun Int.plus(other: Operation): Operation = other + this.toDouble()

operator fun Int.minus(other: Operation): Operation = Constant(this.toDouble()) - other

operator fun Int.times(other: Operation): Operation = other * this.toDouble()

operator fun Int.div(other: Operation): Operation = Constant(this.toDouble()) / other

fun Int.pow(other: Operation): Operation = Power(Constant(this.toDouble()), other)