// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems.Pivot;

import com.revrobotics.RelativeEncoder;
import com.revrobotics.SparkPIDController;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.motors.MayhemCANSparkMax;

public class Pivot extends SubsystemBase {
  MayhemCANSparkMax m_left;
  MayhemCANSparkMax m_right;

  SparkPIDController pid;
  RelativeEncoder encoder;

  /** Creates a new Pivot. */
  public Pivot(MayhemCANSparkMax left, MayhemCANSparkMax right) {
    this.m_left = left;
    this.m_right = right;

    m_right.setInverted(false);

    m_left.getMotor().follow(m_right.getMotor(), true);

    pid = m_right.getMotor().getPIDController();
    encoder = m_right.getMotor().getEncoder();
    setZero();
  }

  public void setZero() {
    m_right.getMotor().getEncoder().setPosition(0.0);
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
    SmartDashboard.putNumber("pivot", encoder.getPosition());
  }

  public void setPower(double d) {
    m_right.setVBusPower(d);
  }

  public void setPosition(double pos) {

  }
}
