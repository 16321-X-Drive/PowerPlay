package org.firstinspires.ftc.teamcode.opmodes

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import org.firstinspires.ftc.teamcode.lib.LinearOpModeEx
import org.firstinspires.ftc.teamcode.subsytems.CameraDetector
import org.opencv.core.Rect


class CameraCalibrationOpMode : LinearOpModeEx() {

    @TeleOp
    class CameraCalibration : LinearOpMode() {
        override fun runOpMode() = CameraCalibrationOpMode().runOpMode(this)
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

    override fun loop() {
        telemetry.addData("decision", detector.readDecision())
    }
}