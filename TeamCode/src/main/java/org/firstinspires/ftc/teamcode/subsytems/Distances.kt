package org.firstinspires.ftc.teamcode.subsytems

import org.firstinspires.ftc.teamcode.hardware.Hardware
import kotlin.math.PI

class Distances(hardware: Hardware) {

    companion object {
        const val LOW_0_DEGREES = 0.478
        const val LOW_45_DEGREES = 0.649
        const val LOW_MIN_DEGREES = 0.257
        const val LOW_MAX_DEGREES = 0.754

        const val HIGH_0_DEGREES = 0.503
        const val HIGH_45_DEGREES = 0.345
        const val HIGH_MIN_DEGREES = 0.289
        const val HIGH_MAX_DEGREES = 0.783
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
        lowAngle = -robotHeading
        highAngle = -robotHeading
    }

}