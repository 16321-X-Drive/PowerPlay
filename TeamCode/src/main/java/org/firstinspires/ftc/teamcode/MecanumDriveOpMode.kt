package org.firstinspires.ftc.teamcode

import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import org.firstinspires.ftc.teamcode.lib.OpModeBase
import org.firstinspires.ftc.teamcode.lib.UseSubsystem
import kotlin.math.atan2
import kotlin.math.hypot
import kotlin.math.sqrt

fun square(power: Double) = power * power * power


@TeleOp
class MecanumDriveOpMode : OpModeBase() {

    @UseSubsystem
    lateinit var drive: MecanumDrive

    @UseSubsystem
    lateinit var color: ColorSensing

    @UseSubsystem
    lateinit var distances: Distances

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

        telemetry.addData("voltage (cm)", distances.frontDist.distanceCm)
        telemetry.addData("voltage (in)", distances.frontDist.distanceIn)
        telemetry.update()

        //

        drive.drive(angle, square(power), gamepad1.right_stick_x.toDouble())
    }

}