package org.firstinspires.ftc.teamcode.autos

import com.acmerobotics.dashboard.FtcDashboard
import com.acmerobotics.dashboard.telemetry.TelemetryPacket
import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.hardware.DcMotor
import org.firstinspires.ftc.teamcode.lib.LinearOpModeEx
import org.firstinspires.ftc.teamcode.subsytems.*
import org.firstinspires.ftc.teamcode.util.Point
import org.firstinspires.ftc.teamcode.util.Pose
import org.firstinspires.ftc.teamcode.util.drawRobot
import org.opencv.core.Rect
import kotlin.math.PI
import kotlin.math.abs

class PositioningTestAuto : LinearOpModeEx() {

    @Autonomous
    class PositioningTest : LinearOpMode() {
        override fun runOpMode() = PositioningTestAuto().runOpMode(this)
    }

    companion object {
        val CAMERA_AREA = Rect(200, 50, 30, 15)
    }

    val drive: AutoMecanumDrive by lazy { AutoMecanumDrive(hardware) }
    val gyro by lazy { Gyro(hardware, this, PI / 2) }
    val distances: Distances by lazy { Distances(hardware) }

    override fun init() {
        gyro.waitForCalibration(true)
    }

    override fun once() {
        val targetPose = Pose(Point(43.0, 50.0), 0.665)
        distances.keepHeading(gyro.robotHeading)
        var pose = distances.calcPos(Distances.Pair.High, Distances.Pair.High, gyro.robotHeading)

        drive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER)

//        while (targetPose.position.dist(pose.position) > 2 && abs(pose.heading - targetPose.heading) < PI / 16) {]
        while (!isStopRequested) {
            distances.keepHeading(gyro.robotHeading)
            pose = distances.calcPos(Distances.Pair.High, Distances.Pair.High, gyro.robotHeading)

            val delta = targetPose.position - pose.position
            drive.drive(delta.len() * 1000, PI / 4, 0.25, 0.0)

            val dashboard = FtcDashboard.getInstance()
            val packet = TelemetryPacket()
            packet.fieldOverlay().clear()
            packet.put("dist", delta.len())
            packet.put("angle", delta.angle())
            packet.drawRobot(targetPose, target = true)
            packet.drawRobot(pose)
            dashboard.sendTelemetryPacket(packet)

            drive.setMode(DcMotor.RunMode.RUN_TO_POSITION)
        }

        drive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER)
    }

}