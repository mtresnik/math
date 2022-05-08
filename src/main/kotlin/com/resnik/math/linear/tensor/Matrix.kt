package com.resnik.math.linear.tensor

import com.resnik.math.symbo.algebra.operation.Constant
import com.resnik.math.symbo.algebra.operation.Variable
import com.resnik.math.symbo.algebra.operation.functions.Cos
import com.resnik.math.symbo.algebra.operation.functions.Sin

class Matrix(val row: Int, val col: Int) : Tensor(TensorDimension(row, col)) {

    override fun toString(): String {
        return inner.contentToString().replace("{", "[").replace("}", "}")
    }

    fun isSquare(): Boolean = row == col

    companion object {

        fun rot2d(): Matrix {
            val retMatrix = Matrix(2, 2)
            retMatrix[0, 0] = Cos(Variable.THETA)
            retMatrix[0, 1] = Constant.NEGATIVE_ONE * Sin(Variable.THETA)
            retMatrix[1, 0] = Sin(Variable.THETA)
            retMatrix[1, 1] = Cos(Variable.THETA)
            return retMatrix
        }

    }

}