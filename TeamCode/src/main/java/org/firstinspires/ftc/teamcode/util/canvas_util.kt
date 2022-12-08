package org.firstinspires.ftc.teamcode.util

import com.acmerobotics.dashboard.canvas.Canvas

fun Canvas.strokeLine(p1: Point, p2: Point) {
    this.strokeLine(p1.x, p1.y, p2.x, p2.y)
}

fun Canvas.strokePolygon(vararg points: Point) {
    var last = points.last()
    for (p in points) {
        this.strokeLine(last, p)
        last = p
    }
}