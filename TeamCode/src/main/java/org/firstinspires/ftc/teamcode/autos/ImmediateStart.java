package org.firstinspires.ftc.teamcode.autos;

import static com.pedropathing.api.Paths.*;
import com.pedropathing.api.Paths;

import com.pedropathing.api.PoseFactory;
import com.pedropathing.follower.Follower;
import com.pedropathing.math.Pose;
import com.pedropathing.paths.Path;
import com.pedropathing.ivy.Command;
import com.pedropathing.ivy.Scheduler;
import static com.pedropathing.ivy.Scheduler.schedule;
import static com.pedropathing.ivy.commands.Commands.*;
import static com.pedropathing.ivy.groups.Groups.sequential;
import static com.pedropathing.ivy.pedro.PedroCommands.follow;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.pedro.Constants;
import org.firstinspires.ftc.teamcode.subsystems.Launcher;

@Autonomous(name = "ImmediateStart", group = "Autonomous")
public class ImmediateStart extends LinearOpMode {

    private Follower follower;

    private final PoseFactory poseFactory = PoseFactory.degrees();


    private final Pose start = poseFactory.of(82.75, 130.75, 90);
    private final Pose moveToLZ = poseFactory.of(124.75, 40.75, 90);
    private final Pose moveToLZControl1 = poseFactory.of(124.75, 130.75, 0);


    // Autonomous routine
    public Command autoRoutine() {
        return sequential(
                follow(follower, moveToLZ())
        );
    }

    @Override
    public void runOpMode() {
        Scheduler.reset();
        follower = Constants.create(hardwareMap);
        assert follower != null: "lowk just try running this again (if this is still showing tell the coders they done f'd up)";
        follower.setPose(start);
        follower.update();

        Launcher launcher = new Launcher();
        ElapsedTime elapsedTime = new ElapsedTime();

        waitForStart();

        // shoot
        if (opModeIsActive()) {
            launcher.spinUp();
            elapsedTime.reset();
            while (elapsedTime.seconds() <= 15 && opModeIsActive()) {
                launcher.shoot();
            }
            launcher.spinDown();
        }

        schedule(autoRoutine());

        while (opModeIsActive()) {
            follower.update();
            Scheduler.execute();

            telemetry.addData("x", follower.pose().x());
            telemetry.addData("y", follower.pose().y());
            telemetry.addData("heading", follower.pose().heading());

            if (follower.currentPath() != null) {
                telemetry.addData("Current path distance remaining", follower.distanceToEndpoint());
                telemetry.addData("Path number", follower.pathIndex());
            }

            telemetry.update();
        }
    }

    public Path moveToLZ() {
        return Paths.curve(start, moveToLZControl1, moveToLZ).constant(moveToLZ);
    }
}