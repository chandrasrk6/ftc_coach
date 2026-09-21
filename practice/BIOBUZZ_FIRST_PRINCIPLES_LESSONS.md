# 🐝 FTC Biobuzz Competition: Robot Control from First Principles
### 🎒 High School (10th Grade Edition)

Welcome to the **10th Grade FTC Robot Control Guide**! This textbook breaks down robotics into simple, common-sense truths based on **First Principles Thinking**.

Instead of guessing code numbers or copying mysterious blocks, you will learn *why* robots move the way they do using 10th-grade math (Algebra & Geometry), physics, and logic.

---

## 🎯 What is First Principles Thinking?

> **First Principles Thinking:** Taking a big, complicated problem, stripping away the guesswork, and rebuilding a solution using basic facts you already know.

### Analogy: Driving a Go-Kart
Imagine you are driving a go-kart toward a stop sign:
- If you guess and press the gas pedal for 3 seconds with your eyes closed, you might stop short or crash into a wall (**Open-Loop Guessing**).
- If you look at the sign, see how far away it is, and press the brakes harder when you get closer, you'll stop perfectly every time (**Closed-Loop Feedback**).

Robotics works the exact same way!

---

## 📚 Table of Contents

1. [Module 1: How Robots Move (10th Grade Geometry \& Physics)](#module-1-how-robots-move-10th-grade-geometry--physics)
2. [Module 2: How the Camera Sees the World (Limelight Vision)](#module-2-how-the-camera-sees-the-world-limelight-vision)
3. [Module 3: Controlling Speed \& Steering (PID + Feedforward)](#module-3-controlling-speed--steering-pid--feedforward)
4. [Module 4: Motor Limits \& Vector Power Mixing](#module-4-motor-limits--vector-power-mixing)
5. [Module 5: Bee Autonomy \& Robot State Machines (FSM)](#module-5-bee-autonomy--robot-state-machines-fsm)
6. [Module 6: Multi-Layer Safety Rules (Never Crash!)](#module-6-multi-layer-safety-rules-never-crash)

---

## Module 1: How Robots Move (10th Grade Geometry & Physics)

### 1. The 3 Ways a Robot Can Move (Degrees of Freedom)
On an FTC field, your robot lives in a 2D plane with 3 simple movements:

```
                  +Y (Drive Forward)
                         ▲
                         │   / Robot Heading (θ)
                         │  /
                         │ /
  (Strafe Left) ─────────┼─────────► +X (Strafe Right)
                         │
                         │
                  -Y (Drive Reverse)
```

1. **$Y$-Axis (Forward / Reverse):** Moving straight ahead or backward.
2. **$X$-Axis (Strafe Left / Right):** Sideways sliding (if using Mecanum wheels).
3. **$\theta$ (Heading / Angle):** Rotating left or right to change direction.

### 2. Pushing Against the Floor (Friction & Torque)
Motors turn gears, which spin wheels. But wheels only move the robot because of **friction** against the foam tiles!
- **Left Motors & Right Motors:** If both spin forward at equal speed, the robot drives straight. If one spins faster than the other, the robot turns!

$$\text{Left Motor Power} = \text{Drive Power} + \text{Turn Power}$$
$$\text{Right Motor Power} = \text{Drive Power} - \text{Turn Power}$$

---

## Module 2: How the Camera Sees the World (Limelight Vision)

### 1. The Limelight's Two Main Numbers (`tx` and `ta`)
A camera doesn't know "the flower is 3 feet away." It sends two simple numbers to your robot 90 times per second:

```
                     Limelight Camera View
               ┌──────────────────────────────┐
               │    tx < 0        tx > 0      │
               │   (Target        (Target     │
               │    Left)          Right)     │
               │            [  🌺  ]          │
               │           (Area  ta%)        │
               └──────────────┬───────────────┘
                          Center (tx = 0°)
```

1. **`tx` (Heading Error in Degrees):**
   - **`tx = 0°`:** The target is dead center in the crosshairs!
   - **`tx = -15°`:** Target is to the **left**. Turn left to center it!
   - **`tx = +15°`:** Target is to the **right**. Turn right to center it!

2. **`ta` (Target Area Percentage):**
   - **`ta = 1.0%`:** Target is **far away** (looks tiny).
   - **`ta = 12.0%`:** Target is **close** (looks big).

> ✏️ **10th Grade Math Rule:** Target Area ($ta$) gets bigger as you get closer. To drive toward a target, your error is:
> $$\text{Distance Error} = \text{Target Area Desired} - \text{Current Area } (ta)$$

---

## Module 3: Controlling Speed & Steering (PID + Feedforward)

### Why Fixed Timers Fail (The Dying Battery Problem)
If you program your robot: `"Drive forward at 50% power for 2 seconds"`, what happens when the battery drops from 13.5 Volts to 11.5 Volts?
- **Result:** The motors spin slower, and your robot stops 1 foot short of the flower!

### The Solution: Smart Feedback Control
Instead of guessing time, calculate motor power continuously based on **Error**:

$$\text{Error} = \text{Target Position} - \text{Current Position}$$

---

### The 3 Parts of PID Control (Explained Simply)

#### 1. Proportional ($P$) — "How far away am I?"
The further away you are, the faster you drive. As you get close, you slow down automatically!

$$\text{Turn Power} = \text{Heading Error } (tx) \times K_p$$

> 💡 **Math Example:**
> - Suppose $tx = +10^\circ$ off-center, and $K_p = 0.025$.
> - $\text{Turn Power} = 10 \times 0.025 = 0.25$ (25% motor power to the right).

---

#### 2. Derivative ($D$) — "How fast am I closing the gap?"
If you approach a stop sign too fast, you'll overshoot and slam the brakes. The **$D$-term** measures how fast the error is changing ($\Delta \text{Error}$) and acts as a dampener to prevent overshooting:

$$\text{Change in Error } (\Delta e) = \text{Current Error} - \text{Previous Error}$$
$$\text{D-Power} = \Delta e \times K_d$$

---

#### 3. Static Friction Feedforward ($k_S$) — "The Minimum Nudge"
Real robot motors have gear friction. If motor power is less than $8\%$ ($0.08$), the robot hums but doesn't move!

- **Solution:** Always add a tiny minimum nudge in the direction of error so the robot never gets stuck $0.5^\circ$ away:

$$\text{Power} = (\text{P-Power}) + (\text{D-Power}) + \text{Static Feedforward } (k_S)$$

---

## Module 4: Motor Limits & Vector Power Mixing

### Preventing Motor Saturation Distortion
FTC motor power can only be between `-1.0` (100% reverse) and `+1.0` (100% forward).

What happens if your code calculates:
- `drivePower = 0.8`
- `turnPower = 0.4`

If you add them together:
- `leftMotor = 0.8 + 0.4 = 1.2` 💥 *(Too high! Motor caps at 1.0)*
- `rightMotor = 0.8 - 0.4 = 0.4`

> ⚠️ **The Problem:** The left motor caps at $1.0$ instead of $1.2$. Because the left motor can't go any faster, your turn gets distorted and the robot turns less than you commanded!

### The 10th Grade Normalization Fix
If any motor exceeds $1.0$, divide **both** motors by the largest number so the steering ratio stays perfect:

```java
double maxPower = Math.max(Math.abs(leftMotor), Math.abs(rightMotor));
if (maxPower > 1.0) {
    leftMotor /= maxPower;
    rightMotor /= maxPower;
}
```

---

## Module 5: Bee Autonomy & Robot State Machines (FSM)

In the **Biobuzz** competition, your robot acts like a worker bee:
1. Search for a flower beacon.
2. Fly (drive) toward the flower.
3. Collect pollen.
4. Return home to the hive!

```
┌──────────────────┐       Flower Seen (isValid = true)      ┌──────────────────────┐
│  1. SEARCH_FLOWER │ ──────────────────────────────────────► │ 2. ALIGN_AND_APPROACH│
└──────────────────┘                                         └──────────┬───────────┘
          ▲                                                             │
          │               Target Lost / Harvest Complete                │ Area >= Target
          └─────────────────────────────────────────────────────────────┘
```

### The Non-Blocking Rule
**NEVER** use `Thread.sleep(2000)` in your robot code! If you sleep the code, the robot cannot read sensors, update vision, or listen to driver emergency stops.

Instead, use a **State Machine** that checks conditions on every loop iteration (~50 times a second).

---

## Module 6: Multi-Layer Safety Rules (Never Crash!)

To keep your robot and teammates safe, always build **3 Layers of Defense**:

| Safety Layer | What Triggered It? | What Does the Robot Do? |
| :--- | :--- | :--- |
| **Layer 1: Deadman Switch** | Driver releases `Right Trigger` on gamepad | Immediately cuts motor powers to `0.0`. |
| **Layer 2: Vision Dropout** | Target walks out of frame (`isValid = false`) | Stops driving forward and begins searching safely. |
| **Layer 3: Proximity Halt** | Target Area $ta \ge 22.0\%$ (Too close!) | Blocks forward drive power so the robot won't crash into a student or wall. |

---

## 🚀 Time to Code! Try Exercise 4 in Java
Now that you know the principles, open your IDE and run:
👉 **[Practice4_BiobuzzFSM.java](file:///c:/Users/chand/OneDrive/Documents/ftc_coach/practice/src/main/java/com/ftc/practice/Practice4_BiobuzzFSM.java)**

Fill in the `// TODO` items using the formulas you learned in this lesson!
