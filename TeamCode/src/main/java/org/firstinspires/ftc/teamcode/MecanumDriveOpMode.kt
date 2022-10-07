package org.firstinspires.ftc.teamcode

import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import org.firstinspires.ftc.teamcode.lib.OpModeBase
import org.firstinspires.ftc.teamcode.lib.UseSubsystem
import kotlin.math.atan2
import kotlin.math.hypot
import kotlin.math.pow

@TeleOp
class MecanumDriveOpMode : OpModeBase() {

    @UseSubsystem
    lateinit var drive: MecanumDrive

    @UseSubsystem
    lateinit var color: ColorSensing

    @UseSubsystem
    lateinit var distances: Distances

    @UseSubsystem
    lateinit var claw: Claw

    override fun coreLoop() {
        telemetry.addData("angle", gamepad1Ex.leftStick.angle)
        telemetry.addData("power", gamepad1Ex.leftStick.dist)
        telemetry.addData("turnPower", gamepad1Ex.rightStick.x)
        telemetry.addData("leftColor", color.readLeftColor())
        telemetry.addData("rightColor", color.readRightColor())
        telemetry.addData("leftDomColor", color.readLeftColor().dominantColor())
        telemetry.addData("rightDomColor", color.readRightColor().dominantColor())

        drive.drive(
            gamepad1Ex.leftStick.angle,
            gamepad1Ex.leftStick.dist.pow(3),
            gamepad1Ex.rightStick.x
        )

        if (gamepad1Ex.x.justPressed) {
            claw.toggle()
        }
    }

}