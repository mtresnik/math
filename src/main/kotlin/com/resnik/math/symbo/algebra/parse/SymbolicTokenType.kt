package com.resnik.math.symbo.algebra.parse

import com.resnik.math.symbo.algebra.operation.functions.FunctionBuilder

enum class SymbolicTokenType {
    OPERATOR, NUMBER, OPEN_PARENTHESES, CLOSED_PARENTHESES, TEXT, FUNCTION, VARIABLE;

    companion object {
        var DEFAULT_FUNCTIONS: MutableMap<String, SymbolicTokenType> = LinkedHashMap()

        init {
            for ((key) in FunctionBuilder.generationMap) {
                DEFAULT_FUNCTIONS[key] = FUNCTION
            }
        }
    }
}