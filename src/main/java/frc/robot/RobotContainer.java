// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import frc.robot.subsystems.VisionSubsystems.Vision;
import frc.robot.subsystems.old.ArmSubsystem.ArmIsAtPosition;
import frc.robot.subsystems.old.ArmSubsystem.ArmSet;
import frc.robot.subsystems.old.ArmSubsystem.ArmSetPower;
import frc.robot.subsystems.old.ArmSubsystem.ArmSubsystem;
import frc.robot.subsystems.old.ClimberSubsystem.ClimberSetPower;
import frc.robot.subsystems.old.ClimberSubsystem.ClimberSetPowerLeft;
import frc.robot.subsystems.old.ClimberSubsystem.ClimberSetPowerRight;
import frc.robot.subsystems.old.ClimberSubsystem.ClimberSubsystem;
import frc.robot.subsystems.old.IntakeRollers.IntakeRollers;
import frc.robot.subsystems.old.IntakeRollers.IntakeRollersSet;
import frc.robot.subsystems.old.ShooterSubsystem.ShootNote;
import frc.robot.subsystems.old.ShooterSubsystem.ShootNotePost;
import frc.robot.subsystems.old.ShooterSubsystem.ShootNotePre;
import frc.robot.subsystems.old.ShooterSubsystem.ShooterMag;
import frc.robot.subsystems.old.ShooterSubsystem.ShooterMagSet;
import frc.robot.subsystems.old.ShooterSubsystem.ShooterWheelBrake;
import frc.robot.subsystems.old.ShooterSubsystem.ShooterWheels;
import frc.robot.subsystems.old.ShooterSubsystem.ShooterWheelsSet;
import frc.robot.Constants.DriveConstants;
import frc.robot.Constants.ModuleConstants;
import frc.robot.Constants.OperatorConstants;
import frc.robot.controls.MayhemExtreme3dPro;
import frc.robot.controls.MayhemLogitechAttack3;
import frc.robot.motors.FakeFalconFX;
import frc.robot.motors.FakeMayhemCANSparkMax;
import frc.robot.motors.IMayhemCANSparkMax;
import frc.robot.motors.IMayhemTalonFX;
import frc.robot.motors.MayhemCANSparkMax;
import frc.robot.motors.MayhemTalonFX;
import frc.robot.motors.MayhemTalonFX.CurrentLimit;
import frc.robot.subsystems.SystemArmZero;
import frc.robot.subsystems.Autonomous.*;
import frc.robot.subsystems.Autonomous.test.AutoTestSquare;
import frc.robot.subsystems.DriveBase.DriveBaseSubsystem;
import frc.robot.subsystems.DriveBase.DriveByJoystick;
import frc.robot.subsystems.DriveBase.DriveZeroGyro;
import frc.robot.subsystems.Intake.Intake;
import frc.robot.subsystems.Intake.IntakeSetPower;
import frc.robot.subsystems.LimeLight.LimeLightSubsystem;
import frc.robot.subsystems.Magazine.Magazine;
import frc.robot.subsystems.Magazine.MagazineSetPower;
import frc.robot.subsystems.Pivot.Pivot;
import frc.robot.subsystems.Pivot.PivotByJoystick;
import frc.robot.subsystems.Pivot.PivotSetPosition;
import frc.robot.subsystems.Shooter.Shooter;
import frc.robot.subsystems.Shooter.ShooterSetPower;
import frc.robot.subsystems.System.SystemAutoShootShort;
import frc.robot.subsystems.System.SystemIntakeNote;
import frc.robot.subsystems.System.SystemShootNote;
import frc.robot.subsystems.Targeting.Targeting;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.RunCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import edu.wpi.first.wpilibj2.command.button.Trigger;

import com.fasterxml.jackson.core.sym.Name;
import com.pathplanner.lib.auto.NamedCommands;
import com.pathplanner.lib.commands.PathPlannerAuto;
import com.revrobotics.CANSparkLowLevel.MotorType;

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
        private static final IMayhemTalonFX intakeTop = new FakeFalconFX(Constants.DriveConstants.kIntakeRollerId,
                        CurrentLimit.HIGH_CURRENT);
        private static final IMayhemTalonFX armLeft = new FakeFalconFX(Constants.DriveConstants.kArmLeftId,
                        CurrentLimit.HIGH_CURRENT);
        private static final IMayhemTalonFX armRight = new FakeFalconFX(Constants.DriveConstants.kArmRightId,
                        CurrentLimit.HIGH_CURRENT);
        private static final IMayhemTalonFX shooterLeft = new FakeFalconFX(Constants.DriveConstants.kShooterLeftId,
                        CurrentLimit.HIGH_CURRENT);
        private static final IMayhemTalonFX shooterRight = new FakeFalconFX(Constants.DriveConstants.kShooterRightId,
                        CurrentLimit.HIGH_CURRENT);
        private static final IMayhemCANSparkMax magLeft = new FakeMayhemCANSparkMax(Constants.DriveConstants.kMagLeftId,
                        MotorType.kBrushless);
        private static final IMayhemCANSparkMax magRight = new FakeMayhemCANSparkMax(
                        Constants.DriveConstants.kMagRightId,
                        MotorType.kBrushless);
        private static final IMayhemTalonFX climberLeft = new FakeFalconFX(Constants.DriveConstants.kClimberLeftId,
                        CurrentLimit.HIGH_CURRENT);
        private static final IMayhemTalonFX climberRight = new FakeFalconFX(Constants.DriveConstants.kClimberRightId,
                        CurrentLimit.HIGH_CURRENT);

        private static final IMayhemTalonFX shooterTopMotor = new MayhemTalonFX(10, CurrentLimit.HIGH_CURRENT);
        private static final IMayhemTalonFX shooterBottomMotor = new MayhemTalonFX(11, CurrentLimit.HIGH_CURRENT);
        private static final MayhemCANSparkMax intakeMotorA = new MayhemCANSparkMax(15, MotorType.kBrushless);
        private static final MayhemCANSparkMax intakeMotorB = new MayhemCANSparkMax(20, MotorType.kBrushless);
        private static final MayhemCANSparkMax magMotor = new MayhemCANSparkMax(14, MotorType.kBrushless);
        private static final MayhemCANSparkMax pivotLeft = new MayhemCANSparkMax(19, MotorType.kBrushless);
        private static final MayhemCANSparkMax pivotRight = new MayhemCANSparkMax(13, MotorType.kBrushless);

        public static final DriveBaseSubsystem m_robotDrive = new DriveBaseSubsystem();
        public static final IntakeRollers m_rollers = new IntakeRollers(intakeTop);
        public static final ShooterMag m_mag = new ShooterMag(magLeft, magRight);
        public static final ShooterWheels m_wheels = new ShooterWheels(shooterLeft,
                        shooterRight);
        public static final ArmSubsystem m_arm = new ArmSubsystem(armLeft, armRight);
        public static final ClimberSubsystem m_climber = new ClimberSubsystem(climberLeft, climberRight);
        public static final Shooter m_shooter = new Shooter(shooterTopMotor, shooterBottomMotor);

        public static final Intake m_intake = new Intake(intakeMotorA, intakeMotorB);
        public static final Magazine m_magazine = new Magazine(magMotor);
        public static final Pivot m_pivot = new Pivot(pivotLeft, pivotRight);

        // public static final Targeting m_targets = new Targeting();
        private static final MayhemExtreme3dPro m_driverStick = new MayhemExtreme3dPro(0);
        private static final MayhemLogitechAttack3 operatorStick = new MayhemLogitechAttack3(2);
        private static final AutoChooser m_auto = new AutoChooser();
        // public static final Vision vision = null; // = new Vision(0);
        public static final LimeLightSubsystem m_limelight = new LimeLightSubsystem();

        /**
         * The container for the robot. Contains subsystems, OI devices, and commands.
         */
        public RobotContainer() {
                // Configure the trigger bindings
                configureBindings();
                configureNamedCommands();
                m_robotDrive.setDefaultCommand(new DriveByJoystick(m_driverStick));

                m_driverStick.Button(9).onTrue(new DriveZeroGyro(0));

                // m_driverStick.Button(7).onTrue(new ShooterSetPower(0));
                // m_driverStick.Button(8).onTrue(new ShooterSetPower(.10));
                // m_driverStick.Button(9).onTrue(new IntakeSetPower(0));
                // m_driverStick.Button(10).onTrue(new IntakeSetPower(.20));
                // m_driverStick.Button(3).onTrue(new MagazineSetPower(0));
                // m_driverStick.Button(5).onTrue(new MagazineSetPower(.2));

                // m_pivot.setDefaultCommand(new
                // PivotByJoystick(operatorStick.Axis(MayhemLogitechAttack3.Axis.Y)));

                operatorStick.Button(11).onTrue(new SystemIntakeNote(.7, .8));
                operatorStick.Button(11).onFalse(new SystemIntakeNote(0));

                operatorStick.Button(10).onTrue(new SystemIntakeNote(-.4, -.4));
                operatorStick.Button(10).onFalse(new SystemIntakeNote(.0));

                operatorStick.Button(1).onTrue(new SystemShootNote());
                // operatorStick.Button(1).onFalse(new ShooterSetPower(.0));

                operatorStick.Button(4).onTrue(new PivotSetPosition(Pivot.ZERO));
                operatorStick.Button(5).onTrue(new PivotSetPosition(Pivot.SHORT_SHOT));
                operatorStick.Button(2).onTrue(new PivotSetPosition(Pivot.AMP_SHOT));
                operatorStick.Button(7).onTrue(new SystemAutoShootShort());

                // m_arm.setDefaultCommand(
                // new RunCommand(
                // () -> {
                // m_arm.moveArm(-m_operatorController.getLeftY());
                // m_arm.setPower(-m_operatorController.getLeftY() / 4);
                // },
                // m_arm));

                // m_auto.addAuto(new AuthDriveMidNote3AndBack());
                m_auto.addAuto(new AutoDriveOut());
                m_auto.addAuto(new AutoTestSquare());
                // m_auto.addAuto(new AutoShootandDriveOut());
                // m_auto.addAuto(new AutoStandStill());
                // m_auto.addAuto(new AutoShootAndStandStill());
                // m_auto.addAuto(new AutoShootAndDrivex2());

                // m_auto.addAuto(new AutoBlueStartLongScore2());
                // m_auto.addAuto(new AutoBlueStartLongShootandDrive());
                // m_auto.addAuto(new AutoBlueStartShortShootandDrive());
                // m_auto.addAuto(new AutoBlueStartShortScore2());
                // m_auto.addAuto(new AutoBlueScore2Mid3());

                // m_auto.addAuto(new AutoRedStartLongScore2());
                // m_auto.addAuto(new AutoRedStartLongShootDrive());
                // m_auto.addAuto(new AutoRedStartShortShootandDrive());
                // m_auto.addAuto(new AutoRedStartShortScore2());
                // m_auto.addAuto(new AutoRedScore2Mid3());

                // m_auto.addAuto(new AutoRedPositiveShootAndStandStill());
                // m_auto.addAuto(new AutoTestSpeeds());
                // m_auto.addAuto(new AutoStealAllNotes(1.0));

                // m_pathPlanner = AutoBuilder.buildAutoChooser();
                // SmartDashboard.putData("AutoChooser:", m_pathPlanner);
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

                // DriverStick.Button(01).onTrue(new CenterOnTag());
                // DriverStick.Button(10).onTrue(new DriveForDistance(0.0, 0.2, 0.0, 1.0));
                // DriverStick.Button(11).onTrue(new DriveForDistance(0.2, 0.0, 0.0, 1.0));
                // DriverStick.Button(12).onTrue(new DriveForDistance(0, 0, -.3, 1.0));

                // OPERATOR BUTTONS

                // shoot automatically at normal speed
                // m_operatorController.button(1).onTrue();
                // trap shot
                // m_operatorController.button(3).onTrue(new ShootNote(2400));

                // // all motors off
                // m_operatorController.button(2).onTrue(
                // new ParallelCommandGroup(
                // new ArmSetPower(0),
                // new ShooterMagSet(0),
                // new ShooterWheelsSet(0),
                // new IntakeRollersSet(0)));
                // // rev beforehand
                // m_operatorController.button(4).onTrue(new ShootNotePre(5500));
                // // releas to shoot
                // m_operatorController.button(4).onFalse(new ShootNotePost(4500));
                // // intake sequence
                // m_operatorController.button(5).onTrue(new SequentialCommandGroup(
                // new IntakeRollersSet(0.5),
                // new ShooterMagSet(0.25),
                // new ShooterWheelsSet(0.00),
                // new ShooterWheelBrake()));
                // m_operatorController.button(5).onFalse(new SequentialCommandGroup(
                // new IntakeRollersSet(0.0),
                // new ShooterMagSet(0.0),
                // new ShooterWheelsSet(0.0)));

                // // zero arm position
                // m_operatorController.button(7).onTrue(new SystemArmZero());
                // m_operatorController.button(8).onTrue(new SystemArmZero());

                // // set arm to long shot position
                // m_operatorController.povUp().onTrue(new SequentialCommandGroup(
                // new ArmSet(ArmSubsystem.LONG_SHOT),
                // new ArmIsAtPosition(ArmSubsystem.POSITION_SLOP)));

                // // set are to intake position
                // m_operatorController.povLeft().onTrue(new SequentialCommandGroup(
                // new ArmSet(ArmSubsystem.NOTE_INTAKE),
                // new ArmIsAtPosition(ArmSubsystem.POSITION_SLOP)));

                // // to shoot at position for amp
                // m_operatorController.povRight().onTrue(new SequentialCommandGroup(
                // new ArmSet(ArmSubsystem.AMP_SHOOT),
                // new ArmIsAtPosition(ArmSubsystem.POSITION_SLOP)));
                // // close shot
                // m_operatorController.povDown().onTrue(new SequentialCommandGroup(
                // new ArmSet(ArmSubsystem.SHORT_SHOT),
                // new ArmIsAtPosition(ArmSubsystem.POSITION_SLOP)));
                // // CLIMBER
                // m_operatorController.leftTrigger().onTrue(new ClimberSetPowerLeft(0.3));
                // m_operatorController.leftTrigger().onFalse(new ClimberSetPowerLeft(0.0));
                // m_operatorController.rightTrigger().onTrue(new ClimberSetPowerRight(0.3));
                // m_operatorController.rightTrigger().onFalse(new ClimberSetPowerRight(0.0));
                // // climber down
                // m_operatorController.button(6).onTrue(new ClimberSetPower(-0.3));
                // m_operatorController.button(6).onFalse(new ClimberSetPower(-0.0));

                // // manual buttons
                // operatorStick.Button(6).onTrue(new IntakeRollersSet(0.5));
                // operatorStick.Button(6).onFalse(new IntakeRollersSet(0.0));

                // operatorStick.Button(7).onTrue(new IntakeRollersSet(-0.5));
                // operatorStick.Button(7).onFalse(new IntakeRollersSet(0.0));

                // operatorStick.Button(11).onTrue(new ShooterMagSet(0.5));
                // operatorStick.Button(11).onFalse(new ShooterMagSet(0.0));
                // operatorStick.Button(10).onTrue(new ShooterMagSet(-0.5));
                // operatorStick.Button(10).onFalse(new ShooterMagSet(0.0));

                // operatorStick.Button(3).onTrue(new ShooterWheelsSet(0.5));
                // operatorStick.Button(3).onFalse(new ShooterWheelsSet(0.0));
                // operatorStick.Button(2).onTrue(new ShooterWheelsSet(-0.5));
                // operatorStick.Button(2).onFalse(new ShooterWheelsSet(0.0));

                // operatorStick.Button(8).onTrue(new ClimberSetPower(-0.3));
                // operatorStick.Button(8).onFalse(new ClimberSetPower(0.0));
                // operatorStick.Button(9).onTrue(new ClimberSetPower(0.3));
                // operatorStick.Button(9).onFalse(new ClimberSetPower(0.0));

                // m_arm.setPower(0);
                // m_wheels.setShooterSpeed(0.0);
                // m_rollers.setPower(0);
                // m_mag.setPowerVbus(0);
                // m_climber.setPower(0);
        }

        private void configureNamedCommands() {
                NamedCommands.registerCommand("SystemShootNote", new SystemShootNote());
                NamedCommands.registerCommand("PivotShortShot", new PivotSetPosition(Pivot.SHORT_SHOT));

                NamedCommands.registerCommand("SystemAutoShootShort", new SystemAutoShootShort());
                NamedCommands.registerCommand("SystemIntakeNote2sec",
                                new SequentialCommandGroup(new SystemIntakeNote(.5),
                                                new WaitCommand(1.5),
                                                new SystemIntakeNote(0))
                                                .handleInterrupt(() -> RobotContainer.m_intake.setPower(0)));
        }

        /**
         * Use this to pass the autonomous command to the main {@link Robot} class.
         *
         * @return the command to run in autonomous
         */
        public Command getAutonomousCommand() {

                // An example command will be run in autonomous
                // return m_auto.getAutoCommand();
                return new PathPlannerAuto("StartShortShootGather1");
        }
}
