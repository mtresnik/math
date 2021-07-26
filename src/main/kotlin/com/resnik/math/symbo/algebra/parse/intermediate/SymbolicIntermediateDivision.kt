package com.resnik.math.symbo.algebra.parse.intermediate

import com.resnik.math.symbo.algebra.operation.Operation
import com.resnik.math.symbo.algebra.operation.base.Division

class SymbolicIntermediateDivision(startIndex: Int, endIndex: Int, op1: SymbolicIntermediateOperation, op2: SymbolicIntermediateOperation)
    : SymbolicIntermediateBinaryOperator(startIndex, endIndex, op1, op2) {

    override fun compile(): Operation = Division(op1.compile(), op2.compile())

}