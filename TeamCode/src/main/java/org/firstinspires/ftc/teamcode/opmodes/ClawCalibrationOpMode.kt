package org.firstinspires.ftc.teamcode.opmodes

import com.qualcomm.robotcore.eventloop.opmode.Disabled
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import org.firstinspires.ftc.teamcode.lib.LinearOpModeEx
import org.firstinspires.ftc.teamcode.subsytems.Claw

class ClawCalibrationOpMode : LinearOpModeEx() {

    @TeleOp
    @Disabled
    class ClawCalibration : LinearOpMode() {
        override fun runOpMode() = ClawCalibrationOpMode().runOpMode(this)
    }

    val claw by lazy { Claw(hardware) }

    override fun loop() {
        telemetry.addData("claw is open", claw.isOpen)

        if (gamepad1.x.justPressed ) {
            claw.toggle()
        }
    }
}