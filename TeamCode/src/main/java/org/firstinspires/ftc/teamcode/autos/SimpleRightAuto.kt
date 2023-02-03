package org.firstinspires.ftc.teamcode.autos

import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import com.qualcomm.robotcore.eventloop.opmode.Disabled
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import org.firstinspires.ftc.teamcode.lib.LinearOpModeEx
import org.firstinspires.ftc.teamcode.opmodes.CameraCalibrationOpMode
import org.firstinspires.ftc.teamcode.subsytems.*
import org.opencv.core.Rect
import kotlin.math.PI
import kotlin.math.abs

class SimpleRightAuto : LinearOpModeEx() {

    @Autonomous(preselectTeleOp = PRESELECTED_TELEOP)
    @Disabled
    class SimpleRight : LinearOpMode() {
        override fun runOpMode() = SimpleRightAuto().runOpMode(this)
    }

    companion object {
        val CAMERA_AREA = Rect(200, 40, 30, 15)
    }

    val detector: CameraDetector by lazy { CameraDetector(hardware, CAMERA_AREA) }
    val distances: Distances by lazy { Distances(hardware) }
    val gyro: Gyro by lazy { Gyro(hardware, this, 0.0) }
    val drive: MecanumDrive by lazy { MecanumDrive(hardware) }

    override fun init() {
        telemetry.addLine("init")
        telemetry.update()
        detector.init()
        telemetry.addLine("done init")
        telemetry.update()
    }

    fun driveToDistY(dist: Double, theta: Double = 0.0, power: Double = 0.75) {
        drive.drive(theta, power, 0.0)
        while (!isStopRequested && abs(distances.distance2.distanceIn - dist) < 1.0) {
            distances.keepHeading(gyro.robotHeading)
            telemetry.addData("dist y", distances.distance2.distanceIn)
            telemetry.update()
        }
        drive.drive(0.0, 0.0, 0.0)
    }

    override fun once() {
        val decision = detector.readDecision()
        detector.close()
        telemetry.addData("decision", decision)
        telemetry.update()


//        drive.driveAndWait(3000.0, 0.0, p, 0.0)

        driveToDistY(24.0)

        when (decision) {
            Decision.Red -> {
                // left
//                drive.driveAndWait(2000.0, PI / 2, p, 0.0)
            }
            Decision.Blue -> {
                // middle

            }
            Decision.Green -> {
                // right
//                drive.driveAndWait(2000.0, -PI / 2, p, 0.0)
            }
        }

        while (!isStopRequested) idle()
    }

}