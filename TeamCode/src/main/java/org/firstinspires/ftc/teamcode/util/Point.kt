package org.firstinspires.ftc.teamcode.util

import kotlin.math.*

data class Point(var x: Double, var y: Double) {
    companion object {
        fun origin() = Point(0.0, 0.0)
    }

    operator fun minus(other: Point) = Point(
        this.x - other.x, this.y - other.y
    )

    operator fun plus(other: Point) = Point(
        this.x + other.x, this.y + other.y
    )

    fun rotatedAround(center: Point, angle: Double) =
        this.minus(center).rotated(angle).plus(center)

    fun rotated(angle: Double) = Point(
        x * cos(angle) - y * sin(angle),
        y * cos(angle) + x * sin(angle)
    )

    fun dist(other: Point) = sqrt((other.x - this.x).pow(2) + (other.y - this.y).pow(2))

    fun len() = sqrt(x * x + y * y)
    fun angle() = atan2(y, x)
}
