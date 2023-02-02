package org.firstinspires.ftc.teamcode.autos

import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import com.qualcomm.robotcore.eventloop.opmode.Disabled
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.hardware.DcMotor
import org.firstinspires.ftc.teamcode.lib.LinearOpModeEx
import org.firstinspires.ftc.teamcode.subsytems.*
import org.opencv.core.Rect
import kotlin.math.PI
import kotlin.math.abs
import kotlin.math.sign

class DoubleConeLeftAuto : LinearOpModeEx() {

    @Autonomous(preselectTeleOp = "RRMecanumDrive")
    @Disabled
    class DoubleConeLeft : LinearOpMode() {
        override fun runOpMode() = DoubleConeLeftAuto().runOpMode(this)
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
        gyro.waitForCalibration(debug = false)
        telemetry.addLine("done init")
        telemetry.update()
    }

    fun goToHeading(heading: Double) {
        drive.setMode(DcMotor.RunMode.RUN_USING_ENCODER)
        while (abs(gyro.robotHeading - heading) > 0.5) {
            telemetry.addData("rh", gyro.robotHeading)
            telemetry.addData("th", heading)
            telemetry.addData("err", gyro.robotHeading - heading)
            telemetry.update()
            val error = gyro.robotHeading - heading
            drive.drive(10.0, 0.0, 0.0, (error.sign * 0.5).coerceIn(-0.5, 0.5))
        }
        drive.drive(0.0, 0.0, 0.0, 0.0)
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
        drive.driveAndWait(9.0, 0.0, 0.25)
        sleep(500)
        lift.offset = -400.0
        sleep(500)
        claw.isOpen = true
        sleep(500)
        drive.driveAndWait(-9.0, 0.0, 0.25)
        lift.height = Lift.Height.Down

        drive.driveAndWait(-80.0, -PI/2, 0.25)
        drive.driveAndWait(20.0, 0.0, 0.25)
        drive.driveAndWait(10.0, -PI/2, 0.25)
        lift.height = Lift.Height.FiveCone

        goToHeading(PI/4)
        drive.driveAndWait(20.0, 0.0, 0.25)
        claw.isOpen = false
        sleep(500)
        drive.driveAndWait(20.0, 0.0, 0.25)
        goToHeading(-PI/2)
        drive.driveAndWait(40.0, -PI/2, 0.25)
        drive.driveAndWait(-20.0, 0.0, 0.25)

    }

}