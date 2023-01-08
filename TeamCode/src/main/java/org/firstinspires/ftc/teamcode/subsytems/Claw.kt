package org.firstinspires.ftc.teamcode.subsytems

import com.qualcomm.robotcore.hardware.Servo
import org.firstinspires.ftc.teamcode.hardware.Hardware

class Claw(hardware: Hardware) {

    private val claw: Servo = hardware.claw

    var isOpen = false
        set(value) {
            field = value
            if (field) {
                claw.position = 0.25
            } else {
                claw.position = 0.35
            }
        }

    fun toggle() {
        isOpen = !isOpen
    }

}