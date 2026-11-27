package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.util.ElapsedTime;
import static com.qualcomm.robotcore.hardware.DcMotor.ZeroPowerBehavior.BRAKE;

import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.PIDFCoefficients;

import org.firstinspires.ftc.teamcode.subsystems.Intake;
import org.firstinspires.ftc.teamcode.subsystems.Launcher;
import org.firstinspires.ftc.teamcode.subsystems.mecanumdrive;
import org.firstinspires.ftc.teamcode.subsystems.pinpointv2;


//@SuppessWarnings("unused")
@Autonomous
public class test_auto extends LinearOpMode {

    mecanumdrive drive = new mecanumdrive();
    pinpointv2 pinpoint = new pinpointv2();
    Launcher launcher = new Launcher();
    Intake intake = new Intake();
    /**
     * This autonomous code will have the robot drive away from the wall then
     * launch preloaded pollen. An optional delay is available if your team chooses
     * to start on the far side!
     */

    public void runOpMode() {
        ElapsedTime autoTimer;

        drive.init(hardwareMap);
        pinpoint.init(hardwareMap);
        intake.init(hardwareMap);
        launcher.init(hardwareMap);

        autoTimer = new ElapsedTime();
        waitForStart();
        if (opModeIsActive()) {
            // Enable the sleep call below (Line 51) to add a delay to the program starting
            // The default delay is 10 seconds so be sure to adjust based on your strategy!
            launcher.spinUp();

            // The flywheel is set to spin for 15 seconds.
            while (autoTimer.seconds() <= 15 && opModeIsActive()) {
                // The flywheel must be within a specified range of the target velocity for the intake and feeder servo to activate!
                launcher.shoot();
            }

            // Robot drives forward 700 milliseconds to no longer touch the wall
            autoTimer.reset();
            while (autoTimer.milliseconds() <= 700 && opModeIsActive()) {
                drive.mecanumDrive(-0.25, 0, 0);
            }
            }

            // Turn off drivetrain motors to prevent extra movement
            drive.mecanumDrive(0, 0, 0);

            // Flywheel begins launch sequence for the preloaded pollen
            autoTimer.reset();


        }
    }