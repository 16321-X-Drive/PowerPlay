package org.firstinspires.ftc.teamcode

import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import org.firstinspires.ftc.teamcode.lib.LinearOpModeEx
import org.firstinspires.ftc.teamcode.lib.UseSubsystem
import kotlin.math.pow

@TeleOp
class MecanumDriveOpMode : LinearOpModeEx() {

    @UseSubsystem
    lateinit var drive: MecanumDrive

    @UseSubsystem
    lateinit var color: ColorSensing

    @UseSubsystem
    lateinit var distances: Distances

    @UseSubsystem
    lateinit var claw: Claw

    override fun loop() {
        telemetry.addData("angle", gamepad1.leftStick.angle)
        telemetry.addData("power", gamepad1.leftStick.dist)
        telemetry.addData("turnPower", gamepad1.rightStick.x)
        telemetry.addData("leftColor", color.readLeftColor())
        telemetry.addData("rightColor", color.readRightColor())
        telemetry.addData("leftDomColor", color.readLeftColor().dominantColor())
        telemetry.addData("rightDomColor", color.readRightColor().dominantColor())

        drive.drive(
            gamepad1.leftStick.angle,
            gamepad1.leftStick.dist.pow(3),
            gamepad1.rightStick.x
        )

        if (gamepad1.x.justPressed) {
            claw.toggle()
        }
    }

}