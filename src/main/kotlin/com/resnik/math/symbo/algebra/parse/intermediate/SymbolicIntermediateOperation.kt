package com.resnik.math.symbo.algebra.parse.intermediate

import com.resnik.math.symbo.algebra.operation.Operation
import com.resnik.math.symbo.algebra.parse.SymbolicTokenType
import com.resnik.math.symbo.parse.IntermediateOperation
import com.resnik.math.symbo.parse.Token

abstract class SymbolicIntermediateOperation(startIndex: Int, endIndex: Int, tokens: List<Token<SymbolicTokenType?>>) : IntermediateOperation<Operation, SymbolicTokenType>(startIndex, endIndex, tokens)