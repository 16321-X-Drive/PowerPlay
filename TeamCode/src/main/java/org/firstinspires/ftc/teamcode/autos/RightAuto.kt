package org.firstinspires.ftc.teamcode.autos

import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import org.firstinspires.ftc.teamcode.lib.LinearOpModeEx
import org.firstinspires.ftc.teamcode.opmodes.CameraCalibrationOpMode
import org.firstinspires.ftc.teamcode.subsytems.CameraDetector
import org.opencv.core.Rect

class RightAuto : LinearOpModeEx() {

    @Autonomous
    class Right : LinearOpMode() {
        override fun runOpMode() = RightAuto().runOpMode(this)
    }

    companion object {
        val CAMERA_AREA = Rect(200, 50, 30, 15)
    }

    val detector: CameraDetector by lazy { CameraDetector(hardware, telemetry) }

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


    }

}