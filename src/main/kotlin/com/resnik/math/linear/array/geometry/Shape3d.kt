package com.resnik.math.linear.array.geometry

interface Shape3d<T : Shape3d<T>> : Shape<T> {

    fun volume(): Double

}