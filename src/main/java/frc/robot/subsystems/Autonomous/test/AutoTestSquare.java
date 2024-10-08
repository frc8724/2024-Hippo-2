// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems.Autonomous.test;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.subsystems.Autonomous.AutoStartingPosition;
import frc.robot.subsystems.DriveBase.DriveForDistance;
import frc.robot.subsystems.DriveBase.DriveStop;

// NOTE:  Consider using this command inline, rather than writing a subclass.  For more
// information, see:
// https://docs.wpilib.org/en/stable/docs/software/commandbased/convenience-features.html
public class AutoTestSquare extends SequentialCommandGroup {
  /** Creates a new AutoTestSquare. */
  public AutoTestSquare() {
    // Add your commands in the addCommands() call, e.g.
    // addCommands(new FooCommand(), new BarCommand());
    addCommands(
        new AutoStartingPosition(0),

        new DriveForDistance(1.0, 0, 0, 1.0), // forward
        new DriveForDistance(1.0, 90, 0, 1.0), // right
        new DriveForDistance(1.0, 180, 0, 1.0), // backwards
        new DriveForDistance(1.0, -90, 0, 1.0) // left

        , new DriveStop());
  }
}
