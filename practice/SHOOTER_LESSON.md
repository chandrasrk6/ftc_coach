# 🏀 Student Lesson: Flywheel Ball Shooter & Auto-Aim Mechanism

Welcome to the **Flywheel Ball Shooter & Dual Target Mechanism** lesson! In this module, you will learn how an FTC robot uses a Limelight vision camera and motor velocity feedback (PIDF) to shoot balls into a **Goal Post (AprilTag)** or deliver a soft toss to a **Person (Student Catch)**.

---

## 🎯 Lesson Goal & Dual Target Modes

### The Goal
Program a robot to automatically measure distance to a target using Limelight vision, auto-align its heading (`tx`), accelerate the flywheel to the exact required RPM, and fire a ball when ready!

### Target Modes
1. **Mode A: Goal Post Shot (AprilTag - Pipeline 0):**
   - **Goal:** Shoot into a high goal target or AprilTag basket.
   - **Trajectory:** Fast, flat trajectory requires higher flywheel RPM (e.g., 2200 to 4500 RPM).
2. **Mode B: Person Catch (Student Catch - Pipeline 1):**
   - **Goal:** Pass a ball safely to a student for a catch.
   - **Trajectory:** Soft, lofted arc requires lower, controlled flywheel RPM (e.g., 1200 to 2500 RPM).

---

## 🧠 Core Engineering Concepts

### 1. Flywheel Motor Velocity Control (FTC `DcMotorEx` PIDF)
Unlike drivetrain motors where we set raw power (`0.0` to `1.0`), a flywheel shooter **must** maintain exact RPM regardless of battery voltage drops.

$$\text{RPM} = \left(\frac{\text{Encoder Ticks per Second}}{\text{Ticks per Revolution}}\right) \times 60$$

We use `flywheelMotor.setVelocity(ticksPerSecond)` which enables internal FTC Closed-Loop PIDF control!

---

### 2. Distance Estimation & Velocity Calculation

The distance to the target is estimated from the Limelight target area (`ta %`):

$$\text{Distance (feet)} \approx \sqrt{\frac{100}{\text{ta}}}$$

#### Velocity Equations:
- **Goal Post Mode:**
  $$\text{Target RPM} = 2200 + (\text{Distance} \times 150)$$
- **Person Catch Mode:**
  $$\text{Target RPM} = 1200 + (\text{Distance} \times 90)$$

---

### 3. Auto-Aiming & Ready-to-Fire State Machine

Before the robot indexer servo pushes a ball into the flywheel, it must verify **two readiness conditions**:

```
        ┌──────────────────────────────────────────────┐
        │                 Auto-Aiming                  │
        │                                              │
        │ 1. Heading Aligned?   |tx| <= 1.5 degrees    │
        │ 2. Flywheel Ready?    |RPM_err| <= 75 RPM    │
        └──────────────────────┬───────────────────────┘
                               │
                      YES to BOTH Conditions
                               │
                               ▼
                    🔥 Trigger Feeder Servo
```

---

## 📋 Student Self-Assessment Checklist

- [ ] I can explain why flywheels require `DcMotorEx` velocity PIDF control instead of raw motor power.
- [ ] I understand the difference between Goal Post trajectory (high speed) vs Person Catch trajectory (soft loft).
- [ ] I know the two conditions required before `isReadyToFire()` returns `true`.
- [ ] I can explain how the feeder servo indexes the ball into the flywheel.

---

## 🛠️ Step-by-Step Lab Progression

1. **Step 1:** Complete `practice/src/main/java/com/ftc/practice/Practice4_FlywheelVelocity.java`.
2. **Step 2:** Complete `practice/src/main/java/com/ftc/practice/Practice5_ShooterAutoAim.java`.
3. **Step 3:** Review `coach_solutions/src/main/java/com/ftc/coach/` solutions.
4. **Step 4:** Test `FlywheelShooterOpMode.java` on the FTC robot!
