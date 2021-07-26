package com.resnik.math.symbo.parse

class Token<TOKEN_TYPE> @JvmOverloads constructor(var startIndex: Int, var endIndex: Int, var type: TOKEN_TYPE, var rep: String? = null) {

    fun convert(t: TOKEN_TYPE): Token<TOKEN_TYPE> {
        return Token(startIndex, endIndex, t, rep)
    }

    override fun toString(): String = "[" + type + ":" + rep + intArrayOf(startIndex, endIndex).contentToString() + "]"

    companion object {
        fun <TOKEN_TYPE> singleIndex(index: Int, type: TOKEN_TYPE): Token<TOKEN_TYPE> {
            return Token(index, index, type)
        }

        fun <TOKEN_TYPE> nullIndex(type: TOKEN_TYPE): Token<TOKEN_TYPE> {
            return singleIndex(-1, type)
        }

        fun <TOKEN_TYPE> nullIndex(type: TOKEN_TYPE, rep: String?): Token<TOKEN_TYPE> {
            return singleIndex(-1, type, rep)
        }

        fun <TOKEN_TYPE> singleIndex(index: Int, type: TOKEN_TYPE, rep: Char): Token<TOKEN_TYPE> {
            return Token(index, index, type, Character.toString(rep))
        }

        fun <TOKEN_TYPE> singleIndex(index: Int, type: TOKEN_TYPE, rep: String?): Token<TOKEN_TYPE> {
            return Token(index, index, type, rep)
        }
    }

}