package org.firstinspires.ftc.teamcode.autos;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.pedropathing.follower.Follower;

import org.firstinspires.ftc.teamcode.pedro.Constants;
import com.pedropathing.api.PoseFactory;
import com.pedropathing.math.Pose;

@Autonomous
public class PathingAuto extends OpMode {
    private Follower follower;
    private final PoseFactory p = PoseFactory.degrees();
    @Override
    public void init() {
        follower = Constants.create(hardwareMap);
    }

    @Override
    public void start() {

    }

    @Override
    public void loop() {}
}
