package com.ftc.practice;

/**
 * EXERCISE 0: Java Foundations for FTC Robotics (Student Worksheet)
 * 
 * Objective:
 * Master essential Java programming concepts required for FTC robotics:
 * 1. Variables & Data Types (double, boolean, int, String)
 * 2. Conditional Decision Logic (if / else if / else)
 * 3. Writing Custom Methods (calculateTurnPower)
 * 4. Enumerated Types (RobotState enum & state transitions)
 */
public class Practice0_JavaBasics {

    // Enum for Robot Autonomous States
    public enum RobotState {
        STOPPED,
        DRIVING_FORWARD,
        TARGET_LOCKED,
        SAFETY_HALT
    }

    // TODO #1: Complete this method to calculate turn power from Limelight tx error
    // Formula: rawPower = tx * 0.025
    // Clamp rawPower between -0.4 and +0.4 using Math.max() and Math.min()
    public static double calculateTurnPower(double tx) {
        double rawPower = 0.0; // Fill in formula
        double clampedPower = 0.0; // Fill in clamping logic
        return clampedPower;
    }

    // TODO #2: Complete this method to determine the robot state based on telemetry
    public static RobotState determineRobotState(boolean triggerPressed, boolean targetSeen, double targetArea) {
        // Rule A: If trigger is NOT pressed -> return RobotState.STOPPED
        // Rule B: If target is seen AND targetArea > 18.0 -> return RobotState.SAFETY_HALT
        // Rule C: If target is seen AND targetArea <= 18.0 -> return RobotState.TARGET_LOCKED
        // Rule D: Otherwise -> return RobotState.DRIVING_FORWARD

        return RobotState.STOPPED; // Replace with your if-else logic
    }

    // TODO #3: Calculate left motor power using Arcade Drive formula
    // leftPower = drivePower + turnPower
    public static double calculateLeftMotor(double drivePower, double turnPower) {
        return 0.0; // Replace with formula
    }

    public static void main(String[] args) {
        System.out.println("==========================================================");
        System.out.println("   Exercise 0: Java Foundations for FTC (Student Work)    ");
        System.out.println("==========================================================\n");

        // Scenario 1: Target far to the right (tx = +12.0 deg)
        double tx = 12.0;
        double turnPower = calculateTurnPower(tx);
        System.out.printf("Test 1 - Heading Error tx = %+4.1f deg -> Calculated Turn Power: %+5.2f\n", tx, turnPower);

        // Scenario 2: Safety Check Decision Logic
        RobotState state1 = determineRobotState(false, true, 5.0);   // Trigger released
        RobotState state2 = determineRobotState(true, true, 20.0);   // Target too close!
        RobotState state3 = determineRobotState(true, true, 8.0);    // Normal target locked

        System.out.println("Test 2 - State 1 (Trigger Released) Expected: STOPPED      | Result: " + state1);
        System.out.println("Test 2 - State 2 (Target Too Close) Expected: SAFETY_HALT  | Result: " + state2);
        System.out.println("Test 2 - State 3 (Normal Approach)  Expected: TARGET_LOCKED | Result: " + state3);

        // Scenario 3: Arcade Drive Power Calculation
        double leftMotor = calculateLeftMotor(0.3, 0.15);
        System.out.printf("\nTest 3 - Drive: +0.30 | Turn: +0.15 -> Left Motor: %+5.2f (Expected: +0.45)\n", leftMotor);
    }
}
