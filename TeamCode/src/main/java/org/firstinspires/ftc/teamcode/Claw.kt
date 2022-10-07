package org.firstinspires.ftc.teamcode

import com.qualcomm.robotcore.hardware.Servo
import org.firstinspires.ftc.teamcode.lib.Subsystem

class Claw(hardware: Hardware) : Subsystem {

    private val leftClaw: Servo = hardware.leftClaw
    private val rightClaw: Servo = hardware.rightClaw

    var isOpen = false
        set(value) {
            field = value
            if (field) {
                leftClaw.position = 0.6
                rightClaw.position = 0.3
            } else {
                leftClaw.position = 0.8
                rightClaw.position = 0.1
            }
        }

    fun toggle() {
        isOpen = !isOpen
    }

}