package com.resnik.math.linear.array.geometry

import com.resnik.math.linear.array.ArrayPoint

open class ConvexPoly(val _faces : List<Face>) : Shape3d<ConvexPoly> {

    override fun contains(point: ArrayPoint): Boolean {
        _faces.forEach { face ->
            val p2f = face.p1 - point
            val d = p2f.dot(face.normal()) / p2f.magnitude()
            if(d < -1e15)
                return false
        }
        return true
    }

    override fun generate(points: List<ArrayPoint>): ConvexPoly = ConvexPoly(pointsToFaces(points))

    override fun getPoints(): List<ArrayPoint> = _faces.flatMap { it.getPoints() }

    override fun volume(): Double = 0.0

    companion object {

        private fun pointsToFaces(_points : List<ArrayPoint>) : List<Face> = _points.chunked(3).map {Face(it[0], it[1], it[2]) }

    }
}