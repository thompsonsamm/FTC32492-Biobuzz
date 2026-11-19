package org.firstinspires.ftc.teamcode;

import static com.qualcomm.robotcore.hardware.DcMotor.ZeroPowerBehavior.BRAKE;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.PIDFCoefficients;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.hardware.gobilda.GoBildaPinpointDriver;

public class mechanisms
{
    static private DcMotor leftFrontDrive = null;
    static private DcMotor leftBackDrive = null;
    static private DcMotor rightFrontDrive = null;
    static private DcMotor rightBackDrive = null;
    static private DcMotorEx launcher = null;
    static private DcMotor intake = null;
    static private CRServo leftIntakeServo = null;
    static private CRServo rightIntakeServo = null;
    static private CRServo windmillServo = null;
    static private GoBildaPinpointDriver pinpoint;
    /*
     * Code to run ONCE when the driver hits INIT
     */

    public void init(HardwareMap hardwareMap, Telemetry telemetry) {

        /*
         * Initialize the hardware variables. Note that the strings used here as parameters
         * to 'get' must correspond to the names assigned during the robot configuration
         * step.
         */
        mechanisms
                .leftFrontDrive = hardwareMap.get(DcMotor.class, "left_front_drive");
        mechanisms
                .rightFrontDrive = hardwareMap.get(DcMotor.class, "right_front_drive");
        mechanisms
                .leftBackDrive = hardwareMap.get(DcMotor.class, "left_back_drive");
        mechanisms
                .rightBackDrive = hardwareMap.get(DcMotor.class, "right_back_drive");
        mechanisms
                .intake = hardwareMap.get(DcMotor.class, "intake");
        mechanisms
                .launcher = hardwareMap.get(DcMotorEx.class, "launcher");
        mechanisms
                .windmillServo = hardwareMap.get(CRServo.class, "windmillServo");
        mechanisms
                .leftIntakeServo = hardwareMap.get(CRServo.class, "left_intake_servo");
        mechanisms
                .rightIntakeServo = hardwareMap.get(CRServo.class, "right_intake_servo");
        mechanisms
                .pinpoint = hardwareMap.get(GoBildaPinpointDriver.class, "pinpoint");

        /*
         * To drive forward, most robots need the motor on one side to be reversed,
         * because the axles point in opposite directions. Pushing the left stick forward
         * MUST make robot go forward. So adjust these two lines based on your first test drive.
         * Note: The settings here assume direct drive on left and right wheels. Gear
         * Reduction or 90 Deg drives may require direction flips
         */
        mechanisms
                .leftFrontDrive.setDirection(DcMotor.Direction.REVERSE);
        mechanisms
                .rightFrontDrive.setDirection(DcMotor.Direction.FORWARD);
        mechanisms
                .leftBackDrive.setDirection(DcMotor.Direction.REVERSE);
        mechanisms
                .rightBackDrive.setDirection(DcMotor.Direction.FORWARD);
        configurePinpoint();
        /*
         * Setting zeroPowerBehavior to BRAKE enables a "brake mode". This causes the motor to
         * slow down much faster when it is coasting. This creates a much more controllable
         * drivetrain. As the robot stops much quicker.
         */
        mechanisms
                .leftFrontDrive.setZeroPowerBehavior(BRAKE);
        mechanisms
                .rightFrontDrive.setZeroPowerBehavior(BRAKE);
        mechanisms
                .leftBackDrive.setZeroPowerBehavior(BRAKE);
        mechanisms
                .rightBackDrive.setZeroPowerBehavior(BRAKE);
        mechanisms
                .intake.setZeroPowerBehavior(BRAKE);

        /*
         * Here we set our launcher to the RUN_USING_ENCODER runmode.
         * If you notice that you have no control over the velocity of the motor, it just jumps
         * right to a number much higher than your set point, make sure that your encoders are plugged
         * into the port right beside the motor itself. And that the motors polarity is consistent
         * through any wiring.
         */

        mechanisms
                .launcher.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        mechanisms
                .launcher.setPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER, new PIDFCoefficients(40, 0, 0, 12.5));

        /*
         * set Feeders to an initial value to initialize the servo controller
         */
        mechanisms
                .leftIntakeServo.setPower(0);
        mechanisms
                .rightIntakeServo.setPower(0);
        mechanisms
                .windmillServo.setPower(0);

        /*
         * Much like our drivetrain motors, we set the left intake servo to reverse so that both
         * servos work to pull elements into the intake.
         */
        mechanisms
                .leftIntakeServo.setDirection(DcMotorSimple.Direction.REVERSE);

        /*
         * Tell the driver that initialization is complete.
         */
        telemetry.addData("Status", "Initialized");
    }

    public static void configurePinpoint(){
        /*
         *  Set the odometry pod positions relative to the point that you want the position to be measured from.
         *
         *  The X pod offset refers to how far sideways from the tracking point the X (forward) odometry pod is.
         *  Left of the center is a positive number, right of center is a negative number.
         *
         *  The Y pod offset refers to how far forwards from the tracking point the Y (strafe) odometry pod is.
         *  Forward of center is a positive number, backwards is a negative number.
         */
        pinpoint.setOffsets(-84.0, -168.0, DistanceUnit.MM); //these are tuned for 3110-0002-0001 Product Insight #1

        /*
         * Set the kind of pods used by your robot. If you're using goBILDA odometry pods, select either
         * the goBILDA_SWINGARM_POD, or the goBILDA_4_BAR_POD.
         * If you're using another kind of odometry pod, uncomment setEncoderResolution and input the
         * number of ticks per unit of your odometry pod.  For example:
         *     pinpoint.setEncoderResolution(13.26291192, DistanceUnit.MM);
         */
        pinpoint.setEncoderResolution(GoBildaPinpointDriver.GoBildaOdometryPods.goBILDA_4_BAR_POD);

        /*
         * Set the direction that each of the two odometry pods count. The X (forward) pod should
         * increase when you move the robot forward. And the Y (strafe) pod should increase when
         * you move the robot to the left.
         */
        pinpoint.setEncoderDirections(GoBildaPinpointDriver.EncoderDirection.FORWARD,
                GoBildaPinpointDriver.EncoderDirection.FORWARD);

        /*
         * Before running the robot, recalibrate the IMU. This needs to happen when the robot is stationary
         * The IMU will automatically calibrate when first powered on, but recalibrating before running
         * the robot is a good idea to ensure that the calibration is "good".
         * resetPosAndIMU will reset the position to 0,0,0 and also recalibrate the IMU.
         * This is recommended before you run your autonomous, as a bad initial calibration can cause
         * an incorrect starting value for x, y, and heading.
         */
        pinpoint.resetPosAndIMU();
    }



    public void mecanumDrive() {} // TODO: move into here
}
