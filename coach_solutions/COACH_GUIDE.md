# 📋 FTC Coach Teaching Guide & Solution Key

This guide is designed for **FTC Coaches, Mentors, and Lead Programmers** to guide students through vision tracking, feedback control math, flywheel shooter mechanisms, and robot safety logic before deploying to physical hardware.

---

## 📂 Folder Structure Overview

- **`practice/` (Student Worksheets):** Contains starter Java files with `// TODO` prompts for students to write.
- **`coach_solutions/` (Coach Answer Keys):** Fully solved Java files with implementation references and expected console outputs.

---

## 🎯 Discussion Questions & Common Pitfalls

### Exercise 1: Proportional Control
- **Question for Students:** *"What happens if we double `KP_TURN` from `0.025` to `0.05`?"*
  - **Answer:** The robot will turn more aggressively toward the target, but if `KP_TURN` is set too high, it will oscillate/wobble back and forth.
- **Common Pitfall:** Forgetting to clamp power values. Without clamping, large angle errors (`tx = 30`) produce `power = 0.75`, causing the robot to jerk violently.

### Exercise 2: Arcade Drive Power Mixing
- **Question for Students:** *"Why can't we just set motor powers to `drive + turn` directly without scaling?"*
  - **Answer:** If `drive = 0.8` and `turn = 0.6`, raw `leftPower = 1.4`. Motor controllers cap at `1.0`, which distorts the turning ratio! Normalizing both values preserves the exact intended turn radius.

### Exercise 3: Summon Safety Simulator
- **Question for Students:** *"Why is a deadman switch (trigger button) required for Summon mode?"*
  - **Answer:** If the vision camera misidentifies a background object or person, an un-gated robot might autonomously drive into a wall or spectator. Releasing the trigger must immediately halt all motors.

### Exercise 4: Flywheel Velocity Math
- **Question for Students:** *"Why do we use FTC `DcMotorEx.setVelocity()` instead of `setPower()` for a flywheel?"*
  - **Answer:** As the battery drains during a 2.5-minute match, `setPower(0.8)` produces lower RPM. Closed-loop `setVelocity()` uses motor encoders and internal PIDF to keep RPM constant regardless of battery voltage!
- **Common Pitfall:** Not capping max RPM. Always clamp target RPM to the motor's physical maximum rating.

### Exercise 5: Shooter Auto-Aim & Ready-to-Fire State Machine
- **Question for Students:** *"Why must both heading alignment AND flywheel speed be ready before firing?"*
  - **Answer:** Firing while turning causes the ball to miss left/right. Firing before the flywheel reaches full RPM causes the shot to fall short.

---

## 🧪 Expected Console Outputs for Coach Solutions

### Solution 1 Output
```
=== Coach Solution Key: Exercise 1 ===
Heading Error (tx):    0.0 deg  ==>  Calculated Turn Power: +0.00
Heading Error (tx):   10.0 deg  ==>  Calculated Turn Power: +0.25
Heading Error (tx):  -15.0 deg  ==>  Calculated Turn Power: -0.38
Heading Error (tx):   25.0 deg  ==>  Calculated Turn Power: +0.40
Heading Error (tx):  -35.0 deg  ==>  Calculated Turn Power: -0.40
```

### Solution 2 Output
```
=== Coach Solution Key: Exercise 2 ===
Drive: +0.5, Turn: +0.2  ==>  Left Motor: +0.70 | Right Motor: +0.30
Drive: +0.8, Turn: +0.6  ==>  Left Motor: +1.00 | Right Motor: +0.14
Drive: -0.4, Turn: +0.3  ==>  Left Motor: -0.10 | Right Motor: -0.70
Drive: +0.0, Turn: +1.0  ==>  Left Motor: +1.00 | Right Motor: -1.00
```

### Solution 4 Output
```
=== Coach Solution Key: Exercise 4 ===
Target Area (ta):  2.0%  ==>  Goal Post RPM: 3261  |  Person Catch RPM: 1836
Target Area (ta):  5.0%  ==>  Goal Post RPM: 2871  |  Person Catch RPM: 1602
Target Area (ta):  8.0%  ==>  Goal Post RPM: 2730  |  Person Catch RPM: 1518
Target Area (ta): 15.0%  ==>  Goal Post RPM: 2587  |  Person Catch RPM: 1432
```

### Solution 5 Output
```
=== Coach Solution Key: Exercise 5 ===
Scenario 1 | tx: +5.2° | Current: 2500 RPM | Target: 2500 RPM ==> Ready: ⏳ SPINNING/ALIGNING
Scenario 2 | tx: +0.4° | Current: 2100 RPM | Target: 2500 RPM ==> Ready: ⏳ SPINNING/ALIGNING
Scenario 3 | tx: +0.8° | Current: 2480 RPM | Target: 2500 RPM ==> Ready: 🔥 FIRE BALL!
Scenario 4 | tx: -1.2° | Current: 2550 RPM | Target: 2500 RPM ==> Ready: 🔥 FIRE BALL!
```
