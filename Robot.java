/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import java.util.concurrent.TimeUnit;

import javax.lang.model.util.ElementScanner6;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;
import edu.wpi.first.wpilibj.GenericHID.RumbleType;
import edu.wpi.first.wpilibj.buttons.Trigger;
import edu.wpi.cscore.UsbCamera;
import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.PowerDistributionPanel;
import edu.wpi.first.wpilibj.Servo;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.Ultrasonic;


public class Robot extends TimedRobot 
{
  Talon leftDrive = new Talon(0);
  Talon rightDrive = new Talon(1);
  VictorSPX rightShooter = new VictorSPX(5);
  VictorSPX leftShooter = new VictorSPX(6);
  VictorSPX feeder = new VictorSPX(7);
  Servo servo = new Servo(2);
  Joystick joystick = new Joystick(0);
  Joystick AmockSpak = new Joystick(1);
  boolean driveSpeed = false;
  boolean buttonClicked;
  KnappSpak knappSpak = new KnappSpak(5);
  short ammo;
  Ultrasonic sensor = new Ultrasonic(1, 0);
  
  PowerDistributionPanel pdp = new PowerDistributionPanel();
  
  //Bestämmer Hastigheten på Hjulen
  double speed = 0.4;
  double turnSpeed = 0.4;
  

  @Override
  public void robotInit() 
  {
    UsbCamera camera1 = CameraServer.getInstance().startAutomaticCapture();
    camera1.setResolution(320, 240);
    rightDrive.setInverted(true);
    rightShooter.setInverted(true);
    servo.setBounds(2, 0, 0, 0, 1);
    sensor.setAutomaticMode(true);

  }


  @Override
  public void teleopInit() 
  {
    ammo = 5;

  }
// nice
  @Override
  public void teleopPeriodic() 
  {
    knappSpak.update();

    lift();
  
    shoot();
    
    drive();
    
    berserk();

    modeChecker();
    
    ammoCounter();

    distanceCheck();

  }



   
//Stoppar alla motorer vid avstånd närmare än 20 cm
  void distanceCheck()
  {
    while(sensor.getRangeMM() < 200)
    {
     speed = 0;
    }

  }

  void shoot(){

    if (joystick.getRawButton(2))
    {
      
      rightShooter.set(ControlMode.PercentOutput, 1);
      leftShooter.set(ControlMode.PercentOutput, 1); 
    // Kontrolerar FeederHastigheten
    feeder.set(ControlMode.PercentOutput, -0.3);
    }
    else{
      rightShooter.set(ControlMode.PercentOutput, 0);
      leftShooter.set(ControlMode.PercentOutput, 0); 
    // Kontrolerar FeederHastigheten
    feeder.set(ControlMode.PercentOutput, -0);
    }
  
  }
    
    

  void lift()
  {
    double servoSpeed = 0.0046;
    double targetPosition = servo.getPosition();

  

    if(joystick.getRawButton(1))
    {
      targetPosition -= servoSpeed;
    }
    else if (joystick.getRawButton(4))
    {
      targetPosition += servoSpeed;
    } 

    if (targetPosition < 0)
      targetPosition = 0;
    else if (targetPosition > 1)
      targetPosition = 1;

    servo.set(targetPosition);

    //System.out.println(servo.getPosition());
  }
  
  
 
 
void drive()
{
 if(joystick.getRawButton(6))
  {
    leftDrive.set(getThrottle() * speed + getSteer() * turnSpeed*0.4);
    rightDrive.set(getThrottle() * speed + getSteer() * -turnSpeed*0.4);
    

  } 
  else{
    leftDrive.set(getThrottle() * speed + getSteer() * turnSpeed);
    rightDrive.set(getThrottle() * speed + getSteer() * -turnSpeed);

  }


}

void berserk()
{
  if(joystick.getRawButton(5))
  {
    leftDrive.setSpeed(0.5);
    rightDrive.setSpeed(0.5);
    rightShooter.set(ControlMode.PercentOutput, 1);
    leftShooter.set(ControlMode.PercentOutput, 1);
   feeder.set(ControlMode.PercentOutput, -0.36);
   servo.set(0.5);
   System.out.println("Berserk");
  }


}

void modeChecker()
{ 
  if(driveSpeed == false){
   if(joystick.getRawButtonPressed(9))
   {
    driveSpeed = true;
   }
  }
if(driveSpeed == true)
{
  speed = 1;
  turnSpeed = 0.9;
  
    leftDrive.set(getThrottle() * speed + getSteer() * turnSpeed*3);
    rightDrive.set(getThrottle() * speed + getSteer() * -turnSpeed*3);
    
    joystick.setRumble(RumbleType.kLeftRumble, 1);
    joystick.setRumble(RumbleType.kRightRumble, 1);
    

  } 
  else{
    joystick.setRumble(RumbleType.kRightRumble, 0);
    joystick.setRumble(RumbleType.kLeftRumble, 0);
    speed = 0.4;
    turnSpeed = 0.4;
  }
if (driveSpeed == true)
{
  if(joystick.getRawButtonPressed(9)){
    driveSpeed = false;
  }
    leftDrive.set(getThrottle() * speed + getSteer() * turnSpeed);
    rightDrive.set(getThrottle() * speed + getSteer() * -turnSpeed);
    joystick.setRumble(RumbleType.kRightRumble, 0);

}

}
  double getThrottle()
  {
    return -joystick.getRawAxis(1);
  }

  double getSteer()
  {
    return joystick.getRawAxis(4);
  }

void ammoCounter()
{
  if(knappSpak.getPressed())
  {
      if(ammo>0)
      ammo--;
      
    System.out.println(ammo);

  }
 }
}  

