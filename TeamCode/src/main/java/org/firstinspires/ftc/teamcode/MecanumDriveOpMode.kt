package org.firstinspires.ftc.teamcode

import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import org.firstinspires.ftc.teamcode.lib.ButtonToggler
import org.firstinspires.ftc.teamcode.lib.OpModeBase
import org.firstinspires.ftc.teamcode.lib.UseSubsystem
import kotlin.math.atan2
import kotlin.math.hypot

fun square(power: Double) = power * power * power


@TeleOp
class MecanumDriveOpMode : OpModeBase() {

    private var clawIsOpen: Boolean = true
    private var xFirstPressed: Boolean = true

    @UseSubsystem
    lateinit var drive: MecanumDrive

    @UseSubsystem
    lateinit var color: ColorSensing

    @UseSubsystem
    lateinit var distances: Distances

    @UseSubsystem
    lateinit var claw: Claw

    private val clawButton = ButtonToggler()

    override fun coreLoop() {
        val angle = atan2(gamepad1.left_stick_y, -gamepad1.left_stick_x).toDouble()
        val power = hypot(gamepad1.left_stick_x, gamepad1.left_stick_y).toDouble()

//        telemetry.addData("angle", angle)
//        telemetry.addData("power", power)
//        telemetry.addData("turnPower", gamepad1.right_stick_x)
//        telemetry.addData("leftColor", color.readLeftColor())
//        telemetry.addData("rightColor", color.readRightColor())
//        telemetry.addData("leftDomColor", color.readLeftColor().dominantColor())
//        telemetry.addData("rightDomColor", color.readRightColor().dominantColor())

//        telemetry.addData("voltage (cm)", distances.frontDist.distanceCm)
//        telemetry.addData("voltage (in)", distances.frontDist.distanceIn)

        telemetry.update()

        drive.drive(angle, square(power), gamepad1.right_stick_x.toDouble())

        if(clawButton.shouldToggle(gamepad1.x)) {
            clawIsOpen = !clawIsOpen
        }


        if(clawIsOpen) {
            claw.openClaw()
        } else {
            claw.closeClaw()
        }


    }

}