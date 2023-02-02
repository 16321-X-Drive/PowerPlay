package org.firstinspires.ftc.teamcode.autos;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.acmerobotics.roadrunner.trajectory.Trajectory;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.drive.SampleMecanumDrive;
import org.firstinspires.ftc.teamcode.trajectorysequence.TrajectorySequence;

@Autonomous
@Disabled
public class RRTest extends LinearOpMode {
    @Override
    public void runOpMode() throws InterruptedException {
        SampleMecanumDrive drive = new SampleMecanumDrive(hardwareMap);

        waitForStart();

        if (isStopRequested()) return;

        Pose2d STACK = new Pose2d(-59, -12, Math.PI);
        Pose2d STACK_MIDPOINT = new Pose2d(-40, -12, Math.PI);
        Pose2d MEDIUM_JUNCTION = new Pose2d(-24, -12, -Math.PI / 2);

        TrajectorySequence traj = drive.trajectorySequenceBuilder(new Pose2d(-35, -62.5, Math.PI / 2))
                // Drop preload
                .splineToConstantHeading(new Vector2d(-20, -53), Math.PI / 2)
                .splineToConstantHeading(new Vector2d(-24, -12), Math.PI)
                .forward(3.0)
                .waitSeconds(1.25)
                .back(3.0)
                // Grab Second Cone
                .lineToSplineHeading(STACK_MIDPOINT)
                .lineToSplineHeading(STACK)
                .waitSeconds(1.25)
                .back(15.0)
                // Drop Second Cone
                .lineToSplineHeading(MEDIUM_JUNCTION)
                .forward(3.0)
                .waitSeconds(1.25)
                .back(3.0)
                // Grab Third Cone
                .lineToSplineHeading(STACK_MIDPOINT)
                .lineToSplineHeading(STACK)
                .waitSeconds(1.25)
                .back(15.0)
                // Drop Third Cone
                .lineToSplineHeading(MEDIUM_JUNCTION)
                .forward(3.0)
                .waitSeconds(1.25)
                .back(3.0)
//                            // Grab Fourth Cone
//                            .lineToSplineHeading(STACK_MIDPOINT)
//                            .lineToSplineHeading(STACK)
//                            .waitSeconds(1.25)
//                            .back(15.0)
//                            // Drop Fourth Cone
//                            .lineToSplineHeading(MEDIUM_JUNCTION)
//                            .forward(3.0)
//                            .waitSeconds(1.25)
//                            .back(3.0)
                // Park
                .lineToLinearHeading(new Pose2d(-12, -12, Math.PI))
                .build();

        drive.setPoseEstimate(new Pose2d(-35, -62.5, Math.PI / 2));
        drive.followTrajectorySequence(traj);
    }
}
