package org.firstinspires.ftc.teamcode.subsytems

import com.qualcomm.robotcore.hardware.DcMotorSimple
import org.firstinspires.ftc.teamcode.hardware.Hardware

class Lift(hardware: Hardware) {

    private val leftMotor = hardware.leftLift
    private val rightMotor = hardware.rightLift

    init {
        rightMotor.direction = DcMotorSimple.Direction.REVERSE
    }

    fun setPower(power: Double) {
        leftMotor.power = power
        rightMotor.power = power
    }

}