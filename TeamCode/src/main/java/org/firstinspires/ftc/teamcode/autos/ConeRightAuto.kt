package org.firstinspires.ftc.teamcode.autos

import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import org.firstinspires.ftc.teamcode.lib.LinearOpModeEx
import org.firstinspires.ftc.teamcode.subsytems.*
import org.opencv.core.Rect
import kotlin.math.PI

class ConeRightAuto : LinearOpModeEx() {

    @Autonomous(preselectTeleOp = "RRMecanumDrive")
    class ConeRight : LinearOpMode() {
        override fun runOpMode() = ConeRightAuto().runOpMode(this)
    }

    companion object {
        val CAMERA_AREA = Rect(140, 170, 30, 15)
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
        val right = -21.0
        drive.driveAndWait(right, -PI/2, 0.25)
        drive.driveAndWait(9.0, 0.0, 0.25)
        sleep(500)
        lift.offset = -400.0
        sleep(500)
        claw.isOpen = true
        sleep(500)
        drive.driveAndWait(-9.0, 0.0, 0.25)
        lift.height = Lift.Height.Down

        when (decision) {
            Decision.Red -> {
                // left
                drive.driveAndWait(-47.0 - right, -PI/2, 0.25)
            }
            Decision.Blue -> {
                // middle
                drive.driveAndWait(4 - right, -PI/2, 0.25)
            }
            Decision.Green -> {
                // right
                drive.driveAndWait(50.0 - right, -PI/2, 0.25)
            }
        }
    }

}