package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.limelight3a.Limelight3A;
import com.qualcomm.hardware.limelight3a.LLResult;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

/**
 * FTC OpMode: Flywheel Ball Shooter with Auto-Aiming & Target Distance Calculation.
 * 
 * Target Modes:
 * 1. GOAL POST MODE (Pipeline 0 - AprilTag): High-velocity shot into target goal post.
 * 2. PERSON CATCH MODE (Pipeline 1 - Person/Badge): Gentle, lofted trajectory for a student catch.
 */
@TeleOp(name = "Limelight Flywheel Shooter", group = "Autonomous")
public class FlywheelShooterOpMode extends LinearOpMode {

    // Hardware Declarations
    private Limelight3A limelight;
    private DcMotor leftFront, rightFront, leftBack, rightBack;
    private DcMotorEx flywheelMotor;
    private Servo feederServo;

    // Motor Specs: GoBILDA 5202/5203 flywheel motor encoder ticks per rev
    private static final double TICKS_PER_REV = 28.0; // REV HD Hex or GoBILDA Yellowjacket flywheel
    private static final double SERVO_REST_POS = 0.2;
    private static final double SERVO_PUSH_POS = 0.7;

    // Control Constants
    private static final double KP_AUTO_AIM = 0.03;      // Turning gain for auto-aiming to target
    private static final double AIM_TOLERANCE_DEG = 1.5;  // Allowed heading error to fire
    private static final double RPM_TOLERANCE = 75.0;     // Allowed velocity error (RPM) to fire

    // Target Shooting Modes
    public enum TargetMode {
        GOAL_POST,    // AprilTag Goal Post (High Velocity Shot)
        PERSON_CATCH  // Person / Catch Mode (Gentle Lofted Shot)
    }

    @Override
    public void runOpMode() throws InterruptedException {
        // Initialize Hardware
        limelight = hardwareMap.get(Limelight3A.class, "limelight");
        limelight.setPollRateHz(100);
        limelight.start();

        leftFront  = hardwareMap.get(DcMotor.class, "leftFront");
        rightFront = hardwareMap.get(DcMotor.class, "rightFront");
        leftBack   = hardwareMap.get(DcMotor.class, "leftBack");
        rightBack  = hardwareMap.get(DcMotor.class, "rightBack");

        leftFront.setDirection(DcMotorSimple.Direction.REVERSE);
        leftBack.setDirection(DcMotorSimple.Direction.REVERSE);

        flywheelMotor = hardwareMap.get(DcMotorEx.class, "flywheelMotor");
        flywheelMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        feederServo = hardwareMap.get(Servo.class, "feederServo");
        feederServo.setPosition(SERVO_REST_POS);

        TargetMode currentMode = TargetMode.GOAL_POST;
        limelight.pipelineSwitch(0); // 0 = Goal AprilTag, 1 = Person Target

        boolean lastDpadUp = false;
        boolean lastDpadDown = false;

        telemetry.addData("Status", "Initialized. Ready for Shooter Target Selection.");
        telemetry.addData("Controls", "D-Pad Up: Goal Post Mode | D-Pad Down: Person Catch Mode | Right Bumper: Auto-Aim & Fire");
        telemetry.update();

        waitForStart();

        while (opModeIsActive()) {
            // Mode Switching
            if (gamepad1.dpad_up && !lastDpadUp) {
                currentMode = TargetMode.GOAL_POST;
                limelight.pipelineSwitch(0);
            } else if (gamepad1.dpad_down && !lastDpadDown) {
                currentMode = TargetMode.PERSON_CATCH;
                limelight.pipelineSwitch(1);
            }
            lastDpadUp   = gamepad1.dpad_up;
            lastDpadDown = gamepad1.dpad_down;

            LLResult result = limelight.getLatestResult();
            boolean autoAimAndFire = gamepad1.right_bumper;

            double currentVelocityTicksSec = flywheelMotor.getVelocity();
            double currentRPM = (currentVelocityTicksSec / TICKS_PER_REV) * 60.0;

            if (result != null && result.isValid()) {
                double tx = result.getTx();
                double ta = result.getTa(); // Distance proxy

                // 1. Calculate Target Distance (Approximate feet based on target area ta)
                double estimatedDistanceFeet = Math.sqrt(100.0 / Math.max(0.1, ta));

                // 2. Calculate Desired Target Flywheel RPM based on distance & mode
                double targetRPM;
                if (currentMode == TargetMode.GOAL_POST) {
                    // Goal Post Mode: High velocity trajectory
                    targetRPM = 2200.0 + (estimatedDistanceFeet * 150.0);
                } else {
                    // Person Catch Mode: Softer, lofted arc trajectory for safe catching
                    targetRPM = 1200.0 + (estimatedDistanceFeet * 90.0);
                }

                // Clamp RPM limits
                targetRPM = Math.max(1000.0, Math.min(4500.0, targetRPM));

                if (autoAimAndFire) {
                    // A. Spin Flywheel to calculated Target RPM using FTC Closed-Loop Velocity PIDF
                    double targetTicksPerSec = (targetRPM / 60.0) * TICKS_PER_REV;
                    flywheelMotor.setVelocity(targetTicksPerSec);

                    // B. Auto-Aim Steering
                    double turnPower = tx * KP_AUTO_AIM;
                    turnPower = Math.max(-0.35, Math.min(0.35, turnPower));
                    setArcadePower(0.0, turnPower); // Rotate in place to center target

                    // C. Check Ready-to-Fire Conditions
                    boolean aligned = Math.abs(tx) <= AIM_TOLERANCE_DEG;
                    boolean speedReady = Math.abs(currentRPM - targetRPM) <= RPM_TOLERANCE;

                    if (aligned && speedReady) {
                        feederServo.setPosition(SERVO_PUSH_POS); // Push ball into flywheel!
                        telemetry.addData("Shooter State", "🔥 FIRING BALL!");
                    } else {
                        feederServo.setPosition(SERVO_REST_POS);
                        telemetry.addData("Shooter State", "SPINNING & ALIGNING...");
                    }

                    telemetry.addData("Target Lock", "ALIGNED: " + aligned + " | RPM READY: " + speedReady);
                } else {
                    // Manual Standby / Manual Controls
                    stopDrivetrain();
                    feederServo.setPosition(SERVO_REST_POS);

                    if (gamepad1.a) {
                        // Manual test spin-up
                        flywheelMotor.setPower(0.6);
                    } else {
                        flywheelMotor.setPower(0.0);
                    }
                    telemetry.addData("Shooter State", "STANDBY (Hold Right Bumper to Auto-Aim & Fire)");
                }

                telemetry.addData("Target Mode", currentMode);
                telemetry.addData("Target Distance", "%.1f ft (ta: %.2f%%)", estimatedDistanceFeet, ta);
                telemetry.addData("Heading Error (tx)", "%.2f deg", tx);
                telemetry.addData("Target RPM", "%.0f RPM", targetRPM);
                telemetry.addData("Current Flywheel RPM", "%.0f RPM", currentRPM);
            } else {
                // Target lost
                stopDrivetrain();
                feederServo.setPosition(SERVO_REST_POS);
                flywheelMotor.setPower(0.0);

                telemetry.addData("Target Mode", currentMode);
                telemetry.addData("Shooter State", "SEARCHING FOR TARGET...");
            }

            telemetry.update();
        }
    }

    private void setArcadePower(double drive, double turn) {
        double left  = drive + turn;
        double right = drive - turn;
        double max = Math.max(Math.abs(left), Math.abs(right));
        if (max > 1.0) {
            left /= max;
            right /= max;
        }
        leftFront.setPower(left);
        leftBack.setPower(left);
        rightFront.setPower(right);
        rightBack.setPower(right);
    }

    private void stopDrivetrain() {
        leftFront.setPower(0);
        leftBack.setPower(0);
        rightFront.setPower(0);
        rightBack.setPower(0);
    }
}
