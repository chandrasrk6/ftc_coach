package com.ftc.practice;

/**
 * EXERCISE 4: Flywheel Target RPM Calculation (Student Worksheet)
 * 
 * Objective:
 * Calculate the required flywheel motor RPM based on target distance and target mode.
 * 
 * Instructions:
 * 1. Calculate estimated distance in feet using formula: sqrt(100.0 / ta)
 * 2. Calculate target RPM based on target mode:
 *    - GOAL_POST Mode:   targetRPM = 2200.0 + (distanceFeet * 150.0)
 *    - PERSON_CATCH Mode: targetRPM = 1200.0 + (distanceFeet * 90.0)
 * 3. Clamp targetRPM between MIN_RPM (1000) and MAX_RPM (4500).
 */
public class Practice4_FlywheelVelocity {

    public enum ShooterMode {
        GOAL_POST,    // High velocity shot for AprilTag Goal Post
        PERSON_CATCH  // Gentle, lofted shot for a student catch
    }

    public static double calculateTargetRPM(double ta, ShooterMode mode) {
        // TODO #1: Calculate estimated distance in feet
        // Hint: Math.sqrt(100.0 / Math.max(0.1, ta))
        double distanceFeet = 0.0;

        // TODO #2: Calculate target RPM depending on shooter mode
        double targetRPM = 0.0;
        if (mode == ShooterMode.GOAL_POST) {
            // High velocity formula
        } else {
            // Soft lofted catch formula
        }

        // TODO #3: Clamp targetRPM between 1000.0 and 4500.0
        double clampedRPM = Math.max(1000.0, Math.min(4500.0, targetRPM));

        return clampedRPM;
    }

    public static void main(String[] args) {
        System.out.println("=== Exercise 4: Flywheel Velocity Math (Student Practice) ===");

        // Test Cases: {ta (screen area %), Mode}
        double[] targetAreas = {2.0, 5.0, 8.0, 15.0};

        for (double ta : targetAreas) {
            double goalRPM  = calculateTargetRPM(ta, ShooterMode.GOAL_POST);
            double catchRPM = calculateTargetRPM(ta, ShooterMode.PERSON_CATCH);

            System.out.printf("Target Area (ta): %4.1f%%  ==>  Goal Post RPM: %4.0f  |  Person Catch RPM: %4.0f\n",
                    ta, goalRPM, catchRPM);
        }
    }
}
