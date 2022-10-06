package org.firstinspires.ftc.teamcode

import com.qualcomm.robotcore.hardware.Servo
import org.firstinspires.ftc.teamcode.lib.Subsystem

class Claw(hardware: Hardware) : Subsystem {

    private val leftClaw : Servo = hardware.leftClaw
    private val rightClaw : Servo = hardware.rightClaw

    fun openClaw() {
        leftClaw.position = 0.6
        rightClaw.position = 0.3
    }

    fun closeClaw() {
        leftClaw.position = 0.8
        rightClaw.position = 0.1
    }


}