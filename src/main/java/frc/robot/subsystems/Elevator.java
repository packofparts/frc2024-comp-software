// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.CANSparkBase.IdleMode;
import com.revrobotics.CANSparkLowLevel.MotorType;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.constants.ElevatorConstants;

public class Elevator extends SubsystemBase {
  /** Creates a new Elevator. */
  private CANSparkMax leftNeo = new CANSparkMax(0,MotorType.kBrushless);
  private CANSparkMax rightNeo = new CANSparkMax(1,MotorType.kBrushless);
  private RelativeEncoder leftEncoder;
  private RelativeEncoder rightEncoder;
  private double setpointMeters = 0;
  private boolean enableSpFollowing = false;
  private PIDController positionalPID = new PIDController(0.01, 0, 0);

  public Elevator() {
    rightNeo.restoreFactoryDefaults();
    leftNeo.restoreFactoryDefaults();

    rightNeo.setInverted(ElevatorConstants.rightMotorInvert);
    leftNeo.setInverted(ElevatorConstants.leftMotorInvert);

    // rightNeo.follow(leftNeo,false);

    leftEncoder = leftNeo.getEncoder();
    rightEncoder = rightNeo.getEncoder();

    leftEncoder.setPosition(0);
    rightEncoder.setPosition(0);

    leftEncoder.setPositionConversionFactor(ElevatorConstants.leftMotorConversionFactor); //meters
    rightEncoder.setPositionConversionFactor(ElevatorConstants.rightMotorConversionFactor); //meters

    leftEncoder.setVelocityConversionFactor(ElevatorConstants.leftMotorConversionFactor / 60); //meters per second
    rightEncoder.setVelocityConversionFactor(ElevatorConstants.rightMotorConversionFactor / 60); //meters per second
    
    leftNeo.setIdleMode(IdleMode.kCoast);
    rightNeo.setIdleMode(IdleMode.kCoast);

    leftNeo.burnFlash();
    rightNeo.burnFlash();
  }

  @Override
  public void periodic() {
    SmartDashboard.putNumber("RightEnc", rightEncoder.getPosition());
    SmartDashboard.putNumber("LeftEnc", leftEncoder.getPosition());
    SmartDashboard.updateValues();
    if (enableSpFollowing){s
      setpointMeters = MathUtil.clamp(setpointMeters, 0, ElevatorConstants.ELEVATOR_MAX_HEIGHT_METERS);
      double pidOut = positionalPID.calculate(getPositionMeters(),setpointMeters);
      SmartDashboard.putNumber("ElevatorPIDOut", pidOut);
      setVelocity(pidOut);
    }
  }

  public void setVelocity(double percentOut){
    rightNeo.set(-percentOut);
    leftNeo.set(percentOut);
  }

  public void changePositionMeters(double increment){
    setpointMeters += increment;
  }

  public void setPositionMeters(double position){
    setpointMeters = position;
  }

  public double getPositionMeters(){
    return leftEncoder.getPosition();
  }

  public void toggleSpFollowing(){
    enableSpFollowing = !enableSpFollowing;
  }
}
