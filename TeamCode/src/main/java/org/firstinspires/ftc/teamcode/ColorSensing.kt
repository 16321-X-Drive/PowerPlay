package org.firstinspires.ftc.teamcode

data class Color(val red: Int, val green: Int, val blue: Int) {

    fun dominantColor(): String = when {
        red > green && red > blue -> "red"
        green > red && green > blue -> "green"
        else -> "blue"
    }

}

class ColorSensing(hardware: Hardware) {

    private val leftColor = hardware.leftColor
    private val rightColor = hardware.rightColor

    fun readLeftColor(): Color =
        Color(
            leftColor.red(),
            leftColor.green(),
            leftColor.blue()
        )

    fun readRightColor(): Color =
        Color(
            rightColor.red(),
            rightColor.green(),
            rightColor.blue()
        )

}