package org.firstinspires.ftc.teamcode.subsytems

import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.DcMotorSimple
import org.firstinspires.ftc.teamcode.hardware.Hardware

class Lift(hardware: Hardware) {

    companion object {
        val RANGE = 0..4500
    }

    enum class Height {
        Down,
        TwoCone,
        ThreeCone,
        FourCone,
        FiveCone,
        LowJunction,
        MediumJunction,
        HighJunction;

        fun ticks() = when (this) {
            Down -> 0
            TwoCone -> 200
            ThreeCone -> 400
            FourCone -> 560
            FiveCone -> 720
            LowJunction -> 1800
            MediumJunction -> 3000
            HighJunction -> 4200
        }
    }

    private val leftMotor = hardware.leftLift
    private val rightMotor = hardware.rightLift
    val reset = hardware.resetLift

    var height: Height = Height.Down
    set(value) {
        field = value
        offset = 0.0
        leftMotor.mode = DcMotor.RunMode.RUN_TO_POSITION
        rightMotor.mode = DcMotor.RunMode.RUN_TO_POSITION
        leftMotor.targetPosition = (field.ticks() + offset.toInt()).coerceIn(RANGE)
        rightMotor.targetPosition = (field.ticks() + offset.toInt()).coerceIn(RANGE)
    }

    var offset = 0.0
    set(value) {
        field = value
        if (!resetting) {
            leftMotor.targetPosition = (height.ticks() + offset.toInt()).coerceIn(RANGE)
            rightMotor.targetPosition = (height.ticks() + offset.toInt()).coerceIn(RANGE)
        }
    }

    var resetting = false

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

    fun tick() {
        if (reset.isPressed) {
            leftMotor.mode = DcMotor.RunMode.STOP_AND_RESET_ENCODER
            rightMotor.mode = DcMotor.RunMode.STOP_AND_RESET_ENCODER
            leftMotor.mode = DcMotor.RunMode.RUN_TO_POSITION
            rightMotor.mode = DcMotor.RunMode.RUN_TO_POSITION
        }
    }

    fun reset() {
        height = Height.LowJunction
        offset = 0.0
        resetting = true
        leftMotor.mode = DcMotor.RunMode.RUN_USING_ENCODER
        rightMotor.mode = DcMotor.RunMode.RUN_USING_ENCODER
        leftMotor.power = -1.0
        rightMotor.power = -1.0
    }

}