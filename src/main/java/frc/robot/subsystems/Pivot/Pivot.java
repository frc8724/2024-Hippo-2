// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems.Pivot;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.motors.MayhemCANSparkMax;

public class Pivot extends SubsystemBase {
  MayhemCANSparkMax m_left;
  MayhemCANSparkMax m_right;

  /** Creates a new Pivot. */
  public Pivot(MayhemCANSparkMax left, MayhemCANSparkMax right) {
    this.m_left = left;
    this.m_right = right;
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }

  public void setPower(double d) {
    m_right.setVBusPower(d);
    m_left.setVBusPower(d);
  }
}
