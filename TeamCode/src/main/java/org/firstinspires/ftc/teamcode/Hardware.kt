package org.firstinspires.ftc.teamcode

import com.qualcomm.robotcore.hardware.AnalogInput
import com.qualcomm.robotcore.hardware.ColorSensor
import com.qualcomm.robotcore.hardware.DcMotorEx
import com.qualcomm.robotcore.hardware.HardwareMap
import com.qualcomm.robotcore.hardware.Servo
import org.firstinspires.ftc.teamcode.hardware.MaxbotixDistanceSensor

class Hardware(hm: HardwareMap) {

    val leftFront = hm.get(DcMotorEx::class.java, "leftFront")!!
    val leftBack = hm.get(DcMotorEx::class.java, "leftBack")!!
    val rightFront = hm.get(DcMotorEx::class.java, "rightFront")!!
    val rightBack = hm.get(DcMotorEx::class.java, "rightBack")!!

    val leftColor = hm.get(ColorSensor::class.java, "leftColor")!!
    val rightColor = hm.get(ColorSensor::class.java, "rightColor")!!

    private val rawDistance = hm.get(AnalogInput::class.java, "distance")!!
    val distance = MaxbotixDistanceSensor(rawDistance)

    val leftClaw = hm.get(Servo::class.java, "leftClaw")!!
    val rightClaw = hm.get(Servo::class.java, "rightClaw")!!
}