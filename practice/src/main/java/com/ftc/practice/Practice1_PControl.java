package com.ftc.practice;

/**
 * EXERCISE 1: Proportional (P) Control Math
 * 
 * Objective:
 * Implement a Proportional Controller that calculates motor turn power
 * based on horizontal angle error (tx) from the Limelight camera.
 * 
 * Challenge:
 * 1. Complete the `calculateTurnPower` method using formula: power = error * Kp
 * 2. Clamp the output so power never exceeds maxSpeed limit (e.g., -0.4 to 0.4).
 */
public class Practice1_PControl {

    private static final double KP_TURN = 0.025; // Proportional gain
    private static final double MAX_SPEED = 0.4;  // Safe max power limit

    public static double calculateTurnPower(double tx) {
        // TODO: Step 1 - Calculate raw power using proportional formula (tx * KP_TURN)
        double rawPower = tx * KP_TURN;

        // TODO: Step 2 - Clamp power between -MAX_SPEED and +MAX_SPEED
        double clampedPower = Math.max(-MAX_SPEED, Math.min(MAX_SPEED, rawPower));

        return clampedPower;
    }

    public static void main(String[] args) {
        System.out.println("=== Exercise 1: P-Control Math Test ===");
        
        // Test Cases (Simulated Limelight horizontal angle offsets tx in degrees)
        double[] testAngleErrors = {0.0, 10.0, -15.0, 25.0, -35.0};

        for (double tx : testAngleErrors) {
            double power = calculateTurnPower(tx);
            System.out.printf("Heading Error (tx): %6.1f deg  ==>  Calculated Turn Power: %+5.2f\n", tx, power);
        }
    }
}
