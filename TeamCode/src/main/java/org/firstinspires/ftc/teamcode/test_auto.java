package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;
import static com.qualcomm.robotcore.hardware.DcMotor.ZeroPowerBehavior.BRAKE;

import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.PIDFCoefficients;


@SuppressWarnings("unused")
@Autonomous
public class test_auto extends LinearOpMode {

    private DcMotor leftFrontDrive = null;
    private DcMotor leftBackDrive = null;
    private DcMotor rightFrontDrive = null;
    private DcMotor rightBackDrive = null;

    public final int LAUNCHER_TARGET_VELOCITY = 1250; //2678 RPM
    public final int LAUNCHER_MIN_VELOCITY = 1200; //2571 RPM

    double leftFrontPower;
    double rightFrontPower;
    double leftBackPower;
    double rightBackPower;
    /**
     * This autonomous code will have the robot drive away from the wall then
     * launch preloaded pollen. An optional delay is available if your team chooses
     * to start on the far side!
     */
    @Override
    public void runOpMode() {
        ElapsedTime autoTimer;

        leftFrontDrive = hardwareMap.get(DcMotor.class, "left_front_drive");
        rightFrontDrive = hardwareMap.get(DcMotor.class, "right_front_drive");
        leftBackDrive = hardwareMap.get(DcMotor.class, "left_back_drive");
        rightBackDrive = hardwareMap.get(DcMotor.class, "right_back_drive");
        DcMotor intake = hardwareMap.get(DcMotor.class, "intake");
        DcMotorEx launcher = hardwareMap.get(DcMotorEx.class, "launcher");
        CRServo windmillServo = hardwareMap.get(CRServo.class, "windmillServo");
        CRServo leftIntakeServo = hardwareMap.get(CRServo.class, "left_intake_servo");
        CRServo rightIntakeServo = hardwareMap.get(CRServo.class, "right_intake_servo");


        // Put initialization blocks here.
        leftFrontDrive.setDirection(DcMotor.Direction.REVERSE);
        rightFrontDrive.setDirection(DcMotor.Direction.FORWARD);
        leftBackDrive.setDirection(DcMotor.Direction.REVERSE);
        rightBackDrive.setDirection(DcMotor.Direction.FORWARD);
        /*
         * Setting zeroPowerBehavior to BRAKE enables a "brake mode". This causes the motor to
         * slow down much faster when it is coasting. This creates a much more controllable
         * drivetrain. As the robot stops much quicker.
         */
        leftFrontDrive.setZeroPowerBehavior(BRAKE);
        rightFrontDrive.setZeroPowerBehavior(BRAKE);
        leftBackDrive.setZeroPowerBehavior(BRAKE);
        rightBackDrive.setZeroPowerBehavior(BRAKE);
        intake.setZeroPowerBehavior(BRAKE);

        // * Here we set our launcher to the RUN_USING_ENCODER runmode.
        // * If you notice that you have no control over the velocity of the motor, it just jumps
        // * right to a number much higher than your set point, make sure that your encoders are plugged
        // * into the port right beside the motor itself. And that the motors polarity is consistent
        // * through any wiring.

        launcher.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        launcher.setPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER, new PIDFCoefficients(40, 0, 0, 12.5));

        /*
         * set Feeders to an initial value to initialize the servo controller
         */
        leftIntakeServo.setPower(0);
        rightIntakeServo.setPower(0);
        windmillServo.setPower(0);

        /*
         * Much like our drivetrain motors, we set the left intake servo to reverse so that both
         * servos work to pull elements into the intake.
         */
        leftIntakeServo.setDirection(DcMotorSimple.Direction.REVERSE);
        autoTimer = new ElapsedTime();
        waitForStart();
        if (opModeIsActive()) {
            // Enable the sleep call below (Line 51) to add a delay to the program starting
            // The default delay is 10 seconds so be sure to adjust based on your strategy!
            //sleep(10000);

            // Robot drives forward 700 milliseconds to no longer touch the wall
            autoTimer.reset();
            while (autoTimer.milliseconds() <= 700 && opModeIsActive()) {
                mecanumDrive(0.25, 0, 0);
            }

            // Turn off drivetrain motors to prevent extra movement
            mecanumDrive(0, 0, 0);

            // Flywheel begins launch sequence for the preloaded pollen
            autoTimer.reset();

            // The flywheel is set to spin for 15 seconds.
            while (autoTimer.seconds() <= 15 && opModeIsActive()) {
                launcher.setVelocity(LAUNCHER_TARGET_VELOCITY);

                // The flywheel must be within a specified range of the target velocity for the intake and feeder servo to activate!
                if (launcher.getVelocity() > LAUNCHER_MIN_VELOCITY){
                    windmillServo.setPower(1);
                    intake.setPower(0.5);
                } else {

                    // If the velocity is not within range, the intake and feeder servo turn off to prevent early feeding
                    windmillServo.setPower(0);
                    intake.setPower(0);
                }
            }
        }
    }

    @SuppressWarnings("SameParameterValue")
    void mecanumDrive(double forward, double strafe, double rotate) {
        leftFrontPower = forward + strafe + rotate;
        rightFrontPower = forward - strafe - rotate;
        leftBackPower = forward - strafe + rotate;
        rightBackPower = forward + strafe - rotate;

        double max = Math.max(
                Math.max(Math.abs(leftFrontPower), Math.abs(rightFrontPower)),
                Math.max(Math.abs(leftBackPower), Math.abs(rightBackPower))
        );

        if (max > 1.0){
            leftFrontPower /= max;
            rightFrontPower /= max;
            leftBackPower /= max;
            rightBackPower /= max;
        }

        /*
         * Send calculated power to wheels
         */
        double scale = .8;
        leftFrontDrive.setPower(leftFrontPower * scale);
        rightFrontDrive.setPower(rightFrontPower * scale);
        leftBackDrive.setPower(leftBackPower * scale);
        rightBackDrive.setPower(rightBackPower * scale);
    }
}
