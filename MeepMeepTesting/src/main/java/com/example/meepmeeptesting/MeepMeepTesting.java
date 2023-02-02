package com.example.meepmeeptesting;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.acmerobotics.roadrunner.profile.VelocityConstraint;
import com.acmerobotics.roadrunner.trajectory.TrajectoryBuilder;
import com.noahbres.meepmeep.MeepMeep;
import com.noahbres.meepmeep.core.colorscheme.scheme.ColorSchemeBlueDark;
import com.noahbres.meepmeep.core.colorscheme.scheme.ColorSchemeRedDark;
import com.noahbres.meepmeep.roadrunner.DefaultBotBuilder;
import com.noahbres.meepmeep.roadrunner.entity.RoadRunnerBotEntity;

public class MeepMeepTesting {
    public static void main(String[] args) {
        System.setProperty("sun.java2d.opengl", "true");

        MeepMeep meepMeep = new MeepMeep(800);

        Pose2d STACK = new Pose2d(-59, -12, Math.PI);
        Pose2d STACK_MIDPOINT = new Pose2d(-40, -12, Math.PI);
        Pose2d MEDIUM_JUNCTION = new Pose2d(-24, -12, -Math.PI / 2);

        // Declare our first bot
        RoadRunnerBotEntity myFirstBot = new DefaultBotBuilder(meepMeep)
                // We set this bot to be blue
                .setColorScheme(new ColorSchemeRedDark())
                .setDimensions(12.5, 13.23)
                .setConstraints(30, 30, Math.toRadians(120), Math.toRadians(120), 12.43)
                .followTrajectorySequence(drive ->
                    drive.trajectorySequenceBuilder(new Pose2d(-35, -62.5, Math.PI / 2))
                            // Drop preload
                            .splineToConstantHeading(new Vector2d(-12, -53), Math.PI / 2)
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
                            .build()
                );

        meepMeep.setBackground(MeepMeep.Background.FIELD_POWERPLAY_KAI_DARK)
                .setDarkMode(true)
                .setBackgroundAlpha(0.95f)

                // Add both of our declared bot entities
                .addEntity(myFirstBot)
                .start();
    }
}