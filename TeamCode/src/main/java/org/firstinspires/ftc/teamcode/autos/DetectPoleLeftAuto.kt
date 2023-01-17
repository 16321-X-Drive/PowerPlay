package org.firstinspires.ftc.teamcode.autos

import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import org.firstinspires.ftc.teamcode.lib.LinearOpModeEx
import org.firstinspires.ftc.teamcode.subsytems.*
import org.opencv.core.Rect
import kotlin.math.PI

class DetectPoleLeftAuto : LinearOpModeEx() {
    @Autonomous(preselectTeleOp = "DetectPoleLeft")
    class DetectPoleLeft : LinearOpMode() {
        override fun runOpMode() = DetectPoleLeftAuto().runOpMode(this)
    }

    fun region() = Rect(140, 150, 30, 15)
    override fun isAuto() = true

    val distances: Distances by lazy { Distances(hardware) }
    val gyro: Gyro by lazy { Gyro(hardware, this, 0.0) }
    val drive: AutoMecanumDrive by lazy { AutoMecanumDrive(hardware) }
    val claw: Claw by lazy { Claw(hardware) }
    val lift: Lift by lazy { Lift(hardware) }
    val detector: CameraDetector by lazy { CameraDetector(hardware, region()) }

    override fun init() {
        telemetry.addLine("init")
        telemetry.update()
        gyro.waitForCalibration(debug = false)
        detector.init()
        telemetry.addLine("done init")
        telemetry.update()
    }

    override fun once() {
        val decision = detector.readDecision()

        telemetry.addData("decision", decision)
        telemetry.update()

        distances.keepHeading(gyro.robotHeading)
        claw.isOpen = false // grab claw

        // push the signal out of the way
        drive.driveAndWait(70.0, 0.0, 0.25)
        drive.driveAndWait(-16.0, 0.0, 0.25)

        // Raise lift
        lift.height = Lift.Height.MediumJunction
        sleep(1000)

        // Drive to pole
        drive.driveAndWait(-15.0, PI /2, 0.25)
        telemetry.addLine("Seeking...")
        telemetry.update()
        val right = drive.driveToPoleOrWait(-30.0, PI /2, 0.15) {
            telemetry.addData("dist", it)
            telemetry.update()
        }

        // Drive forward
        drive.driveAndWait(4.0, 0.0, 0.25)
        sleep(500)

        // Lower cone
        lift.offset = -400.0
        sleep(500)

        // Drop cone
        claw.isOpen = true
        sleep(500)

        // Move backward
        drive.driveAndWait(-4.0, 0.0, 0.25)
        lift.height = Lift.Height.Down

        telemetry.addData("right", right)
        telemetry.update()

        // Park
        when (decision) {
            Decision.Red -> {
                // left
                drive.driveAndWait(40.0 - right, PI /2, 0.25)
            }
            Decision.Blue -> {
                // middle
                drive.driveAndWait(3 - right, PI /2, 0.25)
            }
            Decision.Green -> {
                // right
                drive.driveAndWait(-48.0 - right, PI /2, 0.25)
            }
        }
    }

}