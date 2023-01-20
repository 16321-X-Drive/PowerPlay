package org.firstinspires.ftc.teamcode.opmodes

import com.qualcomm.robotcore.eventloop.opmode.Disabled
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import org.firstinspires.ftc.teamcode.lib.LinearOpModeEx
import org.firstinspires.ftc.teamcode.subsytems.Distances
import org.firstinspires.ftc.teamcode.subsytems.Gyro

class DistanceSensorTestsOpMode : LinearOpModeEx() {

    @TeleOp
    @Disabled
    class DistanceSensorTests : LinearOpMode() {
        override fun runOpMode() = DistanceSensorTestsOpMode().runOpMode(this)
    }

    val distances by lazy { Distances(hardware) }
    val gyro by lazy { Gyro(hardware, this) }

    override fun init() {
        gyro.waitForCalibration(true)
    }

    override fun loop() {
        telemetry.addData("heading", gyro.robotHeading)
        telemetry.addData("low pos", distances.lowServo.position)
        telemetry.addData("high pos", distances.highServo.position)

        distances.keepHeading(gyro.robotHeading)
    }

}