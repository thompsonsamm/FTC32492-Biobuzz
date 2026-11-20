package org.firstinspires.ftc.teamcode.subsystems;

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

    public final int TARGET_VELOCITY = 1420;
    public final int MIN_VELOCITY = 1320;
    public final double FEED_TIME_SEC = 0.3;
    private ElapsedTime feed_timer = new ElapsedTime();

    private HardwareMap hardwareMap;

    public Launcher(HardwareMap hardwareMap) {
        this.hardwareMap = hardwareMap;

    }

    public void init() {
        leftFeederServo = hardwareMap.get(CRServo.class, "left_feeder_servo");
        rightFeederServo = hardwareMap.get(CRServo.class, "right_feeder_servo");
        launcherMotor = hardwareMap.get(DcMotorEx.class, "launcher");

        launcherMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        launcherMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        launcherMotor.setPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER, new PIDFCoefficients(40, 0, 0, 12.5));

        leftFeederServo.setPower(0);
        rightFeederServo.setPower(0);
    }

    public void toggle() {
        if (launcherMotor.getVelocity() < MIN_VELOCITY) {
            launcherMotor.setVelocity(TARGET_VELOCITY);
        } else {
            launcherMotor.setVelocity(0);
        }
    }

    public void spinUp() {
        launcherMotor.setVelocity(TARGET_VELOCITY);
    }
    public void spinDown() {
        launcherMotor.setVelocity(0);
    }
    public void shoot() {
        spinUp();
        if (launcherMotor.getVelocity() > MIN_VELOCITY) {
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
}