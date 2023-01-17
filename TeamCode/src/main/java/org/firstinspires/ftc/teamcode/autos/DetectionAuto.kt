package org.firstinspires.ftc.teamcode.autos

import org.firstinspires.ftc.teamcode.lib.LinearOpModeEx
import org.firstinspires.ftc.teamcode.subsytems.CameraDetector
import org.firstinspires.ftc.teamcode.subsytems.Decision
import org.opencv.core.Rect

abstract class DetectionAuto : LinearOpModeEx() {

    abstract fun region(): Rect

    private var lastDetection: Decision = Decision.Blue

    val decision: Decision
        get() = lastDetection

    val detector: CameraDetector by lazy { CameraDetector(hardware, region()) }

    override fun init() {
        detector.init()
        Thread {
            var newDecision = detector.readDecision()
            while (isInInit) {
                lastDetection = newDecision
                newDecision = detector.readDecision()
                detector.close()
            }
        }.start()
    }

}