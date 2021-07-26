package com.resnik.math.symbo.algebra.operation.base

import com.resnik.math.symbo.algebra.operation.Constant
import com.resnik.math.symbo.algebra.operation.Operation

class Parentheses(val inner : Operation) : Operation(inner) {

    override fun toConstant(): Constant {
        if(!inner.isConstant()){
            return Constant.NaN
        }
        return inner.toConstant()
    }

    override fun toNonConstantString(): String = "($inner)"

    override fun generate(values: Array<Operation>): Operation {
        if(values.isEmpty()){
           return Parentheses(Constant.ONE)
        }
        return Parentheses(values[0])
    }

    fun unwrap() : Operation {
        var curr : Operation = this;
        while(curr is Parentheses){
            curr = curr.inner
        }
        return curr
    }

}