package com.ftc.coach;

/**
 * COACH SOLUTION KEY: Exercise 3 - Complete Robot Summon Simulator
 */
public class Solution3_SummonSimulator {

    private static final double KP_TURN = 0.025;
    private static final double KP_DRIVE = 0.035;
    private static final double TARGET_AREA_DESIRED = 8.0;
    private static final double MIN_SAFETY_AREA = 18.0;
    private static final double MAX_POWER_LIMIT = 0.4;

    static class FrameData {
        boolean targetValid;
        double tx;
        double ta;
        boolean deadmanPressed;

        public FrameData(boolean targetValid, double tx, double ta, boolean deadmanPressed) {
            this.targetValid = targetValid;
            this.tx = tx;
            this.ta = ta;
            this.deadmanPressed = deadmanPressed;
        }
    }

    public static void processTelemetryFrame(int step, FrameData frame) {
        System.out.printf("--- Step %d ---\n", step);

        if (!frame.deadmanPressed) {
            System.out.println("  State: STANDBY (Driver released trigger - Motors 0)");
            return;
        }

        if (!frame.targetValid) {
            System.out.println("  State: SEARCHING (No valid target in sight - Motors 0)");
            return;
        }

        double turnPower = frame.tx * KP_TURN;
        double areaError = TARGET_AREA_DESIRED - frame.ta;
        double drivePower = areaError * KP_DRIVE;

        if (frame.ta > MIN_SAFETY_AREA) {
            drivePower = Math.min(drivePower, 0);
            System.out.println("  ⚠️ SAFETY ALERT: Target too close! Forward drive disabled.");
        }

        drivePower = Math.max(-MAX_POWER_LIMIT, Math.min(MAX_POWER_LIMIT, drivePower));
        turnPower  = Math.max(-MAX_POWER_LIMIT, Math.min(MAX_POWER_LIMIT, turnPower));

        double leftMotor  = drivePower + turnPower;
        double rightMotor = drivePower - turnPower;

        System.out.printf("  Target: LOCKED | tx: %+5.1f deg | ta: %4.1f %%\n", frame.tx, frame.ta);
        System.out.printf("  Outputs -> Drive Power: %+5.2f | Turn Power: %+5.2f\n", drivePower, turnPower);
        System.out.printf("  Motors  -> Left: %+5.2f | Right: %+5.2f\n", leftMotor, rightMotor);
    }

    public static void main(String[] args) {
        System.out.println("==================================================");
        System.out.println("    Coach Solution Key: Exercise 3 Simulator      ");
        System.out.println("==================================================\n");

        FrameData[] scenario = {
            new FrameData(true,  15.0, 3.0,  true),
            new FrameData(true,   8.0, 5.0,  true),
            new FrameData(true,   0.5, 8.0,  true),
            new FrameData(true,  -2.0, 22.0, true),
            new FrameData(false,  0.0, 0.0,  true),
            new FrameData(true, -10.0, 6.0,  false)
        };

        for (int i = 0; i < scenario.length; i++) {
            processTelemetryFrame(i + 1, scenario[i]);
            System.out.println();
        }
    }
}
