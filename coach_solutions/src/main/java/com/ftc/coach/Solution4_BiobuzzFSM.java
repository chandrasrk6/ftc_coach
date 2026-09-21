package com.ftc.coach;

/**
 * COACH SOLUTION KEY: Exercise 4 - FTC Biobuzz Finite State Machine & Closed-Loop Control
 */
public class Solution4_BiobuzzFSM {

    public enum RobotState {
        SEARCH_BEACON,      // Rotating slowly to detect flower beacon
        ALIGN_AND_APPROACH, // Closed-loop tracking toward flower beacon
        HARVEST_POLLEN,     // Executing non-blocking pollen harvesting mechanism
        RETURN_TO_HIVE      // Returning home after successful harvest
    }

    private static final double KP_TURN = 0.025;
    private static final double KD_TURN = 0.005;
    private static final double KS_STATIC = 0.08;
    private static final double TARGET_AREA_DESIRED = 12.0;
    private static final double MIN_SAFETY_AREA = 22.0;
    private static final double MAX_POWER_LIMIT = 0.5;

    private RobotState currentState = RobotState.SEARCH_BEACON;
    private double lastHeadingError = 0.0;
    private int harvestTimerTicks = 0;

    public static class SensorTelemetry {
        public boolean deadmanPressed;
        public boolean targetValid;
        public double tx;
        public double ta;

        public SensorTelemetry(boolean deadmanPressed, boolean targetValid, double tx, double ta) {
            this.deadmanPressed = deadmanPressed;
            this.targetValid = targetValid;
            this.tx = tx;
            this.ta = ta;
        }
    }

    public void update(int cycleIndex, SensorTelemetry sensor) {
        System.out.printf("--- Cycle %d | Current State: %s ---\n", cycleIndex, currentState);

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
                if (sensor.targetValid) {
                    System.out.println("  ✨ Beacon Detected! Transitioning -> ALIGN_AND_APPROACH");
                    currentState = RobotState.ALIGN_AND_APPROACH;
                } else {
                    System.out.println("  🔍 Searching... Rotating slowly (+0.15 turn power)");
                    turnPower = 0.15;
                }
                break;

            case ALIGN_AND_APPROACH:
                if (!sensor.targetValid) {
                    System.out.println("  ⚠️ Target lost! Reverting -> SEARCH_BEACON");
                    currentState = RobotState.SEARCH_BEACON;
                    break;
                }

                // Closed-loop derivative calculation
                double headingError = sensor.tx;
                double dError = headingError - lastHeadingError;

                // Closed-loop turn power with static friction feedforward
                turnPower = (headingError * KP_TURN) + (dError * KD_TURN) + (Math.signum(headingError) * KS_STATIC);
                lastHeadingError = headingError;

                // Closed-loop drive power
                double areaError = TARGET_AREA_DESIRED - sensor.ta;
                drivePower = areaError * 0.04;

                // Proximity Safety Halt
                if (sensor.ta >= MIN_SAFETY_AREA) {
                    drivePower = Math.min(drivePower, 0.0);
                    System.out.println("  ⚠️ PROXIMITY LIMIT: Flower too close! Forward drive disabled.");
                }

                // Check transition condition: Target centered & reached
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

        // Motor Power Distribution & Normalization
        double leftMotor  = drivePower + turnPower;
        double rightMotor = drivePower - turnPower;

        double maxMag = Math.max(Math.abs(leftMotor), Math.abs(rightMotor));
        if (maxMag > MAX_POWER_LIMIT) {
            leftMotor  = (leftMotor / maxMag) * MAX_POWER_LIMIT;
            rightMotor = (rightMotor / maxMag) * MAX_POWER_LIMIT;
        }

        System.out.printf("  Motor Output -> Left: %+5.2f | Right: %+5.2f\n\n", leftMotor, rightMotor);
    }

    public static void main(String[] args) {
        System.out.println("==========================================================");
        System.out.println("   Coach Solution Key: Exercise 4 Biobuzz FSM Simulator  ");
        System.out.println("==========================================================\n");

        Solution4_BiobuzzFSM robot = new Solution4_BiobuzzFSM();

        SensorTelemetry[] scenario = {
            new SensorTelemetry(true,  false,   0.0,  0.0),
            new SensorTelemetry(true,  true,   18.0,  2.0),
            new SensorTelemetry(true,  true,    8.0,  6.0),
            new SensorTelemetry(true,  true,    0.5, 11.8),
            new SensorTelemetry(true,  true,    0.0, 12.0),
            new SensorTelemetry(true,  true,    0.0, 12.0),
            new SensorTelemetry(false, true,    0.0, 12.0)
        };

        for (int i = 0; i < scenario.length; i++) {
            robot.update(i + 1, scenario[i]);
        }
    }
}
