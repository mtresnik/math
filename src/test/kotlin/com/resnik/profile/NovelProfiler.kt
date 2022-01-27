package com.resnik.profile

class NovelProfiler(val name : String = "", val threshold : Long = 500L) {

    var init : Long = System.currentTimeMillis()
    private val stamps = mutableListOf<Long>()

    fun init() {
        stamps.clear()
        init = System.currentTimeMillis()
    }

    fun record() {
        val curr = System.currentTimeMillis()
        if(stamps.isEmpty() || curr - stamps.last() > threshold){
            stamps.add(curr)
        }
    }

    fun getStamps() = stamps.sorted().map { it - init }

}