package com.resnik.math.stats

import com.resnik.math.plot.Histogram
import com.resnik.math.plot.HistogramData
import org.junit.Ignore
import org.junit.Test
import java.awt.Color
import javax.swing.ImageIcon
import javax.swing.JLabel
import javax.swing.JOptionPane

@Ignore
class TestDice {

    @Test
    @Ignore
    fun testDice1(){
        val die = Die()
        val out = die.roll(20000)
        println(out)
        val histogram = Histogram(HistogramData.fromCountList(out, 0.5))
        val image = histogram.plot(drawBoxes = false, connectLines = true)
        val icon = ImageIcon(image)
        val label = JLabel(icon)
        JOptionPane.showMessageDialog(null, label)
    }

    @Test
    @Ignore
    fun testDice2(){
        val dice = Dice(2)
        val out = dice.roll(20000)
        println(out)
        val histogram = Histogram(HistogramData.fromCountList(out, 0.5))
        val image = histogram.plot(drawBoxes = false, connectLines = true)
        val icon = ImageIcon(image)
        val label = JLabel(icon)
        JOptionPane.showMessageDialog(null, label)
    }

    @Test
    @Ignore
    fun testDice3(){
        val dice = Dice(3)
        val out = dice.roll(20000)
        println(out)
        val histogram = Histogram(HistogramData.fromCountList(out, 0.5))
        val image = histogram.plot(drawBoxes = false, connectLines = true)
        val icon = ImageIcon(image)
        val label = JLabel(icon)
        JOptionPane.showMessageDialog(null, label)
    }

    @Test
    @Ignore
    fun testDiceN(){
        val dice = Dice(1000)
        val out = dice.roll(1000000)
        println(out)
        val histogram = Histogram(HistogramData.fromCountList(out, 0.5))
        val image = histogram.plot(drawBoxes = false, connectLines = true)
        val icon = ImageIcon(image)
        val label = JLabel(icon)
        JOptionPane.showMessageDialog(null, label)
    }

    @Test
    @Ignore
    fun testDice4(){
        val rollCount = 40000
        val binsize = 0.5

        val oneDice = Die()
        val oneOut = oneDice.roll(rollCount)

        val twoDice = Dice(2)
        val twoOut = twoDice.roll(rollCount)

        val threeDice = Dice(3)
        val threeOut = threeDice.roll(rollCount)

        val fourDice = Dice(4)
        val fourOut = fourDice.roll(rollCount)

        val fiveDice = Dice(5)
        val fiveOut = fiveDice.roll(rollCount)

        val sixDice = Dice(6)
        val sixOut = sixDice.roll(rollCount)

        val histogram = Histogram(
                HistogramData.fromCountList(oneOut, binsize, color=Color.BLUE),
                HistogramData.fromCountList(twoOut, binsize, color=Color.GREEN),
                HistogramData.fromCountList(threeOut, binsize, color=Color.RED),
                HistogramData.fromCountList(fourOut, binsize, color=Color.CYAN),
                HistogramData.fromCountList(fiveOut, binsize, color=Color.BLACK),
                HistogramData.fromCountList(sixOut, binsize, color=Color.ORANGE)
        )

        val image = histogram.plot(drawBoxes = false, connectLines = true)
        val icon = ImageIcon(image)
        val label = JLabel(icon)
        JOptionPane.showMessageDialog(null, label)
    }

    @Test
    @Ignore
    fun testDice5(){
        val rollCount = 40000
        val binsize = 0.5

        val oneDice = Die(12)
        val oneOut = oneDice.roll(rollCount)

        val twoDice = Dice(2, 12)
        val twoOut = twoDice.roll(rollCount)

        val threeDice = Dice(3, 12)
        val threeOut = threeDice.roll(rollCount)

        val fourDice = Dice(4, 12)
        val fourOut = fourDice.roll(rollCount)

        val fiveDice = Dice(5, 12)
        val fiveOut = fiveDice.roll(rollCount)

        val sixDice = Dice(6, 12)
        val sixOut = sixDice.roll(rollCount)

        val histogram = Histogram(
                HistogramData.fromCountList(oneOut, binsize, color=Color.BLUE),
                HistogramData.fromCountList(twoOut, binsize, color=Color.GREEN),
                HistogramData.fromCountList(threeOut, binsize, color=Color.RED),
                HistogramData.fromCountList(fourOut, binsize, color=Color.CYAN),
                HistogramData.fromCountList(fiveOut, binsize, color=Color.BLACK),
                HistogramData.fromCountList(sixOut, binsize, color=Color.ORANGE)
        )

        val image = histogram.plot(drawBoxes = false, connectLines = true)
        val icon = ImageIcon(image)
        val label = JLabel(icon)
        JOptionPane.showMessageDialog(null, label)
    }


}