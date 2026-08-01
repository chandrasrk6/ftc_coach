package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.limelight3a.Limelight3A;
import com.qualcomm.hardware.limelight3a.LLResult;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

/**
 * FTC OpMode for a robot with a drive train and Limelight 3A camera mechanism
 * that follows a target student (AprilTag or Neural Detector) to simulate a "Summon" feature.
 */
@TeleOp(name = "Limelight Summon / Student Follower", group = "Autonomous")
public class LimelightSummonOpMode extends LinearOpMode {

    // Hardware declaration
    private Limelight3A limelight;
    private DcMotor leftFront, rightFront, leftBack, rightBack;

    // Control Gains (Tune these for your specific robot drivetrain)
    private static final double KP_TURN = 0.025;         // Steering proportional gain
    private static final double KP_DRIVE = 0.035;        // Forward/backward drive proportional gain
    private static final double TARGET_AREA_DESIRED = 8.0; // Target screen area percentage (~3-4 ft away)
    private static final double MIN_SAFETY_AREA = 18.0;   // Maximum allowed area before emergency stop

    @Override
    public void runOpMode() throws InterruptedException {
        // Initialize Limelight Hardware
        limelight = hardwareMap.get(Limelight3A.class, "limelight");
        limelight.setPollRateHz(100);
        limelight.start();
        limelight.pipelineSwitch(0); // Pipeline 0: AprilTag / Neural Target

        // Initialize Drivetrain Motors
        leftFront  = hardwareMap.get(DcMotor.class, "leftFront");
        rightFront = hardwareMap.get(DcMotor.class, "rightFront");
        leftBack   = hardwareMap.get(DcMotor.class, "leftBack");
        rightBack  = hardwareMap.get(DcMotor.class, "rightBack");

        // Reverse left motors for standard positive forward direction
        leftFront.setDirection(DcMotorSimple.Direction.REVERSE);
        leftBack.setDirection(DcMotorSimple.Direction.REVERSE);

        telemetry.addData("Status", "Initialized. Point Limelight at target.");
        telemetry.update();

        waitForStart();

        while (opModeIsActive()) {
            LLResult result = limelight.getLatestResult();

            // Safety Deadman Switch: Driver must hold Right Trigger to enable Summon
            boolean summonActive = gamepad1.right_trigger > 0.3;

            if (summonActive && result != null && result.isValid()) {
                double tx = result.getTx(); // Angle offset in degrees (-31 to +31)
                double ta = result.getTa(); // Target area percentage of frame

                // Steering control calculation
                double turnPower = tx * KP_TURN;

                // Drive control calculation
                double areaError = TARGET_AREA_DESIRED - ta;
                double drivePower = areaError * KP_DRIVE;

                // Safety check: Prevent driving forward if target is too close
                if (ta > MIN_SAFETY_AREA) {
                    drivePower = Math.min(drivePower, 0);
                }

                // Clamp powers to safe speed limits
                drivePower = Math.max(-0.4, Math.min(0.4, drivePower));
                turnPower  = Math.max(-0.4, Math.min(0.4, turnPower));

                // Drive motors using arcade drive distribution
                setArcadePower(drivePower, turnPower);

                telemetry.addData("Summon State", "ACTIVE & TRACKING");
                telemetry.addData("Heading Error (tx)", "%.2f deg", tx);
                telemetry.addData("Target Area (ta)", "%.2f %%", ta);
                telemetry.addData("Drive Power", "%.2f", drivePower);
                telemetry.addData("Turn Power", "%.2f", turnPower);
            } else {
                stopMotors();

                if (!summonActive) {
                    telemetry.addData("Summon State", "STANDBY (Hold Gamepad1 Right Trigger)");
                } else {
                    telemetry.addData("Summon State", "SEARCHING (No valid target in sight)");
                }
            }

            telemetry.update();
        }
    }

    private void setArcadePower(double drive, double turn) {
        double leftPower  = drive + turn;
        double rightPower = drive - turn;

        double max = Math.max(Math.abs(leftPower), Math.abs(rightPower));
        if (max > 1.0) {
            leftPower /= max;
            rightPower /= max;
        }

        leftFront.setPower(leftPower);
        leftBack.setPower(leftPower);
        rightFront.setPower(rightPower);
        rightBack.setPower(rightPower);
    }

    private void stopMotors() {
        leftFront.setPower(0);
        leftBack.setPower(0);
        rightFront.setPower(0);
        rightBack.setPower(0);
    }
}
