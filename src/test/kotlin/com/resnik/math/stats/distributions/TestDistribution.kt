package com.resnik.math.stats.distributions

import com.resnik.math.linear.array.ArrayPoint2d
import com.resnik.math.plot.Histogram
import com.resnik.math.plot.HistogramData
import com.resnik.math.plot.Plot2d
import com.resnik.math.util.CountList
import org.junit.Ignore
import org.junit.Test
import javax.swing.ImageIcon
import javax.swing.JLabel
import javax.swing.JOptionPane
import kotlin.math.PI
import kotlin.math.exp
import kotlin.math.pow
import kotlin.math.sqrt

@Ignore
class TestDistribution {

    @Test
    @Ignore
    fun testDiscreteDistribution() {
        val map = mutableMapOf(Pair(1, 0.2), Pair(2, 0.3), Pair(3, 0.5))
        val distribution = DiscreteDistribution(map)

        val countList = CountList<Int>()
        val numIterations = 100000
        repeat(numIterations) { countList.add(distribution.next()) }
        println(countList)
        val histogramData = HistogramData.fromCountList(countList)
        val histogram = Histogram(histogramData)
        val image = histogram.plot()
        val icon = ImageIcon(image)
        val label = JLabel(icon)
        JOptionPane.showMessageDialog(null, label)
    }

    @Test
    @Ignore
    fun testContinuousDistribution() {
        val variance = 1.5
        val mean = 5.0
        val denom = (variance * sqrt(2.0 * PI))
        // Normal distribution for stddev & mean
        val pdf = {x : Double ->
            exp(((x - mean) / variance).pow(2.0) * (-0.5)) / denom
        }
        val distribution = ContinuousDistribution(pdf, center = mean)
        val countList = CountList<Double>()
        val numIterations = 1000000
        repeat(numIterations) {countList.add(distribution.next())}
        println(countList)
        val histogramData = HistogramData.fromCountList<Double>(countList)
        val histogram = Histogram(histogramData)
        val image = histogram.plot(drawBoxes = false, connectLines = true)
        val icon = ImageIcon(image)
        val label = JLabel(icon)
        JOptionPane.showMessageDialog(null, label)
    }

    @Test
    @Ignore
    fun testNormal() {
        val variance = 1.5
        val mean = 5.0
        val distribution = NormalDistribution(variance, mean)
        val countList : CountList<Double> = CountList<Double>()
        val numIterations = 1000000
        repeat(numIterations) {countList.add(distribution.next())}
        println(countList)
        val histogramData = HistogramData.fromCountList<Double>(countList)
        val histogram = Histogram(histogramData)
        val image = histogram.plot(drawBoxes = false, connectLines = true)
        val icon = ImageIcon(image)
        val label = JLabel(icon)
        JOptionPane.showMessageDialog(null, label)
    }

    @Test
    @Ignore
    fun testCompound() {
        val var1 = 2.0
        val mean1 = 40.0
        val dist1 = NormalDistribution(var1, mean1)

        val var2 = 2.0
        val mean2 = 80.0
        val dist2 = NormalDistribution(var2, mean2)
        val distribution = CompoundDistribution(mapOf(Pair(dist1, 0.5), Pair(
            dist2, 0.5
        )))
        val combined : CountList<Double> = CountList<Double>()
        val firstList = CountList<Double>()
        val secondList = CountList<Double>()
        val numIterations = 100000
        repeat(numIterations) {
            combined.add(distribution.next())
        }
        println(combined)

        val points = mutableListOf<ArrayPoint2d>()
        combined.forEach { entry -> points.add(ArrayPoint2d(entry.key, entry.value.toDouble())) }
        val plot = Plot2d()
        plot.add(points)
        val image = plot.plot(drawLines = false, drawPoints = true)
        val icon = ImageIcon(image)
        val label = JLabel(icon)
        JOptionPane.showMessageDialog(null, label)
    }

}