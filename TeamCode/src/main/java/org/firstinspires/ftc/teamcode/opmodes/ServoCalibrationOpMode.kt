package org.firstinspires.ftc.teamcode.opmodes

import com.qualcomm.robotcore.eventloop.opmode.Disabled
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import org.firstinspires.ftc.teamcode.lib.LinearOpModeEx
import org.firstinspires.ftc.teamcode.subsytems.Distances

class ServoCalibrationOpMode : LinearOpModeEx() {

    @TeleOp
    @Disabled
    class ServoCalibration : LinearOpMode() {
        override fun runOpMode() = ServoCalibrationOpMode().runOpMode(this)
    }

    val distances by lazy { Distances(hardware) }

    var highPosition = 0.0
    var lowPosition = 0.0

    override fun loop() {
        distances.lowServo.position = lowPosition
        distances.highServo.position = highPosition

        telemetry.addData("low position", lowPosition)
        telemetry.addData("high position", highPosition)

        lowPosition += gamepad1.leftStick.y / 10000
        highPosition += gamepad1.rightStick.y / 10000
    }

}