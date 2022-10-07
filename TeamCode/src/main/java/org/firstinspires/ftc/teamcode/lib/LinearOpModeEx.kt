package org.firstinspires.ftc.teamcode.lib

import com.qualcomm.robotcore.eventloop.opmode.*
import org.firstinspires.ftc.robotcore.external.Telemetry
import org.firstinspires.ftc.teamcode.Hardware
import org.reflections.Reflections
import java.lang.reflect.Constructor
import org.reflections.scanners.Scanners.*


class NonSubsystemUseSubsystemException :
    Exception("Only fields extending Subsystem may be annotated with @UseSubsystem.")

open class LinearOpModeEx {
    lateinit var opMode: LinearOpMode

    private fun innerInit(opMode: LinearOpMode) {
        this.opMode = opMode
    }

    private val hardwareCache: MutableMap<Class<*>, HardwareBase> = mutableMapOf()

    private fun getHardware(clazz: Class<*>): Hardware {
        if (!hardwareCache.containsKey(clazz)) {
            val constructor = clazz.getConstructor(LinearOpMode::class.java)
            val newHardware = constructor.newInstance(opMode)
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

                field.set(opMode, newSubsystem)
            }
    }

    val gamepad1: GamepadEx by lazy { GamepadEx(opMode.gamepad1) }
    val gamepad2: GamepadEx by lazy { GamepadEx(opMode.gamepad2) }

    fun waitForStart() {
        opMode.waitForStart()
    }

    fun idle() {
        opMode.idle()
    }

    fun sleep(milliseconds: Long) {
        opMode.sleep(milliseconds)
    }

    val isActive: Boolean get() = opMode.opModeIsActive()
    val isInInit: Boolean get() = opMode.opModeInInit()
    val isStarted: Boolean get() = opMode.isStarted
    val isStopRequested: Boolean get() = opMode.isStopRequested

    val runtime: Double get() = opMode.runtime

    val telemetry: Telemetry get() = opMode.telemetry

    private fun innerRunOpMode() {
        initSubsystems()

        init()

        waitForStart()

        while (isActive) {
            gamepad1.tick()
            gamepad2.tick()
            loop()
            opMode.telemetry.update()
        }
    }

    open fun init() {}

    open fun loop() {}

    companion object {
        @OpModeRegistrar
        @JvmStatic
        private fun register(manager: OpModeManager) {
            val reflections = Reflections("org.firstinspires.ftc.teamcode")

            reflections.get(TypesAnnotated.with(TeleOp::class.java).`as`(Class::class.java))
                .filter { clazz -> clazz.isAnnotationPresent(TeleOp::class.java) }
                .forEach { clazz ->
                    val teleOpAnnotation = clazz.getAnnotation(TeleOp::class.java)!!
                    val name = teleOpAnnotation.name.ifEmpty { clazz.name }
                    val opMode = object : LinearOpMode() {
                        override fun runOpMode() {
                            val ex = clazz.getConstructor().newInstance() as LinearOpModeEx
                            ex.innerInit(this)
                            ex.innerRunOpMode()
                        }
                    }
                    manager.register(name, opMode)
                }
        }
    }
}