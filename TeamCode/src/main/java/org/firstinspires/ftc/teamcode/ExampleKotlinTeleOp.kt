package org.firstinspires.ftc.teamcode

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import com.qualcomm.robotcore.hardware.DcMotorEx
import com.qualcomm.robotcore.hardware.DcMotorSimple

@TeleOp
class ExampleKotlinTeleOp : LinearOpMode() {
    override fun runOpMode() {
        val leftFront = hardwareMap.get(DcMotorEx::class.java, "leftFront")
        val rightFront = hardwareMap.get(DcMotorEx::class.java, "rightFront")
        val leftBack = hardwareMap.get(DcMotorEx::class.java, "leftBack")
        val rightBack = hardwareMap.get(DcMotorEx::class.java, "rightBack")
        rightFront.direction = DcMotorSimple.Direction.REVERSE
        rightBack.direction = DcMotorSimple.Direction.REVERSE

        waitForStart()

        while (!isStopRequested) {
            leftFront.power = -gamepad1.left_stick_y.toDouble()
            leftBack.power = -gamepad1.left_stick_y.toDouble()
            rightFront.power = -gamepad1.right_stick_y.toDouble()
            rightBack.power = -gamepad1.right_stick_y.toDouble()
        }
    }
}