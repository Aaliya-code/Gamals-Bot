package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

@TeleOp

public class NewTeleOp_Aaliya extends LinearOpMode {


    DcMotor fl, fr, bl, br; 
    DcMotor arm; 
    Servo claw;
    private int armTarget = 0;
    double joystick;
    boolean isPressed;
    boolean isDown = true;
    boolean lastCycle = false, currCycle = false;
    int maxArmPosition = 2653;
    int minArmPosition = 0;
    
    int armPosition = 0;
    int armMoveLength = 100;
    
    boolean isopModeValueCalculated = false;
    //double intakePosition = 0.5; // Default intake position
   // boolean isManualMode = false;
   
   double strafemulti;
   double forwardmulti;
   double turnmulti;
   

    @Override
    public void runOpMode()  {
        // Initialize hardware
         fl= hardwareMap.get(DcMotor.class, "fl"); 
         fr= hardwareMap.get(DcMotor.class, "fr"); 
         bl = hardwareMap.get(DcMotor.class, "bl"); 
         br = hardwareMap.get(DcMotor.class, "br"); 
         arm = hardwareMap.get(DcMotor.class, "arm"); 
         claw = hardwareMap.get(Servo.class, "claw"); 
        
        
        

        // Set motor directions
        fl.setDirection(DcMotor.Direction.REVERSE); 
        bl.setDirection(DcMotor.Direction.REVERSE); 
        br.setDirection(DcMotor.Direction.FORWARD); 
        fr.setDirection(DcMotor.Direction.FORWARD); 
        arm.setDirection(DcMotorEx.Direction.REVERSE);
        
        // Set zero power behavior
         fl.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE); 
         fr.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE); 
         bl.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE); 
         br.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE); 
         arm.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        //armMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        //armMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
       // armMotor.setTargetPosition(0);
        //armMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        //armMotor.setPower(0.5);
        
        
        arm.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        arm.setTargetPosition(0);
        arm.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        arm.setPower(0.5);
        //Line 58 commented and line 64 added by NA on 1/15/2025
        arm.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        
        


        waitForStart();

        while (opModeIsActive()) {
            // Gamepad control inputs
            
            if (gamepad1.dpad_up) {
                if (armPosition > minArmPosition) {
                    if (isopModeValueCalculated == false) {
                        armPosition = -(arm.getCurrentPosition());
                        armPosition = armPosition - armMoveLength;
                        telemetry.addData ("Arm Free Position", armPosition);
                        isopModeValueCalculated = true;
                    }
                }
            
            move_arm(armPosition);
            }
            else if (gamepad1.dpad_down) {
                if (armPosition < maxArmPosition) {
                    if(isopModeValueCalculated == false) {
                    armPosition = (arm.getCurrentPosition());
                    armPosition = armPosition - armMoveLength;
                    telemetry.addData("Arm Free Position", armPosition);
                    isopModeValueCalculated = true;
                }
            }
            move_arm(armPosition);
            }
            else {
                isopModeValueCalculated = false;
            }
            // Arm control
            if (gamepad1.a) { //Home Positon
                
                 move_arm(0); 
                 claw.setPosition(0.5);
            }
            
           /* else if (gamepad2.dpad_up) { // Specimen In position
                
                 intake.setPosition(0);
                move_arm(-900); 
                claw.setPosition(0.8);
            }
            
            else if (gamepad2.dpad_left) { // Specimen In position
                
                 intake.setPosition(0);
                move_arm(-600); 
                claw.setPosition(0);
            }*/
            //2653
            else if (gamepad1.right_trigger > 0.05) { // spcimen grab position
                
               //intake.setPosition(1);
                claw.setPosition(1); 
                move_arm(2350); //125
                //intake.setPosition(1); // grab position
                
                
            } else if (gamepad1.x) { // Lower bucket scoring
                
                move_arm(1020);
                claw.setPosition(0.5);
                //intake.setPosition(1);
            }
             /*else if (gamepad2.dpad_right) { //intake vertical
                move_arm(-250);
                intake.setPosition(1);
               
            }*/
            else if (gamepad1.y) { // sample grab
                
                move_arm(2660); // drive position // 600
                claw.setPosition(1);
                //intake.setPosition(0.5); 
                //claw.setPosition(0);
            }
            else if(gamepad1.b) { // specimen hang
                move_arm(890);
            }

            // Base movement logic
           /* if (gamepad2.left_bumper) {
                isManualMode = true;
                telemetry.addData("Manual Mode", isManualMode);
            } */
            //else {
            //    isManualMode = false; // Full power driving
            //}
            
            // base code
            
             strafemulti = 0.7;
             forwardmulti = 0.7;
             turnmulti = 0.7;
             
             
            double leftx = -strafemulti*(gamepad1.left_stick_x); //strafe left and right
            double lefty = -forwardmulti*(gamepad1.left_stick_y + gamepad1.right_stick_y); //forward and backward
            double rightx = -turnmulti*(gamepad1.right_stick_x); //turn left and right


            
            fl.setPower(lefty - leftx - rightx);
            fr.setPower(lefty + leftx + rightx);
            bl.setPower(lefty + leftx - rightx);
            br.setPower(lefty - leftx + rightx);

            
            /*if(gamepad2.right_trigger>0.3){
                //isManualMode = false;
                
                float fwdBackArm = gamepad2.right_trigger;
                telemetry.addData("Arm Free Position", fwdBackArm);
                //float UpDownWrist = gamepad2.left_trigger;
            
                armMotor.setPower(fwdBackArm);
                //intake.setPosition(UpDownWrist);
            }*/
            
            // frontLeft.setPower((basepower * -lefty1) + (basepower * leftx1) + basepower * rightx1);
            // backRight.setPower((basepower * lefty1) - (basepower * leftx1) + basepower * rightx1);
            // frontRight.setPower((basepower * lefty1) + (basepower * leftx1) + basepower * rightx1);
            // backLeft.setPower((basepower * lefty1) - (basepower * leftx1) + basepower * -rightx1);


            // Arm Motor control
            

            // Intake control 
           
            
            

            // Claw control 
            lastCycle = currCycle;
            currCycle = gamepad1.right_bumper;
            telemetry.addData("Claw Last", lastCycle);
            telemetry.addData("Claw", currCycle);
            if (currCycle && !lastCycle) {
                isDown = !isDown;
                if (isDown) {
                    claw.setPosition(0.5);  
                } else {
                    claw.setPosition(1);
                }
            
            
            /*if (gamepad2.dpad_up) {
                move_wrist("grab");
                move_arm(-2600);
            }  if (gamepad2.dpad_down) {
                move_wrist("back");
            } else if (gamepad2.dpad_left) {
                move_wrist("home");
            } else if (gamepad2.dpad_right) {
                move_wrist("bucket");
            }*/

            // Send telemetry data
            
        }
        
          /*if (gamepad1.dpad_up) {
                intakePosition += 0.01; // Move up
            } 
            else if (gamepad1.dpad_down) {
                intakePosition -= 0.01; // Move down
            }

            // Keep intake within valid servo range
            intakePosition = Math.max(0.0, Math.min(1.0, intakePosition));
            intake.setPosition(intakePosition);*/
            
            
        telemetry.addData("Arm Position", arm.getCurrentPosition());
            //telemetry.addData("Manual Mode", isManualMode);
            telemetry.addData("Arm Target Position", armTarget);
            //telemetry.addData("Base Power", basepower);
            telemetry.addData("Low Power Mode", gamepad1.right_bumper);
            telemetry.update();
    }
    }
    

    /*public void move_wrist(String pos) {
        if (pos.equals("home")) {
            intake.setPosition(1);
            telemetry.addData("p", "home");
        } else if (pos.equals("back")) {
            intake.setPosition(0);
            telemetry.addData("p", "back");
        } else if (pos.equals("grab")) {
            intake.setPosition(0.5);
            telemetry.addData("p", "grab");
        } else if (pos.equals("bucket")) {
            intake.setPosition(0.8);
            telemetry.addData("p", "bucket");
        }
    }*/
  public void move_arm(int armTarget) {
        arm.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        arm.setTargetPosition(armTarget);
        arm.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        arm.setPower(0.5);
  }  
  


}
    
    
    
    
    
    
