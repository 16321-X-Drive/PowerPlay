package org.firstinspires.ftc.teamcode.autos

import com.acmerobotics.dashboard.FtcDashboard
import com.acmerobotics.roadrunner.geometry.Pose2d
import com.acmerobotics.roadrunner.geometry.Vector2d
import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit
import org.firstinspires.ftc.teamcode.drive.SampleMecanumDrive
import org.firstinspires.ftc.teamcode.lib.LinearOpModeEx
import org.firstinspires.ftc.teamcode.subsytems.*
import org.firstinspires.ftc.teamcode.trajectorysequence.TrajectorySequence
import org.opencv.core.Rect
import kotlin.math.PI

class RRRightAuto : LinearOpModeEx() {

    @Autonomous(preselectTeleOp = PRESELECTED_TELEOP)
    class RRRight : LinearOpMode() {
        override fun runOpMode() = RRRightAuto().runOpMode(this)
    }

    override fun isAuto() = true
    
    val gyro: Gyro by lazy { Gyro(hardware, this, 0.0) }
    val drive: SampleMecanumDrive by lazy { SampleMecanumDrive(hardwareMap) }
    val claw: Claw by lazy { Claw(hardware) }
    val lift: Lift by lazy { Lift(hardware) }
    val detector: CameraDetector by lazy { CameraDetector(hardware, region) }

    private val region = Rect(140, 162, 30, 15)

    lateinit var traj1: TrajectorySequence
    lateinit var traj2: TrajectorySequence
    lateinit var traj3: TrajectorySequence
    lateinit var trajParkWall: TrajectorySequence
    lateinit var trajParkMiddle: TrajectorySequence
    lateinit var trajParkOut: TrajectorySequence

    val HEIGHT = -8.0

    val towardWall = 0.0

    val STACK = Pose2d(57.0, HEIGHT, towardWall)
    val STACK_MIDPOINT = Pose2d(45.0, HEIGHT, towardWall)
    val STACK_UP = Pose2d(57.0, HEIGHT, towardWall)
    val STACK_MIDPOINT_UP = Pose2d(45.0, HEIGHT, towardWall)
    val MEDIUM_JUNCTION = Pose2d(24.0, HEIGHT, -Math.PI / 2)
    val LOW_JUNCTION = Pose2d(50.0, HEIGHT, -Math.PI / 2)
    val START = Pose2d(26.0, -62.5, Math.PI / 2)

    override fun init() {
        telemetry.addLine("init")
        telemetry.update()
        gyro.waitForCalibration(debug = false)
        detector.init()

        traj1 = drive.trajectorySequenceBuilder(START) // Drop preload
            .splineToConstantHeading(Vector2d(5.0, -58.0), Math.PI / 2)
            .splineToConstantHeading(Vector2d(5.0, -25.0), Math.PI / 2)
            .splineToConstantHeading(Vector2d(14.0, HEIGHT - 3.0), Math.PI)
            .addTemporalMarker {
                lift.height = Lift.Height.HighJunction
            }
            .waitSeconds(2.0)
            .forward(2.0)
            .build()

        traj2 = drive.trajectorySequenceBuilder(traj1.end().copy(x = 20.0))
            .back(3.0)
            // Grab Second Cone
            .lineToSplineHeading(STACK_MIDPOINT_UP)
            .lineToSplineHeading(STACK_UP)
            .addTemporalMarker {
                claw.isOpen = false
            }
            .waitSeconds(0.5)
            .addTemporalMarker {
                lift.height = Lift.Height.MediumJunction
            }
            .waitSeconds(1.25)
            // Drop Second Cone
            .back(15.0)
            .lineToSplineHeading(MEDIUM_JUNCTION)
            .forward(3.0)
            .build()

        traj3 = drive.trajectorySequenceBuilder(traj2.end())
            .back(3.0)
            // Grab Third Cone
            .lineToSplineHeading(STACK_MIDPOINT)
            .lineToSplineHeading(STACK)
            .addTemporalMarker {
                claw.isOpen = false
            }
            .waitSeconds(0.5)
            .addTemporalMarker {
                lift.height = org.firstinspires.ftc.teamcode.subsytems.Lift.Height.LowJunction
            }
            .waitSeconds(1.25)
            // Drop Third Cone
            .back(5.0)
            .lineToSplineHeading(LOW_JUNCTION)
            .forward(3.0)
            .build()

        trajParkWall = drive.trajectorySequenceBuilder(traj3.end())
            .back(5.0)
            .lineToLinearHeading(Pose2d(traj3.end().vec(), towardWall))
            .lineToConstantHeading(Vector2d(55.0, HEIGHT))
            .build()

        trajParkMiddle = drive.trajectorySequenceBuilder(traj3.end())
            .back(5.0)
            .lineToLinearHeading(Pose2d(traj3.end().vec(), towardWall))
            .lineToConstantHeading(Vector2d(50.0, HEIGHT))
            .lineToConstantHeading(Vector2d(31.0, HEIGHT))
            .build()

        trajParkOut = drive.trajectorySequenceBuilder(traj3.end())
            .back(5.0)
            .lineToLinearHeading(Pose2d(traj3.end().vec(), towardWall))
            .lineToConstantHeading(Vector2d(50.0, HEIGHT))
            .lineToConstantHeading(Vector2d(10.0, HEIGHT))
            .build()

        telemetry.addLine("done init")
        telemetry.update()
    }

    fun moveToPole() {
        val dash = FtcDashboard.getInstance()

        drive.setMotorPowers(0.3, -0.3, 0.3, -0.3)
        while (hardware.poleSensor.getDistance(DistanceUnit.INCH) > 15) {
            dash.telemetry.addData("dist", hardware.poleSensor.getDistance(DistanceUnit.INCH))
            dash.telemetry.update()
            drive.update()
        }
        drive.setMotorPowers(0.0, 0.0, 0.0, 0.0)
        lift.offset = -100.0
        sleep(500)
        claw.isOpen = true
    }

    override fun once() {
        val decision = detector.readDecision()
        val finalTraj = when (decision to 1.0) {
            Decision.Red to -1.0 -> trajParkWall
            Decision.Blue to -1.0 -> trajParkMiddle
            Decision.Green to -1.0 -> trajParkOut
            Decision.Green to 1.0 -> trajParkWall
            Decision.Blue to 1.0 -> trajParkMiddle
            else -> trajParkOut
        }

        telemetry.addData("decision", decision)
        telemetry.update()

        claw.isOpen = false // grab claw

        drive.poseEstimate = START
        drive.followTrajectorySequence(traj1)
        moveToPole()
        lift.height = Lift.Height.FiveCone
        drive.followTrajectorySequence(traj2)
        moveToPole()
        lift.height = Lift.Height.FourCone
        drive.followTrajectorySequence(traj3)
        moveToPole()
        lift.height = Lift.Height.Down
        drive.followTrajectorySequence(finalTraj)

        sleep(1000)
        RRLeftAuto.finalHeading = drive.poseEstimate.heading
    }
}