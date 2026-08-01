# FTC Robot Limelight Summon & Student Follower

An FTC (FIRST Tech Challenge) robot control implementation using a **Limelight 3A vision sensor** and a 4-motor drivetrain to follow a student or simulate an autonomous **"Summon"** feature.

---

## 🌟 Overview

This project provides an FTC OpMode (`LimelightSummonOpMode.java`) that allows a robot to dynamically detect, track, and maintain a fixed safe distance from a target person or AprilTag badge.

### Key Features
- **Proportional Control Loop (P-Controller):** Smoothly aligns heading (`tx`) and distance (`ta`).
- **Gamepad Safety Deadman Switch:** Requires continuous input on Gamepad 1 (Right Trigger) to keep Summon mode active.
- **Proximity Safety Halt:** Prevents forward motion if the target moves closer than a set threshold (`MIN_SAFETY_AREA`).
- **Fail-Safe Target Loss Protection:** Automatically zeroes all motor powers if target visibility is interrupted.

---

## 🛠️ Hardware & Software Requirements

- **FTC Control Hub / Expansion Hub** running FTC SDK 9.x+
- **Limelight 3A Vision Camera** (connected via Ethernet or USB-C)
- **4-Motor Drivetrain** (Mecanum, Tank, or Arcade configuration)
- **Gamepad Controller** (Logitech F310 or DualShock 4)
- **Target Beacon:** AprilTag Badge (e.g., Tag ID 1) or Limelight Neural Network Detector (Person Model).

---

## 📁 Repository Structure

```
ftc_coach/
├── TeamCode/
│   └── src/
│       └── main/
│           └── java/
│               └── org/
│                   └── firstinspires/
│                       └── ftc/
│                           └── LimelightSummonOpMode.java
├── .gitignore
└── README.md
```

---

## ⚙️ Configuration & Tuning

In `LimelightSummonOpMode.java`, adjust the following parameters for your robot's weight and motor gear ratios:

| Parameter | Default Value | Description |
| :--- | :--- | :--- |
| `KP_TURN` | `0.025` | Steering proportional gain for heading error (`tx`) |
| `KP_DRIVE` | `0.035` | Drive proportional gain for distance error (`ta`) |
| `TARGET_AREA_DESIRED` | `8.0` | Target percentage screen area (~3–4 ft stopping distance) |
| `MIN_SAFETY_AREA` | `18.0` | Emergency halt area threshold (too close to student) |

---

## 🚀 How to Run

1. Open this project directory in **Android Studio**.
2. Deploy the app to your FTC **Control Hub**.
3. Launch `Limelight Summon / Student Follower` from the **TeleOp / Autonomous** OpMode list on the Driver Station.
4. Press **START** and hold **Right Trigger** on Gamepad 1 to activate Summon mode.
