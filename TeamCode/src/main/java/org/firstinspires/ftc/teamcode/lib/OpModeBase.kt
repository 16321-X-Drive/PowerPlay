package org.firstinspires.ftc.teamcode.lib

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import org.firstinspires.ftc.teamcode.Hardware
import java.lang.reflect.Constructor

class NonSubsystemUseSubsystemException :
    Exception("Only fields extending Subsystem may be annotated with @UseSubsystem.")

open class OpModeBase : LinearOpMode() {
    private val hardwareCache: MutableMap<Class<*>, HardwareBase> = mutableMapOf()

    private fun getHardware(clazz: Class<*>): Hardware {
        if (!hardwareCache.containsKey(clazz)) {
            val constructor = clazz.getConstructor(LinearOpMode::class.java)
            val newHardware = constructor.newInstance(this)
            hardwareCache[clazz] = newHardware as HardwareBase
        }

        return hardwareCache[clazz] as Hardware
    }

    private fun <U> findValidConstructor(clazz: Class<U>): Constructor<U>? =
        clazz.constructors.find {
            it.parameterTypes.all { argType ->
                Hardware::class.java.isAssignableFrom(argType)
            }
        } as Constructor<U>?

    private fun initSubsystems() {
        this::class.java.fields
            .filter { it.getAnnotation(UseSubsystem::class.java) != null }
            .forEach { field ->
                if (!Subsystem::class.java.isAssignableFrom(field.type))
                    throw NonSubsystemUseSubsystemException()

                val constructor = findValidConstructor(field.type) ?: TODO()
                val args = constructor.parameterTypes.map { getHardware(it) }
                val newSubsystem = constructor.newInstance(*args.toTypedArray())

                field.set(this, newSubsystem)
            }
    }

    val gamepad1Ex = GamepadEx(gamepad1)
    val gamepad2Ex = GamepadEx(gamepad2)

    final override fun runOpMode() {
        initSubsystems()

        initialize()

        waitForStart()

        while (opModeIsActive()) {
            gamepad1Ex.tick()
            gamepad2Ex.tick()
            coreLoop()
            telemetry.update()
        }
    }

    open fun initialize() {}

    open fun coreLoop() {}
}