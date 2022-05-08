package com.resnik.math.symbo.algebra.parse.intermediate

abstract class SymbolicIntermediateBinaryOperator(
    startIndex: Int,
    endIndex: Int,
    var op1: SymbolicIntermediateOperation,
    var op2: SymbolicIntermediateOperation
) : SymbolicIntermediateOperator(startIndex, endIndex, mutableListOf(op1, op2))