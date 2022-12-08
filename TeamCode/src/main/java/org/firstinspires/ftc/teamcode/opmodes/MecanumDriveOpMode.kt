package org.firstinspires.ftc.teamcode.opmodes

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import org.firstinspires.ftc.teamcode.hardware.Hardware
import org.firstinspires.ftc.teamcode.lib.LinearOpModeEx
import org.firstinspires.ftc.teamcode.subsytems.*
import kotlin.math.PI
import kotlin.math.pow

class MecanumDriveOpMode : LinearOpModeEx() {

    @TeleOp
    class MecanumDrive : LinearOpMode() {
        override fun runOpMode() = MecanumDriveOpMode().runOpMode(this)
    }

    val drive by lazy { MecanumDrive(hardware) }
    val lift by lazy { Lift(hardware) }

    //    val color by lazy { ColorSensing(hardware) }
    val distances by lazy { Distances(hardware) }
    val claw by lazy { Claw(hardware) }
//    val gyro by lazy { Gyro(hardware, this) }

    override fun init() {
//        gyro.waitForCalibration(debug = true)
    }

    override fun loop() {
        telemetry.addData("angle", gamepad1.leftStick.angle)
        telemetry.addData("power", gamepad1.leftStick.dist)
        telemetry.addData("turn power", gamepad1.rightStick.x)
        telemetry.addData("lift", lift.height)
        telemetry.addData("claw", claw.isOpen)
//        telemetry.addData("robot heading", gyro.robotHeading)
//        telemetry.addData("left color", color.readLeftColor().dominantColor())
//        telemetry.addData("right color", color.readRightColor().dominantColor())

//        telemetry.addData("distance 1", distances.distance1.distanceIn)
//        telemetry.addData("distance 2", distances.distance2.distanceIn)
//        telemetry.addData("distance 3", distances.distance3.distanceIn)
//        telemetry.addData("distance 4", distances.distance4.distanceIn)

        when {
            gamepad1.leftBumper.isDown -> drive.drive(0.0, 0.0, -0.5)
            gamepad1.rightBumper.isDown -> drive.drive(0.0, 0.0, 0.5)
            gamepad1.leftTrigger != 0.0 -> drive.drive(0.0, gamepad1.leftTrigger * 0.5, 0.0)
            else -> {
                val speed = 1.0 - (gamepad1.rightTrigger * 0.75)
                drive.drive(
//            gamepad1.leftStick.angle - gyro.robotHeading,
                    gamepad1.leftStick.angle,
                    gamepad1.leftStick.dist.pow(3) * speed,
                    gamepad1.rightStick.x * speed
                )
            }
        }

        if (gamepad2.dpadDown.justPressed) {
            lift.height = lift.height.down()
        } else if (gamepad2.dpadUp.justPressed) {
            lift.height = lift.height.up()
        }

        if (gamepad2.rightBumper.justPressed) {
            claw.toggle()
        }

//        distances.keepHeading(gyro.robotHeading)
    }

}