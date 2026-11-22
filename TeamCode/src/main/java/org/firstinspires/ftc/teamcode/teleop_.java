package org.firstinspires.ftc.teamcode;

import static com.qualcomm.robotcore.hardware.DcMotor.ZeroPowerBehavior.BRAKE;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.PIDFCoefficients;

import org.firstinspires.ftc.teamcode.subsystems.Intake;
import org.firstinspires.ftc.teamcode.subsystems.Launcher;
import org.firstinspires.ftc.teamcode.subsystems.mecanumdrive;
import org.firstinspires.ftc.teamcode.subsystems.pinpointv2;
@TeleOp(name = "teleop1", group = "StarterBot")
//@Disabled
public class teleop_ extends OpMode {

    // Declare OpMode members.
    mecanumdrive drive = new mecanumdrive();
    pinpointv2 pinpoint = new pinpointv2();
    Launcher launcher = new Launcher();
    Intake intake = new Intake();
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
        drive.init(hardwareMap);
        pinpoint.init(hardwareMap);
        intake.init(hardwareMap);
        launcher.init(hardwareMap);

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
        drive.mecanumDrive(-gamepad1.left_stick_y, gamepad1.left_stick_x, gamepad1.right_stick_x);
        if (gamepad2.xWasPressed()) {
            launcher.spinUp();
        }
        if (gamepad2.bWasPressed()) {
            launcher.spinDown();
        }
        if (gamepad2.right_trigger>.1) {
            launcher.shoot();
        }else {
            launcher.stopFeeder();
        }
        if (gamepad2.left_trigger>.1) {
            intake.setPower(1);
        } else if (gamepad2.left_bumper) {
            intake.setPower(-1);
        } else {
            intake.setPower(0);
        }
    }
        /*intake.setPower(intakePower);
        leftIntakeServo.setPower(intakePower);
        rightIntakeServo.setPower(intakePower);*/
        /*
         * Show motor powers on the Driver Station via telemetry.
         */
        /*telemetry.addData("Triggers", "left (%.2f, right (%.2f)",gamepad2.left_trigger, gamepad2.right_trigger);
        telemetry.addData("intake", intake.getCurrentPosition());
        telemetry.addData("launcher", launcher.getCurrentPosition());
        pinpoint.report();
    /*
     * Code to run ONCE after the driver hits STOP
     */
    @Override
    public void stop() {
    }
}