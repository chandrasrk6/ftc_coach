package com.ftc.practice;

/**
 * EXERCISE 2: Arcade Drive Power Mixer & Normalization
 * 
 * Objective:
 * Combine forward drive power and turn power into left/right wheel powers,
 * ensuring no motor receives a value greater than 1.0 or less than -1.0.
 */
public class Practice2_ArcadeDrive {

    public static double[] mixArcadeDrive(double drive, double turn) {
        // Step 1: Calculate raw arcade drive powers
        double leftPower  = drive + turn;
        double rightPower = drive - turn;

        // Step 2: Find the maximum absolute magnitude between left and right
        double maxMagnitude = Math.max(Math.abs(leftPower), Math.abs(rightPower));

        // Step 3: If magnitude exceeds 1.0, scale both down proportionally
        if (maxMagnitude > 1.0) {
            leftPower /= maxMagnitude;
            rightPower /= maxMagnitude;
        }

        return new double[]{leftPower, rightPower};
    }

    public static void main(String[] args) {
        System.out.println("=== Exercise 2: Arcade Drive Mixing Test ===");

        // Test Inputs: {drive, turn}
        double[][] inputs = {
            {0.5, 0.2},   // Moving forward while turning right slightly
            {0.8, 0.6},   // High forward + high turn (exceeds 1.0! needs normalization)
            {-0.4, 0.3},  // Reversing while turning
            {0.0, 1.0}    // Pure spin in place
        };

        for (double[] input : inputs) {
            double drive = input[0];
            double turn  = input[1];
            double[] outputs = mixArcadeDrive(drive, turn);

            System.out.printf("Drive: %+4.1f, Turn: %+4.1f  ==>  Left Motor: %+5.2f | Right Motor: %+5.2f\n",
                    drive, turn, outputs[0], outputs[1]);
        }
    }
}
