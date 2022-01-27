package com.resnik.math.symbo.algebra.parse

import com.resnik.math.symbo.algebra.operation.Constant
import com.resnik.math.symbo.algebra.operation.Operation
import com.resnik.math.symbo.algebra.operation.Variable
import com.resnik.math.symbo.algebra.parse.intermediate.*
import com.resnik.math.symbo.parse.Token
import java.util.*

class SymbolicSyntaxAnalyzer(val tokenizer: SymbolicTokenizer = SymbolicTokenizer(), val repMap: Map<String, SymbolicTokenType> = SymbolicTokenType.DEFAULT_FUNCTIONS) {

    @Throws(SyntaxException::class)
    fun analyze(inputList: List<Token<SymbolicTokenType?>>): Operation? {
        justifySyntax(inputList)
        val symbolicIntermediateOperation: SymbolicIntermediateOperation? = genIntermediateOperation(inputList)
        return symbolicIntermediateOperation?.compile()
                ?.evaluate(Variable.I, Constant.I)
                ?.evaluate(Variable.E, Constant.E)
                ?.evaluate(Variable.PI, Constant.PI)
    }

    @Throws(SyntaxException::class)
    fun analyze(inputString : String): Operation? {
        return analyze(tokenizer.tokenize(inputString, repMap))
    }

    @Throws(SyntaxException::class)
    fun justifySyntax(inputList: List<Token<SymbolicTokenType?>>) {
        justifySigns(inputList)
    }

    @Throws(SyntaxException::class)
    fun justifySigns(inputList: List<Token<SymbolicTokenType?>>) {
        for (i in 0 until inputList.size - 1) {
            val curr: Token<SymbolicTokenType?> = inputList[i]
            if (curr.type !== SymbolicTokenType.OPERATOR) {
                continue
            }
            if (curr.rep.equals(SymbolicTokenizer.MINUS) || curr.rep.equals(SymbolicTokenizer.PLUS)) {
                val next: Token<SymbolicTokenType?> = inputList[i + 1]
                val message = "Invalid syntax at:" + curr.toString().toString() + "\t" + next.toString()
                if (next.type === SymbolicTokenType.CLOSED_PARENTHESES) {
                    throw SyntaxException(message)
                }
                if (next.type === SymbolicTokenType.OPERATOR) {
                    throw SyntaxException(message)
                }
            }
        }
    }

    companion object {
        fun genTokenSets(inputList: List<Token<SymbolicTokenType?>>): List<SymbolicTokenSet> {
            val parenthesesSets: List<SymbolicTokenSet> = genParenthesesSets(inputList)
            val functionSets: List<SymbolicTokenSet> = genFunctionSets(parenthesesSets, inputList)
            val variableSets: List<SymbolicTokenSet> = genVariableSets(functionSets, inputList)
            return genNumberSets(variableSets, inputList)
        }

        fun genParenthesesSets(inputList: List<Token<SymbolicTokenType?>>): List<SymbolicTokenSet> {
            val retList: MutableList<SymbolicTokenSet> = ArrayList<SymbolicTokenSet>()
            var inner: MutableList<Token<SymbolicTokenType?>> = mutableListOf()
            var balance = 0
            var startIndex = -1
            for (i in inputList.indices) {
                val token: Token<SymbolicTokenType?> = inputList[i]
                if (token.type === SymbolicTokenType.OPEN_PARENTHESES) {
                    balance--
                } else if (token.type === SymbolicTokenType.CLOSED_PARENTHESES) {
                    balance++
                }
                if (balance == -1 && token.type === SymbolicTokenType.OPEN_PARENTHESES) {
                    startIndex = i
                }
                if (balance == 0 && token.type === SymbolicTokenType.CLOSED_PARENTHESES) {
                    for (j in startIndex + 1 until i) {
                        inner.add(inputList[j])
                    }
                    retList.add(SymbolicTokenSet(startIndex, i, SymbolicTokenSetType.PARENTHESES, inner))
                    startIndex = -1
                    inner = mutableListOf()
                }
            }
            return retList
        }

        fun genFunctionSets(curr: List<SymbolicTokenSet>, inputList: List<Token<SymbolicTokenType?>>): List<SymbolicTokenSet> {
            val clone: MutableList<SymbolicTokenSet> = ArrayList(curr)
            val retList: MutableList<SymbolicTokenSet> = ArrayList(curr)
            for (i in inputList.indices) {
                if (indexProcessedToken(i, curr)) {
                    continue
                }
                val token: Token<SymbolicTokenType?> = inputList[i]
                if (token.type !== SymbolicTokenType.FUNCTION) {
                    continue
                }
                var found: SymbolicTokenSet? = null
                val expectedIndex = i + 1
                for (tokenSet in clone) {
                    if (tokenSet.type !== SymbolicTokenSetType.PARENTHESES) {
                        continue
                    }
                    if (tokenSet.startIndex == expectedIndex) {
                        found = tokenSet
                        break
                    }
                }
                if (found == null) {
                    throw SyntaxException("Missing argument(s) for function:" + token.rep)
                }
                clone.remove(found)
                retList.remove(found)
                val function = SymbolicTokenSet(i, found.endIndex, SymbolicTokenSetType.FUNCTION, found.tokens)
                function.rep = token.rep
                retList.add(function)
            }
            retList.sortWith(Comparator.comparingInt { a: SymbolicTokenSet -> a.startIndex })
            return retList
        }

        fun genVariableSets(curr: List<SymbolicTokenSet>, inputList: List<Token<SymbolicTokenType?>>): List<SymbolicTokenSet> {
            val retList: MutableList<SymbolicTokenSet> = ArrayList(curr)
            for (i in inputList.indices) {
                if (indexProcessedToken(i, curr)) {
                    continue
                }
                if(inputList[i].type == null){
                    continue
                }
                val token: Token<SymbolicTokenType?> = inputList[i]
                if (token.type !== SymbolicTokenType.VARIABLE) {
                    continue
                }
                val variable = SymbolicTokenSet(i, i, SymbolicTokenSetType.VARIABLE, listOf(token))
                variable.rep = token.rep
                retList.add(variable)
            }
            retList.sortWith(Comparator.comparingInt { a: SymbolicTokenSet -> a.startIndex })
            return retList
        }

        fun genNumberSets(curr: List<SymbolicTokenSet>, inputList: List<Token<SymbolicTokenType?>>): List<SymbolicTokenSet> {
            val retList: MutableList<SymbolicTokenSet> = ArrayList(curr)
            for (i in inputList.indices) {
                if (indexProcessedToken(i, curr)) {
                    continue
                }
                val token: Token<SymbolicTokenType?> = inputList[i]
                if (token.type !== SymbolicTokenType.NUMBER) {
                    continue
                }
                val number = SymbolicTokenSet(i, i, SymbolicTokenSetType.NUMBER, mutableListOf(token))
                number.rep = token.rep
                retList.add(number)
            }
            retList.sortWith(Comparator.comparingInt { a: SymbolicTokenSet -> a.startIndex })
            return retList
        }

        fun genIntermediateOperation(inputList: List<Token<SymbolicTokenType?>>): SymbolicIntermediateOperation? {
            val tokenSets: List<SymbolicTokenSet> = genTokenSets(inputList)
            val symbolicIntermediateOperations: List<SymbolicIntermediateOperation> = genIntermediateOperations(tokenSets)
            val operators: List<SymbolicIntermediateOperation> = generateIntermediateOperators(symbolicIntermediateOperations, inputList)
            return if (operators.size == 1) {
                operators[0]
            } else null
        }

        fun genIntermediateOperations(curr: List<SymbolicTokenSet>): List<SymbolicIntermediateOperation> {
            val retList: MutableList<SymbolicIntermediateOperation> = ArrayList<SymbolicIntermediateOperation>()
            for (symbolicTokenSet in curr) {
                when (symbolicTokenSet.type) {
                    SymbolicTokenSetType.NUMBER -> retList.add(SymbolicIntermediateNumber(symbolicTokenSet.startIndex, symbolicTokenSet.endIndex, symbolicTokenSet.tokens.first()))
                    SymbolicTokenSetType.VARIABLE -> retList.add(SymbolicIntermediateVariable(symbolicTokenSet.startIndex, symbolicTokenSet.endIndex, symbolicTokenSet.tokens.first()))
                    SymbolicTokenSetType.PARENTHESES -> retList.add(SymbolicIntermediateParentheses(symbolicTokenSet.startIndex, symbolicTokenSet.endIndex, genIntermediateOperation(symbolicTokenSet.tokens)!!))
                    SymbolicTokenSetType.FUNCTION -> retList.add(SymbolicIntermediateFunction(symbolicTokenSet.startIndex, symbolicTokenSet.endIndex, genIntermediateOperation(symbolicTokenSet.tokens)!!, symbolicTokenSet.rep!!))
                    else -> {
                    }
                }
            }
            return retList
        }

        fun generateIntermediateOperators(curr: List<SymbolicIntermediateOperation>, inputList: List<Token<SymbolicTokenType?>>): List<SymbolicIntermediateOperation> {
            val ident: List<SymbolicIntermediateOperation> = generateIdentities(curr, inputList)
            val powers: List<SymbolicIntermediateOperation> = generatePowerOperators(ident, inputList)
            val mulDiv: List<SymbolicIntermediateOperation> = generateMultDivOperators(powers, inputList)
            return generateAddSub(mulDiv, inputList)
        }

        fun generateIdentities(curr: List<SymbolicIntermediateOperation>, inputList: List<Token<SymbolicTokenType?>>): List<SymbolicIntermediateOperation> {
            val clone: MutableList<SymbolicIntermediateOperation> = ArrayList(curr)
            val retList: List<SymbolicIntermediateOperation>
            for (i in inputList.indices) {
                val token: Token<SymbolicTokenType?> = inputList[i]
                if (indexProcessedOperation(i, clone)) {
                    continue
                }
                if (token.type !== SymbolicTokenType.OPERATOR) {
                    continue
                }
                if (token.rep.equals(SymbolicTokenizer.PLUS)) {
                    val left: SymbolicIntermediateOperation? = left(i, clone)
                    val right: SymbolicIntermediateOperation? = right(i, clone)
                            ?: throw SyntaxException("PLUS requires a right element.")
                    if (left == null) {
                        clone.remove(right)
                        clone.add(SymbolicIntermediateIdentity(i, right!!.endIndex, right))
                    }
                } else if (token.rep.equals(SymbolicTokenizer.MINUS)) {
                    val left: SymbolicIntermediateOperation? = left(i, clone)
                    val right: SymbolicIntermediateOperation? = right(i, clone)
                            ?: throw SyntaxException("SUB requires a right element.")
                    if (left == null) {
                        clone.remove(right)
                        clone.add(SymbolicIntermediateNegation(i, right!!.endIndex, right))
                    }
                }
            }
            retList = ArrayList(clone)
            retList.sortedWith(Comparator.comparingInt { a: SymbolicIntermediateOperation -> a.startIndex })
            return retList
        }

        fun generatePowerOperators(curr: List<SymbolicIntermediateOperation>?, inputList: List<Token<SymbolicTokenType?>>): List<SymbolicIntermediateOperation> {
            val clone: MutableList<SymbolicIntermediateOperation> = ArrayList(curr)
            val retList: List<SymbolicIntermediateOperation>
            for (i in inputList.indices) {
                val token: Token<SymbolicTokenType?> = inputList[i]
                if (indexProcessedOperation(i, clone)) {
                    continue
                }
                if (token.type !== SymbolicTokenType.OPERATOR) {
                    continue
                }
                if (token.rep.equals(SymbolicTokenizer.POW)) {
                    val left: SymbolicIntermediateOperation? = left(i, clone)
                    val right: SymbolicIntermediateOperation? = right(i, clone)
                    if (left == null || right == null) {
                        throw SyntaxException("POW requires both left and right elements.")
                    }
                    clone.remove(left)
                    clone.remove(right)
                    clone.add(SymbolicIntermediatePower(left.startIndex, right.endIndex, left, right))
                }
            }
            retList = ArrayList(clone)
            retList.sortedWith(Comparator.comparingInt { a: SymbolicIntermediateOperation -> a.startIndex })
            return retList
        }

        fun generateMultDivOperators(curr: List<SymbolicIntermediateOperation>?, inputList: List<Token<SymbolicTokenType?>>): List<SymbolicIntermediateOperation> {
            val clone: MutableList<SymbolicIntermediateOperation> = ArrayList(curr)
            val retList: List<SymbolicIntermediateOperation>
            for (i in inputList.indices) {
                val token: Token<SymbolicTokenType?> = inputList[i]
                if (indexProcessedOperation(i, clone)) {
                    continue
                }
                if (token.type !== SymbolicTokenType.OPERATOR) {
                    continue
                }
                if (token.rep.equals(SymbolicTokenizer.MULT)) {
                    val left: SymbolicIntermediateOperation? = left(i, clone)
                    val right: SymbolicIntermediateOperation? = right(i, clone)
                    if (left == null || right == null) {
                        throw SyntaxException("MULT requires both left and right elements.")
                    }
                    clone.remove(left)
                    clone.remove(right)
                    clone.add(SymbolicIntermediateMultiplication(left.startIndex, right.endIndex, left, right))
                } else if (token.rep.equals(SymbolicTokenizer.DIVIDE)) {
                    val left: SymbolicIntermediateOperation? = left(i, clone)
                    val right: SymbolicIntermediateOperation? = right(i, clone)
                    if (left == null || right == null) {
                        throw SyntaxException("DIV requires both left and right elements.")
                    }
                    clone.remove(left)
                    clone.remove(right)
                    clone.add(SymbolicIntermediateDivision(left.startIndex, right.endIndex, left, right))
                }
            }
            retList = ArrayList(clone)
            retList.sortedWith(Comparator.comparingInt { a: SymbolicIntermediateOperation -> a.startIndex })
            return retList
        }

        fun generateAddSub(curr: List<SymbolicIntermediateOperation>?, inputList: List<Token<SymbolicTokenType?>>): List<SymbolicIntermediateOperation> {
            val clone: MutableList<SymbolicIntermediateOperation> = ArrayList(curr)
            var retList: List<SymbolicIntermediateOperation>
            for (i in inputList.indices) {
                val token: Token<SymbolicTokenType?> = inputList[i]
                if (indexProcessedOperation(i, clone)) {
                    continue
                }
                if (token.type !== SymbolicTokenType.OPERATOR) {
                    continue
                }
                if (token.rep.equals(SymbolicTokenizer.PLUS)) {
                    val left: SymbolicIntermediateOperation? = left(i, clone)
                    val right: SymbolicIntermediateOperation? = right(i, clone)
                            ?: throw SyntaxException("PLUS requires a right element.")
                    if (left != null) {
                        clone.remove(left)
                        clone.remove(right)
                        clone.add(SymbolicIntermediateAddition(left.startIndex, right!!.endIndex, left, right))
                    }
                } else if (token.rep.equals(SymbolicTokenizer.MINUS)) {
                    val left: SymbolicIntermediateOperation? = left(i, clone)
                    val right: SymbolicIntermediateOperation? = right(i, clone)
                            ?: throw SyntaxException("SUB requires a right element.")
                    if (left != null) {
                        clone.remove(left)
                        clone.remove(right)
                        clone.add(SymbolicIntermediateSubtraction(left.startIndex, right!!.endIndex, left, right))
                    }
                }
            }
            retList = ArrayList(clone)
            retList.sortedWith(Comparator.comparingInt { a: SymbolicIntermediateOperation -> a.startIndex })
            return retList
        }

        fun left(i: Int, tokenList: List<SymbolicIntermediateOperation>): SymbolicIntermediateOperation? {
            for (tokenSet in tokenList) {
                if (ParseRangeUtils.inRange(i - 1, tokenSet.startIndex, tokenSet.endIndex)) {
                    return tokenSet
                }
            }
            return null
        }

        fun right(i: Int, tokenList: List<SymbolicIntermediateOperation>): SymbolicIntermediateOperation? {
            for (tokenSet in tokenList) {
                if (ParseRangeUtils.inRange(i + 1, tokenSet.startIndex, tokenSet.endIndex)) {
                    return tokenSet
                }
            }
            return null
        }

        fun indexProcessedOperation(i: Int, opList: List<SymbolicIntermediateOperation>): Boolean {
            for (t in opList) {
                if (ParseRangeUtils.inRange(i, t.startIndex, t.endIndex)) {
                    return true
                }
            }
            return false
        }

        fun indexProcessedToken(i: Int, tokenList: List<SymbolicTokenSet>): Boolean {
            for (t in tokenList) {
                if (ParseRangeUtils.inRange(i, t.startIndex, t.endIndex)) {
                    return true
                }
            }
            return false
        }
    }
}