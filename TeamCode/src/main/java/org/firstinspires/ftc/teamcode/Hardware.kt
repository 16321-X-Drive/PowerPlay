package org.firstinspires.ftc.teamcode

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.hardware.AnalogInput
import com.qualcomm.robotcore.hardware.AnalogSensor
import com.qualcomm.robotcore.hardware.ColorSensor
import com.qualcomm.robotcore.hardware.DcMotorEx
import com.qualcomm.robotcore.hardware.Servo
import org.firstinspires.ftc.teamcode.hardware.MaxbotixDistanceSensor
import org.firstinspires.ftc.teamcode.lib.UseDevice
import org.firstinspires.ftc.teamcode.lib.HardwareBase

class Hardware(opMode: LinearOpMode) : HardwareBase(opMode) {

    @UseDevice
    lateinit var leftFront: DcMotorEx

    @UseDevice
    lateinit var leftBack: DcMotorEx

    @UseDevice
    lateinit var rightFront: DcMotorEx

    @UseDevice
    lateinit var rightBack: DcMotorEx

    @UseDevice
    lateinit var leftColor: ColorSensor

    @UseDevice
    lateinit var rightColor: ColorSensor

    @UseDevice("distance")
    lateinit var rawDistance: AnalogInput

    val distance by lazy { MaxbotixDistanceSensor(rawDistance) }

    @UseDevice
    lateinit var leftClaw: Servo

    @UseDevice
    lateinit var rightClaw: Servo

}