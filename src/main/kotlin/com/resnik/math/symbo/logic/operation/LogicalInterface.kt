package com.resnik.math.symbo.logic.operation

interface LogicalInterface<PARAM : LogicalInterface<PARAM>> {

    operator fun plus(other: PARAM): PARAM = or(other)

    operator fun times(other: PARAM): PARAM = and(other)

    fun or(other: Boolean): PARAM

    fun or(other: PARAM): PARAM

    fun and(other: PARAM): PARAM

    fun not(): PARAM

    fun implies(other: PARAM): PARAM

    fun iff(other: PARAM): PARAM

}