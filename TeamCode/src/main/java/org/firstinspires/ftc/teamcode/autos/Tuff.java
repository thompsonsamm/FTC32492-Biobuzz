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

import org.firstinspires.ftc.teamcode.pedro.Constants;

@Autonomous(name = "Tuff", group = "Autonomous")
public class Tuff extends LinearOpMode {

    private Follower follower;

    private final PoseFactory poseFactory = PoseFactory.degrees();

    private final Pose start = poseFactory.of(82.75, 130.75, 90);
    private final Pose moveToPickup = poseFactory.of(130.75, 130.75, 90);
    private final Pose moveToPickupControl1 = poseFactory.of(94.75, 112.75, 0);
    private final Pose moveToPickupControl2 = poseFactory.of(127.1635, 97.4086, 0);
    private final Pose moveToLZ = poseFactory.of(124.75, 40.75, 90);
    private final Pose moveToLZControl1 = poseFactory.of(109.75, 85.75, 0);

    // Autonomous routine
    public Command autoRoutine() {
        return sequential(
                follow(follower, moveToPickup()),
                follow(follower, moveToLZ())
        );
    }

    @Override
    public void runOpMode() {
        Scheduler.reset();
        follower = Constants.create(hardwareMap);
        follower.setPose(start);
        follower.update();

        waitForStart();
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

    public Path moveToPickup() {
        return Paths.curve(start, moveToPickupControl1, moveToPickupControl2, moveToPickup).constant(moveToPickup);
    }

    public Path moveToLZ() {
        return Paths.curve(moveToPickup, moveToLZControl1, moveToLZ).constant(moveToLZ);
    }
}
