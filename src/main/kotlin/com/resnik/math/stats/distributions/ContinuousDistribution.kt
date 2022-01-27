package com.resnik.math.stats.distributions

open class ContinuousDistribution(val pdf : Function1<Double, Double>,
                                  center : Double = 0.0,
                                  range : Double = 100.0,
                                  numPoints : Int = 1000)
    : Distribution<Double> {

    private val pdfMap = mutableMapOf<Double, Double>()
    private val internalDiscreteDistribution : DiscreteDistribution<Double>

    init {
        if(numPoints < 2){
            pdfMap[center] = pdf(center)
        } else {
            val min = center - range
            repeat(numPoints) {
                val t = it.toDouble() / (numPoints - 1)
                val x = min + 2 * t * range
                pdfMap[x] = pdf(x)
            }
        }
        internalDiscreteDistribution = DiscreteDistribution(pdfMap)

    }

    override fun next(): Double = internalDiscreteDistribution.next()

}