// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import com.pathplanner.lib.auto.AutoBuilder;
import com.pathplanner.lib.util.HolonomicPathFollowerConfig;
import com.pathplanner.lib.util.PIDConstants;
import com.pathplanner.lib.util.ReplanningConfig;
import com.revrobotics.CANSparkLowLevel;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import edu.wpi.first.wpilibj2.command.PrintCommand;
import frc.robot.commands.FollowPath;
import frc.robot.commands.InitializePathPlanner;
import frc.robot.commands.kS_Characterization;
import frc.robot.commands.kV_Characterization;

/**
 * The VM is configured to automatically run this class, and to call the functions corresponding to
 * each mode, as described in the TimedRobot documentation. If you change the name of this class or
 * the package after creating this project, you must also update the build.gradle file in the
 * project.
 */
public class Robot extends TimedRobot {
  private Command m_autonomousCommand;

  private RobotContainer m_robotContainer;
  public SendableChooser<Command> pathSelector = new SendableChooser<>();


  /**
   * This function is run when the robot is first started up and should be used for any
   * initialization code.
   */
  @Override
  public void robotInit() {
    // Instantiate our RobotContainer.  This will perform all our button bindings, and put our
    // autonomous chooser on the dashboard.
    SmartDashboard.putData("Pick your Auton...",pathSelector);
    m_robotContainer = new RobotContainer();
    // AutoBuilder.configureHolonomic(
    // m_robotContainer.getSwerveSubsystem()::getRobotPose, 
    // (Pose2d pose)->m_robotContainer.getSwerveSubsystem().resetRobotPose(pose),
    // m_robotContainer.getSwerveSubsystem()::getChassisSpeeds, 
    // (ChassisSpeeds speeds)->m_robotContainer.getSwerveSubsystem().setChassisSpeed(speeds),
    // new HolonomicPathFollowerConfig( // HolonomicPathFollowerConfig, this should likely live in
    //                                   // your Constants class
    //       new PIDConstants(3.0, 0.0, 0.0), // Translation PID constants
    //       new PIDConstants(3.0, 0.0, 0.0), // Rotation PID constants
    //       4.5, // Max module speed, in m/s
    //       0.4669, // Drive base radius in meters. Distance from robot center to furthest module.
    //       new ReplanningConfig() // Default path replanning config. See the API for the options
    //                           // here
                              
    // ), ()->false, m_robotContainer.getSwerveSubsystem());
    new InitializePathPlanner(m_robotContainer.getSwerveSubsystem(), m_robotContainer.getPoseEstimator()).initialize();

    pathSelector.addOption("5_meter_return", new FollowPath(m_robotContainer.getSwerveSubsystem(), m_robotContainer.getPoseEstimator(), "5_Meter_Return"));
    pathSelector.addOption("Simpy", new FollowPath(m_robotContainer.getSwerveSubsystem(), m_robotContainer.getPoseEstimator(), "Simpy"));
    pathSelector.addOption("2_meter", new FollowPath(m_robotContainer.getSwerveSubsystem(), m_robotContainer.getPoseEstimator(), "2_Meter"));
    pathSelector.addOption("None", new PrintCommand("Damn that sucks"));
    pathSelector.addOption("kSCharacterization", new kS_Characterization(m_robotContainer.getSwerveSubsystem()));
    pathSelector.addOption("kVCharacterization", new kV_Characterization(m_robotContainer.getSwerveSubsystem()));
    pathSelector.addOption("Goofy Loop", new FollowPath(m_robotContainer.getSwerveSubsystem(), m_robotContainer.getPoseEstimator(), "Goofy Loop"));
    pathSelector.addOption("3_Piece_Dynamic", new FollowPath(m_robotContainer.getSwerveSubsystem(), m_robotContainer.getPoseEstimator(), "3_Piece_Dynamic"));
  }

  /**
   * This function is called every 20 ms, no matter the mode. Use this for items like diagnostics
   * that you want ran during disabled, autonomous, teleoperated and test.
   *
   * <p>This runs after the mode specific periodic functions, but before LiveWindow and
   * SmartDashboard integrated updating.
   */
  @Override
  public void robotPeriodic() {
    // Runs the Scheduler.  This is responsible for polling buttons, adding newly-scheduled
    // commands, running already-scheduled commands, removing finished or interrupted commands,
    // and running subsystem periodic() methods.  This must be called from the robot's periodic
    // block in order for anything in the Command-based framework to work.
    CommandScheduler.getInstance().run();
    SmartDashboard.putData("Pick your Auton...",pathSelector);
    SmartDashboard.updateValues();
  }

  /** This function is called once each time the robot enters Disabled mode. */
  @Override
  public void disabledInit() {}

  @Override
  public void disabledPeriodic() {}

  /** This autonomous runs the autonomous command selected by your {@link RobotContainer} class. */
  @Override
  public void autonomousInit() {
    m_autonomousCommand = pathSelector.getSelected();

    // schedule the autonomous command (example)
    if (m_autonomousCommand != null) {
      m_autonomousCommand.schedule();
    }
    //CANSparkLowLevel.enableExternalUSBControl(true);

  }

  /** This function is called periodically during autonomous. */
  @Override
  public void autonomousPeriodic() {}

  @Override
  public void teleopInit() {
    // This makes sure that the autonomous stops running when
    // teleop starts running. If you want the autonomous to
    // continue until interrupted by another command, remove
    // this line or comment it out.
    if (m_autonomousCommand != null) {
      m_autonomousCommand.cancel();
    }
  }

  /** This function is called periodically during operator control. */
  @Override
  public void teleopPeriodic() {}

  @Override
  public void testInit() {
    // Cancels all running commands at the start of test mode.
    CommandScheduler.getInstance().cancelAll();
    CANSparkLowLevel.enableExternalUSBControl(true);
  }

  /** This function is called periodically during test mode. */
  @Override
  public void testPeriodic() {}

  /** This function is called once when the robot is first started up. */
  @Override
  public void simulationInit() {}

  /** This function is called periodically whilst in simulation. */
  @Override
  public void simulationPeriodic() {}
}
