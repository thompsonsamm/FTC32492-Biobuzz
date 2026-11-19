package org.firstinspires.ftc.teamcode;

import static com.qualcomm.robotcore.hardware.DcMotor.ZeroPowerBehavior.BRAKE;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.PIDFCoefficients;

import org.firstinspires.ftc.teamcode.subsystems.mecanumdrive;
import org.firstinspires.ftc.teamcode.subsystems.pinpointv2;
@TeleOp(name = "teleop", group = "StarterBot")
//@Disabled
public class teleop_ extends OpMode {

    // Declare OpMode members.
    private DcMotorEx launcher = null;
    private DcMotor intake = null;
    private CRServo leftIntakeServo = null;
    private CRServo rightIntakeServo = null;
    private CRServo left_feeder_servo = null;
    private CRServo right_feeder_servo = null;
    // line below calls the "mecanumdrive" subsystem for controlling the motors in all opmode classes and the "pinpointv2" one for the odometry.
    mecanumdrive drive = new mecanumdrive();
    pinpointv2 pinpoint = new pinpointv2();
    /*
     * These two variables are used to control the velocity of the launcher motor.
     * They are both in encoder ticks per second. The motors we use in the FIRST Tech Challenge
     * have encoders with a resolution of 28 ticks per revolution. We can convert this to RPM
     * by dividing the value by 28, to get to revolutions per second, before multiplying by 60
     * to get revolutions per minute.
     * We pass the target velocity variable to our motor to set the goal. We use the min velocity
     * in the launch() function to only run the windmill servo when the motor is spinning fast
     * enough to make a successful throw.
     */
    //Note: 50 velocity is equal to 107 RPM
    public final int LAUNCHER_TARGET_VELOCITY = 1420;
    public final int LAUNCHER_MIN_VELOCITY = 1320;
    private final boolean launcherToggle = false;
    // Create a variable to set to the intake.
    double intakePower;

    /*
     * Code to run ONCE when the driver hits INIT
     */
    @Override
    public void init() {

        /*
         * Initialize the hardware variables. Note that the strings used here as parameters
         * to 'get' must correspond to the names assigned during the robot configuration
         * step.
         */
        intake = hardwareMap.get(DcMotor.class, "intake");
        launcher = hardwareMap.get(DcMotorEx.class, "launcher");
        left_feeder_servo = hardwareMap.get(CRServo.class, "left_feeder_servo");
        right_feeder_servo = hardwareMap.get(CRServo.class, "right_feeder_servo");
        leftIntakeServo = hardwareMap.get(CRServo.class, "left_intake_servo");
        rightIntakeServo = hardwareMap.get(CRServo.class, "right_intake_servo");
        drive.init(hardwareMap);
        pinpoint.init(hardwareMap);
        /*
         * To drive forward, most robots need the motor on one side to be reversed,
         * because the axles point in opposite directions. Pushing the left stick forward
         * MUST make robot go forward. So adjust these two lines based on your first test drive.
         * Note: The settings here assume direct drive on left and right wheels. Gear
         * Reduction or 90 Deg drives may require direction flips
         */
        /*
         * Setting zeroPowerBehavior to BRAKE enables a "brake mode". This causes the motor to
         * slow down much faster when it is coasting. This creates a much more controllable
         * drivetrain. As the robot stops much quicker.
         */
        intake.setZeroPowerBehavior(BRAKE);

        /*
         * Here we set our launcher to the RUN_USING_ENCODER runmode.
         * If you notice that you have no control over the velocity of the motor, it just jumps
         * right to a number much higher than your set point, make sure that your encoders are plugged
         * into the port right beside the motor itself. And that the motors polarity is consistent
         * through any wiring.
         */

        launcher.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        launcher.setPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER, new PIDFCoefficients(40, 0, 0, 12.5));

        /*
         * set Feeders to an initial value to initialize the servo controller
         */
        leftIntakeServo.setPower(0);
        rightIntakeServo.setPower(0);
        left_feeder_servo.setPower(0);
        right_feeder_servo.setPower(0);

        /*
         * Much like our drivetrain motors, we set the left intake servo to reverse so that both
         * servos work to pull elements into the intake.
         */
        leftIntakeServo.setDirection(DcMotorSimple.Direction.REVERSE);

        /*
         * Tell the driver that initialization is complete.
         */
        telemetry.addData("Status", "Initialized");
    }

    /*
     * Code to run REPEATEDLY after the driver hits INIT, but before they hit START
     */
    @Override
    public void init_loop() {
    }

    /*
     * Code to run ONCE when the driver hits START
     */
    @Override
    public void start() {
    }

    /*
     * Code to run REPEATEDLY after the driver hits START but before they hit STOP
     */
    @Override
    public void loop() {
        /*
         * Here we call a function called mecanumDrive. The mecanumDrive function takes the input from
         * the joysticks, and applies power to the drive motors to move the robot as requested
         * by the driver. Moving the left joystick forwards/back moves all motors forwards/back,
         * moving the right joystick left/right rotates the robot clockwise or counterclockwise,
         * and moving the left joystick left moves the motors in the right way to create a sideways
         * "strafe" movement. Combinations of these inputs can be used to create more complex maneuvers.
         * Note, moving the joystick forward on most gamepads results in a negative signal, so
         * we invert it before passing it to the function.
         */
        drive.mecanumDrive(-gamepad1.left_stick_y, gamepad1.left_stick_x, gamepad1.right_stick_x);

        /*
         * Set the intake power variable to equal the right trigger, minus the left trigger.
         * Each trigger outputs a signal from 0-1, with 0 as fully released, and 1 fully depressed.
         * This gives us proportional control of the intake speed. The speed increases as we pull
         * the right trigger further. It's occasionally helpful to be able to reverse the intake,
         * so we also factor in the left trigger. If the left trigger is fully depressed,
         * the intakePower variable will be -1. If the right trigger is fully depressed, the variable
         * will be 1. If the driver pulls both triggers, the intake will remain off.
         * We use this technique (creating a variable, and setting it to our control inputs) to
         * allow us to avoid setting the same motors/servos power more than once per loop. That can
         * create erratic behavior.
         */
        windmill();
        intakePower = (gamepad2.right_trigger - gamepad2.left_trigger) + (gamepad1.right_trigger - gamepad1.left_trigger);
        intake();
        launch();

        /*
         * Here we set our intake motor and servos to their intake power. The order of operations
         * here is important though. The gamepad triggers define the starting point for the intake
         * power variable in each loop of our code, but inside our launch function we also sometimes
         * change the intake power. So we need to give our launch function a chance to modify the
         * variable before we write it to our motor and servos.
         */

        /*intake.setPower(intakePower);
        leftIntakeServo.setPower(intakePower);
        rightIntakeServo.setPower(intakePower);*/

        /*
         * Show motor powers on the Driver Station via telemetry.
         */
        telemetry.addData("Triggers", "left (%.2f, right (%.2f)",gamepad2.left_trigger, gamepad2.right_trigger);
        telemetry.addData("intake", intake.getCurrentPosition());
        telemetry.addData("launcher", launcher.getCurrentPosition());
        pinpoint.report();
    }

    /*
     * Code to run ONCE after the driver hits STOP
     */
    @Override
    public void stop() {
    }

    void windmill() {
        if (gamepad2.left_bumper) {
            left_feeder_servo.setPower(1);
            right_feeder_servo.setPower(1);
        } else {
            left_feeder_servo.setPower(0);
            right_feeder_servo.setPower(0);
        }
    }

    void launch() {
        /*
         * Calling gamepad2.right_bumper returns a boolean which will be true if the bumper is
         * held down, and false if it is not. Notably, this will continue to be true for every
         * cycle of our code that the driver holds down that bumper.
         * The first step of our launch() function is checking to see if the user is currently
         * holding down the right gamepad. If they are, then we want to start spinning up the launcher.
         * Otherwise, we start spinning the launcher down.
         */
        if (gamepad2.rightBumperWasPressed()) {
            launcher.setVelocity(launcher.getVelocity() > LAUNCHER_MIN_VELOCITY ? 0 : LAUNCHER_TARGET_VELOCITY);
        }
    }

    void intake() {
        if (Math.abs(intakePower) > .1f) {
            intake.setPower(intakePower);
            leftIntakeServo.setPower(intakePower);
            rightIntakeServo.setPower(intakePower);
        } else {
            intake.setPower(0);
            leftIntakeServo.setPower(0);
            rightIntakeServo.setPower(0);
        }
    }
}