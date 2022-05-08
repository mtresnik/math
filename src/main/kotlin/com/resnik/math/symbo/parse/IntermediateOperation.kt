package com.resnik.math.symbo.parse

abstract class IntermediateOperation<OPERATION, TOKEN_TYPE>(
    val startIndex: Int,
    val endIndex: Int,
    val tokens: List<Token<TOKEN_TYPE?>>
) {

    abstract fun compile(): OPERATION

}