package org.firstinspires.ftc.teamcode

import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import org.firstinspires.ftc.teamcode.lib.LinearOpModeEx
import kotlin.math.pow

@TeleOp
class MecanumDriveOpMode : LinearOpModeEx() {

    val hardware by lazy { Hardware(hardwareMap) }

    val drive by lazy { MecanumDrive(hardware) }
    val color by lazy { ColorSensing(hardware) }
    val distances by lazy { Distances(hardware) }
    val claw by lazy { Claw(hardware) }

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