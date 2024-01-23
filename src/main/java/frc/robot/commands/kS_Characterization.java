// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.SwerveModule;
import frc.robot.subsystems.SwerveSubsystem;

public class kS_Characterization extends Command {
  /** Creates a new kS_Characterization. */
  private final SwerveSubsystem mSwerve;
  private final SwerveModule[] mModules;
  private double [] expkS = new double[4];
  private boolean [] modLock = {false,false,false,false};
  private double curRequestedDutyCycle = 0;
  public kS_Characterization(SwerveSubsystem swerve) {
    mSwerve = swerve;
    mModules = mSwerve.getRawModules();

    addRequirements(mSwerve);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    curRequestedDutyCycle = 0;
    expkS = new double[4];

    for (int i = 0; i<4; i++){
      modLock[i] = false;
    }
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    curRequestedDutyCycle += 0.001;
    SmartDashboard.putNumber("CharacterizationDutyCycle", curRequestedDutyCycle);
    mSwerve.setChassisSpeed(curRequestedDutyCycle, 0, 0,false,true);
    for (int i = 0; i<4; i++){
      if (Math.abs(mModules[i].getTransVelocity())>0.00001 && !modLock[i]){
        expkS[i] = Math.abs(mModules[i].getTransAppliedVolts()/mModules[i].getTranslationNominalVoltage());
        modLock[i] = true;
      }
      SmartDashboard.putNumber("expkS"+i, expkS[i]);

    }
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {}

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {

    boolean flag = true;
    for(int i=0; i<4; i++){
      SmartDashboard.putNumber("expkS"+i, expkS[i]);

      if (!modLock[i]){
        flag = false;
      }
    }
    SmartDashboard.putBoolean("kSFinished", flag);
    return flag;
  }
}
