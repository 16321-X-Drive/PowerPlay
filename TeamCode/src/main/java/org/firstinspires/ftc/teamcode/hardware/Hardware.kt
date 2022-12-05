package org.firstinspires.ftc.teamcode.hardware

import com.qualcomm.hardware.bosch.BNO055IMU
import com.qualcomm.robotcore.hardware.AnalogInput
import com.qualcomm.robotcore.hardware.ColorSensor
import com.qualcomm.robotcore.hardware.DcMotorEx
import com.qualcomm.robotcore.hardware.HardwareMap
import com.qualcomm.robotcore.hardware.Servo

class Hardware(hm: HardwareMap) {

    val map = hm

    val leftFront = hm.get(DcMotorEx::class.java, "leftFront")!!
    val leftBack = hm.get(DcMotorEx::class.java, "leftBack")!!
    val rightFront = hm.get(DcMotorEx::class.java, "rightFront")!!
    val rightBack = hm.get(DcMotorEx::class.java, "rightBack")!!

    val claw = hm.get(Servo::class.java, "claw")!!

    val leftLift = hm.get(DcMotorEx::class.java, "leftLift")!!
    val rightLift = hm.get(DcMotorEx::class.java, "rightLift")!!

    val leftColor = hm.get(ColorSensor::class.java, "leftColor")!!
    val rightColor = hm.get(ColorSensor::class.java, "rightColor")!!

    val lowDistServo = hm.get(Servo::class.java, "lowDist")!!
    val highDistServo = hm.get(Servo::class.java, "highDist")!!

    private val rawDistance1 = hm.get(AnalogInput::class.java, "distance1")!!
    val distance1 = MaxbotixDistanceSensor(rawDistance1)
    private val rawDistance2 = hm.get(AnalogInput::class.java, "distance2")!!
    val distance2 = MaxbotixDistanceSensor(rawDistance2)
    private val rawDistance3 = hm.get(AnalogInput::class.java, "distance3")!!
    val distance3 = MaxbotixDistanceSensor(rawDistance3)
    private val rawDistance4 = hm.get(AnalogInput::class.java, "distance4")!!
    val distance4 = MaxbotixDistanceSensor(rawDistance4)

    val imu = hm.get(BNO055IMU::class.java, "imu")!!
}
