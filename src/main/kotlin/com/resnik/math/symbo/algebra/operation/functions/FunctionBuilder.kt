package com.resnik.math.symbo.algebra.operation.functions

import com.resnik.math.symbo.algebra.operation.Constant
import com.resnik.math.symbo.algebra.operation.Operation
import com.resnik.math.symbo.algebra.operation.Variable
import java.util.LinkedHashMap
import java.util.function.Function

object FunctionBuilder {

    var generationMap: MutableMap<String, Function<Void?, Operation>> = LinkedHashMap<String, Function<Void?, Operation>>()
    var paramsMap: MutableMap<String, Array<Variable>> = LinkedHashMap<String, Array<Variable>>()

    fun substitute(name: String): Operation {
        return substitute(name, arrayOfNulls<Operation>(0))
    }

    fun substitute(name: String, args: Array<Operation?>): Operation {
        require(generationMap.containsKey(name)) { "Function definition doesn't exist:$name" }
        require(paramsMap.containsKey(name)) { "Parameters don't exist:$name" }
        require(paramsMap[name]!!.size == args.size) { "Mismatched parameters for function:$name" }
        var operation: Operation = generationMap[name]!!.apply(null)
        val params: Array<Variable> = paramsMap[name]!!
        for (i in args.indices) {
            operation = operation.evaluate(params[i], args[i]!!)
        }
        return operation
    }

    fun generate(name: String, params: Array<Variable>, inner: Function<Void?, Operation>) {
        if (generationMap.containsKey(name)) {
            return
        }
        require(inner.apply(null).getVariables().size == params.size) { "Please define inner variables for function:$name" }
        generationMap[name] = inner
        paramsMap[name] = params
    }

    init {
        generate("sin", arrayOf(Variable.X), Function { Sin(Variable.X) })
        generate("cos", arrayOf(Variable.X), Function { Cos(Variable.X) })
        val base = Variable("BASE")
        generate("log_", arrayOf(base, Variable.X), Function { Log(base, Variable.X) })
        generate("ln", arrayOf<Variable>(Variable.X), Function<Void?, Operation> { Log.ln(Variable.X) })
        generate("log", arrayOf<Variable>(Variable.X), Function { Log(Constant.TEN, Variable.X) })
        generate("abs", arrayOf<Variable>(Variable.X), Function { AbsoluteValue(Variable.X) })
        generate("sqrt", arrayOf<Variable>(Variable.X), Function<Void?, Operation> { Variable.X.pow(Constant(0.5)) })
    }
}
