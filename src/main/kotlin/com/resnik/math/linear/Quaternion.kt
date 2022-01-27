package com.resnik.math.linear

import com.resnik.math.linear.array.ArrayPoint
import com.resnik.math.linear.array.ArrayVector
import kotlin.math.acos
import kotlin.math.cos
import kotlin.math.pow
import kotlin.math.sin

class Quaternion(val s : Double, val x : Double, val y : Double, val z : Double) {

    constructor(s : Double = 0.0, v : ArrayVector) : this(s, v[0], v[1], v[2])

    operator fun plus(other: Quaternion) : Quaternion = Quaternion(s + other.s, x + other.x, y + other.y, z + other.z)

    operator fun times(scalar : Double) : Quaternion = Quaternion(scalar * s, scalar * x, scalar * y, scalar * z)

    operator fun div(scalar: Double) : Quaternion = this * (1 / scalar)

    operator fun Double.times(other : Quaternion) = other * this

    fun real() : Quaternion = Quaternion(s, ArrayVector(0.0, 0.0, 0.0))

    fun pure() : Quaternion = Quaternion(0.0, x, y, z)

    fun conjugate() : Quaternion = Quaternion(s, -x, -y, -z)

    fun norm() : Double = ArrayVector(s,x,y,z).magnitude()

    fun normalize() : Quaternion {
        val norm = norm()
        return Quaternion(s / norm, x / norm, y / norm, z / norm)
    }

    fun inv() : Quaternion = conjugate() / (norm().pow(2.0))

    fun dot(other: Quaternion) : Double = s * other.s + x * other.x + y * other.y + z * other.z

    fun theta(other: Quaternion) : Double = acos(this.dot(other) / (this.norm() * other.norm()))

    operator fun times(other: Quaternion) : Quaternion {
        val w1 = s
        val x1 = x
        val y1 = y
        val z1 = z

        val w2 = other.s
        val x2 = other.x
        val y2 = other.y
        val z2 = other.z

        val w3 = (w1 * w2 - x1*x2 - y1*y2 - z1*z2)
        val x3 = (w1 * x2 + x1 * w2 + y1*z2 - z1*y2)
        val y3 = (w1 * y2 - x1*z2 + y1 * w2 + z1 * x2)
        val z3 = (w1 * z2 + x1 * y2 - y1 * x2 + z1 * w2)

        return Quaternion(w3, x3, y3, z3)
    }

    fun rotationQuaternion(theta : Double, about : ArrayVector) = Quaternion(cos(theta/2), about.normalized() * sin(theta/2))

    fun rotate(theta : Double, about : ArrayVector) : Quaternion {
        val q = rotationQuaternion(theta, about)
        return q * this * q.inv()
    }

    override fun toString(): String = "[$s, $x i + $y j + $z k]"

    fun toPoint() : ArrayPoint = ArrayPoint(x,y,z)

    companion object {

        @JvmStatic
        fun slerp(p0 : Quaternion, p1 : Quaternion, t : Double) : Quaternion {
            val theta = p0.theta(p1)
            return p0 * (sin((1 - t) * theta) / sin(theta)) + p1 * (sin(t*theta) / sin(theta))
        }

    }

}