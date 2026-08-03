package com.ftc.coach;

/**
 * COACH SOLUTION KEY: Exercise 4 - Flywheel Target RPM Calculation
 */
public class Solution4_FlywheelVelocity {

    public enum ShooterMode {
        GOAL_POST,
        PERSON_CATCH
    }

    public static double calculateTargetRPM(double ta, ShooterMode mode) {
        // Step 1: Calculate distance in feet
        double distanceFeet = Math.sqrt(100.0 / Math.max(0.1, ta));

        // Step 2: Calculate target RPM based on mode
        double targetRPM;
        if (mode == ShooterMode.GOAL_POST) {
            targetRPM = 2200.0 + (distanceFeet * 150.0);
        } else {
            targetRPM = 1200.0 + (distanceFeet * 90.0);
        }

        // Step 3: Clamp targetRPM
        return Math.max(1000.0, Math.min(4500.0, targetRPM));
    }

    public static void main(String[] args) {
        System.out.println("=== Coach Solution Key: Exercise 4 ===");

        double[] targetAreas = {2.0, 5.0, 8.0, 15.0};

        for (double ta : targetAreas) {
            double goalRPM  = calculateTargetRPM(ta, ShooterMode.GOAL_POST);
            double catchRPM = calculateTargetRPM(ta, ShooterMode.PERSON_CATCH);

            System.out.printf("Target Area (ta): %4.1f%%  ==>  Goal Post RPM: %4.0f  |  Person Catch RPM: %4.0f\n",
                    ta, goalRPM, catchRPM);
        }
    }
}
