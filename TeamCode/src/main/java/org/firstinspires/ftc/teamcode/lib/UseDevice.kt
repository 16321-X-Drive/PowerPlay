package org.firstinspires.ftc.teamcode.lib

@Target(AnnotationTarget.FIELD)
@Retention(AnnotationRetention.RUNTIME)
@MustBeDocumented
annotation class UseDevice(val name: String = "")