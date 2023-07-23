package com.resnik.math.linear.tensor

import com.resnik.math.symbo.algebra.operation.*
import com.resnik.math.symbo.algebra.operation.functions.Cos
import com.resnik.math.symbo.algebra.operation.functions.Sin
import kotlin.math.pow


class Matrix(val row: Int, val col: Int) : Tensor(TensorDimension(row, col)) {

    val width = col
    val height = row

    override fun toString(): String {
        return inner.contentToString().replace("{", "[").replace("}", "}")
    }

    fun isSquare(): Boolean = row == col

    private fun requireSquare() {
        if (!isSquare()) {
            throw IllegalStateException("Matrix is not square: ($row x $col)")
        }
    }

    fun transpose(): Matrix {
        val retMatrix = Matrix(col, row)
        repeat(row) { y ->
            repeat(col) { x->
                retMatrix[x][y] = this[y][x]
            }
        }
        return retMatrix
    }

    fun inverse(): Matrix {
        requireSquare()
        val det = this.det()
        if (det == Constant.ZERO) {
            throw IllegalStateException("Matrix is not invertible")
        }
        if (row == 1) {
            return generate(arrayOf(arrayOf(Constant(1.0) / det)))
        }
        val adjugate = this.adjugate()
        adjugate.scaleInPlace(1.0 / det)
        return adjugate
    }

    fun det(): Operation {
        if (!isSquare()) {
            throw IllegalStateException("Matrix is not square: ($row x $col)")
        }
        if (row == 1) {
            return this[0,0].toScalar().value
        }
        if (row == 2) {
            val a = this[0,0].toScalar().value
            val b = this[0,1].toScalar().value
            val c = this[1,0].toScalar().value
            val d = this[1,1].toScalar().value
            return a * d - b * c
        }
        if (row == 3) {
            val a = this[0,0].toScalar().value
            val b = this[0,1].toScalar().value
            val c = this[0,2].toScalar().value
            val d = this[1,0].toScalar().value
            val e = this[1,1].toScalar().value
            val f = this[1,2].toScalar().value
            val g = this[2,0].toScalar().value
            val h = this[2,1].toScalar().value
            val i = this[2,2].toScalar().value
            return a * (e * i - f * h) - b * (d * i - f * g) + c * (d * h - e * g)
        }

        // Generic determinant flow
        var sign: Double
        var retSum : Operation = Constant.ZERO
        repeat(col) { COL ->
            sign = (-1.0).pow(COL + 2)
            val tempMatrix = removeRowCol(0, COL)
            val tempElem = this[0, COL].toScalar().value
            val currRes = tempElem * tempMatrix.det() * Constant(sign)
            retSum += currRes
        }
        return retSum
    }

    fun removeRowCol(r1: Int, c1: Int): Matrix {
        val retMatrix = Matrix(this.row - 1, this.col - 1)
        val rows = (0..this.row).toMutableList()
        val cols = (0 .. this.col).toMutableList()
        rows.remove(r1)
        cols.remove(c1)
        repeat(retMatrix.row) { ROW ->
            repeat(retMatrix.col) { COL ->
                retMatrix[ROW][COL] = this[rows[ROW]][cols[COL]]
            }
        }
        return retMatrix
    }

    fun scaleInPlace(scalar: Operation) {
        repeat(row) { y ->
            repeat(col) { x ->
                this[y,x] = this[y,x].toScalar().value * scalar
            }
        }
    }

    fun cofactor(): Matrix {
        requireSquare()
        val retMatrix = Matrix(this.row, this.col)
        var sign = 1.0
        repeat(row) { y->
            repeat(col) { x->
                sign = (-1.0).pow(y + x + 2.0)
                val tempMatrix = removeRowCol(y, x)
                val value = sign * tempMatrix.det()
                retMatrix[y,x] = value
            }
        }
        return retMatrix
    }

    fun adjugate(): Matrix {
        return this.cofactor().transpose()
    }

    operator fun times(other: Matrix): Matrix {
        return this.dot(other)
    }

    fun dot(B: Matrix): Matrix {
        var B = B
        val m = this.row
        val n = this.col
        var o = B.row
        var p = B.col
        require(n == o) { "Matrices are of improper lengths: \n(" + m + "x" + n + ")\t(" + o + "x" + p + ")" }
        val retMatrix = Matrix(m, p)
        for (ROW_C in 0 until m) {
            for (COL_C in 0 until p) {
                var result : Operation = Constant.ZERO
                for (i in 0 until n) {
                    val A_elem = this[ROW_C][i].toScalar().value
                    val B_elem = B[i][COL_C].toScalar().value
                    result += A_elem * B_elem
                }
                retMatrix[ROW_C][COL_C] = result
            }
        }
        return retMatrix
    }

    operator fun plus(other: Matrix): Matrix {
        return this.add(other)
    }

    fun add(B: Matrix): Matrix {
        val m = this.row
        val n = this.col
        val o = B.row
        val p = B.col
        require(!(m != o || n != p)) { "Matrices are of improper lengths: \n(" + m + "x" + n + ")\t(" + o + "x" + p + ")" }
        val retMatrix = Matrix(m, n)
        for (ROW in 0 until m) {
            for (COL in 0 until n) {
                retMatrix[ROW][COL] = this[ROW][COL] + B[ROW][COL]
            }
        }
        return retMatrix
    }


    companion object {

        fun identity(size: Int): Matrix {
            val retMatrix = Matrix(size, size)
            repeat(size) {
                retMatrix[it][it] = Constant.ONE
            }
            return retMatrix
        }

        fun rot2d(): Matrix {
            val retMatrix = Matrix(2, 2)
            retMatrix[0, 0] = Cos(Variable.THETA)
            retMatrix[0, 1] = Constant.NEGATIVE_ONE * Sin(Variable.THETA)
            retMatrix[1, 0] = Sin(Variable.THETA)
            retMatrix[1, 1] = Cos(Variable.THETA)
            return retMatrix
        }

        fun generate(data: DoubleArray): Matrix {
            val retMatrix = Matrix(data.size, 1)
            data.indices.forEach { index ->
                retMatrix[index][0] = data[index]
            }
            return retMatrix
        }

        fun generate(data: Array<DoubleArray>): Matrix {
            val row = data.size
            val col = data[0].size
            val ret = Matrix(row, col)
            repeat(row) { y->
                repeat(col) { x->
                    ret[y][x] = data[y][x]
                }
            }
            return ret
        }

        fun generate(data: Array<Array<Operation>>): Matrix {
            val row = data.size
            val col = data[0].size
            val ret = Matrix(row, col)
            repeat(row) { y->
                repeat(col) { x->
                    ret[y][x] = data[y][x]
                }
            }
            return ret
        }

        fun transpose(A: Array<DoubleArray>): Array<DoubleArray> {
            val m = A.size
            val n = A[0].size
            val retArray = Array(n) { DoubleArray(m) }
            for (ROW in 0 until m) {
                for (COL in 0 until n) {
                    retArray[COL][ROW] = A[ROW][COL]
                }
            }
            return retArray
        }

        fun dot(A: Array<DoubleArray>, B: Array<DoubleArray>): Array<DoubleArray>? {
            var B = B
            val m = A.size
            val n = A[0].size
            var o = B.size
            var p = B[0].size
            if ((n == 1 && p == 1 || m == 1 && o == 1) && (m > 1 || n > 1) && (o > 1 || p > 1)) {
                B = transpose(B)
                o = B.size
                p = B[0].size
            }
            require(n == o) { "Matrices are of improper lengths: \n(" + m + "x" + n + ")\t(" + o + "x" + p + ")" }
            val retMatrix = Array(m) { DoubleArray(p) }
            for (ROW_C in 0 until m) {
                for (COL_C in 0 until p) {
                    var result = 0.0
                    for (i in 0 until n) {
                        val A_elem = A[ROW_C][i]
                        val B_elem = B[i][COL_C]
                        result += A_elem * B_elem
                    }
                    retMatrix[ROW_C][COL_C] = result
                }
            }
            return retMatrix
        }

        fun add(A: Array<DoubleArray>, B: Array<DoubleArray>): Array<DoubleArray>? {
            val m = A.size
            val n = A[0].size
            val o = B.size
            val p = B[0].size
            require(!(m != o || n != p)) { "Matrices are of improper lengths: \n(" + m + "x" + n + ")\t(" + o + "x" + p + ")" }
            val retMatrix = Array(m) { DoubleArray(n) }
            for (ROW in 0 until m) {
                for (COL in 0 until n) {
                    retMatrix[ROW][COL] = A[ROW][COL] + B[ROW][COL]
                }
            }
            return retMatrix
        }

        fun subtract(A: Array<DoubleArray>, B: Array<DoubleArray>): Array<DoubleArray>? {
            val m = A.size
            val n = A[0].size
            val o = B.size
            val p = B[0].size
            require(!(m != o || n != p)) { "Matrices are of improper lengths: \n(" + m + "x" + n + ")\t(" + o + "x" + p + ")" }
            val retMatrix = Array(m) { DoubleArray(n) }
            for (ROW in 0 until m) {
                for (COL in 0 until n) {
                    retMatrix[ROW][COL] = A[ROW][COL] - B[ROW][COL]
                }
            }
            return retMatrix
        }

    }

}