package frc.robot.states.mech_states;

import frc.robot.states.MechState;
import frc.robot.subsystems.AimingSubsystem;
import frc.robot.subsystems.IntakeSubsystem;
import frc.robot.subsystems.LauncherSubsystem;

public class ReadyForAim extends MechState {

    public ReadyForAim (LauncherSubsystem launcherSubsystem,AimingSubsystem aimingSubsystem,IntakeSubsystem intakeSubsystem) {
        super(launcherSubsystem,aimingSubsystem,intakeSubsystem);
    }

    @Override
    public void speakerPosition(){
        mSpeakerPositionCommand.schedule();;
    }

    @Override
    public void ampPosition(){
        mAmpPositionCommand.schedule();
    }
    
    @Override
    public void trapPosition(){
        mTrapPositionCommand.schedule();
    }

    @Override
    public void handoffPosition(){
        mHandoffPositionCommand.schedule();
    }
}