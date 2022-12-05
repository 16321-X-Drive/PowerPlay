package org.firstinspires.ftc.teamcode.subsytems

import com.qualcomm.robotcore.hardware.DcMotor.RunMode
import com.qualcomm.robotcore.hardware.DcMotorSimple
import org.firstinspires.ftc.teamcode.hardware.Hardware
import kotlin.math.PI
import kotlin.math.abs
import kotlin.math.cos
import kotlin.math.sin

class AutoMecanumDrive(hardware: Hardware) {

    private val leftFront = hardware.leftFront
    private val leftBack = hardware.leftBack
    private val rightFront = hardware.rightFront
    private val rightBack = hardware.rightBack

    init {
        rightFront.direction = DcMotorSimple.Direction.REVERSE
        rightBack.direction = DcMotorSimple.Direction.REVERSE

        setMode(RunMode.STOP_AND_RESET_ENCODER)
    }

    private fun setMode(mode: RunMode) {
        leftFront.mode = mode
        leftBack.mode = mode
        rightFront.mode = mode
        rightBack.mode = mode
    }

    fun isBusy() = leftFront.isBusy

    fun driveAndWait(dist: Double, theta: Double, power: Double, turn: Double = 0.0) {
        drive(dist, theta, power, turn)
        setMode(RunMode.RUN_TO_POSITION)
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

        leftFront.targetPosition = (dist * leftFrontPow).toInt()
        rightFront.targetPosition = (dist * rightFrontPow).toInt()
        leftBack.targetPosition = (dist * leftBackPow).toInt()
        rightBack.targetPosition = (dist * rightBackPow).toInt()

        leftFront.power = leftFrontPow
        rightFront.power = rightFrontPow
        leftBack.power = leftBackPow / 2
        rightBack.power = rightBackPow / 2
    }
}