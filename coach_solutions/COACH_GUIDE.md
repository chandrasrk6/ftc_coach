# 📋 FTC Coach Teaching Guide & Solution Key

This guide is designed for **FTC Coaches, Mentors, and Lead Programmers** to guide students through vision tracking, feedback control math, and robot safety logic before deploying to physical hardware.

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
