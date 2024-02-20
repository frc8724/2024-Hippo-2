// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import frc.robot.subsystems.VisionSubsystems.Vision;
import frc.robot.Constants.DriveConstants;
import frc.robot.Constants.ModuleConstants;
import frc.robot.Constants.OperatorConstants;
import frc.robot.controls.MayhemExtreme3dPro;
import frc.robot.motors.FakeFalconFX;
import frc.robot.motors.FakeMayhemCANSparkMax;
import frc.robot.motors.IMayhemCANSparkMax;
import frc.robot.motors.IMayhemTalonFX;
import frc.robot.motors.MayhemCANSparkMax;
import frc.robot.motors.MayhemTalonFX;
import frc.robot.motors.MayhemTalonFX.CurrentLimit;
import frc.robot.subsystems.SystemArmZero;
import frc.robot.subsystems.ArmSubsystem.ArmIsAtPosition;
import frc.robot.subsystems.ArmSubsystem.ArmSet;
import frc.robot.subsystems.ArmSubsystem.ArmSetPower;
import frc.robot.subsystems.ArmSubsystem.ArmSubsystem;
import frc.robot.subsystems.Autonomous.AutoChooser;
import frc.robot.subsystems.Autonomous.AutoDriveOut;
import frc.robot.subsystems.Autonomous.AutoDriveandShootandPickup;
import frc.robot.subsystems.Autonomous.AutoPathPlanner001;
import frc.robot.subsystems.Autonomous.AutoStandStill;
import frc.robot.subsystems.ClimberSubsystem.ClimberSubsystem;
import frc.robot.subsystems.DriveBase.DriveBaseSubsystem;
import frc.robot.subsystems.DriveBase.DriveForDistance;
import frc.robot.subsystems.DriveBase.DriveZeroGyro;
import frc.robot.subsystems.DriveBase.DriveZeroWheels;
import frc.robot.subsystems.DriveBase.DrivebaseResetEncoders;
import frc.robot.subsystems.IntakeRollers.IntakeRollers;
import frc.robot.subsystems.IntakeRollers.IntakeRollersSet;
import frc.robot.subsystems.LimeLight.CenterOnTag;
import frc.robot.subsystems.LimeLight.LimeLightSubsystem;
import frc.robot.subsystems.ShooterSubsystem.ShootNote;
import frc.robot.subsystems.ShooterSubsystem.ShooterMag;
import frc.robot.subsystems.ShooterSubsystem.ShooterMagSet;
import frc.robot.subsystems.ShooterSubsystem.ShooterWheels;
import frc.robot.subsystems.ShooterSubsystem.ShooterWheelsSet;
import frc.robot.subsystems.ShooterSubsystem.ShooterWheelsSetTicksPer100ms;
import frc.robot.subsystems.Targeting.Targeting;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.RunCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import edu.wpi.first.wpilibj2.command.button.Trigger;

/**
 * This class is where the bulk of the robot should be declared. Since
 * Command-based is a
 * "declarative" paradigm, very little robot logic should actually be handled in
 * the {@link Robot}
 * periodic methods (other than the scheduler calls). Instead, the structure of
 * the robot (including
 * subsystems, commands, and trigger mappings) should be declared here.
 */
public class RobotContainer {
  private static final IMayhemTalonFX intakeTop = new MayhemTalonFX(Constants.DriveConstants.kIntakeRollerId,
      CurrentLimit.HIGH_CURRENT);
  private static final IMayhemTalonFX armLeft = new MayhemTalonFX(Constants.DriveConstants.kArmLeftId,
      CurrentLimit.HIGH_CURRENT);
  private static final IMayhemTalonFX armRight = new MayhemTalonFX(Constants.DriveConstants.kArmRightId,
      CurrentLimit.HIGH_CURRENT);
  private static final IMayhemTalonFX shooterLeft = new MayhemTalonFX(Constants.DriveConstants.kShooterLeftId,
      CurrentLimit.HIGH_CURRENT);
  private static final IMayhemTalonFX shooterRight = new MayhemTalonFX(Constants.DriveConstants.kShooterRightId,
      CurrentLimit.HIGH_CURRENT);
  private static final IMayhemCANSparkMax magLeft = new MayhemCANSparkMax(Constants.DriveConstants.kMagLeftId,
      MotorType.kBrushless);
  private static final IMayhemCANSparkMax magRight = new MayhemCANSparkMax(Constants.DriveConstants.kMagRightId,
       MotorType.kBrushless);
  private static final IMayhemTalonFX climberLeft = new FakeFalconFX(Constants.DriveConstants.kMagRightId,
      CurrentLimit.HIGH_CURRENT);
  private static final IMayhemTalonFX climberRight = new FakeFalconFX(Constants.DriveConstants.kMagRightId,
      CurrentLimit.HIGH_CURRENT);

  public static final DriveBaseSubsystem m_robotDrive = new DriveBaseSubsystem();
  public static final IntakeRollers m_rollers = new IntakeRollers(intakeTop);
  public static final ShooterMag m_mag = new ShooterMag(magLeft, magRight);
  public static final ShooterWheels m_wheels = new ShooterWheels(shooterLeft, shooterRight);
  public static final ArmSubsystem m_arm = new ArmSubsystem(armLeft, armRight);
  public static final ClimberSubsystem m_climber = new ClimberSubsystem(climberLeft, climberRight);

  public static final Targeting m_targets = new Targeting();
  private static final MayhemExtreme3dPro DriverStick = new MayhemExtreme3dPro(0);
  private static final AutoChooser m_auto = new AutoChooser();
  public static final Vision vision = new Vision(0);
  public static final LimeLightSubsystem m_limelight = new LimeLightSubsystem();

  // Replace with CommandPS4Controller or CommandJoystick if needed
  private final CommandXboxController m_operatorController = new CommandXboxController(
      OperatorConstants.kOperatorControllerPort);

  /**
   * The container for the robot. Contains subsystems, OI devices, and commands.
   */
  public RobotContainer() {
    // Configure the trigger bindings
    configureBindings();
    m_robotDrive.setDefaultCommand(
        new RunCommand(
            () -> m_robotDrive.drive(
                -DriverStick.DeadbandAxis(MayhemExtreme3dPro.Axis.Y, 0.10)
                    * DriveConstants.kMaxSpeedMetersPerSecond,
                -DriverStick.DeadbandAxis(MayhemExtreme3dPro.Axis.X, 0.10)
                    * DriveConstants.kMaxSpeedMetersPerSecond,
                -DriverStick.DeadbandAxis(MayhemExtreme3dPro.Axis.Z, 0.20)
                    * ModuleConstants.kMaxModuleAngularSpeedRadiansPerSecond,
                true),
            m_robotDrive));

    m_arm.setDefaultCommand(
        new RunCommand(
            () -> {
                m_arm.moveArm(-m_operatorController.getLeftY());
                m_arm.setPower(-m_operatorController.getLeftY()/4);}
            ,
            m_arm)
    );

    m_auto.addAuto(new AutoDriveOut());
    m_auto.addAuto(new AutoDriveandShootandPickup());
    m_auto.addAuto(new AutoStandStill());
    //m_auto.addAuto(new AutoPathPlanner001());


  }

  /**
   * Use this method to define your trigger->command mappings. Triggers can be
   * created via the
   * {@link Trigger#Trigger(java.util.function.BooleanSupplier)} constructor with
   * an arbitrary
   * predicate, or via the named factories in {@link
   * edu.wpi.first.wpilibj2.command.button.CommandGenericHID}'s subclasses for
   * {@link
   * CommandXboxController
   * Xbox}/{@link edu.wpi.first.wpilibj2.command.button.CommandPS4Controller
   * PS4} controllers or
   * {@link edu.wpi.first.wpilibj2.command.button.CommandJoystick Flight
   * joysticks}.
   */
  private void configureBindings() {

    DriverStick.Button(9).onTrue(
        new SequentialCommandGroup(
            new DriveZeroWheels(),
            new DriveZeroGyro(),
            new WaitCommand(1.0),
            new DrivebaseResetEncoders(),
            new InstantCommand(() -> m_robotDrive.drive(0, 0, 0, true), m_robotDrive)));

    // DriverStick.Button(01).onTrue(new CenterOnTag());
    // DriverStick.Button(10).onTrue(new DriveForDistance(0.0, 0.2, 0.0, 1.0));
    // DriverStick.Button(11).onTrue(new DriveForDistance(0.2, 0.0, 0.0, 1.0));
    // DriverStick.Button(12).onTrue(new DriveForDistance(0, 0, -.3, 1.0));

    //OPERATOR BUTTONS

    //intake sequence with all needed systems on and off
    m_operatorController.button(5).onTrue(
        new ParallelCommandGroup(
            new IntakeRollersSet(0.5),
            new ShooterMagSet(0.3),
            new ShooterWheelsSet(-0.05)
        ));
    m_operatorController.button(5).onFalse(
        new ParallelCommandGroup(
            new IntakeRollersSet(0.0),
            new ShooterMagSet(0.0),
            new ShooterWheelsSet(0.0)
        ));        
    //  manual intake
    m_operatorController.leftTrigger(2).onTrue(new IntakeRollersSet(1.0));
    // manual intake reverse/spit out
     m_operatorController.button(8).onTrue(new IntakeRollersSet(-1.0));

    // manual shooter
    m_operatorController.rightTrigger(3).onTrue(new ShooterWheelsSetTicksPer100ms(1500));
    // manual mag 
    m_operatorController.button(6).onTrue(new ShooterMagSet(5));
    // shoot note automatically
    m_operatorController.button(3).onTrue(new ShootNote());
    //"zero" arm at that position
    m_operatorController.button(7).onTrue(new SystemArmZero());
    // turn arm motors off so you can return to manual after shooting sequence
    m_operatorController.button(2).onTrue(new ArmSetPower(0));
    //sets arm to the highest position
    m_operatorController.button(4).onTrue(
    new SequentialCommandGroup(
        new ArmSet(0),
    new ArmIsAtPosition(ArmSubsystem.NOTE_INTAKE)));
    // sets arm to intake position
    m_operatorController.button(1).onTrue(
    new SequentialCommandGroup(
        new ArmSet(ArmSubsystem.NOTE_INTAKE),
        new ArmIsAtPosition(ArmSubsystem.NOTE_INTAKE)));  
         // m_operatorController.button(1).onTrue(new ArmSet(4*ArmSubsystem.NOTE_INTAKE));

    m_arm.setPower(0);
    m_wheels.setShooterSpeed(0.0);
    m_rollers.setPower(0);
    m_mag.setPowerVbus(0);
    m_climber.setPower(0);
}

  /**
   * Use this to pass the autonomous command to the main {@link Robot} class.
   *
   * @return the command to run in autonomous
   */
  public Command getAutonomousCommand() {
    // An example command will be run in autonomous
    return m_auto.getAutoCommand();
  }

}
