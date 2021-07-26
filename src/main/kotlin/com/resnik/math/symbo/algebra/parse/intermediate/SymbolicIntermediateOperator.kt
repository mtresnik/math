package com.resnik.math.symbo.algebra.parse.intermediate

import com.resnik.math.symbo.algebra.parse.SymbolicTokenType
import com.resnik.math.symbo.parse.Token

abstract class SymbolicIntermediateOperator(startIndex: Int, endIndex: Int, var operations: MutableList<SymbolicIntermediateOperation>)
    : SymbolicIntermediateOperation(startIndex, endIndex, yokeAll(operations)) {

    companion object {
        fun yokeAll(operations: MutableList<SymbolicIntermediateOperation>): MutableList<Token<SymbolicTokenType?>> {
            val retList: MutableList<Token<SymbolicTokenType?>> = mutableListOf()
            for (op in operations) {
                retList.addAll(op.tokens)
            }
            return retList
        }
    }

}