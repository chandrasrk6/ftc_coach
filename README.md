# FTC Robot Limelight Summon & Student Follower

An FTC (FIRST Tech Challenge) robot control implementation using a **Limelight 3A vision sensor** and a 4-motor drivetrain to follow a student or simulate an autonomous **"Summon"** feature.

---

## 🌟 Overview

This project provides an FTC OpMode (`LimelightSummonOpMode.java`) that allows a robot to dynamically detect, track, and maintain a fixed safe distance from a target person using **AprilTags**, **Color Detection (HSV Blob Tracking)**, or **Neural Network Detection**.

### Key Features
- **3 Dynamic Tracking Pipelines:** Switch seamlessly during runtime via Gamepad 1:
  - 🏷️ **Pipeline 0 (D-Pad Left):** AprilTag "Summon Beacon"
  - 🎨 **Pipeline 1 (D-Pad Right):** Option 2 — HSV Color Detection (e.g. Neon Shirt / Marker)
  - 🤖 **Pipeline 2 (D-Pad Up):** Neural Network Detector (Person Model)
- **Proportional Control Loop (P-Controller):** Smoothly aligns heading (`tx`) and distance (`ta`).
- **Gamepad Safety Deadman Switch:** Requires continuous input on Gamepad 1 (Right Trigger) to keep Summon mode active.
- **Proximity Safety Halt:** Prevents forward motion if the target moves closer than a set threshold (`MIN_SAFETY_AREA`).
- **Fail-Safe Target Loss Protection:** Automatically zeroes all motor powers if target visibility is interrupted.

---

## 🎨 Recipe: Option 2 — Color Detection Setup (Limelight Web GUI)

Follow this recipe to configure **Pipeline 1** on your Limelight to track a specific color (such as a bright neon orange, cyan, or red shirt/marker worn by the student):

1. **Connect to Limelight Web Interface:**
   - Power on the Control Hub & Limelight.
   - Connect your laptop to the robot's Wi-Fi network and navigate to `http://10.XX.YY.11:5801` (or `http://limelight.local:5801`).

2. **Select & Configure Pipeline 1:**
   - Click the **Pipelines** tab at the top.
   - Select **Pipeline 1** and rename it to `Color_Blob_Follower`.
   - Set **Pipeline Type** to **Color Detection / Thresholding**.

3. **Tune HSV (Hue, Saturation, Value) Thresholds:**
   - Switch the view mode to **Threshold** view (shows black-and-white binary mask).
   - Place the target object/shirt in front of the camera under your arena lighting.
   - Adjust the sliders until **only** your target object appears bright white, and the background remains pitch black:
     - **Hue (H):** Isolates the color spectrum (e.g., Orange: 10–25, Cyan: 85–105).
     - **Saturation (S):** Filters out pale/white colors (set Min Saturation > 100 to ignore white lights).
     - **Value (V):** Filters out dark shadows (set Min Value > 80).

4. **Apply Contour & Noise Filters:**
   - Under the **Contour Filtering** section:
     - Set **Area (% of image):** Min `0.5%`, Max `50%` (ignores tiny specs of dust or huge walls).
     - Set **Aspect Ratio:** Near square/rectangle for markers, or human torso ratios.
     - Set **Erosion & Dilation:** Set to `1` or `2` to remove speckle noise and smooth blob edges.

5. **Set Target Speck & Crosshair:**
   - Choose **Target Grouping:** *Single Target* or *Largest Target*.
   - Click **Save Pipeline**!

---

## 🛠️ Hardware & Software Requirements

- **FTC Control Hub / Expansion Hub** running FTC SDK 9.x+
- **Limelight 3A Vision Camera** (connected via Ethernet or USB-C)
- **4-Motor Drivetrain** (Mecanum, Tank, or Arcade configuration)
- **Gamepad Controller** (Logitech F310 or DualShock 4)

---

## 🎓 Plain Java Practice Exercises & Coach Solutions

Before working with the physical robot, students can build confidence by running the plain Java simulation exercises:

- 🟢 **[Practice1_PControl.java](file:///c:/Users/chand/OneDrive/Documents/ftc_coach/practice/src/main/java/com/ftc/practice/Practice1_PControl.java):** Master Proportional Control math and power clamping (`// TODO` Student Worksheet).
- 🟡 **[Practice2_ArcadeDrive.java](file:///c:/Users/chand/OneDrive/Documents/ftc_coach/practice/src/main/java/com/ftc/practice/Practice2_ArcadeDrive.java):** Practice Arcade Drive power mixing and magnitude normalization (`// TODO` Student Worksheet).
- 🔴 **[Practice3_SummonSimulator.java](file:///c:/Users/chand/OneDrive/Documents/ftc_coach/practice/src/main/java/com/ftc/practice/Practice3_SummonSimulator.java):** Run a full console simulation testing target lock, deadman switch, and emergency stop (`// TODO` Student Worksheet).
- 📋 **[COACH_GUIDE.md](file:///c:/Users/chand/OneDrive/Documents/ftc_coach/coach_solutions/COACH_GUIDE.md):** Complete Coach Teaching Guide, Discussion Questions, Common Pitfalls, and Answer Keys located in `coach_solutions/`.

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
├── practice/ (Student Worksheets)
│   ├── README_PRACTICE.md
│   └── src/
│       └── main/
│           └── java/
│               └── com/
│                   └── ftc/
│                       └── practice/
│                           ├── Practice1_PControl.java
│                           ├── Practice2_ArcadeDrive.java
│                           └── Practice3_SummonSimulator.java
├── coach_solutions/ (Coach Reference Solutions)
│   ├── COACH_GUIDE.md
│   └── src/
│       └── main/
│           └── java/
│               └── com/
│                   └── ftc/
│                       └── coach/
│                           ├── Solution1_PControl.java
│                           ├── Solution2_ArcadeDrive.java
│                           └── Solution3_SummonSimulator.java
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

## 🎮 Driver Controls & How to Run

1. Open this project directory in **Android Studio**.
2. Deploy the app to your FTC **Control Hub**.
3. Launch `Limelight Summon / Student Follower` from the Driver Station TeleOp list.
4. Use Gamepad 1 to control pipeline and summon mode:
   - 🎯 **Switch Pipelines:** Press `D-Pad Left` (AprilTag), `D-Pad Right` (Color Blob), or `D-Pad Up` (Neural).
   - 🚀 **Activate Summon:** Hold **Right Trigger** on Gamepad 1 to follow the target. Release to instantly stop.
