package com.ftc.coach;

/**
 * COACH SOLUTION KEY: Exercise 1 - Proportional (P) Control Math
 */
public class Solution1_PControl {

    private static final double KP_TURN = 0.025; // Steering proportional gain
    private static final double MAX_SPEED = 0.4;  // Safe max power limit

    public static double calculateTurnPower(double tx) {
        // Step 1: Calculate raw power using proportional formula
        double rawPower = tx * KP_TURN;

        // Step 2: Clamp power between -MAX_SPEED and +MAX_SPEED
        double clampedPower = Math.max(-MAX_SPEED, Math.min(MAX_SPEED, rawPower));

        return clampedPower;
    }

    public static void main(String[] args) {
        System.out.println("=== Coach Solution Key: Exercise 1 ===");

        double[] testAngleErrors = {0.0, 10.0, -15.0, 25.0, -35.0};

        for (double tx : testAngleErrors) {
            double power = calculateTurnPower(tx);
            System.out.printf("Heading Error (tx): %6.1f deg  ==>  Calculated Turn Power: %+5.2f\n", tx, power);
        }
    }
}
