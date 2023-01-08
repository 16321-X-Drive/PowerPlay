package org.firstinspires.ftc.teamcode.subsytems

import org.firstinspires.ftc.teamcode.hardware.Hardware
import org.firstinspires.ftc.teamcode.util.Point
import org.firstinspires.ftc.teamcode.util.Pose
import org.firstinspires.ftc.teamcode.util.RobotConstants
import kotlin.math.PI

class Distances(hardware: Hardware) {

    enum class Pair {
        Low,
        High
    }

    companion object {
        const val LOW_0_DEGREES = 0.211
        const val LOW_45_DEGREES = 0.0485
        const val LOW_MIN_DEGREES = 0.0
        const val LOW_MAX_DEGREES = 0.38

        const val HIGH_0_DEGREES = 0.2935
        const val HIGH_45_DEGREES = 0.460
        const val HIGH_MIN_DEGREES = 0.0952
        const val HIGH_MAX_DEGREES = 0.550

    }

    val lowServo = hardware.lowDistServo
    val highServo = hardware.highDistServo

    val distance1 = hardware.distance1
    val distance2 = hardware.distance2
    val distance3 = hardware.distance3
    val distance4 = hardware.distance4

    var lowAngle: Double = 0.0
        set(value) {
            field = value
            val pos = LOW_0_DEGREES - value * ((LOW_45_DEGREES - LOW_0_DEGREES) / (PI / 4))
            lowServo.position = pos.coerceIn(LOW_MIN_DEGREES, LOW_MAX_DEGREES)
        }
    var highAngle: Double = 0.0
        set(value) {
            field = value
            val pos = HIGH_0_DEGREES + value * ((HIGH_45_DEGREES - HIGH_0_DEGREES) / (PI / 4))
            highServo.position = pos.coerceIn(HIGH_MIN_DEGREES, HIGH_MAX_DEGREES)
        }

    fun keepHeading(robotHeading: Double) {
        lowAngle = robotHeading
        highAngle = robotHeading
    }

    // dist from the -x wall
    fun x(pair: Pair): Double =
        when (pair) {
            Pair.Low -> RobotConstants.FIELD_WIDTH -  distance1.distanceIn - RobotConstants.SENSOR_RADIUS
            Pair.High -> distance4.distanceIn + RobotConstants.SENSOR_RADIUS
        }

    // dist from the -y wall
    fun y(pair: Pair): Double =
        when (pair) {
            Pair.Low -> RobotConstants.SENSOR_RADIUS + distance2.distanceIn
            Pair.High -> RobotConstants.SENSOR_RADIUS + distance3.distanceIn
        }

    fun getOffset(pair: Pair) = when (pair) {
        Pair.Low -> RobotConstants.LOW_SENSOR_OFFSET
        Pair.High -> RobotConstants.HIGH_SENSOR_OFFSET
    }

    fun calcPos(xPair: Pair, yPair: Pair, heading: Double): Pose {
        // dist from the +, + corner
        val offset = Point(
            x(xPair) + getOffset(xPair).rotated(heading).x,
            y(yPair) - getOffset(yPair).rotated(heading).y,
        )
        // convert to field centric
        val pos = Point(RobotConstants.FIELD_WIDTH / 2, RobotConstants.FIELD_WIDTH / 2) - offset

        return Pose(pos, heading)
    }
}