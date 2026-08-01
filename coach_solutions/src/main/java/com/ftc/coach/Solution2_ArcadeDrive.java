package com.ftc.coach;

/**
 * COACH SOLUTION KEY: Exercise 2 - Arcade Drive Power Mixer
 */
public class Solution2_ArcadeDrive {

    public static double[] mixArcadeDrive(double drive, double turn) {
        // Step 1: Calculate raw arcade drive powers
        double leftPower  = drive + turn;
        double rightPower = drive - turn;

        // Step 2: Find maximum magnitude
        double maxMagnitude = Math.max(Math.abs(leftPower), Math.abs(rightPower));

        // Step 3: Scale down if max exceeds 1.0
        if (maxMagnitude > 1.0) {
            leftPower /= maxMagnitude;
            rightPower /= maxMagnitude;
        }

        return new double[]{leftPower, rightPower};
    }

    public static void main(String[] args) {
        System.out.println("=== Coach Solution Key: Exercise 2 ===");

        double[][] inputs = {
            {0.5, 0.2},
            {0.8, 0.6},
            {-0.4, 0.3},
            {0.0, 1.0}
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
