package com.resnik.profile

import com.resnik.math.linear.array.ArrayPoint2d
import com.resnik.math.plot.Plot2d
import org.junit.Ignore
import org.junit.Test
import javax.swing.ImageIcon
import javax.swing.JLabel
import javax.swing.JOptionPane

class TestProfile {

    @Ignore
    @Test
    fun test1() {
        val profiler = NovelProfiler(threshold=0)
        profiler.init()
        var a = 5000
        for(i in 0..a){
            repeat(a - i){
                println("i:$i")
            }
            profiler.record()
        }
        println(profiler.getStamps().size)
        val points = profiler.getStamps().mapIndexed{x, y ->
            ArrayPoint2d(x.toDouble(), (y - profiler.init).toDouble())
        }.toMutableList()
        val plot2d = Plot2d()
        plot2d.add(points)
        val image = plot2d.plot(drawPoints = false)
        val icon = ImageIcon(image)
        val label = JLabel(icon)
        JOptionPane.showMessageDialog(null, label)
    }

}