# ☕ Java Foundations for FTC Robotics
### 🎒 High School (10th Grade Beginner Edition)

Welcome to **Java Foundations for FTC Robotics**! 

Before programming an actual FIRST Tech Challenge (FTC) robot, you need to understand the fundamental Java building blocks. This lesson explains every Java concept using **real FTC hardware examples** (Motors, Gamepads, Limelight Cameras, and Sensors).

---

## 📚 Table of Contents

1. [Lesson 1: Variables \& Data Types (The Robot's Memory)](#lesson-1-variables--data-types-the-robots-memory)
2. [Lesson 2: If-Else Statements (The Robot's Brain)](#lesson-2-if-else-statements-the-robots-brain)
3. [Lesson 3: Methods (Reusable Robot Actions)](#lesson-3-methods-reusable-robot-actions)
4. [Lesson 4: Classes \& Objects (Connecting Code to Hardware)](#lesson-4-classes--objects-connecting-code-to-hardware)
5. [Lesson 5: Enums (State Machines for Autonomous Mode)](#lesson-5-enums-state-machines-for-autonomous-mode)
6. [Lesson 6: The FTC OpMode Lifecycle (`loop` vs `runOpMode`)](#lesson-6-the-ftc-opmode-lifecycle-loop-vs-runopmode)

---

## Lesson 1: Variables & Data Types (The Robot's Memory)

In Java, a **variable** is a labeled box in memory that stores a piece of information.

FTC robotics uses 4 main data types:

| Data Type | What it Stores | Example in FTC | Java Code Example |
| :--- | :--- | :--- | :--- |
| `double` | Decimal numbers | Motor speeds (`-1.0` to `+1.0`), camera offsets (`tx`), seconds | `double motorPower = 0.5;` |
| `boolean` | `true` or `false` | Is gamepad button pressed? Is target seen? | `boolean targetSeen = true;` |
| `int` | Whole numbers | Encoder tick counts, pipeline IDs, loop counts | `int pipelineId = 1;` |
| `String` | Text in quotes | Telemetry display messages, state names | `String robotState = "SEARCHING";` |

### 💡 Real FTC Code Example
```java
double driveSpeed = 0.65;              // 65% motor speed
boolean isTriggerPressed = gamepad1.right_trigger > 0.2;
int leftEncoderTicks = 1440;            // Motor rotated 1440 ticks
String status = "Target Locked!";
```

---

## Lesson 2: If-Else Statements (The Robot's Brain)

An **`if` statement** allows your robot to make decisions based on sensor input or controller buttons.

### Syntax
```java
if (condition) {
    // Code runs if condition is TRUE
} else if (anotherCondition) {
    // Code runs if first was false, but this is TRUE
} else {
    // Code runs if everything above was FALSE
}
```

### 💡 Real FTC Safety Check Example
```java
if (!gamepad1.right_trigger > 0.2) {
    // Driver released trigger -> Safety Stop!
    leftMotorPower = 0.0;
    rightMotorPower = 0.0;
} else if (targetArea > 18.0) {
    // Target is too close -> Stop driving forward!
    leftMotorPower = 0.0;
    rightMotorPower = 0.0;
} else {
    // Safe -> Drive forward!
    leftMotorPower = 0.4;
    rightMotorPower = 0.4;
}
```

---

## Lesson 3: Methods (Reusable Robot Actions)

A **Method** is a block of code that performs a specific calculation or action. Instead of writing the same math formula 10 times, you put it inside a method and call it whenever you need it!

### Structure of a Method
```java
public returnType methodName(parameterType parameterName) {
    // Math or action logic
    return result;
}
```

### 💡 Example: P-Controller Math Method
```java
// Method that takes camera heading error (tx) and calculates turn speed
public double calculateTurnPower(double tx) {
    double KP_TURN = 0.025;
    double rawTurnPower = tx * KP_TURN;
    
    // Clamp power between -0.4 and +0.4 for safety
    double clampedPower = Math.max(-0.4, Math.min(0.4, rawTurnPower));
    return clampedPower;
}
```

How you call it in your FTC loop:
```java
double turnPower = calculateTurnPower(limelightTx);
```

---

## Lesson 4: Classes & Objects (Connecting Code to Hardware)

FTC uses **Object-Oriented Programming (OOP)**. 
- A **Class** is a blueprint (like a schematic drawing of a motor).
- An **Object** is the actual physical hardware instance created from that blueprint.

```
      Blueprint (Class: DcMotor) ──────► Hardware Object (leftDrive)
```

### 💡 Real FTC Hardware Connection Example
```java
public class MyFirstRobotOpMode extends OpMode {
    // Declare motor object references
    private DcMotor leftDrive;
    private DcMotor rightDrive;

    @Override
    public void init() {
        // hardwareMap connects your code to physical hub ports!
        leftDrive  = hardwareMap.get(DcMotor.class, "left_drive");
        rightDrive = hardwareMap.get(DcMotor.class, "right_drive");
        
        // Set motor direction
        leftDrive.setDirection(DcMotor.Direction.REVERSE);
    }

    @Override
    public void loop() {
        // Send power commands to physical motors
        leftDrive.setPower(0.5);
        rightDrive.setPower(0.5);
    }
}
```

---

## Lesson 5: Enums (State Machines for Autonomous Mode)

An **`enum` (Enumeration)** is a custom Java type that holds a fixed list of named constants. Enums are essential for writing **Autonomous State Machines** (like a bee searching, approaching, harvesting, and returning home).

### Creating an Enum
```java
public enum AutoState {
    IDLE,
    SEARCH_TARGET,
    DRIVE_TO_FLOWER,
    HARVEST_POLLEN,
    PARK
}
```

### Using an Enum with a `switch` Statement
```java
AutoState currentState = AutoState.SEARCH_TARGET;

switch (currentState) {
    case SEARCH_TARGET:
        turnPower = 0.2; // Spin slowly to scan field
        if (targetSeen) {
            currentState = AutoState.DRIVE_TO_FLOWER; // State transition!
        }
        break;
        
    case DRIVE_TO_FLOWER:
        drivePower = 0.4;
        if (targetArea >= 12.0) {
            currentState = AutoState.HARVEST_POLLEN;
        }
        break;
        
    case HARVEST_POLLEN:
        // Activate intake arm
        break;
}
```

---

## Lesson 6: The FTC OpMode Lifecycle (`loop` vs `runOpMode`)

In FTC, your code runs inside an **OpMode**. There are two styles:

### 1. Iterative `OpMode` (Event-Driven)
The FTC SDK calls `loop()` continuously 50+ times per second. **Never** use `Thread.sleep()` here!

```java
public class TeleOpMode extends OpMode {
    @Override public void init() { /* Setup motors */ }
    @Override public void loop() { /* Runs 50x per sec */ }
}
```

### 2. Linear `LinearOpMode` (Sequential)
Uses a single `runOpMode()` method with a `while(opModeIsActive())` loop.

```java
public class AutoOpMode extends LinearOpMode {
    @Override
    public void runOpMode() {
        // 1. Setup
        waitForStart();
        
        // 2. Main Autonomous Loop
        while (opModeIsActive()) {
            // Autonomous logic runs here
        }
    }
}
```

---

## 🔬 Hands-On Practice Exercise!

Ready to write your first FTC Java code?
Open: 👉 **[Practice0_JavaBasics.java](file:///c:/Users/chand/OneDrive/Documents/ftc_coach/practice/src/main/java/com/ftc/practice/Practice0_JavaBasics.java)**
