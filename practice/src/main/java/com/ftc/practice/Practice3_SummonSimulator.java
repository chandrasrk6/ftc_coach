package com.ftc.practice;

/**
 * EXERCISE 3: Complete FTC Limelight Summon Robot Simulator (Student Worksheet)
 * 
 * Objective:
 * Implement safety rules and motor power calculations for a simulated target follower.
 * 
 * Instructions:
 * 1. Check if deadman switch is pressed. If not, print STANDBY and return.
 * 2. Check if target is valid. If not, print SEARCHING and return.
 * 3. Calculate turnPower and drivePower using P-gain formulas.
 * 4. Implement emergency stop if target area (ta) > MIN_SAFETY_AREA (too close!).
 * 5. Clamp powers to MAX_POWER_LIMIT (-0.4 to 0.4).
 */
public class Practice3_SummonSimulator {

    private static final double KP_TURN = 0.025;
    private static final double KP_DRIVE = 0.035;
    private static final double TARGET_AREA_DESIRED = 8.0; // Desired distance area (%)
    private static final double MIN_SAFETY_AREA = 18.0;   // Too close halt limit (%)
    private static final double MAX_POWER_LIMIT = 0.4;    // Safe max motor power

    static class FrameData {
        boolean targetValid;    // Is target seen by camera?
        double tx;              // Horizontal angle error (deg)
        double ta;              // Target area (%)
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

        // TODO #1: Check if driver is holding the deadman switch trigger
        if (!frame.deadmanPressed) {
            System.out.println("  State: STANDBY (Driver released trigger - Motors 0)");
            return;
        }

        // TODO #2: Check if camera sees a valid target
        if (!frame.targetValid) {
            System.out.println("  State: SEARCHING (No valid target in sight - Motors 0)");
            return;
        }

        // TODO #3: Calculate turnPower (tx * KP_TURN) and drivePower ((TARGET_AREA_DESIRED - ta) * KP_DRIVE)
        double turnPower  = 0.0; // Fill in formula
        double drivePower = 0.0; // Fill in formula

        // TODO #4: Safety Check - If ta > MIN_SAFETY_AREA, prevent driving forward (drivePower = Math.min(drivePower, 0))
        if (frame.ta > MIN_SAFETY_AREA) {
            System.out.println("  ⚠️ SAFETY ALERT: Target too close! Forward drive disabled.");
            // Add safety logic here
        }

        // TODO #5: Clamp drivePower and turnPower between -MAX_POWER_LIMIT and +MAX_POWER_LIMIT
        drivePower = Math.max(-MAX_POWER_LIMIT, Math.min(MAX_POWER_LIMIT, drivePower));
        turnPower  = Math.max(-MAX_POWER_LIMIT, Math.min(MAX_POWER_LIMIT, turnPower));

        // TODO #6: Calculate Arcade Drive Left and Right Motor powers
        double leftMotor  = drivePower + turnPower;
        double rightMotor = drivePower - turnPower;

        System.out.printf("  Target: LOCKED | tx: %+5.1f deg | ta: %4.1f %%\n", frame.tx, frame.ta);
        System.out.printf("  Outputs -> Drive Power: %+5.2f | Turn Power: %+5.2f\n", drivePower, turnPower);
        System.out.printf("  Motors  -> Left: %+5.2f | Right: %+5.2f\n", leftMotor, rightMotor);
    }

    public static void main(String[] args) {
        System.out.println("==================================================");
        System.out.println("  FTC Limelight Summon Simulator (Student Work)   ");
        System.out.println("==================================================\n");

        FrameData[] scenario = {
            new FrameData(true,  15.0, 3.0,  true),  // Step 1: Target far right and far away
            new FrameData(true,   8.0, 5.0,  true),  // Step 2: Getting closer & centering
            new FrameData(true,   0.5, 8.0,  true),  // Step 3: Centered at target distance
            new FrameData(true,  -2.0, 22.0, true),  // Step 4: Student walked too close
            new FrameData(false,  0.0, 0.0,  true),  // Step 5: Target lost
            new FrameData(true, -10.0, 6.0,  false)  // Step 6: Trigger released
        };

        for (int i = 0; i < scenario.length; i++) {
            processTelemetryFrame(i + 1, scenario[i]);
            System.out.println();
        }
    }
}
