package com.resnik.math.symbo.algebra.parse

import com.resnik.math.symbo.parse.Token
import com.resnik.math.symbo.parse.TokenizationException
import java.util.*
import java.util.function.ToIntFunction

class SymbolicTokenizer {

    fun tokenize(inputString: String?, functions: Map<String, SymbolicTokenType>): MutableList<Token<SymbolicTokenType?>> {
        var inputString = inputString
        inputString = preProcess(inputString)
        val numbers: MutableList<Token<SymbolicTokenType>> = tokenizeNumbers(inputString)
        val operators: MutableList<Token<SymbolicTokenType>> = tokenizeOperators(numbers, inputString)
        val parentheses: MutableList<Token<SymbolicTokenType>> = tokenizeParentheses(operators, inputString)
        val text: MutableList<Token<SymbolicTokenType>> = tokenizeText(parentheses, inputString)
        val funcs: MutableList<Token<SymbolicTokenType>> = tokenizeFunctions(text, functions)
        val vars: MutableList<Token<SymbolicTokenType>> = tokenizeVariables(funcs, functions)
        return postProcess(vars)
    }

    fun tokenizeNumbers(inputString: String?): MutableList<Token<SymbolicTokenType>> {
        val VALID = ALL_NUMBERS + DECIMAL
        val arrayRep = inputString!!.toCharArray()
        val retList: MutableList<Token<SymbolicTokenType>> = ArrayList<Token<SymbolicTokenType>>()
        var accumulate = ""
        for (i in arrayRep.indices) {
            if (arrayRep[i] == DECIMAL && accumulate.contains(DECIMAL.toString() + "")) {
                throw TokenizationException("Invalid number formatting.")
            }
            if (!VALID.contains(arrayRep[i].toString() + "")) {
                if (accumulate.isEmpty()) {
                    continue
                }
                val start = i - accumulate.length
                val end = i - 1
                val number: Token<SymbolicTokenType> = Token(start, end, SymbolicTokenType.NUMBER, accumulate)
                retList.add(number)
                accumulate = ""
                continue
            }
            accumulate += arrayRep[i]
        }
        if (!accumulate.isEmpty()) {
            val start = arrayRep.size - accumulate.length
            val end = arrayRep.size - 1
            val number: Token<SymbolicTokenType> = Token(start, end, SymbolicTokenType.NUMBER, accumulate)
            retList.add(number)
        }
        return retList
    }

    fun tokenizeOperators(tokenList: MutableList<Token<SymbolicTokenType>>, inputString: String?): MutableList<Token<SymbolicTokenType>> {
        val retList: MutableList<Token<SymbolicTokenType>> = ArrayList<Token<SymbolicTokenType>>(tokenList)
        val arrayRep = inputString!!.toCharArray()
        retList.sortWith(Comparator.comparingInt { a: Token<SymbolicTokenType> -> a.startIndex })
        for (i in arrayRep.indices) {
            if (indexProcessed(i, retList)) {
                continue
            }
            if (OPERATORS!!.contains(arrayRep[i].toString() + "")) {
                val operator: Token<SymbolicTokenType> = Token.singleIndex(i, SymbolicTokenType.OPERATOR, arrayRep[i])
                retList.add(operator)
            }
        }
        retList.sortWith(Comparator.comparingInt { a: Token<SymbolicTokenType> -> a.startIndex })
        return retList
    }

    fun tokenizeParentheses(tokenList: MutableList<Token<SymbolicTokenType>>, inputString: String?): MutableList<Token<SymbolicTokenType>> {
        val retList: MutableList<Token<SymbolicTokenType>> = ArrayList<Token<SymbolicTokenType>>(tokenList)
        val arrayRep = inputString!!.toCharArray()
        retList.sortWith(Comparator.comparingInt { a: Token<SymbolicTokenType> -> a.startIndex })
        for (i in arrayRep.indices) {
            if (indexProcessed(i, retList)) {
                continue
            }
            if (arrayRep[i] == OPEN_PARENTHESES || arrayRep[i] == CLOSED_PARENTHESES) {
                val parenthesis: Token<SymbolicTokenType> = Token.singleIndex(i, if (arrayRep[i] == OPEN_PARENTHESES) SymbolicTokenType.OPEN_PARENTHESES else SymbolicTokenType.CLOSED_PARENTHESES, arrayRep[i])
                retList.add(parenthesis)
            }
        }
        retList.sortWith(Comparator.comparingInt { a: Token<SymbolicTokenType> -> a.startIndex })
        return retList
    }

    fun tokenizeText(tokenList: MutableList<Token<SymbolicTokenType>>, inputString: String?): MutableList<Token<SymbolicTokenType>> {
        val retList: MutableList<Token<SymbolicTokenType>> = ArrayList<Token<SymbolicTokenType>>(tokenList)
        val arrayRep = inputString!!.toCharArray()
        retList.sortWith(Comparator.comparingInt { a: Token<SymbolicTokenType> -> a.startIndex })
        var accumulated = ""
        for (i in arrayRep.indices) {
            if (indexProcessed(i, retList)) {
                if (!accumulated.isEmpty()) {
                    val start = i - accumulated.length
                    val end = i - 1
                    val text: Token<SymbolicTokenType> = Token(start, end, SymbolicTokenType.TEXT, accumulated)
                    retList.add(text)
                }
                accumulated = ""
                continue
            }
            accumulated += arrayRep[i]
        }
        if (!accumulated.isEmpty()) {
            val start = arrayRep.size - accumulated.length
            val end = arrayRep.size - 1
            val text: Token<SymbolicTokenType> = Token(start, end, SymbolicTokenType.TEXT, accumulated)
            retList.add(text)
        }
        retList.sortWith(Comparator.comparingInt { a: Token<SymbolicTokenType> -> a.startIndex })
        return retList
    }

    fun tokenizeFunctions(tokenList: MutableList<Token<SymbolicTokenType>>, functions: Map<String, SymbolicTokenType>): MutableList<Token<SymbolicTokenType>> {
        val retList: MutableList<Token<SymbolicTokenType>> = ArrayList<Token<SymbolicTokenType>>()
        for (i in tokenList.indices) {
            val curr: Token<SymbolicTokenType> = tokenList[i]
            val next: Token<SymbolicTokenType>? = if (i < tokenList.size - 1) tokenList[i + 1] else null
            if (curr.type !== SymbolicTokenType.TEXT) {
                retList.add(curr)
                continue
            }
            if (next != null && next.type === SymbolicTokenType.OPEN_PARENTHESES) {
                var innerFunc: String? = null
                for (func in functions!!.entries) {
                    if (func.value !== SymbolicTokenType.FUNCTION) {
                        continue
                    }
                    if (func.key!!.length > curr.rep!!.length) {
                        continue
                    }
                    if (curr.rep!!.endsWith(func.key!!)) {
                        innerFunc = func.key
                        break
                    }
                }
                if (innerFunc != null) {
                    val endIndex: Int = curr.rep!!.indexOf(innerFunc)
                    if (endIndex != 0) {
                        val newRep: String = curr.rep!!.substring(0, endIndex)
                        val rem: Token<SymbolicTokenType> = Token.nullIndex(SymbolicTokenType.TEXT, newRep)
                        retList.add(rem)
                    }
                    val function: Token<SymbolicTokenType> = Token.nullIndex(SymbolicTokenType.FUNCTION, innerFunc)
                    retList.add(function)
                } else {
                    val rem: Token<SymbolicTokenType> = curr.convert(SymbolicTokenType.TEXT)
                    retList.add(rem)
                }
            } else {
                val rem: Token<SymbolicTokenType> = curr.convert(SymbolicTokenType.TEXT)
                retList.add(rem)
            }
        }
        return retList
    }

    fun tokenizeVariables(tokenList: MutableList<Token<SymbolicTokenType>>, variableMap: Map<String, SymbolicTokenType>): MutableList<Token<SymbolicTokenType>> {
        val retList: MutableList<Token<SymbolicTokenType>> = ArrayList<Token<SymbolicTokenType>>()
        for (i in tokenList!!.indices) {
            val curr: Token<SymbolicTokenType> = tokenList[i]
            if (curr.type !== SymbolicTokenType.TEXT) {
                retList.add(curr)
                continue
            }
            retList.addAll(maxVariables(curr.rep!!, variableMap)!!)
        }
        return retList
    }

    companion object {
        val ALL_NUMBERS: String? = "0123456789"
        const val DECIMAL = '.'
        val PLUS: String? = "+"
        val MINUS: String? = "-"
        val MULT: String? = "*"
        val DIVIDE: String? = "/"
        val POW: String? = "^"
        val OPERATORS: String? = PLUS + MINUS + MULT + DIVIDE + POW
        const val OPEN_PARENTHESES = '('
        const val CLOSED_PARENTHESES = ')'
        fun preProcess(inputString: String?): String? {
            var balance = 0
            for (c in inputString!!.toCharArray()) {
                if (c == OPEN_PARENTHESES) {
                    balance--
                } else if (c == CLOSED_PARENTHESES) {
                    balance++
                }
            }
            if (balance != 0) {
                throw TokenizationException("Imbalanced number of parentheses.")
            }
            return inputString
                    .replace(" ", "")
                    .replace("\\(\\)".toRegex(), "")
        }

        fun postProcess(inputList: MutableList<Token<SymbolicTokenType>>): MutableList<Token<SymbolicTokenType?>> {
            var currList: MutableList<Token<SymbolicTokenType>> = justifyMult(inputList)
            var collapsed: MutableList<Token<SymbolicTokenType>> = collapseSigns(currList)
            while (collapsed!!.size != currList!!.size) {
                currList = collapsed
                collapsed = collapseSigns(currList)
            }
            val ret : MutableList<Token<SymbolicTokenType?>> = mutableListOf()
            currList.forEach { ret.add(it as Token<SymbolicTokenType?>) }
            return ret
        }

        fun justifyMult(inputList: MutableList<Token<SymbolicTokenType>>): MutableList<Token<SymbolicTokenType>> {
            val retList: MutableList<Token<SymbolicTokenType>> = ArrayList<Token<SymbolicTokenType>>()
            for (i in inputList!!.indices) {
                val curr: Token<SymbolicTokenType> = inputList[i]
                val next: Token<SymbolicTokenType>? = if (i < inputList.size - 1) inputList[i + 1] else null
                retList.add(curr)
                if ((curr.type === SymbolicTokenType.NUMBER || curr.type === SymbolicTokenType.VARIABLE || curr.type === SymbolicTokenType.CLOSED_PARENTHESES)
                        && next != null && next.type !== SymbolicTokenType.OPERATOR && next.type !== SymbolicTokenType.CLOSED_PARENTHESES) {
                    retList.add(Token.nullIndex(SymbolicTokenType.OPERATOR, MULT))
                }
            }
            return retList
        }

        fun collapseSigns(inputList: MutableList<Token<SymbolicTokenType>>): MutableList<Token<SymbolicTokenType>> {
            val retList: MutableList<Token<SymbolicTokenType>> = ArrayList<Token<SymbolicTokenType>>()
            var i = 0
            while (i < inputList!!.size) {
                val curr: Token<SymbolicTokenType> = inputList[i]
                val next: Token<SymbolicTokenType>? = if (i < inputList.size - 1) inputList[i + 1] else null
                if (curr.type === SymbolicTokenType.OPERATOR && next != null && next.type === SymbolicTokenType.OPERATOR) {
                    if ((curr.rep.equals(PLUS) || curr.rep.equals(MINUS)) && (next.rep.equals(PLUS) || next.rep.equals(MINUS))) {
                        if (curr.rep.equals(next.rep)) {
                            retList.add(Token.nullIndex(SymbolicTokenType.OPERATOR, PLUS))
                        } else {
                            retList.add(Token.nullIndex(SymbolicTokenType.OPERATOR, MINUS))
                        }
                        i++
                        i++
                        continue
                    }
                }
                retList.add(curr)
                i++
            }
            return retList
        }

        fun indexProcessed(i: Int, tokenList: MutableList<Token<SymbolicTokenType>>): Boolean {
            for (t in tokenList!!) {
                if (ParseRangeUtils.inRange(i, t.startIndex, t.endIndex)) {
                    return true
                }
            }
            return false
        }

        fun maxVariables(inputString: String, variableMap: Map<String, SymbolicTokenType>): MutableList<Token<SymbolicTokenType>> {
            val retList: MutableList<Token<SymbolicTokenType>> = ArrayList<Token<SymbolicTokenType>>()
            var max: String? = null
            var maxCount = -1
            for (varEntry in variableMap!!.entries) {
                if (varEntry.value !== SymbolicTokenType.VARIABLE) {
                    continue
                }
                if (inputString!!.contains(varEntry.key!!) && varEntry.key!!.length > maxCount) {
                    max = varEntry.key
                    maxCount = max!!.length
                }
            }
            val rem: Array<String?> = ParseStringUtils.findRem(inputString, max)
            if (max == null || rem.isEmpty()) {
                retList.add(Token.nullIndex(SymbolicTokenType.VARIABLE, inputString))
                return retList
            }
            if (rem.size == 1) {
                if (inputString!!.indexOf(max) == 0) {
                    retList.add(Token.nullIndex(SymbolicTokenType.VARIABLE, max))
                    val RHS: MutableList<Token<SymbolicTokenType>> = maxVariables(rem[0]!!, variableMap)
                    retList.addAll(RHS!!)
                    return retList
                }
                val LHS: MutableList<Token<SymbolicTokenType>> = maxVariables(rem[0]!!, variableMap)
                retList.addAll(LHS!!)
                retList.add(Token.nullIndex(SymbolicTokenType.VARIABLE, max))
                return retList
            }
            if (rem.size == 2) {
                val LHS: MutableList<Token<SymbolicTokenType>> = maxVariables(rem[0]!!, variableMap)
                val RHS: MutableList<Token<SymbolicTokenType>> = maxVariables(rem[1]!!, variableMap)
                retList.addAll(LHS!!)
                retList.add(Token.nullIndex(SymbolicTokenType.VARIABLE, max))
                retList.addAll(RHS!!)
                return retList
            }
            retList.add(Token.nullIndex(SymbolicTokenType.VARIABLE, inputString))
            return retList
        }
    }
}