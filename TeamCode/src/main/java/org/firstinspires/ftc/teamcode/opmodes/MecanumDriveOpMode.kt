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
    val claw by lazy { Claw(hardware) }

    override fun loop() {
        /*
        x - rotate left
        b - rotate right
        left/right bumpers - rotate slower
        left trigger - forward slow
        right trigger - backward slow
        dpad - move that direction
         */

        when {
            gamepad1.leftBumper.isDown && !gamepad1.rightBumper.isDown -> drive.drive(0.0, 0.0, -0.45)
            gamepad1.rightBumper.isDown && !gamepad1.leftBumper.isDown -> drive.drive(0.0, 0.0, 0.45)
            gamepad1.x.isDown -> drive.drive(0.0, 0.0, -0.25)
            gamepad1.b.isDown -> drive.drive(0.0, 0.0, 0.25)
            gamepad1.leftTrigger != 0.0 -> drive.drive(PI/2, gamepad1.leftTrigger * 0.5, 0.0)
            gamepad1.rightTrigger != 0.0 -> drive.drive(PI/2, gamepad1.rightTrigger * -0.5, 0.0)
            gamepad1.dpadUp.isDown -> drive.drive(PI/2, 0.5, 0.0)
            gamepad1.dpadLeft.isDown -> drive.drive(PI, 0.5, 0.0)
            gamepad1.dpadDown.isDown -> drive.drive(3*PI/2, 0.5, 0.0)
            gamepad1.dpadRight.isDown -> drive.drive(0.0, 0.5, 0.0)
            else -> {
                var speed = 1.0 - (if (gamepad1.leftBumper.isDown && gamepad1.rightBumper.isDown) 0.75 else 0.5)
                speed *= if (lift.height == Lift.Height.HighJunction) 0.8 else 1.0
                drive.drive(
//            gamepad1.leftStick.angle - gyro.robotHeading,
                    gamepad1.leftStick.angle,
                    gamepad1.leftStick.dist.pow(3) * speed,
                    gamepad1.rightStick.x * speed
                )
            }
        }

        when {
            gamepad2.dpadDown.justPressed -> lift.height = Lift.Height.Down
            gamepad2.dpadRight.justPressed -> lift.height = Lift.Height.MediumJunction
            gamepad2.dpadLeft.justPressed -> lift.height = Lift.Height.LowJunction
            gamepad2.dpadUp.justPressed -> lift.height = Lift.Height.HighJunction
            gamepad2.a.justPressed -> lift.height = Lift.Height.TwoCone
            gamepad2.x.justPressed -> lift.height = Lift.Height.ThreeCone
            gamepad2.b.justPressed -> lift.height = Lift.Height.FourCone
            gamepad2.y.justPressed -> lift.height = Lift.Height.FiveCone
        }
        lift.offset += gamepad2.leftStick.y * 8
        telemetry.addData("offset", lift.offset)
        lift.tick()

        if (gamepad2.rightBumper.justPressed) {
            claw.toggle()
        }

        telemetry.addData("reset", lift.reset.isPressed)
        if (gamepad2.leftBumper.justPressed) {
            lift.reset()
        }
    }

}

val gyro by lazy {  }

