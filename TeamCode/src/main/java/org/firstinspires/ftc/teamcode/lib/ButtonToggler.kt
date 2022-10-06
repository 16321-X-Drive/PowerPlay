package org.firstinspires.ftc.teamcode.lib

class ButtonToggler {

    private var buttonFirstPressed : Boolean = true

    fun shouldToggle(buttonValue : Boolean) : Boolean {

        val returnBool : Boolean

        if(buttonValue && buttonFirstPressed) {
            returnBool = true
            buttonFirstPressed = false
            // This will run one time when the button is pressed
        } else if(!buttonValue) {
            buttonFirstPressed = true
            returnBool = false
            // This will run when the button is released
        } else {
            returnBool = false
            // This will run if the button is being held
        }

        return returnBool

    }

}