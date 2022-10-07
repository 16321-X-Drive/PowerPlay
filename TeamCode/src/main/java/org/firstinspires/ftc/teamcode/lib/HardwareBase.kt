package org.firstinspires.ftc.teamcode.lib

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode

open class HardwareBase(private val opMode: LinearOpMode) {
    init {
        val fields = this::class.java.declaredFields
        fields.toList().forEach { field ->
            val annotation = field.getAnnotation(UseDevice::class.java) ?: return@forEach
            val name = annotation.name.ifEmpty { field.name }
            field.set(this, opMode.hardwareMap.get(field.type, name))
            println("field `${field.name}` as `$name` w/ type ${field.type}")
        }
    }
}
