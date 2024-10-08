// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems.Targeting;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.RobotContainer;

public class Targeting extends SubsystemBase {
  private final double fieldOfViewDegrees = 45.0;

  /** Creates a new Targeting. */
  public Targeting() {
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
    // SmartDashboard.putNumber("Targeting: Heading", getHeadingToTarget());
  }

  /**
   * Get the heading (bearing) to target.
   * Positive is to the right.
   * Negative is to the left.
   * NaN is no target.
   * 
   * @return
   */
  public double getHeadingToTarget() {
    // var target = RobotContainer.vision.getTarget();

    // if (target == null) {
    // return Double.NaN;
    // }

    // var point = target.getCenter();

    double targetHeading = 0.0;
    // // if the target is on the right half...
    // if (point.x > 0.5) {
    // targetHeading = (point.x - 0.5) * fieldOfViewDegrees / 2;
    // } else {
    // targetHeading = -(0.5 - point.x) * fieldOfViewDegrees / 2;
    // }

    return targetHeading;
  }
}
