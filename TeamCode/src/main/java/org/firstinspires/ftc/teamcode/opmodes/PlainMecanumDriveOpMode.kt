package org.firstinspires.ftc.teamcode.opmodes

import com.qualcomm.robotcore.eventloop.opmode.Disabled
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import org.firstinspires.ftc.teamcode.lib.LinearOpModeEx
import org.firstinspires.ftc.teamcode.subsytems.Claw
import org.firstinspires.ftc.teamcode.subsytems.Distances
import org.firstinspires.ftc.teamcode.subsytems.Lift
import org.firstinspires.ftc.teamcode.subsytems.MecanumDrive
import kotlin.math.pow

class PlainMecanumDriveOpMode : LinearOpModeEx() {

    @TeleOp
    @Disabled
    class PlainMecanumDrive : LinearOpMode() {
        override fun runOpMode() = PlainMecanumDriveOpMode().runOpMode(this)
    }

    val drive by lazy { MecanumDrive(hardware) }

    override fun loop() {
        when {
            gamepad1.leftBumper.isDown -> drive.drive(0.0, 0.0, -0.5)
            gamepad1.rightBumper.isDown -> drive.drive(0.0, 0.0, 0.5)
            gamepad1.leftTrigger != 0.0 -> drive.drive(0.0, gamepad1.leftTrigger * 0.5, 0.0)
            else -> {
                val speed = 1.0 - (gamepad1.rightTrigger * 0.75)
                drive.drive(
//            gamepad1.leftStick.angle - gyro.robotHeading,
                    gamepad1.leftStick.angle,
                    gamepad1.leftStick.dist.pow(3) * speed * 0.6,
                    gamepad1.rightStick.x * speed
                )
            }
        }
    }

}