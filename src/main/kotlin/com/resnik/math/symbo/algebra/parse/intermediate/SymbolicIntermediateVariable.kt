package com.resnik.math.symbo.algebra.parse.intermediate

import com.resnik.math.symbo.algebra.operation.Operation
import com.resnik.math.symbo.algebra.operation.Variable
import com.resnik.math.symbo.algebra.parse.SymbolicTokenType
import com.resnik.math.symbo.parse.Token
import com.resnik.math.symbo.parse.TokenizationException

class SymbolicIntermediateVariable(startIndex: Int, endIndex: Int, token: Token<SymbolicTokenType?>) :
    SymbolicIntermediateOperation(startIndex, endIndex, mutableListOf(token)) {

    override fun compile(): Operation {
        return Variable(tokens[0].rep!!)
    }

    override fun toString(): String {
        return "IVariable:" + tokens[0].rep
    }

    init {
        if (token.type !== SymbolicTokenType.VARIABLE) {
            throw TokenizationException("SymbolicIntermediateVariable requires variable type.")
        }
    }
}