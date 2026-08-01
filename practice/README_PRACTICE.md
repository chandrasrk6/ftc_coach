# 🎓 FTC Java Practice Exercises: Path to Limelight Summon

Before deploying vision code to a physical FTC robot, students can master the core algorithms using these **Plain Java Practice Exercises**. These modules run on any computer without needing an FTC Control Hub or Limelight hardware.

---

## 📖 Student Lesson & Refresher Guide

👉 **Start Here:** Read **[STUDENT_LESSON.md](file:///c:/Users/chand/OneDrive/Documents/ftc_coach/practice/STUDENT_LESSON.md)** for a complete explanation of vision telemetry (`tx`/`ta`), Proportional Control math, Arcade Drive power mixing, safety rules, expectations, and goals!

---

## 🎯 Learning Objectives

1. Understand **Proportional Control (P-Controller)** math: `power = error * Kp`.
2. Learn **Power Clamping & Scaling** to prevent motor overload.
3. Master **Arcade Drive Power Distribution** (mixing `drive` and `turn`).
4. Implement **Safety Deadman Switches & Emergency Thresholds**.

---

## 📚 Practice Modules

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

---

## 🚀 How Students Can Run These Exercises

Students can run these files in any IDE (IntelliJ IDEA, Eclipse, VS Code, or Android Studio):

1. Open the project folder in your preferred Java IDE.
2. Navigate to `practice/src/main/java/com/ftc/practice/`.
3. Right-click any exercise file (e.g. `Practice3_SummonSimulator.java`) and select **Run 'Practice3_SummonSimulator.main()'**.
