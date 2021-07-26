package com.resnik.math.symbo.algebra.parse.intermediate

import com.resnik.math.symbo.algebra.operation.Constant
import com.resnik.math.symbo.algebra.operation.Operation
import com.resnik.math.symbo.algebra.parse.SymbolicTokenType
import com.resnik.math.symbo.parse.Token
import com.resnik.math.symbo.parse.TokenizationException

class SymbolicIntermediateNumber(startIndex: Int, endIndex: Int, token: Token<SymbolicTokenType?>) : SymbolicIntermediateOperation(startIndex, endIndex, mutableListOf(token)) {

    override fun compile(): Operation {
        val rep = tokens[0].rep
        val dRep = rep!!.toDouble()
        return Constant(dRep)
    }

    init {
        if (token.type !== SymbolicTokenType.NUMBER) {
            throw TokenizationException("SymbolicIntermediateNumber requires number type.")
        }
    }
}