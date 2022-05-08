package com.resnik.math.symbo.algebra.parse.intermediate

import com.resnik.math.symbo.algebra.operation.Operation
import com.resnik.math.symbo.algebra.operation.base.Negation

class SymbolicIntermediateNegation(startIndex: Int, endIndex: Int, operation: SymbolicIntermediateOperation) :
    SymbolicIntermediateUnaryOperator(startIndex, endIndex, operation) {

    override fun compile(): Operation = Negation(operation.compile())

}
