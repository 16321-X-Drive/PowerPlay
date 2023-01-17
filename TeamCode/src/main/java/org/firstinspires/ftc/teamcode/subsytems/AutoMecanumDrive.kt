package org.firstinspires.ftc.teamcode.subsytems

import com.qualcomm.robotcore.hardware.DcMotor.RunMode
import com.qualcomm.robotcore.hardware.DcMotorSimple
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit
import org.firstinspires.ftc.teamcode.hardware.Hardware
import java.lang.Thread.sleep
import kotlin.math.PI
import kotlin.math.abs
import kotlin.math.cos
import kotlin.math.sin

class AutoMecanumDrive(hardware: Hardware) {

    private val leftFront = hardware.leftFront
    private val leftBack = hardware.leftBack
    private val rightFront = hardware.rightFront
    private val rightBack = hardware.rightBack

    private val poleSensor = hardware.poleSensor

    init {
        rightFront.direction = DcMotorSimple.Direction.REVERSE
        rightBack.direction = DcMotorSimple.Direction.REVERSE

        setMode(RunMode.STOP_AND_RESET_ENCODER)
    }

    fun setMode(mode: RunMode) {
        leftFront.mode = mode
        leftBack.mode = mode
        rightFront.mode = mode
        rightBack.mode = mode
    }

    fun isBusy() = leftFront.isBusy

    fun driveToPoleOrWait(dist: Double, theta: Double, power: Double, turn: Double = 0.0, maxDist: Double = 15.0, run: (Double) -> Unit = {}): Double {
        setMode(RunMode.STOP_AND_RESET_ENCODER)
        drive(dist, theta, power, turn)
        setMode(RunMode.RUN_TO_POSITION)
        drive(dist, theta, power, turn)
        while (isBusy() && poleSensor.getDistance(DistanceUnit.INCH) > maxDist) {
            run(poleSensor.getDistance(DistanceUnit.INCH))
            Thread.yield()
        }
        run(poleSensor.getDistance(DistanceUnit.INCH))
        sleep(200)
        val distDriven = dist * (leftFront.currentPosition.toDouble() / leftFront.targetPosition.toDouble())

        setMode(RunMode.STOP_AND_RESET_ENCODER)

        return distDriven
    }

    fun driveAndWait(dist: Double, theta: Double, power: Double, turn: Double = 0.0) {
        setMode(RunMode.STOP_AND_RESET_ENCODER)
        drive(dist, theta, power, turn)
        setMode(RunMode.RUN_TO_POSITION)
        drive(dist, theta, power, turn)
        while (isBusy()) Thread.yield()
        setMode(RunMode.STOP_AND_RESET_ENCODER)
    }

    fun drive(dist: Double, theta: Double, power: Double, turn: Double) {
        // Taken from: https://www.youtube.com/watch?v=gnSW2QpkGXQ

        val sin = sin(theta - PI / 4 - PI / 2)
        val cos = cos(theta - PI / 4 - PI / 2)
        val max = abs(sin).coerceAtLeast(abs(cos))

        var leftFrontPow = power * cos / max - turn
        var rightFrontPow = power * sin / max + turn
        var leftBackPow = power * sin / max - turn
        var rightBackPow = power * cos / max + turn

        if (power + abs(turn) > 1) {
            leftFrontPow /= power + turn
            rightFrontPow /= power + turn
            leftBackPow /= power + turn
            rightBackPow /= power + turn
        }

        leftFront.targetPosition = (dist * 100 * leftFrontPow).toInt()
        rightFront.targetPosition = (dist * 100 * rightFrontPow).toInt()
        leftBack.targetPosition = (dist * 100 * leftBackPow).toInt()
        rightBack.targetPosition = (dist * 100 * rightBackPow).toInt()

        leftFront.power = leftFrontPow
        rightFront.power = rightFrontPow
        leftBack.power = leftBackPow
        rightBack.power = rightBackPow
    }
}