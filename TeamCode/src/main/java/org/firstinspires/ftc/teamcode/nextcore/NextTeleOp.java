package org.firstinspires.ftc.teamcode.nextcore;

import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.teamcode.drive.SampleMecanumDrive;

@Config
public class NextTeleOp extends LinearOpMode {

    public double SHOOTING_X;
    public double SHOOTING_Y;

    public double STARTING_X;
    public double STARTING_Y;

    public SampleMecanumDrive drive = null;
    public Mechanisms mech = null;

    public NextTeleOp(double STARTING_X, double STARTING_Y, double SHOOTING_X, double SHOOTING_Y) {
        this.STARTING_X = STARTING_X;
        this.STARTING_Y = STARTING_Y;

        this.SHOOTING_X = SHOOTING_X;
        this.SHOOTING_Y = SHOOTING_Y;
    }

    public void runOpMode() {
        drive = new SampleMecanumDrive(hardwareMap);
        mech = new Mechanisms(hardwareMap);

        Pose2d startPose = new Pose2d(STARTING_X, STARTING_Y, Math.toRadians(0));

        drive.setPoseEstimate(startPose);

        drive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        waitForStart();
        mech.runtime.reset();
    }

    public void runControls() {
        // Mechanisms - GAMEPAD 1
        if (gamepad1.a) mech.wobbleControl(Mechanisms.wobblePos.CLOSE);

        if (gamepad1.b) mech.wobbleControl(Mechanisms.wobblePos.OPEN);

        if (gamepad1.x) mech.runIntake(Mechanisms.intakeState.IN);

        if (gamepad1.y) mech.runIntake(Mechanisms.intakeState.OFF);

        if (gamepad1.left_bumper) mech.setShooter(Mechanisms.motorPower.HIGH);

        if (gamepad1.right_bumper) mech.setShooter(Mechanisms.motorPower.OFF);

        if (gamepad1.right_trigger > 0.5) mech.pushRings();

        // Mechanisms - GAMEPAD 2
        if (gamepad2.dpad_up) mech.wobbleArmControl(Mechanisms.wobbleArmPos.UP);

        if (gamepad2.dpad_down) mech.wobbleArmControl(Mechanisms.wobbleArmPos.DOWN);

        if (gamepad2.dpad_left) mech.wobbleControl(Mechanisms.wobblePos.OPEN);

        if (gamepad2.dpad_right) mech.wobbleControl(Mechanisms.wobblePos.CLOSE);

        if (gamepad2.b) mech.wobbleTurretControl(Mechanisms.wobbleTurretPos.IN);

        if (gamepad2.a) mech.wobbleTurretControl(Mechanisms.wobbleTurretPos.OUT);

        // idk what this is
        if (gamepad2.y) {
            mech.stickOne.setPosition(1);
            telemetry.addLine("eeeeeee");
            telemetry.addLine();
        }

        if (gamepad2.x) {
            mech.stickOne.setPosition(0.5);
            telemetry.addLine("aaaaa");
            telemetry.addLine();
        }
    }

    public void runDrive() {
        Pose2d poseEstimate = drive.getPoseEstimate();

        // Mecanum Drive Control
        drive.setWeightedDrivePower(
                new Pose2d(
                        Range.clip(-gamepad1.left_stick_y, -0.8, 0.8),
                        Range.clip(-gamepad1.left_stick_x, -0.8, 0.8),
                        Range.clip(-gamepad1.right_stick_x, -0.8, 0.8)
                )
        );

        drive.update();

        telemetry.addData("x", poseEstimate.getX());
        telemetry.addData("y", poseEstimate.getY());
        telemetry.addData("heading", poseEstimate.getHeading());
        telemetry.addLine();
        telemetry.addData("S1", mech.shooterOne.getVelocity());
        telemetry.addData("S2", mech.shooterTwo.getVelocity());
        telemetry.update();
    }
}
