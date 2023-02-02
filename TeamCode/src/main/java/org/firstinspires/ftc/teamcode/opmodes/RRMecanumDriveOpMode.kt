package org.firstinspires.ftc.teamcode.opmodes

import com.acmerobotics.roadrunner.geometry.Pose2d
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import org.firstinspires.ftc.teamcode.autos.RRAuto
import org.firstinspires.ftc.teamcode.drive.SampleMecanumDrive
import org.firstinspires.ftc.teamcode.hardware.Hardware
import org.firstinspires.ftc.teamcode.lib.LinearOpModeEx
import org.firstinspires.ftc.teamcode.subsytems.*
import kotlin.math.PI
import kotlin.math.pow

class RRMecanumDriveOpMode : LinearOpModeEx() {

    enum class Mode {
        Normal,
        Following
    }

    enum class Direction {
        Forward,
        Left,
        Right,
        Back,
    }


    @TeleOp
    class RRMecanumDrive : LinearOpMode() {
        override fun runOpMode() = RRMecanumDriveOpMode().runOpMode(this)
    }

    val drive by lazy { MecanumDrive(hardware) }
    val lift by lazy { Lift(hardware) }
    val claw by lazy { Claw(hardware) }
    lateinit var gyro: Gyro

    var mode = Mode.Normal
    var rrDrive: SampleMecanumDrive? = null

    override fun init() {
        super.init()
        gyro = Gyro(hardware, this, startAngle = RRAuto.finalHeading)
        gyro.waitForCalibration(debug = true)
    }

    fun runDirection(direction: Direction) {
        val targetAngle = when (direction) {
            Direction.Forward -> PI / 2.0
            Direction.Left -> PI
            Direction.Right -> 0.0
            Direction.Back -> -PI / 2.0
        }

        rrDrive = SampleMecanumDrive(this.hardwareMap)
        val heading = gyro.robotHeading
        val traj = rrDrive!!.trajectoryBuilder(Pose2d(0.0, 0.0, heading))
            .lineToLinearHeading(Pose2d(0.0, 0.0, targetAngle))
            .build()
        rrDrive!!.poseEstimate = Pose2d(0.0, 0.0, heading)
        rrDrive!!.followTrajectoryAsync(traj)
        mode = Mode.Following
    }

    fun runSpinBack(finalAngle: Double) {
        rrDrive = SampleMecanumDrive(this.hardwareMap)
        val traj = rrDrive!!.trajectoryBuilder(Pose2d(0.0, 0.0, PI / 2))
            .back(15.0)
            .lineToSplineHeading(Pose2d(0.0, -35.0, finalAngle))
            .build()
        rrDrive!!.poseEstimate = Pose2d(0.0, 0.0, PI / 2)
        rrDrive!!.followTrajectoryAsync(traj)
        mode = Mode.Following
    }

    override fun loop() {
        when (mode) {
            Mode.Normal -> {
                var b = true
                if (gamepad1.rightBumper.isDown) {
                    b = false
                    when {
                        gamepad1.dpadUp.isDown -> runDirection(Direction.Forward)
                        gamepad1.dpadLeft.isDown -> runDirection(Direction.Left)
                        gamepad1.dpadRight.isDown -> runDirection(Direction.Right)
                        gamepad1.dpadDown.isDown -> runDirection(Direction.Back)
                        gamepad1.x.isDown -> runSpinBack(PI)
                        gamepad1.b.isDown -> runSpinBack(0.0)
                        else -> b = true
                    }
                }

                /*
                x - rotate left
                b - rotate right
                left trigger - forward slow
                right trigger - backward slow
                dpad - move that direction
                 */

                if (b) {
                    when {
                        gamepad1.x.isDown -> drive.drive(0.0, 0.0, -0.25)
                        gamepad1.b.isDown -> drive.drive(0.0, 0.0, 0.25)
                        gamepad1.leftTrigger != 0.0 -> drive.drive(
                            PI / 2,
                            gamepad1.leftTrigger * 0.5,
                            0.0
                        )
                        gamepad1.rightTrigger != 0.0 -> drive.drive(
                            PI / 2,
                            gamepad1.rightTrigger * -0.5,
                            0.0
                        )
                        gamepad1.dpadUp.isDown -> drive.drive(PI / 2, 0.5, 0.0)
                        gamepad1.dpadLeft.isDown -> drive.drive(PI, 0.5, 0.0)
                        gamepad1.dpadDown.isDown -> drive.drive(3 * PI / 2, 0.5, 0.0)
                        gamepad1.dpadRight.isDown -> drive.drive(0.0, 0.5, 0.0)
                        else -> {
                            var speed =
                                1.0 - (if (gamepad1.leftBumper.isDown && gamepad1.rightBumper.isDown) 0.75 else 0.5)
                            speed *= if (lift.height == Lift.Height.HighJunction) 0.8 else 1.0
                            drive.drive(
                                gamepad1.leftStick.angle,
                                gamepad1.leftStick.dist.pow(3) * speed,
                                gamepad1.rightStick.x * speed
                            )
                        }
                    }
                }
            }
            Mode.Following -> {
                if (rrDrive?.isBusy == true && gamepad1.rightBumper.isDown) {
                    rrDrive?.update()
                } else {
                    mode = Mode.Normal
                    rrDrive = null
                }
            }
        }

        // Arm Stuff
        when {
            gamepad2.dpadDown.justPressed -> lift.height = Lift.Height.Down
            gamepad2.dpadRight.justPressed -> lift.height = Lift.Height.MediumJunction
            gamepad2.dpadLeft.justPressed -> lift.height = Lift.Height.LowJunction
            gamepad2.dpadUp.justPressed -> lift.height = Lift.Height.HighJunction
            gamepad2.a.justPressed -> lift.height = Lift.Height.TwoCone
            gamepad2.x.justPressed -> lift.height = Lift.Height.ThreeCone
            gamepad2.b.justPressed -> lift.height = Lift.Height.FourCone
            gamepad2.y.justPressed -> lift.height = Lift.Height.FiveCone
        }
        lift.offset += gamepad2.leftStick.y * 8
        telemetry.addData("offset", lift.offset)
        lift.tick()

        if (gamepad2.rightBumper.justPressed) {
            claw.toggle()
        }

        telemetry.addData("reset", lift.reset.isPressed)
        if (gamepad2.leftBumper.justPressed) {
            lift.reset()
        }
    }

}
