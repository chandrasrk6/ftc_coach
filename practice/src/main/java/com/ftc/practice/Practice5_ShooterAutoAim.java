package com.ftc.practice;

/**
 * EXERCISE 5: Shooter Auto-Aim & Ready-to-Fire State Machine (Student Worksheet)
 * 
 * Objective:
 * Implement the state machine that evaluates heading alignment (tx) and
 * flywheel velocity (RPM) before triggering the ball feeder servo.
 * 
 * Instructions:
 * 1. Check if heading error |tx| <= AIM_TOLERANCE_DEG (1.5 degrees).
 * 2. Check if flywheel velocity error |currentRPM - targetRPM| <= RPM_TOLERANCE (75 RPM).
 * 3. If BOTH conditions are true, trigger feeder servo to launch!
 */
public class Practice5_ShooterAutoAim {

    private static final double AIM_TOLERANCE_DEG = 1.5;
    private static final double RPM_TOLERANCE = 75.0;

    public static boolean isReadyToFire(double tx, double currentRPM, double targetRPM) {
        // TODO #1: Calculate absolute heading error in degrees
        boolean headingAligned = false; // Replace with Math.abs(tx) <= AIM_TOLERANCE_DEG

        // TODO #2: Calculate absolute flywheel velocity error in RPM
        boolean speedReady = false; // Replace with Math.abs(currentRPM - targetRPM) <= RPM_TOLERANCE

        // TODO #3: Return true ONLY if heading is aligned AND speed is ready!
        return headingAligned && speedReady;
    }

    public static void main(String[] args) {
        System.out.println("=== Exercise 5: Shooter Ready-To-Fire Test (Student Practice) ===");

        // Test Scenarios: {tx (deg), currentRPM, targetRPM}
        double[][] scenarios = {
            { 5.2, 2500, 2500 }, // Off-center, but RPM ready -> Should NOT fire
            { 0.4, 2100, 2500 }, // Aligned, but flywheel spinning up -> Should NOT fire
            { 0.8, 2480, 2500 }, // Aligned AND RPM ready! -> SHOULD FIRE! 🔥
            { -1.2, 2550, 2500 } // Aligned AND RPM ready! -> SHOULD FIRE! 🔥
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
