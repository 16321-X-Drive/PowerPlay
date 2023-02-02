package org.firstinspires.ftc.teamcode.autos

import com.acmerobotics.roadrunner.geometry.Pose2d
import com.acmerobotics.roadrunner.geometry.Vector2d
import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import org.firstinspires.ftc.teamcode.drive.SampleMecanumDrive
import org.firstinspires.ftc.teamcode.lib.LinearOpModeEx
import org.firstinspires.ftc.teamcode.subsytems.*
import org.firstinspires.ftc.teamcode.trajectorysequence.TrajectorySequence
import org.opencv.core.Rect

class RRAuto(private val d: Double) : LinearOpModeEx() {

    companion object {
        @JvmStatic
        var finalHeading: Double = 0.0
    }

    @Autonomous(preselectTeleOp = "RRMecanumDrive")
    class RRLeft : LinearOpMode() {
        override fun runOpMode() = RRAuto(-1.0).runOpMode(this)
    }

    @Autonomous(preselectTeleOp = "RRMecanumDrive")
    class RRRight : LinearOpMode() {
        override fun runOpMode() = RRAuto(1.0).runOpMode(this)
    }

    fun region() = Rect(140, 150, 30, 15)
    override fun isAuto() = true

    val gyro: Gyro by lazy { Gyro(hardware, this, 0.0) }
    val drive: SampleMecanumDrive by lazy { SampleMecanumDrive(hardwareMap) }
    val claw: Claw by lazy { Claw(hardware) }
    val lift: Lift by lazy { Lift(hardware) }
    val detector: CameraDetector by lazy { CameraDetector(hardware, region()) }

    lateinit var TRAJ_AWAY: TrajectorySequence
    lateinit var TRAJ_MIDDLE: TrajectorySequence
    lateinit var TRAJ_WALL: TrajectorySequence

    val HEIGHT = -9.5

    val STACK = Pose2d(d * 57.0, HEIGHT, Math.PI)
    val STACK_MIDPOINT = Pose2d(d * 45.0, HEIGHT, Math.PI)
    val MEDIUM_JUNCTION = Pose2d(d * 22.0, HEIGHT, -Math.PI / 2)
    val START = Pose2d(d * 26.0, -62.5, Math.PI / 2)

    override fun init() {
        telemetry.addLine("init")
        telemetry.update()
        gyro.waitForCalibration(debug = false)
        detector.init()

        for (decision in Decision.values()) {
            val traj = drive.trajectorySequenceBuilder(START) // Drop preload
                .splineToConstantHeading(Vector2d(d * 6.0, -56.0), Math.PI / 2)
                .splineToConstantHeading(Vector2d(d * 6.0, -25.0), Math.PI / 2)
                .splineToConstantHeading(Vector2d(d * 22.0, HEIGHT), Math.PI)
                .addTemporalMarker {
                    lift.height = Lift.Height.HighJunction
                }
                .waitSeconds(2.0)
                .forward(3.0)
                .addTemporalMarker {
                    lift.offset = -600.0
                }
                .waitSeconds(0.5)
                .addTemporalMarker {
                    claw.isOpen = true
                }
                .waitSeconds(1.0)
                .addTemporalMarker {
                    lift.height = Lift.Height.FiveCone
                }
                .back(3.0)
                // Grab Second Cone
                .lineToSplineHeading(STACK_MIDPOINT)
                .lineToSplineHeading(STACK)
                .addTemporalMarker {
                    claw.isOpen = false
                }
                .waitSeconds(0.5)
                .addTemporalMarker {
                    lift.height = Lift.Height.MediumJunction
                }
                .waitSeconds(1.25)
                .back(15.0)
                // Drop Second Cone
                .lineToSplineHeading(MEDIUM_JUNCTION)
                .forward(3.0)
                .addTemporalMarker {
                    lift.offset = -600.0
                }
                .waitSeconds(0.5)
                .addTemporalMarker {
                    claw.isOpen = true
                }
                .waitSeconds(1.0)
                .addTemporalMarker {
                    lift.height = Lift.Height.FourCone
                }
                .back(3.0)
                // Grab Third Cone
                .lineToSplineHeading(STACK_MIDPOINT)
                .lineToSplineHeading(STACK)
                .addTemporalMarker {
                    claw.isOpen = false
                }
                .waitSeconds(0.5)
                .addTemporalMarker {
                    lift.height = Lift.Height.MediumJunction
                }
                .waitSeconds(1.25)
                .back(15.0)
                // Drop Third Cone
                .lineToSplineHeading(MEDIUM_JUNCTION)
                .forward(3.0)
                .addTemporalMarker {
                    lift.offset = -600.0
                }
                .waitSeconds(0.5)
                .addTemporalMarker {
                    claw.isOpen = true
                }
                .waitSeconds(1.0)
                .back(3.0)

            // Park
            when (decision to d) {
                Decision.Green to -1.0, Decision.Red to 1.0 -> {
                    TRAJ_AWAY = traj.lineToLinearHeading(Pose2d(d * 10.0, HEIGHT, Math.PI)).build()
                }
                Decision.Blue to -1.0, Decision.Blue to 1.0 -> {
                    TRAJ_MIDDLE = traj.lineToLinearHeading(Pose2d(d * 10.0, HEIGHT, Math.PI)).build()
                }
                else -> {
                    TRAJ_WALL = traj.lineToLinearHeading(Pose2d(d * 10.0, HEIGHT, Math.PI)).build()
                }
            }

        }

        telemetry.addLine("done init")
        telemetry.update()
    }

    override fun once() {
        val decision = detector.readDecision()

        telemetry.addData("decision", decision)
        telemetry.update()

        claw.isOpen = false // grab claw

        drive.poseEstimate = START
        drive.followTrajectorySequence(
            when (decision to d) {
                Decision.Green to -1.0, Decision.Red to 1.0 -> TRAJ_AWAY
                Decision.Blue to -1.0, Decision.Blue to 1.0 -> TRAJ_MIDDLE
                else -> TRAJ_WALL
            }
        )
        lift.height = Lift.Height.Down
        sleep(1000)

        finalHeading = drive.poseEstimate.heading
    }
}