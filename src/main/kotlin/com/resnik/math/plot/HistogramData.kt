package com.resnik.math.plot

import com.resnik.math.util.CountList
import java.awt.Color
import kotlin.math.abs

class HistogramData(val label : String = "data",
                    val color : Color = Color.BLUE) : CountList<Double>() {

    companion object {

        fun <T : Number> fromCountListBinned(countList: CountList<T>,
                          binsize: Double,
                          label : String = "data",
                          color : Color = Color.BLUE) : HistogramData {
            val data = mutableListOf<Double>()
            countList.forEach{entry->
                val value = entry.key
                val count = entry.value
                repeat(count){
                    data.add(value.toDouble())
                }
            }
            return fromArray(data.toDoubleArray(), binsize, label, color)
        }

        fun fromArray(data : DoubleArray,
                      binsize : Double,
                      label : String = "data",
                      color : Color = Color.BLUE) : HistogramData {
            val histogramData = HistogramData(label, color)
            val max = data.max()
            val min = data.min()
            val n = ((max!! - min!!) / binsize).toInt()
            val bins = mutableListOf<Double>()
            repeat(n + 1){
                val curr = min + binsize / 2 + binsize*it
                bins.add(curr)
            }
            data.forEach { curr ->
                bins.forEach { bin ->
                    if(abs(curr - bin) <= binsize){
                        histogramData.add(bin)
                    }
                }
            }
            return histogramData
        }

        fun <T : Number> fromCountList(countList: CountList<T>,
                                       binsize: Double,
                                       label : String = "data",
                                       color : Color = Color.BLUE) : HistogramData {
            val histogramData = HistogramData(label, color)
            countList.forEach{entry -> repeat(entry.value){
                histogramData.add(entry.key.toDouble())
            } }
            return histogramData
        }

    }
}