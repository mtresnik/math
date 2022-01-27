package com.resnik.math.linear.array.geometry

interface Shape2d<T : Shape2d<T>> : Shape<T> {

    fun area() : Double

}