package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp

public class FullTeleOp extends LinearOpMode{

    DcMotor fl, fr, bl, br; 
    DcMotor arm; 
    Servo claw; 

double strafemulti;
double forwardmulti;
double turnmulti;
 
 //Holding power to counteract gravity 
   final double HOLD_POWER = 0.5; 
   final double LOW_POWER_MULTIPLIER = 0.5; // Reduces speed by 50% when in low 
   int targetPosition = 0; 
   
   @Override
   public void runOpMode() { 
        // Initialize motors and servos 
         fl= hardwareMap.get(DcMotor.class, "fl"); 
         fr= hardwareMap.get(DcMotor.class, "fr"); 
         bl = hardwareMap.get(DcMotor.class, "bl"); 
         br = hardwareMap.get(DcMotor.class, "br"); 
         arm = hardwareMap.get(DcMotor.class, "arm"); 
         claw = hardwareMap.get(Servo.class, "claw"); 
        
 
        // Set motor directions 
        //frontLeft.setDirection(DcMotor.Direction.REVERSE); 
        //bl.setDirection(DcMotor.Direction.REVERSE); 
        //br.setDirection(DcMotor.Direction.FORWARD); 
        //frontRight.setDirection(DcMotor.Direction.FORWARD); 
        // arm.setDirection(DcMotorEx.Direction.REVERSE); 
 
        // Set motors to brake when at zero power 
         fl.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE); 
         fr.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE); 
         bl.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE); 
         br.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE); 
         arm.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE); 

        
 
         waitForStart(); 

        arm.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER); 
        arm.setMode(DcMotor.RunMode.RUN_USING_ENCODER); 
 
         while (opModeIsActive()) { 
              
             // Drivetrain Code 

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
 
             // Arm control with presets on gamepad2 
 
             // Manual control with gamepad1 D-pad 
             if (gamepad1.y) { 
                 arm.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER); 
                 arm.setPower(0.3); // Move up 
                 targetPosition = arm.getCurrentPosition(); // Update target position 
                 telemetry.addLine("Manual Move Up"); 
                 
             } 
          else if (gamepad1.a) { 
                 arm.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER); 
                 arm.setPower(-0.3); // Move down 
                 targetPosition = arm.getCurrentPosition(); // Update target position 
                 telemetry.addLine("Manual Move Down"); 
                 
         }
          else if (gamepad2.x) { 
                 arm.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER); 
                 arm.setMode(DcMotor.RunMode.RUN_USING_ENCODER); 
                 arm.setTargetPosition(-6729); 
                 moveArmToPosition(-6729); 
                 arm.setPower(0.4); // Move down 
                arm.setMode(DcMotor.RunMode.RUN_TO_POSITION); 
                arm.setPower(HOLD_POWER); 
                  targetPosition = arm.getCurrentPosition(); // Update target position 
                 telemetry.addLine("Preset Intake Out"); 
                 
         } 
             else { 
                 // Apply holding power to maintain the last set position 
                 arm.setTargetPosition(targetPosition); 
                 arm.setMode(DcMotor.RunMode.RUN_TO_POSITION); 
                 arm.setPower(HOLD_POWER); 
             } 
 
             // Claw control 
             if (gamepad1.x) {
                 claw.setPosition(0); 
             }
             if (gamepad1.b){
                  claw.setPosition(1); 
             }
             
 
                
 
 
            // Update telemetry 
             telemetry.addData("Arm Position", arm.getCurrentPosition()); 
             telemetry.addData("Servo Position", claw.getPosition()); 
            telemetry.addData("Target Position", targetPosition); 
             telemetry.addData("Low Power Mode", gamepad1.left_bumper ? "Enabled" : 
"Disabled"); 
             telemetry.addData("Status", "Running"); 
             telemetry.update(); 
         } 
     } 
 
      // Method to move arm to a specific position using encoder 
     private void moveArmToPosition(int position) { 
         arm.setTargetPosition(position); 
         arm.setMode(DcMotor.RunMode.RUN_TO_POSITION); 
         arm.setPower(0.3);  // Set arm power to move to the target position 
         while (arm.isBusy() && opModeIsActive()) { 
             telemetry.addData("Moving to position", position); 
             telemetry.addData("Current position", arm.getCurrentPosition()); 
             telemetry.update(); 
         } 
         arm.setPower(0); 
         arm.setMode(DcMotor.RunMode.RUN_USING_ENCODER); // Reset to manual 

    } 
} 
 



 

