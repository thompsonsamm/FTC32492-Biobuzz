package org.firstinspires.ftc.teamcode.subsystems;

import com.pedropathing.ivy.Command;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.PIDFCoefficients;
import com.qualcomm.robotcore.util.ElapsedTime;

public class Launcher {
    private CRServo leftFeederServo;
    private CRServo rightFeederServo;

    private DcMotorEx launcherMotor;

    public final int INITIAL_TARGET_VELOCITY = 1220;
    public int TARGET_VELOCITY = INITIAL_TARGET_VELOCITY;
    public final int MIN_VELOCITY = 30;
    //50 velocity is 107 rpm
    public final double FEED_TIME_SEC = 0.3;
    private ElapsedTime feed_timer = new ElapsedTime(); // todo(juni): remove this?
    private ElapsedTime shootTimer = new ElapsedTime();

    public Command shootCommand(double timeSec) {
        return Command.build()
                .setStart(() -> { spinUp(); shootTimer.reset(); })
                .setExecute(this::shoot)
                .setDone(() -> shootTimer.seconds() <= timeSec)
                .setEnd(endCondition -> spinDown());
    }

    public void init(HardwareMap hardwareMap) {
        leftFeederServo = hardwareMap.get(CRServo.class, "left_feeder_servo");
        rightFeederServo = hardwareMap.get(CRServo.class, "right_feeder_servo");
        launcherMotor = hardwareMap.get(DcMotorEx.class, "launcher");

        launcherMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        launcherMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        launcherMotor.setPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER, new PIDFCoefficients(40, 0, 0, 12.5));

        leftFeederServo.setDirection(CRServo.Direction.REVERSE);

        leftFeederServo.setPower(0);
        rightFeederServo.setPower(0);
    }

    public void spinUp() {
        launcherMotor.setVelocity(TARGET_VELOCITY);
    }
    public void spinDown() {
        launcherMotor.setVelocity(0);
    }
    public void shoot() {
        spinUp();
        if (launcherMotor.getVelocity() > (TARGET_VELOCITY - MIN_VELOCITY) && launcherMotor.getVelocity() < (TARGET_VELOCITY + MIN_VELOCITY))  {
            leftFeederServo.setPower(1);
            rightFeederServo.setPower(1);
        } else {
            leftFeederServo.setPower(0);
            rightFeederServo.setPower(0);
        }


    }
    public void stopFeeder() {
        leftFeederServo.setPower(0);
        rightFeederServo.setPower(0);
    }
    public void speedUp() {
        TARGET_VELOCITY += 10;
    }
    public void speedDown() {
        TARGET_VELOCITY -= 10;
    }
    public void speedNormal() {
        TARGET_VELOCITY = INITIAL_TARGET_VELOCITY;
    }
    public double currentVelocity() {
        return launcherMotor.getVelocity();
    }
    public int targetVelocity() {
        return TARGET_VELOCITY;
    }
}