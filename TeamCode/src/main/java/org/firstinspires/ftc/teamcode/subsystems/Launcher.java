package org.firstinspires.ftc.teamcode.subsystems;

import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
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
        leftFeederServo = hardwareMap.get(CRServo.class, "leftFeederServo");
        rightFeederServo = hardwareMap.get(CRServo.class, "rightFeederServo");
        launcherMotor = hardwareMap.get(DcMotorEx.class, "launcher");

        launcherMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
    }

    public void toggle() {
        if (launcherMotor.getVelocity() < MIN_VELOCITY) {
            launcherMotor.setVelocity(TARGET_VELOCITY);
        } else {
            launcherMotor.setVelocity(0);
        }
    }

}
