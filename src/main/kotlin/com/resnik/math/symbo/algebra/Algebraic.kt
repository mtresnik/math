package com.resnik.math.symbo.algebra

interface Algebraic<PARAM : Algebraic<PARAM>> {

    operator fun plus(other: PARAM) : PARAM

    operator fun minus(other: PARAM) : PARAM

    operator fun times(other: PARAM) : PARAM

    operator fun div(other: PARAM) : PARAM

    fun pow(other : PARAM) : PARAM

    fun sqrt() : PARAM

}