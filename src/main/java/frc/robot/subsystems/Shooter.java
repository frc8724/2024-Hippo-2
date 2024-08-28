// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.motors.IMayhemTalonFX;

public class Shooter extends SubsystemBase {
  IMayhemTalonFX topMotor;
  IMayhemTalonFX bottomMotor;

  /** Creates a new Shooter. */
  public Shooter(IMayhemTalonFX top, IMayhemTalonFX bottom) {
    this.topMotor = top;
    this.bottomMotor = bottom;

    this.topMotor.setInverted(false);
    this.bottomMotor.setInverted(false);

    this.bottomMotor.follow(this.topMotor);
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }

  public void setPower(double d) {
    this.topMotor.set(ControlMode.PercentOutput, d);
  }
}
