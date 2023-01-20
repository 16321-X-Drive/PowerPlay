package org.firstinspires.ftc.teamcode.opmodes

import com.qualcomm.robotcore.eventloop.opmode.Disabled
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import com.qualcomm.robotcore.hardware.DcMotor
import org.firstinspires.ftc.teamcode.lib.LinearOpModeEx
import org.firstinspires.ftc.teamcode.subsytems.AutoMecanumDrive
import org.firstinspires.ftc.teamcode.subsytems.Gyro
import kotlin.math.PI
import kotlin.math.absoluteValue
import kotlin.math.sign

class MinTurnCalibration : LinearOpModeEx() {

    @TeleOp
    @Disabled
    class MinTurn() : LinearOpMode() {
        override fun runOpMode() = MinTurnCalibration().runOpMode(this)
    }

    val drive: AutoMecanumDrive by lazy { AutoMecanumDrive(hardware) }
    val gyro: Gyro by lazy { Gyro(hardware, this) }
    var power: Double = 0.0

    fun Double.coerceMagMax(max: Double) = this.sign * this.absoluteValue.coerceAtMost(max)
    fun Double.coerceMagMin(min: Double) = this.sign * this.absoluteValue.coerceAtLeast(min)

    fun turn() {
        val tolerance = 0.05

        while (!isStopRequested) {
            val headingError = gyro.robotHeading - PI/2
            if (headingError < tolerance && headingError > -tolerance) break
            telemetry.addData("error", headingError)
            telemetry.addData("heading", gyro.robotHeading)
            telemetry.update()
            drive.drive(1000000.0, 0.0, 0.0, (headingError / 4.0).coerceMagMax(0.3).coerceMagMin(0.1))
        }
        telemetry.addLine("Done")
        telemetry.update()
        drive.drive(0.0, 0.0, 0.0, 0.0)
        drive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER)
    }

    override fun init() {
        super.init()
        gyro.waitForCalibration(debug = true)
    }

    override fun once() {
        super.once()
        drive.drive(10.0, 0.0, 0.0, power)
        drive.setMode(DcMotor.RunMode.RUN_TO_POSITION)
        turn()
    }

    override fun loop() {
//        power += gamepad1.leftStick.y * 0.01
//        power = power.coerceIn(-1.0, 1.0)
//        telemetry.addData("power", power)
//        drive.drive(100000000.0, 0.0, 0.0, power)
    }
}