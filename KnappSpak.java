/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import edu.wpi.first.wpilibj.DigitalInput;

public class KnappSpak extends DigitalInput
{
  boolean isDown = false;
  boolean wasDown = false;

  public KnappSpak(int port) 
  {
    super(port);

    isDown = super.get();
    wasDown = isDown;
  }

  public boolean getPressed()
  {    
    return isDown & !wasDown;
  }

  public boolean getReleased()
  {    
    return !isDown & wasDown;
  }

  public void update()
  {
    wasDown = isDown;
    isDown = super.get();
  }
}
