package com.resnik.math.symbo.algebra.parse

class ParseStringUtils {

    companion object {
        fun findRem(test: String, key: String?): Array<String?> {
            if (key == null || !test.contains(key)) {
                return arrayOfNulls(0)
            }
            if (test.length == key.length) {
                return arrayOfNulls(0)
            }
            val index = test.indexOf(key)
            if (index == 0) {
                return arrayOf(test.substring(key.length))
            }
            return if (test.endsWith(key)) {
                arrayOf(test.substring(0, test.length - key.length))
            } else arrayOf(
                test.substring(0, index),
                test.substring(index + key.length)
            )
        }
    }

}