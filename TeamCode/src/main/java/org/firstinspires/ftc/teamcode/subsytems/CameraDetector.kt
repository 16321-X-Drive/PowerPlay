package org.firstinspires.ftc.teamcode.subsytems

import org.firstinspires.ftc.robotcore.external.Telemetry
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName
import org.firstinspires.ftc.teamcode.hardware.Hardware
import org.firstinspires.ftc.teamcode.opmodes.CameraCalibrationOpMode
import org.openftc.easyopencv.OpenCvCameraFactory
import org.openftc.easyopencv.OpenCvCameraRotation
import org.openftc.easyopencv.OpenCvWebcam

enum class Decision {
    Red, Green, Blue
}

class CameraDetector(val hardware: Hardware) {

    private lateinit var pipeline: PowerPlayPipeline
    private lateinit var webcam: OpenCvWebcam

    fun readDecision() = pipeline.readDecision()

    fun init() {
        val cameraMonitorViewId = hardware.map.appContext.resources.getIdentifier(
            "cameraMonitorViewId",
            "id",
            hardware.map.appContext.packageName
        )

        webcam = OpenCvCameraFactory.getInstance().createWebcam(
            hardware.map.get(
                WebcamName::class.java, "Webcam 1"
            ), cameraMonitorViewId
        )

        pipeline = PowerPlayPipeline(CameraCalibrationOpMode.CAMERA_AREA)

        webcam.openCameraDevice()
        webcam.setPipeline(pipeline)
        webcam.startStreaming(320, 240, OpenCvCameraRotation.UPRIGHT)
    }

    fun close() {
        webcam.stopRecordingPipeline()
        webcam.stopStreaming()
        webcam.closeCameraDevice()
    }
}