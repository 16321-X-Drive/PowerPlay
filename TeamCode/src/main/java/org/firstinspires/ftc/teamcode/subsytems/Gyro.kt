package org.firstinspires.ftc.teamcode.subsytems

import com.qualcomm.hardware.bosch.BNO055IMU
import org.firstinspires.ftc.teamcode.hardware.Hardware
import org.firstinspires.ftc.teamcode.lib.LinearOpModeEx
import java.util.*

class Gyro(hardware: Hardware, private val opMode: LinearOpModeEx?, private var startAngle: Double = 0.0) {

    private val imu = hardware.imu

    init {
        val params = BNO055IMU.Parameters()
        params.angleUnit = BNO055IMU.AngleUnit.RADIANS
        params.accelUnit = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC
        params.loggingEnabled = false
        imu.initialize(params)
    }

    fun waitForCalibration(debug: Boolean = false, timeout: Long = 99999999) {
        if (debug) {
            opMode?.telemetry?.addData("imu", "calibrating")
            opMode?.telemetry?.update()
        }

        val start = System.currentTimeMillis();
        while (opMode?.isStopRequested != true && !imu.isGyroCalibrated && (System.currentTimeMillis() - start) < timeout) {
            opMode?.idle()
        }
        startAngle -= imu.angularOrientation.firstAngle.toDouble()

        if (debug) {
            opMode?.telemetry?.addData("imu", "calibrated")
            opMode?.telemetry?.update()
        }
    }

    val robotHeading: Double get() = startAngle + imu.angularOrientation.firstAngle.toDouble()
}