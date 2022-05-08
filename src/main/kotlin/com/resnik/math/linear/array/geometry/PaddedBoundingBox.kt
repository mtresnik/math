package com.resnik.math.linear.array.geometry

import com.resnik.math.linear.array.ArrayPoint

class PaddedBoundingBox(vararg points: ArrayPoint) : BoundingBox(*points) {

    var minXPadding = 0.0
    var maxXPadding = 0.0
    var minYPadding = 0.0
    var maxYPadding = 0.0

    override fun minX(): Double {
        return super.minX() + minXPadding
    }

    override fun maxX(): Double {
        return super.maxX() + maxXPadding
    }

    override fun minY(): Double {
        return super.minY() + minYPadding
    }

    override fun maxY(): Double {
        return super.maxY() + maxYPadding
    }
}