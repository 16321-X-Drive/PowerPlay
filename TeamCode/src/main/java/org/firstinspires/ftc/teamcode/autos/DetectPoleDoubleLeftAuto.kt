package org.firstinspires.ftc.teamcode.autos

import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import com.qualcomm.robotcore.eventloop.opmode.Disabled
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import org.firstinspires.ftc.teamcode.lib.LinearOpModeEx
import org.firstinspires.ftc.teamcode.subsytems.*
import org.opencv.core.Rect
import kotlin.math.PI

class DetectPoleDoubleLeftAuto : LinearOpModeEx() {
    @Autonomous(preselectTeleOp = PRESELECTED_TELEOP)
    @Disabled
    class DetectPoleDoubleLeft : LinearOpMode() {
        override fun runOpMode() = DetectPoleDoubleLeftAuto().runOpMode(this)
    }

    fun region() = Rect(140, 150, 30, 15)
    override fun isAuto() = true

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

        claw.isOpen = false // grab claw

        // push the signal out of the way
        drive.driveAndWait(100.0, 0.0, 0.25)
        drive.turnToAngle(0.0, gyro)

        // Raise lift
        lift.height = Lift.Height.HighJunction
        sleep(1000)

        // Drive to pole
        drive.driveAndWait(-15.0, PI /2, 0.25)
        telemetry.addLine("Seeking...")
        telemetry.update()
        var (right, forward) = drive.driveToPoleOrWait(-30.0, PI /2, 0.15) {
            telemetry.addData("dist", it)
            telemetry.update()
        }
        forward = forward.coerceAtMost(15.0)
        drive.turnToAngle(0.0, gyro)
        telemetry.addData("forward", forward)
        telemetry.update()

        // Drive forward
        drive.driveAndWait(forward - 7, 0.0, 0.25)
        telemetry.addData("forward", forward)
        telemetry.addData("-->", forward - 7)
        telemetry.update()
//        sleep(5000)

        // Lower cone
        lift.offset = -400.0
        sleep(500)

        // Drop cone
        claw.isOpen = true
        lift.offset = 400.0
        sleep(500)

        // Move backward
        drive.driveAndWait(-6.0, 0.0, 0.25)
        lift.height = Lift.Height.FiveCone

        drive.driveAndWait(12 - right, PI /2, 0.25)

        telemetry.addLine("fixed")
        telemetry.update()

        drive.turnToAngle(PI/2, gyro)
        drive.driveAndWait(44.0, 0.0, 0.25)
        claw.isOpen = false
        sleep(500)
        lift.offset = 500.0
        sleep(500)
        drive.driveAndWait(-45.0, 0.0, 0.25)
        drive.turnToAngle(0.0, gyro)
        lift.height = Lift.Height.HighJunction
        sleep(500)

        var (right2, forward2) = drive.driveToPoleOrWait(-60.0, PI /2,0.15, ) {
            telemetry.addData("dist", it)
            telemetry.update()
        }
        forward2 = forward2.coerceAtMost(15.0)

        // Drive forward
        drive.driveAndWait(forward2 - 7, 0.0, 0.25)
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

        // Park
        when (decision) {
            Decision.Red -> {
                // left
                drive.driveAndWait(40.0 - right2, PI /2, 0.25)
            }
            Decision.Blue -> {
                // middle
                drive.driveAndWait(-10 - right2, PI /2, 0.25)
            }
            Decision.Green -> {
                // right
                drive.driveAndWait(-48.0 - right2, PI /2, 0.25)
            }
        }
        drive.driveAndWait(-4.0, 0.0, 0.25)
    }

}