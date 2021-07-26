package com.resnik.math.symbo.parse

import java.lang.RuntimeException

class TokenizationException(private val msg : String) : RuntimeException(msg)