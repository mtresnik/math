package com.resnik.math.symbo.algebra.parse.intermediate

abstract class SymbolicIntermediateUnaryOperator(startIndex: Int, endIndex: Int, var operation: SymbolicIntermediateOperation)
    : SymbolicIntermediateOperator(startIndex, endIndex, mutableListOf(operation))