package org.firstinspires.ftc.teamcode.opmodes

import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import org.firstinspires.ftc.teamcode.hardware.Hardware
import org.firstinspires.ftc.teamcode.lib.LinearOpModeEx

@TeleOp
class ClawCalibration : LinearOpModeEx() {

    val hardware by lazy { Hardware(hardwareMap) }

    override fun loop() {
        telemetry.addData("left claw", gamepad1.leftTrigger)
        telemetry.addData("right claw", gamepad2.rightTrigger)

        hardware.leftClaw.position = gamepad1.leftTrigger
        hardware.rightClaw.position = gamepad1.rightTrigger
    }
}