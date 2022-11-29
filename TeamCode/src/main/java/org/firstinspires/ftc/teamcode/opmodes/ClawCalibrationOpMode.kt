package org.firstinspires.ftc.teamcode.opmodes

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import org.firstinspires.ftc.teamcode.lib.LinearOpModeEx

class ClawCalibrationOpMode : LinearOpModeEx() {

    @TeleOp
    class ClawCalibration : LinearOpMode() {
        override fun runOpMode() = ClawCalibrationOpMode().runOpMode(this)
    }

    override fun loop() {
        telemetry.addData("left claw", gamepad1.leftTrigger)
        telemetry.addData("right claw", gamepad2.rightTrigger)

//        hardware.leftClaw.position = gamepad1.leftTrigger
//        hardware.rightClaw.position = gamepad1.rightTrigger
    }
}