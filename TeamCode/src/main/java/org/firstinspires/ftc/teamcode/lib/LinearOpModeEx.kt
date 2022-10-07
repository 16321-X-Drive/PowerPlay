package org.firstinspires.ftc.teamcode.lib

import com.qualcomm.robotcore.eventloop.opmode.*
import com.qualcomm.robotcore.hardware.HardwareMap
import org.firstinspires.ftc.robotcore.external.Telemetry
import org.reflections.Reflections
import org.reflections.scanners.Scanners.*

open class LinearOpModeEx {
    lateinit var opMode: LinearOpMode

    private fun innerInit(opMode: LinearOpMode) {
        this.opMode = opMode
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
    val hardwareMap: HardwareMap get() = opMode.hardwareMap

    private fun innerRunOpMode() {
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