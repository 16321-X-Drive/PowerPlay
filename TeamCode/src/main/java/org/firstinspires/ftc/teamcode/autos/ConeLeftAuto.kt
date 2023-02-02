package org.firstinspires.ftc.teamcode.autos

import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import org.firstinspires.ftc.teamcode.lib.LinearOpModeEx
import org.firstinspires.ftc.teamcode.subsytems.*
import org.opencv.core.Rect
import kotlin.math.PI
import kotlin.math.abs

class ConeLeftAuto : LinearOpModeEx() {

    @Autonomous(preselectTeleOp = "RRMecanumDrive")
    class ConeLeft : LinearOpMode() {
        override fun runOpMode() = ConeLeftAuto().runOpMode(this)
    }

    companion object {
        val CAMERA_AREA = Rect(140, 150, 30, 15)
    }

    val detector: CameraDetector by lazy { CameraDetector(hardware, CAMERA_AREA) }
    val distances: Distances by lazy { Distances(hardware) }
    val gyro: Gyro by lazy { Gyro(hardware, this, 0.0) }
    val drive: AutoMecanumDrive by lazy { AutoMecanumDrive(hardware) }
    val claw: Claw by lazy { Claw(hardware) }
    val lift: Lift by lazy { Lift(hardware) }

    override fun init() {
        telemetry.addLine("init")
        telemetry.update()
        detector.init()
//        gyro.waitForCalibration(debug = false)
        telemetry.addLine("done init")
        telemetry.update()
    }

    override fun once() {
        distances.keepHeading(gyro.robotHeading)
        claw.isOpen = false

        val decision = detector.readDecision()
        detector.close()
        telemetry.addData("decision", decision)
        telemetry.update()

        drive.driveAndWait(70.0, 0.0, 0.25)
        drive.driveAndWait(-20.0, 0.0, 0.25)

        lift.height = Lift.Height.MediumJunction
        sleep(1000)
        val right = -23.0
        drive.driveAndWait(right, PI/2, 0.25)
        drive.driveAndWait(8.0, 0.0, 0.25)
        sleep(500)
        lift.offset = -400.0
        sleep(500)
        claw.isOpen = true
        sleep(500)
        drive.driveAndWait(-8.0, 0.0, 0.25)
        lift.height = Lift.Height.Down

        when (decision) {
            Decision.Red -> {
                // left
                drive.driveAndWait(50.0 - right, PI/2, 0.25)
            }
            Decision.Blue -> {
                // middle
                drive.driveAndWait(3 - right, PI/2, 0.25)
            }
            Decision.Green -> {
                // right
                drive.driveAndWait(-48.0 - right, PI/2, 0.25)
            }
        }
    }

}