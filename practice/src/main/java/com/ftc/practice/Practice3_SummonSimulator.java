package com.ftc.practice;

/**
 * EXERCISE 3: Complete FTC Limelight Summon Robot Simulator
 * 
 * Objective:
 * Simulate a full robot summon controller over 8 time steps using plain Java.
 * Evaluates target visibility, safety distance limits, and motor output calculation.
 */
public class Practice3_SummonSimulator {

    // Control Gains & Constants
    private static final double KP_TURN = 0.025;
    private static final double KP_DRIVE = 0.035;
    private static final double TARGET_AREA_DESIRED = 8.0; // Desired distance area (%)
    private static final double MIN_SAFETY_AREA = 18.0;   // Too close halt limit (%)
    private static final double MAX_POWER_LIMIT = 0.4;    // Safe max motor power

    // Simulated Sensor Reading Class
    static class FrameData {
        boolean targetValid; // Is target seen by camera?
        double tx;           // Horizontal angle error (deg)
        double ta;           // Target area (%)
        boolean deadmanPressed; // Is driver holding trigger?

        public FrameData(boolean targetValid, double tx, double ta, boolean deadmanPressed) {
            this.targetValid = targetValid;
            this.tx = tx;
            this.ta = ta;
            this.deadmanPressed = deadmanPressed;
        }
    }

    public static void processTelemetryFrame(int step, FrameData frame) {
        System.out.printf("--- Step %d ---\n", step);

        if (!frame.deadmanPressed) {
            System.out.println("  State: STANDBY (Deadman switch released - Holding motors at 0)");
            return;
        }

        if (!frame.targetValid) {
            System.out.println("  State: SEARCHING (No valid target in sight - Motors 0)");
            return;
        }

        // 1. Compute Turn Power
        double turnPower = frame.tx * KP_TURN;

        // 2. Compute Drive Power
        double areaError = TARGET_AREA_DESIRED - frame.ta;
        double drivePower = areaError * KP_DRIVE;

        // 3. Emergency Safety Stop if target is too close
        if (frame.ta > MIN_SAFETY_AREA) {
            drivePower = Math.min(drivePower, 0);
            System.out.println("  ⚠️ SAFETY ALERT: Target too close! Forward drive disabled.");
        }

        // 4. Clamp powers to safe bounds
        drivePower = Math.max(-MAX_POWER_LIMIT, Math.min(MAX_POWER_LIMIT, drivePower));
        turnPower  = Math.max(-MAX_POWER_LIMIT, Math.min(MAX_POWER_LIMIT, turnPower));

        // 5. Mix into Arcade Drive Left & Right
        double leftMotor  = drivePower + turnPower;
        double rightMotor = drivePower - turnPower;

        System.out.printf("  Target: LOCKED | tx: %+5.1f deg | ta: %4.1f %%\n", frame.tx, frame.ta);
        System.out.printf("  Outputs -> Drive Power: %+5.2f | Turn Power: %+5.2f\n", drivePower, turnPower);
        System.out.printf("  Motors  -> Left: %+5.2f | Right: %+5.2f\n", leftMotor, rightMotor);
    }

    public static void main(String[] args) {
        System.out.println("==================================================");
        System.out.println("    FTC Limelight Summon Simulator (Plain Java)    ");
        System.out.println("==================================================\n");

        // Simulation Timeline (8 Steps simulating student movement & driver interaction)
        FrameData[] scenario = {
            new FrameData(true,  15.0, 3.0,  true),  // Step 1: Target far right and far away
            new FrameData(true,   8.0, 5.0,  true),  // Step 2: Getting closer & centering
            new FrameData(true,   0.5, 8.0,  true),  // Step 3: Perfectly centered at target distance
            new FrameData(true,  -2.0, 22.0, true),  // Step 4: Student walked too close (triggers safety stop)
            new FrameData(false,  0.0, 0.0,  true),  // Step 5: Student walked out of frame (target lost)
            new FrameData(true, -10.0, 6.0,  false)  // Step 6: Target seen, but driver released trigger
        };

        for (int i = 0; i < scenario.length; i++) {
            processTelemetryFrame(i + 1, scenario[i]);
            System.out.println();
        }
    }
}
