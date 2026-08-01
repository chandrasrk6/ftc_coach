package com.ftc.practice;

/**
 * EXERCISE 2: Arcade Drive Power Mixer & Normalization (Student Worksheet)
 * 
 * Objective:
 * Combine forward drive power and turn power into left/right wheel powers,
 * ensuring no motor receives a value greater than 1.0 or less than -1.0.
 * 
 * Instructions:
 * 1. Calculate leftPower (drive + turn) and rightPower (drive - turn).
 * 2. Find the maximum absolute magnitude between leftPower and rightPower.
 * 3. If the magnitude is > 1.0, divide both powers by the magnitude.
 */
public class Practice2_ArcadeDrive {

    public static double[] mixArcadeDrive(double drive, double turn) {
        // TODO #1: Calculate raw arcade drive powers
        double leftPower  = 0.0; // Replace with (drive + turn)
        double rightPower = 0.0; // Replace with (drive - turn)

        // TODO #2: Find the max magnitude between Math.abs(leftPower) and Math.abs(rightPower)
        double maxMagnitude = 1.0; // Replace with Math.max()

        // TODO #3: Scale both down if maxMagnitude > 1.0
        // Hint: leftPower /= maxMagnitude; rightPower /= maxMagnitude;

        return new double[]{leftPower, rightPower};
    }

    public static void main(String[] args) {
        System.out.println("=== Exercise 2: Arcade Drive Mixing (Student Practice) ===");

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
