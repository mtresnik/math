package com.resnik.math.plot

import com.resnik.math.linear.array.ArrayPoint2d
import java.awt.BasicStroke
import java.awt.Color
import java.awt.Graphics2D
import java.awt.image.BufferedImage

class Histogram(
    vararg val histogramData: HistogramData,
    width: Int = 500,
    height: Int = 500,
    background: Color = Color.WHITE,
    axisColor: Color = Color.BLACK,
    lineColor: Color = Color.LIGHT_GRAY,
    axisThickness: Float = 5.0f,
    dataLineThickness: Float = 2.0f
) : Chart(width, height, 0, background, axisColor, lineColor, axisThickness, dataLineThickness) {

    private fun drawLines(graphics2D: Graphics2D) {
        // Get max number of lines from all histogram data
        val numLines = histogramData.maxByOrNull { data -> data.max() }?.max() ?: return
        val spacing = (height - axisThickness * 2) / (numLines)
        graphics2D.paint = lineColor
        graphics2D.stroke = BasicStroke(1.0f)
        repeat(numLines) { line ->
            val y = (spacing * (line + 1) + axisThickness.toInt()).toInt()
            graphics2D.drawLine(axisThickness.toInt(), y, width - axisThickness.toInt(), y)
        }
    }

    private fun drawBoxes(graphics2D: Graphics2D) {
        val numLines = histogramData.maxByOrNull { data -> data.max() }?.max() ?: return
        val verticalSpacing = (height - axisThickness * 2) / (numLines)
        val allBins = mutableSetOf<Double>()
        histogramData.forEach { dataSet ->
            dataSet.forEach { entry ->
                allBins.add(entry.key)
            }
        }
        val binList = allBins.toMutableList()
        binList.sort()
        val numBins = binList.size
        val combinedBinSize = (width - axisThickness * 2) / numBins
        val barwidth = combinedBinSize / histogramData.size
        binList.forEachIndexed { binIndex, bin ->
            // Get each histogram dataset and draw the bar represented by the count
            val binStartX = (axisThickness + combinedBinSize * binIndex)
            histogramData.forEachIndexed { dataIndex, histogramData ->
                if (bin in histogramData) {
                    val barX = binStartX + dataIndex * barwidth
                    val count = histogramData[bin]
                    val barHeight = count * verticalSpacing
                    graphics2D.paint = histogramData.color
                    graphics2D.fillRect(
                        barX.toInt(),
                        height - axisThickness.toInt() - barHeight.toInt(),
                        barwidth.toInt(),
                        barHeight.toInt()
                    )
                    graphics2D.paint = axisColor
                    graphics2D.drawRect(
                        barX.toInt(),
                        height - axisThickness.toInt() - barHeight.toInt(),
                        barwidth.toInt(),
                        barHeight.toInt()
                    )
                }
            }

        }
    }

    fun connectLines(graphics2D: Graphics2D) {
        val numLines = histogramData.maxByOrNull { data -> data.max() }?.max() ?: return
        val verticalSpacing = (height - axisThickness * 2) / (numLines)
        val allBins = mutableSetOf<Double>()
        histogramData.forEach { dataSet ->
            dataSet.forEach { entry ->
                allBins.add(entry.key)
            }
        }
        val binList = allBins.toMutableList()
        binList.sort()
        val numBins = binList.size
        val combinedBinSize = (width - axisThickness * 2) / numBins
        val barwidth = combinedBinSize / histogramData.size
        val lineList: MutableList<MutableMap<Double, ArrayPoint2d>> =
            MutableList(histogramData.size) { mutableMapOf<Double, ArrayPoint2d>() }
        binList.forEachIndexed { binIndex, bin ->
            // Get each histogram dataset and draw the bar represented by the count
            val binStartX = (axisThickness + combinedBinSize * binIndex)
            histogramData.forEachIndexed { dataIndex, histogramData ->
                if (bin in histogramData) {
                    val barX = binStartX + dataIndex * barwidth
                    val count = histogramData[bin]
                    val barHeight = count * verticalSpacing
                    val point =
                        ArrayPoint2d((barX + barwidth / 2).toDouble(), (height - axisThickness - barHeight).toDouble())
                    lineList[dataIndex][bin] = point
                }
            }
        }
        lineList.forEachIndexed { index, pointMap ->
            val color = histogramData[index].color
            graphics2D.paint = color
            graphics2D.stroke = BasicStroke(dataLineThickness)
            pointMap.entries.zipWithNext { a: MutableMap.MutableEntry<Double, ArrayPoint2d>, b: MutableMap.MutableEntry<Double, ArrayPoint2d> ->
                val point1 = a.value
                val point2 = b.value
                val x1 = point1.x().toInt()
                val y1 = point1.y().toInt()
                val x2 = point2.x().toInt()
                val y2 = point2.y().toInt()
                graphics2D.drawLine(x1, y1, x2, y2)
            }
        }
    }

    fun plot(drawBoxes: Boolean = true, connectLines: Boolean = false): BufferedImage {
        val pair = renderAxes()
        val bufferedImage = pair.first
        val graphics2D = pair.second
        drawLines(graphics2D)
        if (drawBoxes) drawBoxes(graphics2D)
        if (connectLines) connectLines(graphics2D)
        return bufferedImage
    }

}