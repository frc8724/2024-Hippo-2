// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems.Magazine;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.motors.MayhemCANSparkMax;

public class Magazine extends SubsystemBase {
  MayhemCANSparkMax m_motor;

  /** Creates a new Magazine. */
  public Magazine(MayhemCANSparkMax motor) {
    m_motor = motor;
    m_motor.getMotor().setSmartCurrentLimit(20);

    setPower(0);
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }

  public void setPower(double d) {
    m_motor.setVBusPower(d);
  }
}
