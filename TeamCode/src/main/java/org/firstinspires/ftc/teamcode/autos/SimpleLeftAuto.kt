package org.firstinspires.ftc.teamcode.autos

import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import com.qualcomm.robotcore.eventloop.opmode.Disabled
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import org.firstinspires.ftc.teamcode.lib.LinearOpModeEx
import org.firstinspires.ftc.teamcode.subsytems.*
import org.opencv.core.Rect
import kotlin.math.PI
import kotlin.math.abs

class SimpleLeftAuto : LinearOpModeEx() {

    @Autonomous(preselectTeleOp = "MecanumDrive")
    @Disabled
    class SimpleLeft : LinearOpMode() {
        override fun runOpMode() = SimpleLeftAuto().runOpMode(this)
    }

    companion object {
        val CAMERA_AREA = Rect(140, 150, 30, 15)
    }

    val detector: CameraDetector by lazy { CameraDetector(hardware, CAMERA_AREA) }
    val distances: Distances by lazy { Distances(hardware) }
    val gyro: Gyro by lazy { Gyro(hardware, this, 0.0) }
    val drive: AutoMecanumDrive by lazy { AutoMecanumDrive(hardware) }
    val claw: Claw by lazy { Claw(hardware) }

    override fun init() {
        telemetry.addLine("init")
        telemetry.update()
        detector.init()
//        gyro.waitForCalibration(debug = false)
        telemetry.addLine("done init")
        telemetry.update()
    }

//    fun driveToDistX(dist: Double, theta: Double = -PI/2, power: Double = 0.25) {
//        drive.drive(theta, power, 0.0)
//        while (!isStopRequested && abs(distances.distance4.distanceIn - dist) > 1.0) {
//            if (distances.distance3.distanceIn > dist) {
//                drive.drive(theta, power, 0.0)
//            } else {
//                drive.drive(theta, -power, 0.0)
//            }
//
//            distances.keepHeading(gyro.robotHeading)
//            telemetry.addData("dist y", distances.distance4.distanceIn)
//            telemetry.update()
//        }
//        drive.drive(0.0, 0.0, 0.0)
//    }
//
//    fun driveToDistY(dist: Double, theta: Double = -PI/2, power: Double = 0.25) {
//        drive.drive(theta, power, 0.0)
//        while (!isStopRequested && abs(distances.distance3.distanceIn - dist) > 1.0) {
//            val error = abs(distances.distance3.distanceIn - dist)
//            if (distances.distance3.distanceIn > dist) {
//                drive.drive(theta, power * (error * 0.1).coerceIn(0.3, 1.0), 0.0)
//            } else {
//                drive.drive(theta, -power  * (error * 0.1).coerceIn(0.3, 1.0), 0.0)
//            }
//
//            distances.keepHeading(gyro.robotHeading)
//            telemetry.addData("dist y", distances.distance3.distanceIn)
//            telemetry.update()
//        }
//        drive.drive(0.0, 0.0, 0.0)
//    }

//    fun goToHeading(heading: Double) {
//        telemetry.addData("rh", gyro.robotHeading)
//        telemetry.addData("th", heading)
//        telemetry.update()
//        while (abs(gyro.robotHeading - heading) > PI/8) {
//            val error = gyro.robotHeading - heading
//            drive.drive(0.0, 0.0, (error * 0.2).coerceIn(-0.5, 0.5))
//        }
//        drive.drive(0.0, 0.0, 0.0)
//    }

    override fun once() {
        distances.keepHeading(gyro.robotHeading)
        claw.isOpen = false

        val decision = detector.readDecision()
        detector.close()
        telemetry.addData("decision", decision)
        telemetry.update()

        drive.driveAndWait(70.0, 0.0, 0.25)
        drive.driveAndWait(-20.0, 0.0, 0.25)

        when (decision) {
            Decision.Red -> {
                // left
                drive.driveAndWait(45.0, PI/2, 0.25)
            }
            Decision.Blue -> {
                // middle

            }
            Decision.Green -> {
                // right
                drive.driveAndWait(-45.0, PI/2, 0.25)
            }
        }
    }

}