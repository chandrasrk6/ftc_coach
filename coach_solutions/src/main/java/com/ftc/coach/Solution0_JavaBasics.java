package com.ftc.coach;

/**
 * COACH SOLUTION KEY: Exercise 0 - Java Foundations for FTC Robotics
 */
public class Solution0_JavaBasics {

    public enum RobotState {
        STOPPED,
        DRIVING_FORWARD,
        TARGET_LOCKED,
        SAFETY_HALT
    }

    public static double calculateTurnPower(double tx) {
        double rawPower = tx * 0.025;
        return Math.max(-0.4, Math.min(0.4, rawPower));
    }

    public static RobotState determineRobotState(boolean triggerPressed, boolean targetSeen, double targetArea) {
        if (!triggerPressed) {
            return RobotState.STOPPED;
        } else if (targetSeen && targetArea > 18.0) {
            return RobotState.SAFETY_HALT;
        } else if (targetSeen) {
            return RobotState.TARGET_LOCKED;
        } else {
            return RobotState.DRIVING_FORWARD;
        }
    }

    public static double calculateLeftMotor(double drivePower, double turnPower) {
        return drivePower + turnPower;
    }

    public static void main(String[] args) {
        System.out.println("==========================================================");
        System.out.println("   Coach Solution Key: Exercise 0 Java Foundations       ");
        System.out.println("==========================================================\n");

        double tx = 12.0;
        double turnPower = calculateTurnPower(tx);
        System.out.printf("Test 1 - Heading Error tx = %+4.1f deg -> Calculated Turn Power: %+5.2f\n", tx, turnPower);

        RobotState state1 = determineRobotState(false, true, 5.0);
        RobotState state2 = determineRobotState(true, true, 20.0);
        RobotState state3 = determineRobotState(true, true, 8.0);

        System.out.println("Test 2 - State 1 (Trigger Released) Expected: STOPPED      | Result: " + state1);
        System.out.println("Test 2 - State 2 (Target Too Close) Expected: SAFETY_HALT  | Result: " + state2);
        System.out.println("Test 2 - State 3 (Normal Approach)  Expected: TARGET_LOCKED | Result: " + state3);

        double leftMotor = calculateLeftMotor(0.3, 0.15);
        System.out.printf("\nTest 3 - Drive: +0.30 | Turn: +0.15 -> Left Motor: %+5.2f (Expected: +0.45)\n", leftMotor);
    }
}
