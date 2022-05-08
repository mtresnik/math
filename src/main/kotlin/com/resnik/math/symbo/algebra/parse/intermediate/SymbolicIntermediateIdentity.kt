package com.resnik.math.symbo.algebra.parse.intermediate

import com.resnik.math.symbo.algebra.operation.Operation

class SymbolicIntermediateIdentity(startIndex: Int, endIndex: Int, operation: SymbolicIntermediateOperation) :
    SymbolicIntermediateUnaryOperator(startIndex, endIndex, operation) {

    override fun compile(): Operation = operation.compile()

}