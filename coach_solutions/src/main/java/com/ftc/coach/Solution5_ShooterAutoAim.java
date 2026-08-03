package com.ftc.coach;

/**
 * COACH SOLUTION KEY: Exercise 5 - Shooter Auto-Aim & Ready-to-Fire
 */
public class Solution5_ShooterAutoAim {

    private static final double AIM_TOLERANCE_DEG = 1.5;
    private static final double RPM_TOLERANCE = 75.0;

    public static boolean isReadyToFire(double tx, double currentRPM, double targetRPM) {
        boolean headingAligned = Math.abs(tx) <= AIM_TOLERANCE_DEG;
        boolean speedReady = Math.abs(currentRPM - targetRPM) <= RPM_TOLERANCE;
        return headingAligned && speedReady;
    }

    public static void main(String[] args) {
        System.out.println("=== Coach Solution Key: Exercise 5 ===");

        double[][] scenarios = {
            { 5.2, 2500, 2500 },
            { 0.4, 2100, 2500 },
            { 0.8, 2480, 2500 },
            { -1.2, 2550, 2500 }
        };

        for (int i = 0; i < scenarios.length; i++) {
            double tx = scenarios[i][0];
            double currentRPM = scenarios[i][1];
            double targetRPM = scenarios[i][2];

            boolean ready = isReadyToFire(tx, currentRPM, targetRPM);
            System.out.printf("Scenario %d | tx: %+4.1f° | Current: %4.0f RPM | Target: %4.0f RPM ==> Ready: %s\n",
                    i + 1, tx, currentRPM, targetRPM, ready ? "🔥 FIRE BALL!" : "⏳ SPINNING/ALIGNING");
        }
    }
}
