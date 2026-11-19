package org.firstinspires.ftc.teamcode.subsystems;

import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.DcMotor;

public class Intake {
    private CRServo leftIntakeServo;
    private CRServo rightIntakeServo;
    private DcMotor intakeMotor;
    private HardwareMap hardwareMap;
    
    public Intake(HardwareMap hardwareMap) {
        this.hardwareMap = hardwareMap;
    }

    public void init() {
        intakeMotor = hardwareMap.get(DcMotor.class, "intake");
        leftIntakeServo = hardwareMap.get(CRServo.class, "left_intake_servo");
        rightIntakeServo = hardwareMap.get(CRServo.class, "right_intake_servo");
    }

    public void setPower(double power) {
        intakeMotor.setPower(power);
        leftIntakeServo.setPower(power);
        rightIntakeServo.setPower(power);
    }
}
