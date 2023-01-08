package org.firstinspires.ftc.teamcode.autos

import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import com.qualcomm.robotcore.eventloop.opmode.Disabled
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import org.firstinspires.ftc.teamcode.lib.LinearOpModeEx
import org.firstinspires.ftc.teamcode.opmodes.CameraCalibrationOpMode
import org.firstinspires.ftc.teamcode.subsytems.AutoMecanumDrive
import org.firstinspires.ftc.teamcode.subsytems.CameraDetector
import org.firstinspires.ftc.teamcode.subsytems.Decision
import org.opencv.core.Rect
import kotlin.math.PI

class RightAuto : LinearOpModeEx() {

    @Autonomous
    @Disabled
    class Right : LinearOpMode() {
        override fun runOpMode() = RightAuto().runOpMode(this)
    }

    companion object {
        val CAMERA_AREA = Rect(200, 40, 30, 15)
    }

    val detector: CameraDetector by lazy { CameraDetector(hardware, CAMERA_AREA) }

    val drive: AutoMecanumDrive by lazy { AutoMecanumDrive(hardware) }

    override fun init() {
        telemetry.addLine("init")
        telemetry.update()
        detector.init()
        telemetry.addLine("done init")
        telemetry.update()
    }

    override fun once() {
        val decision = detector.readDecision()
        detector.close()
        telemetry.addData("decision", decision)
        telemetry.update()

        val p = 0.5

        drive.driveAndWait(3000.0, 0.0, p, 0.0)

        when (decision) {
            Decision.Red -> {
                // left
                drive.driveAndWait(2000.0, PI / 2, p, 0.0)
            }
            Decision.Blue -> {
                // middle

            }
            Decision.Green -> {
                // right
                drive.driveAndWait(2000.0, -PI / 2, p, 0.0)
            }
        }
    }

}