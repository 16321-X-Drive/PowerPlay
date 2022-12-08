package org.firstinspires.ftc.teamcode.util

object RobotConstants {
    const val SENSOR_RADIUS = 1.875
    val LOW_SENSOR_OFFSET = Point(4.75, -3.6875)
    val HIGH_SENSOR_OFFSET = Point(-4.75, -3.6875)

    val WIDTH = 13.5
    val TOP_HEIGHT = 6.0
    val BOTTOM_HEIGHT = 7.5

    val TL = Point(-WIDTH, TOP_HEIGHT)
    val TR = Point(WIDTH, TOP_HEIGHT)
    val BL = Point(-WIDTH, -BOTTOM_HEIGHT)
    val BR = Point(WIDTH, -BOTTOM_HEIGHT)

    val FIELD_WIDTH = 24 * 6.0
}