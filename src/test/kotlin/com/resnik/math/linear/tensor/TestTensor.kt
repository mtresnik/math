package com.resnik.math.linear.tensor

import com.resnik.math.symbo.algebra.ComplexNumber
import com.resnik.math.symbo.algebra.operation.Constant
import com.resnik.math.symbo.algebra.operation.Constant.Companion.toConstant
import com.resnik.math.symbo.algebra.operation.Operation
import com.resnik.math.symbo.algebra.operation.Variable
import org.junit.Test

class TestTensor {

    @Test
    fun testScalar1(){
        val scalar1 = Scalar(0.5)
        println(scalar1)
    }

    @Test
    fun testVector1(){
        val vec1 = Vector(3){ Scalar(it.toDouble()) }
        println(vec1)
    }

    @Test
    fun testVector2() {
        val vec1 = Vector(3)
        vec1[0] = Variable.X
        vec1[1] = Variable.Y
        vec1[2] = Variable.Z
        println(vec1)
        val vec2 = Vector(arrayOf<Operation>(Constant.ONE, Constant.TWO, Constant.TEN))
        println(vec2)
        val vec3 = vec1 + vec2
        println(vec3)
    }

    @Test
    fun testVector3(){
        val vec1 = Vector(3.0, 4.0)
        val vec2 = Vector(4.0, 3.0)
        println(vec1.sum())
        println(vec2.sum())
        println(vec1 * vec2)
        println(vec1.theta(vec2))
    }

    @Test
    fun testVector4(){
        val vec1 = Vector(3)
        vec1[0] = Variable.X
        vec1[1] = Variable.Y
        vec1[2] = Variable.Z
        println(vec1)
        val vecSub = vec1.evaluate(Variable.X, Constant(5.0)).evaluate(Variable.Y, Constant(10.0)).evaluate(Variable.Z, Constant(-5.0))
        println(vecSub)
        println(vec1)
        val norm = vec1.norm()
        println(norm)
        if(vecSub.isVector()){
            println(vecSub.toVector().norm())
        }
    }

    @Test
    fun testMatrix1(){
        val mat1 = Matrix(2,2)
        println(mat1)
    }

    @Test
    fun testMatrix2(){
        val mat1 = Matrix(2,2)
        println(mat1[0,1])
        mat1[0,1] = Scalar(0.5)
        println(mat1[0,1])
        println(mat1)
        mat1[1,1] = Variable.X
        println(mat1)
        mat1[0,0] = ComplexNumber(0.5, 0.5).toConstant()
        println(mat1)
        println(mat1.isSquare())
    }

    @Test
    fun testMatrix3(){
        val rot2 = Matrix.rot2d()
        println(rot2)
        val subbed = rot2.evaluate(Variable.THETA, Constant.PI)
        println(subbed)
        println(ComplexNumber.cos(ComplexNumber.PI))
    }

    @Test
    fun testTensor1(){
        val ten1 = Tensor(TensorDimension(3,3,3))
        println(ten1)
    }

    @Test
    fun testIndex(){
        val iter = TensorIndex(0,0,0,0).to(TensorIndex(2,2,2,2))
        while (iter.hasNext()){
            val curr = iter.curr()
            println("Curr: $curr")
            iter.next()
        }
    }

}