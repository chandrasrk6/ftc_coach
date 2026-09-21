package com.ftc.practice;

/**
 * EXERCISE 4: FTC Biobuzz Finite State Machine & Closed-Loop Control (Student Worksheet)
 * 
 * Objective:
 * Implement a non-blocking Finite State Machine (FSM) that controls an FTC robot 
 * searching for flowers, approaching pollen beacons with PID control, harvesting pollen,
 * and enforcing multi-layered safety rules.
 * 
 * First-Principles Breakdown:
 * 1. State Machine: Non-blocking state transitions based on sensory feedback.
 * 2. Closed-Loop PID: Power = Kp * error + Kd * (error - prevError) + kS * sgn(error).
 * 3. Actuator Scaling: Normalize motor powers so direction is preserved during saturation.
 * 4. Safety Fail-safes: Instant power cutoff when target lost or driver releases trigger.
 */
public class Practice4_BiobuzzFSM {

    public enum RobotState {
        SEARCH_BEACON,      // Rotating slowly to detect flower beacon
        ALIGN_AND_APPROACH, // Closed-loop tracking toward flower beacon
        HARVEST_POLLEN,     // Executing non-blocking pollen harvesting mechanism
        RETURN_TO_HIVE      // Returning home after successful harvest
    }

    // First Principles Constants
    private static final double KP_TURN = 0.025;
    private static final double KD_TURN = 0.005;
    private static final double KS_STATIC = 0.08;        // Static friction feedforward boost
    private static final double TARGET_AREA_DESIRED = 12.0; // Desired flower distance area (%)
    private static final double MIN_SAFETY_AREA = 22.0;   // Emergency halt area threshold (%)
    private static final double MAX_POWER_LIMIT = 0.5;

    // FSM State Variables
    private RobotState currentState = RobotState.SEARCH_BEACON;
    private double lastHeadingError = 0.0;
    private int harvestTimerTicks = 0;

    public static class SensorTelemetry {
        public boolean deadmanPressed; // Driver trigger held
        public boolean targetValid;    // Vision sees flower beacon
        public double tx;              // Horizontal angle error (deg)
        public double ta;              // Target area (%)

        public SensorTelemetry(boolean deadmanPressed, boolean targetValid, double tx, double ta) {
            this.deadmanPressed = deadmanPressed;
            this.targetValid = targetValid;
            this.tx = tx;
            this.ta = ta;
        }
    }

    public void update(int cycleIndex, SensorTelemetry sensor) {
        System.out.printf("--- Cycle %d | Current State: %s ---\n", cycleIndex, currentState);

        // FAIL-SAFE LAYER 1: Check driver deadman switch trigger
        if (!sensor.deadmanPressed) {
            System.out.println("  🚨 FAIL-SAFE TRIGGERED: Driver released deadman trigger! All motors HALTED.");
            currentState = RobotState.SEARCH_BEACON;
            lastHeadingError = 0.0;
            return;
        }

        double drivePower = 0.0;
        double turnPower  = 0.0;

        switch (currentState) {
            case SEARCH_BEACON:
                // If vision camera spots a valid flower beacon, transition to ALIGN_AND_APPROACH
                if (sensor.targetValid) {
                    System.out.println("  ✨ Beacon Detected! Transitioning -> ALIGN_AND_APPROACH");
                    currentState = RobotState.ALIGN_AND_APPROACH;
                } else {
                    System.out.println("  🔍 Searching... Rotating slowly (+0.15 turn power)");
                    turnPower = 0.15;
                }
                break;

            case ALIGN_AND_APPROACH:
                // FAIL-SAFE LAYER 2: If target is lost during approach, revert to SEARCH_BEACON
                if (!sensor.targetValid) {
                    System.out.println("  ⚠️ Target lost! Reverting -> SEARCH_BEACON");
                    currentState = RobotState.SEARCH_BEACON;
                    break;
                }

                // TODO #1: Calculate Heading Error Derivative (dError = current tx - lastHeadingError)
                double headingError = sensor.tx;
                double dError = 0.0; // Fill in formula: headingError - lastHeadingError

                // TODO #2: Calculate Turn Power using Proportional + Derivative + Static Feedforward
                // turnPower = (headingError * KP_TURN) + (dError * KD_TURN) + (Math.signum(headingError) * KS_STATIC)
                turnPower = 0.0; // Fill in formula

                // Save current heading error for next cycle's derivative
                lastHeadingError = headingError;

                // TODO #3: Calculate Drive Power using Proportional Control (TARGET_AREA_DESIRED - ta) * 0.04
                drivePower = 0.0; // Fill in formula

                // TODO #4: Proximity Safety Limit - If sensor.ta >= MIN_SAFETY_AREA, prevent forward motion
                if (sensor.ta >= MIN_SAFETY_AREA) {
                    System.out.println("  ⚠️ PROXIMITY LIMIT: Flower too close! Forward drive disabled.");
                    // Fill in safety clamp
                }

                // Check transition condition: Target centered (|tx| < 1.5) AND target distance reached (ta >= 11.5)
                if (Math.abs(sensor.tx) < 1.5 && sensor.ta >= 11.5) {
                    System.out.println("  🎯 Target Reached & Centered! Transitioning -> HARVEST_POLLEN");
                    currentState = RobotState.HARVEST_POLLEN;
                    harvestTimerTicks = 0;
                    drivePower = 0.0;
                    turnPower = 0.0;
                }
                break;

            case HARVEST_POLLEN:
                harvestTimerTicks++;
                System.out.printf("  🐝 Harvesting Pollen... [Tick %d/3]\n", harvestTimerTicks);
                drivePower = 0.0;
                turnPower = 0.0;

                if (harvestTimerTicks >= 3) {
                    System.out.println("  ✅ Pollen Harvested! Transitioning -> RETURN_TO_HIVE");
                    currentState = RobotState.RETURN_TO_HIVE;
                }
                break;

            case RETURN_TO_HIVE:
                System.out.println("  🏠 Returning to Home Hive... Driving Reverse (-0.3)");
                drivePower = -0.3;
                turnPower = 0.0;
                break;
        }

        // TODO #5: Motor Power Distribution & Normalization
        // Unnormalized motor powers:
        double leftMotor  = drivePower + turnPower;
        double rightMotor = drivePower - turnPower;

        // Normalize if max magnitude > MAX_POWER_LIMIT
        double maxMag = Math.max(Math.abs(leftMotor), Math.abs(rightMotor));
        if (maxMag > MAX_POWER_LIMIT) {
            leftMotor  = (leftMotor / maxMag) * MAX_POWER_LIMIT;
            rightMotor = (rightMotor / maxMag) * MAX_POWER_LIMIT;
        }

        System.out.printf("  Motor Output -> Left: %+5.2f | Right: %+5.2f\n\n", leftMotor, rightMotor);
    }

    public static void main(String[] args) {
        System.out.println("==========================================================");
        System.out.println("  FTC Biobuzz First-Principles FSM Simulator (Student)   ");
        System.out.println("==========================================================\n");

        Practice4_BiobuzzFSM robot = new Practice4_BiobuzzFSM();

        SensorTelemetry[] scenario = {
            new SensorTelemetry(true,  false,   0.0,  0.0), // Cycle 1: Search beacon
            new SensorTelemetry(true,  true,   18.0,  2.0), // Cycle 2: Beacon spotted, approach (far right)
            new SensorTelemetry(true,  true,    8.0,  6.0), // Cycle 3: Closing in
            new SensorTelemetry(true,  true,    0.5, 11.8), // Cycle 4: Centered & reached -> start harvest
            new SensorTelemetry(true,  true,    0.0, 12.0), // Cycle 5: Harvesting (Tick 2)
            new SensorTelemetry(true,  true,    0.0, 12.0), // Cycle 6: Harvesting complete -> Return to Hive
            new SensorTelemetry(false, true,    0.0, 12.0)  // Cycle 7: Driver releases trigger fail-safe check
        };

        for (int i = 0; i < scenario.length; i++) {
            robot.update(i + 1, scenario[i]);
        }
    }
}
