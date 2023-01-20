package org.firstinspires.ftc.teamcode.opmodes

import com.acmerobotics.dashboard.FtcDashboard
import com.acmerobotics.dashboard.telemetry.TelemetryPacket
import com.qualcomm.robotcore.eventloop.opmode.Disabled
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import org.firstinspires.ftc.teamcode.lib.LinearOpModeEx
import org.firstinspires.ftc.teamcode.subsytems.*
import org.firstinspires.ftc.teamcode.util.drawRobot
import kotlin.math.PI
import kotlin.math.pow

class SensorTestsOpMode : LinearOpModeEx() {
    @TeleOp
    @Disabled
    class SensorTests : LinearOpMode() {
        override fun runOpMode() = SensorTestsOpMode().runOpMode(this)
    }

    val drive by lazy { MecanumDrive(hardware) }
    val distances by lazy { Distances(hardware) }
    val gyro by lazy { Gyro(hardware, this, PI / 2) }

    override fun init() {
        gyro.waitForCalibration(debug = true)
    }

    override fun loop() {
        telemetry.addData("angle", gamepad1.leftStick.angle)
        telemetry.addData("power", gamepad1.leftStick.dist)
        telemetry.addData("turn power", gamepad1.rightStick.x)
        telemetry.addData("robot heading", gyro.robotHeading)

        telemetry.addData("distance 1", distances.distance1.distanceIn)
        telemetry.addData("distance 2", distances.distance2.distanceIn)
        telemetry.addData("distance 3", distances.distance3.distanceIn)
        telemetry.addData("distance 4", distances.distance4.distanceIn)

        val pose =
            distances.calcPos(Distances.Pair.High, Distances.Pair.High, gyro.robotHeading)
        telemetry.addData("pos", pose)

        val dashboard = FtcDashboard.getInstance()
        val packet = TelemetryPacket()
        packet.put("d1", distances.distance1.distanceIn)
        packet.put("d2", distances.distance2.distanceIn)
        packet.put("d3", distances.distance3.distanceIn)
        packet.put("d4", distances.distance4.distanceIn)
        packet.put("x", pose.position.x)
        packet.put("y", pose.position.y)
        packet.put("heading", gyro.robotHeading)
        packet.fieldOverlay().clear()
        packet.drawRobot(pose)
        dashboard.sendTelemetryPacket(packet)

        drive.drive(
//            gamepad1.leftStick.angle - gyro.robotHeading,
            gamepad1.leftStick.angle,
            gamepad1.leftStick.dist.pow(3),
            gamepad1.rightStick.x
        )

        distances.keepHeading(gyro.robotHeading)
    }


}