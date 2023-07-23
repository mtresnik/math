package com.resnik.math.optimize

import com.resnik.math.linear.array.ArrayMatrix
import com.resnik.math.linear.array.ArrayVector
import org.junit.Ignore
import org.junit.Test
import java.awt.Color
import java.awt.Graphics2D
import java.awt.image.BufferedImage
import java.util.*
import javax.swing.ImageIcon
import javax.swing.JLabel
import javax.swing.JOptionPane

@Ignore
@Deprecated("Inefficient for non-linear optimizations compared to standard gradient models.")
class TestNeuralMinimizer {

    @Test
    @Ignore
    fun testNeuralMinimizer(){
        val unboundedGeneralMinimizer = UnboundedGeneralMinimizer(NeuralNetworkDataset(), numIterations = 2000)
        val best = unboundedGeneralMinimizer.minimize { it.error() }
        println(best.error())
        val width = 500
        val height = width
        val image = BufferedImage(width, height, BufferedImage.TYPE_INT_RGB)
        val graphics: Graphics2D = image.createGraphics()
        graphics.background = Color.WHITE
        graphics.clearRect(0,0, width, height)
        repeat(width){ col ->
            val x = col.toDouble() / (width - 1)
            repeat(height){ row ->
                val y = row.toDouble() / (height - 1)
                val z = best.feedForward(ArrayVector(x, y))[0].toFloat().coerceIn(0.0f, 1.0f)
                image.setRGB(col, row, Color(z,z,z).rgb)
            }
        }
        graphics.dispose()
        val icon = ImageIcon(image)
        val label = JLabel(icon)
        JOptionPane.showMessageDialog(null, label)
    }

    private class NeuralNetworkData(private val random : Random = Random(),
                                    val bias0 : ArrayVector = ArrayVector.randomGaussian(2),
                                    val bias1 : ArrayVector = ArrayVector.randomGaussian(2),
                                    val bias2 : ArrayVector = ArrayVector.randomGaussian(1),
                                    val weight0 : ArrayMatrix = ArrayMatrix(2, 2) {random.nextGaussian()},
                                    val weight1 : ArrayMatrix = ArrayMatrix(2, 1){random.nextGaussian()}
    ) {

        fun getSize() : Int {
            return bias0.size() + bias1.size() + bias2.size() + weight0.numElements() + weight1.numElements()
        }

        fun add(delta : NeuralNetworkData) : NeuralNetworkData {
            return NeuralNetworkData(random,
                bias0 + delta.bias0,
                bias1 + delta.bias1,
                bias2 + delta.bias2,
                weight0 + delta.weight0,
                weight1 +  delta.weight1)
        }

        fun scale(scale : Double) : NeuralNetworkData {
            return NeuralNetworkData(random, bias0 * scale, bias1 * scale, bias2 * scale, weight0 * scale, weight1 * scale)
        }

        private fun transferFunction(x : Double) : Double {
            return x.coerceAtLeast(0.0)
        }

        fun feedForward(input: ArrayVector): ArrayVector {
            val input1 = (weight0 * input.toColMatrix() + bias1.toColMatrix()).apply { transferFunction(it) }
            return (ArrayVector(weight1.toVector()!! * input1.toVector()!!) + bias2).apply { transferFunction(it) }
        }

        fun error() : Double {
            val input1 = ArrayVector(0.0, 0.0)
            val output1 = ArrayVector(0.0)
            val input2 = ArrayVector(0.0, 1.0)
            val output2 = ArrayVector(1.0)
            val input3 = ArrayVector(1.0, 0.0)
            val output3 = ArrayVector(1.0)
            val input4 = ArrayVector(1.0, 1.0)
            val output4 = ArrayVector(0.0)
            val error1 = (feedForward(input1) - output1).magnitude()
            val error2 = (feedForward(input2) - output2).magnitude()
            val error3 = (feedForward(input3) - output3).magnitude()
            val error4 = (feedForward(input4) - output4).magnitude()
            return error1 + error2 + error3 + error4
        }

    }

    private class NeuralNetworkDataset : OptimizeDataset<NeuralNetworkData, NeuralNetworkData> {
        override fun genSeed(): NeuralNetworkData {
            return NeuralNetworkData()
        }

        override fun genDirection(): NeuralNetworkData {
            return NeuralNetworkData()
        }

        override fun getSize(x: NeuralNetworkData): Int {
            return x.getSize()
        }

        override fun scale(y: NeuralNetworkData, scale: Double): NeuralNetworkData {
            return y.scale(scale)
        }

        override fun add(x: NeuralNetworkData, y: NeuralNetworkData): NeuralNetworkData {
            return x.add(y)
        }
    }


}