package org.firstinspires.ftc.teamcode.opmodes

import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import org.firstinspires.ftc.teamcode.hardware.Hardware
import org.firstinspires.ftc.teamcode.lib.LinearOpModeEx
import org.firstinspires.ftc.teamcode.subsytems.*
import kotlin.math.pow

@TeleOp
class MecanumDriveOpMode : LinearOpModeEx() {

    val hardware by lazy { Hardware(hardwareMap) }

    val drive by lazy { MecanumDrive(hardware) }
    val color by lazy { ColorSensing(hardware) }
    val distances by lazy { Distances(hardware) }
    val claw by lazy { Claw(hardware) }
    val gyro by lazy { Gyro(hardware, this) }

    override fun init() {
        gyro.waitForCalibration(debug = true)
    }

    override fun loop() {
        telemetry.addData("angle", gamepad1.leftStick.angle)
        telemetry.addData("power", gamepad1.leftStick.dist)
        telemetry.addData("turn power", gamepad1.rightStick.x)
        telemetry.addData("robot heading", gyro.robotHeading)

        drive.drive(
            gamepad1.leftStick.angle - gyro.robotHeading,
            gamepad1.leftStick.dist.pow(3),
            gamepad1.rightStick.x
        )

        if (gamepad1.x.justPressed) {
            claw.toggle()
        }
    }

}