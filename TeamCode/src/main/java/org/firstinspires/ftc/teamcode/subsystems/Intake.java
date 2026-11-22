package org.firstinspires.ftc.teamcode.subsystems;

import static com.qualcomm.robotcore.hardware.DcMotor.ZeroPowerBehavior.BRAKE;

import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.DcMotor;

public class Intake {
    private CRServo leftIntakeServo;
    private CRServo rightIntakeServo;
    private DcMotor intakeMotor;

    public void init(HardwareMap hardwareMap) {
        intakeMotor = hardwareMap.get(DcMotor.class, "intake");
        leftIntakeServo = hardwareMap.get(CRServo.class, "left_intake_servo");
        rightIntakeServo = hardwareMap.get(CRServo.class, "right_intake_servo");

        intakeMotor.setZeroPowerBehavior(BRAKE);

        leftIntakeServo.setPower(0);
        rightIntakeServo.setPower(0);

        leftIntakeServo.setDirection(DcMotorSimple.Direction.REVERSE);


    }

    public void setPower(double power) {
        intakeMotor.setPower(power);
        leftIntakeServo.setPower(power);
        rightIntakeServo.setPower(power);
    }
}
