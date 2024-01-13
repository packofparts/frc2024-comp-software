// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import com.pathplanner.lib.auto.AutoBuilder;
import com.pathplanner.lib.path.PathPlannerPath;
import com.pathplanner.lib.util.HolonomicPathFollowerConfig;
import com.pathplanner.lib.util.PIDConstants;
import com.pathplanner.lib.util.ReplanningConfig;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.SwerveSubsystem;


public class FollowPath extends Command {
  /** Creates a new FollowPath. */
  private final SwerveSubsystem mSwerve;
  private final Command mFinalCmd;
  public FollowPath(SwerveSubsystem swerveSubsystem) {
    mSwerve = swerveSubsystem;
    addRequirements(mSwerve);
    AutoBuilder.configureHolonomic(swerveSubsystem::getRobotPose, (pose)->mSwerve.resetRobotPose(pose),
    mSwerve::getChassisSpeeds, speeds->mSwerve.setChassisSpeed(speeds),
    new HolonomicPathFollowerConfig( // HolonomicPathFollowerConfig, this should likely live in
                                     // your Constants class
        new PIDConstants(5.0, 0.0, 0.0), // Translation PID constants
        new PIDConstants(5.0, 0.0, 0.0), // Rotation PID constants
        3, // Max module speed, in m/s
        0.4, // Drive base radius in meters. Distance from robot center to furthest module.
        new ReplanningConfig() // Default path replanning config. See the API for the options
                               // here
    ), null, mSwerve);

    PathPlannerPath path = PathPlannerPath.fromPathFile("1 Meter");
    mFinalCmd = AutoBuilder.followPath(path);
    addRequirements(mSwerve);
    // Use addRequirements() here to declare subsystem dependencies.
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    mFinalCmd.execute();
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {}

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {}

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return mFinalCmd.isFinished();
  }
}
