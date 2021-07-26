package com.resnik.math.symbo.algebra.parse.intermediate

import com.resnik.math.symbo.algebra.operation.Operation
import com.resnik.math.symbo.algebra.operation.functions.FunctionBuilder

class SymbolicIntermediateFunction(startIndex: Int, endIndex: Int, operation: SymbolicIntermediateOperation, var name: String) : SymbolicIntermediateUnaryOperator(startIndex, endIndex, operation) {

    override fun compile(): Operation = FunctionBuilder.substitute(name, arrayOf(operation.compile()))

}
