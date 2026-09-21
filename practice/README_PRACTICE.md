# 🎓 FTC Java Practice Exercises: Path to Limelight Summon

Before deploying vision code to a physical FTC robot, students can master the core algorithms using these **Plain Java Practice Exercises**. These modules run on any computer without needing an FTC Control Hub or Limelight hardware.

---

## 📖 Student Lesson & Refresher Guide

☕ **Java Beginner Primer:** Read **[JAVA_FOUNDATIONS_FOR_FTC.md](file:///c:/Users/chand/OneDrive/Documents/ftc_coach/practice/JAVA_FOUNDATIONS_FOR_FTC.md)** for a beginner-friendly 10th-grade guide to Java variables, if-else logic, custom methods, enums, and FTC OpModes.

👉 **Start Here:** Read **[STUDENT_LESSON.md](file:///c:/Users/chand/OneDrive/Documents/ftc_coach/practice/STUDENT_LESSON.md)** for a complete explanation of vision telemetry (`tx`/`ta`), Proportional Control math, Arcade Drive power mixing, safety rules, expectations, and goals!

🐝 **Biobuzz Special:** Read **[BIOBUZZ_FIRST_PRINCIPLES_LESSONS.md](file:///c:/Users/chand/OneDrive/Documents/ftc_coach/practice/BIOBUZZ_FIRST_PRINCIPLES_LESSONS.md)** for a deep dive into robot control engineering derived from First Principles (Kinematics, Sensor Noise, PID + Feedforward, Vector Normalization, Non-blocking FSMs, and Multi-layer Safety).

---

## 🎯 Learning Objectives

0. Master **Java Foundations**: variables, if-else logic, methods, and enums for FTC.
1. Understand **Proportional Control (P-Controller)** math: `power = error * Kp`.
2. Learn **Power Clamping & Scaling** to prevent motor overload.
3. Master **Arcade Drive Power Distribution** (mixing `drive` and `turn`).
4. Implement **Safety Deadman Switches & Emergency Thresholds**.
5. Build **Finite State Machines (FSM)** with Closed-Loop PID and Vector Normalization.

---

## 📚 Practice Modules

### Exercise 0: Java Foundations for FTC
- **File:** [Practice0_JavaBasics.java](file:///c:/Users/chand/OneDrive/Documents/ftc_coach/practice/src/main/java/com/ftc/practice/Practice0_JavaBasics.java)
- **Concepts:**
  - Variables & data types (`double`, `boolean`, `int`, `String`).
  - Conditional decision logic (`if` / `else if` / `else`).
  - Writing custom calculation methods.
  - Using Enums for robot states.

### Exercise 1: Proportional (P) Control Math
- **File:** [Practice1_PControl.java](file:///c:/Users/chand/OneDrive/Documents/ftc_coach/practice/src/main/java/com/ftc/practice/Practice1_PControl.java)
- **Concepts:**
  - Calculating heading error (`tx`) from camera crosshair.
  - Applying `KP_TURN` gain.
  - Clamping values to safe speed range (`-0.4` to `+0.4`).

### Exercise 2: Arcade Drive Power Mixer
- **File:** [Practice2_ArcadeDrive.java](file:///c:/Users/chand/OneDrive/Documents/ftc_coach/practice/src/main/java/com/ftc/practice/Practice2_ArcadeDrive.java)
- **Concepts:**
  - Mixing forward `drive` power and rotational `turn` power:
    ```java
    leftPower  = drive + turn;
    rightPower = drive - turn;
    ```
  - Normalizing motor powers so no motor exceeds `1.0` or `-1.0`.

### Exercise 3: Complete Target Tracker Simulator
- **File:** [Practice3_SummonSimulator.java](file:///c:/Users/chand/OneDrive/Documents/ftc_coach/practice/src/main/java/com/ftc/practice/Practice3_SummonSimulator.java)
- **Concepts:**
  - Simulates 6 continuous time steps of a student walking around the robot.
  - Handles target lost state (`result.isValid() == false`).
  - Enforces driver deadman switch (`gamepad1.right_trigger`).
  - Triggers emergency stop if the student gets too close (`ta > MIN_SAFETY_AREA`).

### Exercise 4: Biobuzz Finite State Machine & Closed-Loop Control
- **File:** [Practice4_BiobuzzFSM.java](file:///c:/Users/chand/OneDrive/Documents/ftc_coach/practice/src/main/java/com/ftc/practice/Practice4_BiobuzzFSM.java)
- **Concepts:**
  - Non-blocking Finite State Machine transitions (`SEARCH_BEACON`, `ALIGN_AND_APPROACH`, `HARVEST_POLLEN`, `RETURN_TO_HIVE`).
  - Closed-loop PID control with static friction feedforward ($k_S$).
  - Vector power normalization under saturation.
  - Multi-layered fail-safe triggers.

---

## 🚀 How Students Can Run These Exercises

Students can run these files in any IDE (IntelliJ IDEA, Eclipse, VS Code, or Android Studio):

1. Open the project folder in your preferred Java IDE.
2. Navigate to `practice/src/main/java/com/ftc/practice/`.
3. Right-click any exercise file (e.g. `Practice4_BiobuzzFSM.java`) and select **Run 'Practice4_BiobuzzFSM.main()'**.

