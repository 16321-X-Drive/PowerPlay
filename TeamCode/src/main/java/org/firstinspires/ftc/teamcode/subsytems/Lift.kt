package org.firstinspires.ftc.teamcode.subsytems

import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.DcMotorSimple
import org.firstinspires.ftc.teamcode.hardware.Hardware

class Lift(hardware: Hardware) {

    enum class Height {
        Down,
        LowJunction,
        MediumJunction,
        HighJunction;

        fun ticks() = when (this) {
            Down -> 0
            LowJunction -> 1800
            MediumJunction -> 3000
            HighJunction -> 4400
        }

        fun up() = when (this) {
            Down -> LowJunction
            LowJunction -> MediumJunction
            MediumJunction -> HighJunction
            HighJunction -> HighJunction
        }

        fun down() = when (this) {
            Down -> Down
            LowJunction -> Down
            MediumJunction -> LowJunction
            HighJunction -> MediumJunction
        }
    }


    private val leftMotor = hardware.leftLift
    private val rightMotor = hardware.rightLift

//    private val reset = hardware.

    var height: Height = Height.Down
    set(value) {
        field = value
        leftMotor.targetPosition = field.ticks()
        rightMotor.targetPosition = field.ticks()
    }

    init {
        rightMotor.direction = DcMotorSimple.Direction.REVERSE

        leftMotor.power = 1.0
        rightMotor.power = 1.0
        leftMotor.mode = DcMotor.RunMode.STOP_AND_RESET_ENCODER
        rightMotor.mode = DcMotor.RunMode.STOP_AND_RESET_ENCODER
        height = Height.Down
        leftMotor.mode = DcMotor.RunMode.RUN_TO_POSITION
        rightMotor.mode = DcMotor.RunMode.RUN_TO_POSITION
    }

    fun setPower(power: Double) {
        leftMotor.power = power
        rightMotor.power = power
    }

}