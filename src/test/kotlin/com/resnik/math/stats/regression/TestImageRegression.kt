package com.resnik.math.stats.regression

import com.resnik.math.linear.array.ArrayPoint
import kotlinx.coroutines.*
import org.junit.Ignore
import org.junit.Test
import java.awt.Color
import java.awt.image.BufferedImage
import java.io.IOException
import java.net.URL
import java.util.concurrent.Executors
import javax.imageio.ImageIO
import javax.swing.ImageIcon
import javax.swing.JLabel
import javax.swing.JOptionPane

class TestImageRegression {

    @Throws(IOException::class)
    fun resizeImage(originalImage: BufferedImage, targetWidth: Int, targetHeight: Int): BufferedImage {
        val resizedImage = BufferedImage(targetWidth, targetHeight, originalImage.type)
        val graphics2D = resizedImage.createGraphics()
        graphics2D.drawImage(originalImage, 0, 0, targetWidth, targetHeight, null)
        graphics2D.dispose()
        return resizedImage
    }
    @Test
    @Ignore
    fun testNext() {
        val url =
            URL("https://upload.wikimedia.org/wikipedia/commons/thumb/e/ec/Mona_Lisa%2C_by_Leonardo_da_Vinci%2C_from_C2RMF_retouched.jpg/1200px-Mona_Lisa%2C_by_Leonardo_da_Vinci%2C_from_C2RMF_retouched.jpg")
        val before: BufferedImage = ImageIO.read(url)

        val w = before.width
        val h = before.height
        var input = resizeImage(before, w / 4, h / 4)

        val regressionPower = 5
        val seedPoints = 20
        val output: BufferedImage = BufferedImage(input.width, input.height, input.type)
        val dispatcher = Executors.newFixedThreadPool(200).asCoroutineDispatcher()
        val scope = CoroutineScope(dispatcher)
        val jobs = (0 until input.height).map { row ->
            scope.launch(dispatcher) {
                println("Row: $row / ${input.height}")
                // Add all row to three polynomial regressions for r g and b
                val redList = mutableListOf<ArrayPoint>()
                val greenList = mutableListOf<ArrayPoint>()
                val blueList = mutableListOf<ArrayPoint>()
                repeat(seedPoints) {
                    val colD = input.width * Math.random()
                    val col = colD.toInt()
                    val x = colD / input.width
                    val pixel = Color(input.getRGB(col, row))
                    val r = (pixel.red.toDouble() / 255.0).coerceIn(0.0, 1.0)
                    redList.add(ArrayPoint(x, r.toDouble()))
                    val g = (pixel.green.toDouble() / 255.0).coerceIn(0.0, 1.0)
                    greenList.add(ArrayPoint(x, g.toDouble()))
                    val b = (pixel.blue.toDouble() / 255.0).coerceIn(0.0, 1.0)
                    blueList.add(ArrayPoint(x, b.toDouble()))
                }
                val redRegression = PolynomialRegression(redList, regressionPower)
                val greenRegression = PolynomialRegression(greenList, regressionPower)
                val blueRegression = PolynomialRegression(blueList, regressionPower)
                repeat(input.width) { col ->
                    val x = col.toDouble() / input.width
                    val r = (redRegression(x).coerceIn(0.0, 1.0) * 255).toInt()
                    val g = (greenRegression(x).coerceIn(0.0, 1.0) * 255).toInt()
                    val b = (blueRegression(x).coerceIn(0.0, 1.0) * 255).toInt()
                    output.setRGB(col, row, Color(r,g,b).rgb)
                }
            }
        }
        runBlocking(dispatcher) { jobs.joinAll() }
        val icon = ImageIcon(output)
        val label = JLabel(icon)
        JOptionPane.showMessageDialog(null, label)
    }

}