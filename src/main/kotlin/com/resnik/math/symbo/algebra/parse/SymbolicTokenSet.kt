package com.resnik.math.symbo.algebra.parse

import com.resnik.math.symbo.parse.Token

class SymbolicTokenSet(firstIndex: Int, secondIndex: Int, type: SymbolicTokenSetType, tokens: List<Token<SymbolicTokenType?>>) {

    var startIndex = -1
    var endIndex = -1
    var type: SymbolicTokenSetType
    var tokens: MutableList<Token<SymbolicTokenType?>>
    var rep: String? = null
    override fun toString(): String {
        return "[" + type + ":(" + startIndex + "," + endIndex + ")" + (if (rep != null) ":$rep" else "") + "]"
    }

    init {
        startIndex = firstIndex
        endIndex = secondIndex
        this.type = type
        this.tokens = tokens.toMutableList()
    }
}