package com.ftc.practice;

/**
 * EXERCISE 1: Proportional (P) Control Math (Student Worksheet)
 * 
 * Objective:
 * Implement a Proportional Controller that calculates motor turn power
 * based on horizontal angle error (tx) from the Limelight camera.
 * 
 * Instructions:
 * 1. Calculate raw turn power using the formula: power = tx * KP_TURN
 * 2. Clamp the output so power never exceeds -MAX_SPEED or +MAX_SPEED.
 */
public class Practice1_PControl {

    private static final double KP_TURN = 0.025; // Proportional gain for steering
    private static final double MAX_SPEED = 0.4;  // Safe max motor power limit

    public static double calculateTurnPower(double tx) {
        // TODO #1: Calculate raw power using the proportional control formula (tx * KP_TURN)
        double rawPower = 0.0; // Replace with your formula

        // TODO #2: Clamp rawPower so it stays between -MAX_SPEED and +MAX_SPEED
        // Hint: Use Math.max() and Math.min()
        double clampedPower = 0.0; // Replace with your clamping logic

        return clampedPower;
    }

    public static void main(String[] args) {
        System.out.println("=== Exercise 1: P-Control Math (Student Practice) ===");
        
        // Test Cases (Simulated Limelight horizontal angle offsets tx in degrees)
        double[] testAngleErrors = {0.0, 10.0, -15.0, 25.0, -35.0};

        for (double tx : testAngleErrors) {
            double power = calculateTurnPower(tx);
            System.out.printf("Heading Error (tx): %6.1f deg  ==>  Calculated Turn Power: %+5.2f\n", tx, power);
        }
    }
}
