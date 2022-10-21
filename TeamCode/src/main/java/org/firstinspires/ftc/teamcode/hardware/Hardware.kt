package org.firstinspires.ftc.teamcode.hardware

import com.qualcomm.hardware.bosch.BNO055IMU
import com.qualcomm.robotcore.hardware.AnalogInput
import com.qualcomm.robotcore.hardware.ColorSensor
import com.qualcomm.robotcore.hardware.DcMotorEx
import com.qualcomm.robotcore.hardware.HardwareMap
import com.qualcomm.robotcore.hardware.Servo

class Hardware(hm: HardwareMap) {

    val leftFront = hm.get(DcMotorEx::class.java, "leftFront")!!
    val leftBack = hm.get(DcMotorEx::class.java, "leftBack")!!
    val rightFront = hm.get(DcMotorEx::class.java, "rightFront")!!
    val rightBack = hm.get(DcMotorEx::class.java, "rightBack")!!

    val leftClaw = hm.get(Servo::class.java, "leftClaw")!!
    val rightClaw = hm.get(Servo::class.java, "rightClaw")!!

    val leftLift = hm.get(DcMotorEx::class.java, "leftLift")!!
    val rightLift = hm.get(DcMotorEx::class.java, "rightLift")!!

    val leftColor = hm.get(ColorSensor::class.java, "leftColor")!!
    val rightColor = hm.get(ColorSensor::class.java, "rightColor")!!

    private val rawDistance = hm.get(AnalogInput::class.java, "distance")!!
    val distance = MaxbotixDistanceSensor(rawDistance)

    val imu = hm.get(BNO055IMU::class.java, "imu")!!
}
