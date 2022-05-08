package com.resnik.math.symbo.algebra.operation.functions

import com.resnik.math.symbo.algebra.operation.Constant
import com.resnik.math.symbo.algebra.operation.Operation

class AbsoluteValue(val inner: Operation) : Operation(inner) {

    override fun toConstant(): Constant = Constant(inner.toConstant().value.r())

    override fun toNonConstantString(): String = "|$inner|"

    override fun generate(values: Array<Operation>): Operation = AbsoluteValue(values.first())

}