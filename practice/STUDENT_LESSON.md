# 📚 Student Lesson: FTC Robot Vision Tracking & Feedback Control

Welcome to the **Limelight Robot Summon & Target Follower** lesson! This document explains the core robotics concepts, math formulas, safety rules, and expectations for completing your programming lab.

---

## 🎯 Lesson Goal & Expectations

### The Goal
By the end of this lesson, you will understand how a robot uses camera sensor data to autonomously align itself with a student, drive toward them, and stop at a safe distance.

### What is Expected of You
1. **Understand Vision Telemetry:** Learn how the Limelight camera measures angle offset (`tx`) and distance proxy (`ta`).
2. **Master Proportional Control (P-Control):** Implement the formula `power = error * Kp` to smoothly control steering and speed.
3. **Prevent Motor Saturation:** Use clamping and normalization so motor powers stay within safe operating ranges.
4. **Prioritize Safety:** Program fail-safes (deadman trigger switch, target-loss cutoff, emergency proximity halt).
5. **Complete Practice Worksheets:** Solve the Java worksheets in `practice/` before deploying code to the physical FTC robot Control Hub.

---

## 🧠 Concept Refresher

### 1. Vision Camera Telemetry (`tx` & `ta`)

The Limelight camera processes video frames at 100 Hz and sends targeting data to your OpMode:

```
                  ┌──────────────────────────────┐
                  │          Camera View         │
                  │                              │
                  │           tx < 0   tx > 0    │
                  │             │        │       │
                  │             ▼        ▼       │
                  │         [ Target ]           │
                  │         (Area ta %)          │
                  │                              │
                  └──────────────┼───────────────┘
                              Center 
                             (tx = 0)
```

- **Heading Error (`tx`):** Horizontal offset from the camera crosshair measured in degrees (range: `-31°` to `+31°`).
  - If `tx < 0`: Target is to the **left** of center (robot must turn left).
  - If `tx > 0`: Target is to the **right** of center (robot must turn right).
  - If `tx = 0`: Target is perfectly centered.
- **Target Area (`ta`):** Percentage of the camera frame occupied by the target (range: `0%` to `100%`).
  - Small `ta` (e.g. `2.0%`): Target is **far away** (robot drives forward).
  - Large `ta` (e.g. `18.0%`): Target is **very close** (robot must stop!).

---

### 2. Proportional (P) Controller Math

Instead of driving at a fixed speed, a **Proportional Controller** adjusts motor power based on how large the error is:

$$\text{Motor Power} = \text{Error} \times K_p$$

- **Steering Power:**
  $$\text{turnPower} = \text{tx} \times K_p^{\text{turn}}$$
- **Drive Power:**
  $$\text{drivePower} = (\text{Desired Area} - \text{ta}) \times K_p^{\text{drive}}$$

> 💡 **Why P-Control?** When the target is far off-center, the error is large, so the robot turns quickly. As the target approaches the center, the error shrinks to zero, causing the robot to slow down and stop smoothly without overshooting!

---

### 3. Power Clamping & Arcade Drive Mixing

Motors cannot receive power values outside the range `[-1.0, 1.0]`. For safety during testing, we clamp speeds to `[-0.4, 0.4]`.

#### Step A: Clamping
```java
drivePower = Math.max(-0.4, Math.min(0.4, drivePower));
turnPower  = Math.max(-0.4, Math.min(0.4, turnPower));
```

#### Step B: Arcade Drive Distribution
To combine forward motion (`drive`) and rotation (`turn`), mix the powers into left and right motor pairs:

```java
leftPower  = drivePower + turnPower;
rightPower = drivePower - turnPower;
```

#### Step C: Normalization
If `leftPower` or `rightPower` exceeds `1.0`, divide both by the largest magnitude:

```java
double max = Math.max(Math.abs(leftPower), Math.abs(rightPower));
if (max > 1.0) {
    leftPower /= max;
    rightPower /= max;
}
```

---

### 4. Essential Safety Rules

Robots moving autonomously near people must have multi-layered safety mechanisms:

1. **Gamepad Deadman Switch:** Summon mode MUST require continuous press on Gamepad 1 (Right Trigger). Releasing the trigger immediately cuts all motor power to `0`.
2. **Target Loss Fail-Safe:** If `result.isValid() == false` (target walked out of frame or lighting changed), set motor powers to `0`.
3. **Proximity Safety Halt:** If target area `ta > MIN_SAFETY_AREA` (target is closer than ~2 feet), force `drivePower <= 0` to prevent crashing into the student.

---

## 📋 Student Self-Assessment Checklist

Before moving from Java practice code to the physical FTC robot, verify you can answer:

- [ ] Can you explain what `tx` and `ta` mean in Limelight telemetry?
- [ ] What happens if $K_p$ is set too high vs. too low?
- [ ] Why do we normalize motor powers in Arcade Drive mixing?
- [ ] Does your code stop all motors immediately if the target disappears or the driver releases the trigger?

---

## 🛠️ Step-by-Step Lab Progression

1. **Step 1:** Open `practice/src/main/java/com/ftc/practice/Practice1_PControl.java` and complete `// TODO #1` & `// TODO #2`.
2. **Step 2:** Open `practice/src/main/java/com/ftc/practice/Practice2_ArcadeDrive.java` and complete `// TODO #1`, `#2`, & `#3`.
3. **Step 3:** Open `practice/src/main/java/com/ftc/practice/Practice3_SummonSimulator.java` and implement all 6 scenario step handlers.
4. **Step 4:** Deploy `LimelightSummonOpMode.java` to the FTC Control Hub and tune $K_p^{\text{turn}}$ and $K_p^{\text{drive}}$ on the real robot!
