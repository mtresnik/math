package com.resnik.math.linear

import com.resnik.math.linear.array.ArrayPoint
import com.resnik.math.linear.array.ArrayVector
import com.resnik.math.linear.array.geometry.BoundingBox
import com.resnik.math.linear.array.geometry.ShapeCollection
import org.junit.Test
import java.awt.Color

class TestQuaternion {

    @Test
    fun testQuat1(){
        val quat1 = Quaternion(1.0, 1.0, -5.0, -2.0)
        val quat2 = Quaternion(-1.0, 3.0, 4.0, 3.0)

        println(quat1 * quat2)
    }

    @Test
    fun testQuat2(){
        var boundingBox = BoundingBox(ArrayPoint(0.0,0.0), ArrayPoint(0.0, 1.0), ArrayPoint(1.0, 0.0), ArrayPoint(1.0, 1.0))
        val newBoundingBox = boundingBox.rotate(Math.PI/4, ArrayVector(0.0,0.0,1.0))
        val shapeCollection = ShapeCollection()
        shapeCollection.addShape(newBoundingBox, Color.RED)
        shapeCollection.render()
    }

}