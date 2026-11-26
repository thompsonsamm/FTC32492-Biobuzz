package org.firstinspires.ftc.teamcode.pedro;

import com.pedropathing.algorithm.Foresight;
import com.pedropathing.algorithm.ForesightConfig;
import com.pedropathing.controllers.Controller;
import com.pedropathing.follower.Follower;
import com.pedropathing.math.Matrix;
import com.pedropathing.math.Vector2D;
import com.pedropathing.revhub.drivetrains.Mecanum;
import com.pedropathing.revhub.drivetrains.MecanumConfig;
import com.pedropathing.revhub.localizers.PinpointConfig;
import com.pedropathing.revhub.localizers.PinpointLocalizer;
import com.qualcomm.hardware.gobilda.GoBildaPinpointDriver;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

public class Constants {
    public static MecanumConfig drivetrainConfig = new MecanumConfig(c -> {
        c.frontLeftName.set("left_front_drive");
        c.frontRightName.set("right_front_drive");
        c.backLeftName.set("left_back_drive");
        c.backRightName.set("right_back_drive");
        c.frontLeftDirection.set(DcMotorSimple.Direction.REVERSE);
        c.frontRightDirection.set(DcMotorSimple.Direction.FORWARD);
        c.backLeftDirection.set(DcMotorSimple.Direction.REVERSE);
        c.backRightDirection.set(DcMotorSimple.Direction.FORWARD);
    });
    public static PinpointConfig localizerConfig = new PinpointConfig(c -> {
        c.name.set("pinpoint");
        c.podType.set(GoBildaPinpointDriver.GoBildaOdometryPods.goBILDA_4_BAR_POD);
        c.xPodOffset.set(-5.448130810354638);
        c.yPodOffset.set(-2.289223558320774);
        c.xPodDirection.set(GoBildaPinpointDriver.EncoderDirection.REVERSED);
        c.yPodDirection.set(GoBildaPinpointDriver.EncoderDirection.REVERSED);
        c.globalDistanceUnit.set(DistanceUnit.INCH);
        c.offsetUnits.set(DistanceUnit.INCH);
    });

   // TODO: foresight tuning
   public static ForesightConfig foresightConfig = new ForesightConfig(
           c -> {
               Controller primaryTranslationalForward = Controller.proportional(0.2179241750794978);
               Controller secondaryTranslationalForward = Controller.proportional(0.08051715965067183);
               Controller primaryTranslationalLateral = Controller.proportional(0.2465611570500122);
               Controller secondaryTranslationalLateral = Controller.proportional(0.09109775929452585);

               c.forwardTranslational.set(Controller.piecewise(secondaryTranslationalForward).put(2.5, primaryTranslationalForward));
               c.strafeTranslational.set(Controller.piecewise(secondaryTranslationalLateral).put(2.5, primaryTranslationalLateral));

               c.coast.set(Controller.proportionalFeedforward(0.016264346322485117));
               c.brake.set(Controller.proportionalFeedforward(0.013824694374112349));

               c.headingFeedback.set(Controller.proportional(3.5823506639406917));
               c.headingBrakeCoefficients.set(Vector2D.cartesian(0.05092790129301398, 0.003725028431778645));

               c.linearBrakeCoefficients.set(Matrix.diag(0.08285280685815237, 0.06543707374923509));
               c.quadraticBrakeCoefficients.set(Matrix.diag(9.722521892728326E-4, 0.0011219885569959615));

               c.maxAchievableForwardVelocity.set(64.70596936912833);
               c.maxAchievableStrafeVelocity.set(52.62494487062048);
               c.naturalForwardDeceleration.set(29.166485494639264);
               c.naturalStrafeDeceleration.set(53.61481214235504);
           }
   );

    public static Follower create(HardwareMap h) {
        return new Follower(
                new PinpointLocalizer(h, localizerConfig),
                new Mecanum(h, drivetrainConfig),
                new Foresight(foresightConfig)
        );
        //return null;
    }
}