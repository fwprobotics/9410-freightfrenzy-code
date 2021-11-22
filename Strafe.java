/* Copyright (c) 2017 FIRST. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted (subject to the limitations in the disclaimer below) provided that
 * the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice, this list
 * of conditions and the following disclaimer.
 *
 * Redistributions in binary form must reproduce the above copyright notice, this
 * list of conditions and the following disclaimer in the documentation and/or
 * other materials provided with the distribution.
 *
 * Neither the name of FIRST nor the names of its contributors may be used to endorse or
 * promote products derived from this software without specific prior written permission.
 *
 * NO EXPRESS OR IMPLIED LICENSES TO ANY PARTY'S PATENT RIGHTS ARE GRANTED BY THIS
 * LICENSE. THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;


/**
 * This file contains an minimal example of a Linear "OpMode". An OpMode is a 'program' that runs in either
 * the autonomous or the teleop period of an FTC match. The names of OpModes appear on the menu
 * of the FTC Driver Station. When an selection is made from the menu, the corresponding OpMode
 * class is instantiated on the Robot Controller and executed.
 *
 * This particular OpMode just executes a basic Tank Drive Teleop for a two wheeled robot
 * It includes all the skeletal structure that all linear OpModes contain.
 *
 * Use Android Studios to Copy this Class, and Paste it into your team's code folder with a new name.
 * Remove or comment out the @Disabled line to add this opmode to the Driver Station OpMode list
 */

@TeleOp(name="Strafe", group="Linear Opmode")
public class Strafe extends LinearOpMode {

    // Declare OpMode members.
    private ElapsedTime runtime = new ElapsedTime();
    private DcMotor one = null;
    private DcMotor two = null;
    private DcMotor three = null;
    private DcMotor four = null;
    private DcMotor arm = null;
    private Servo claw1 = null;
    private Servo claw2 = null;
    private DcMotor c = null;

    @Override
    public void runOpMode() {
        telemetry.addData("Status", "Initialized");
        telemetry.update();

        // Initialize the hardware variables. Note that the strings used here as parameters
        // to 'get' must correspond to the names assigned during the robot configuration
        // step (using the FTC Robot Controller app on the phone).
        one  = hardwareMap.get(DcMotor.class, "1");
        two = hardwareMap.get(DcMotor.class, "2");
        three  = hardwareMap.get(DcMotor.class, "3");
        four = hardwareMap.get(DcMotor.class, "4");
        arm = hardwareMap.get(DcMotor.class, "arm");
        claw1 = hardwareMap.get(Servo.class, "c1");
        claw2 = hardwareMap.get(Servo.class, "c2");
        c = hardwareMap.get(DcMotor.class, "c");

        // Most robots need the motor on one side to be reversed to drive forward
        // Reverse the motor that runs backwards when connected directly to the battery
        one.setDirection(DcMotorSimple.Direction.FORWARD);
        two.setDirection(DcMotorSimple.Direction.FORWARD);
        three.setDirection(DcMotorSimple.Direction.REVERSE);
        four.setDirection(DcMotorSimple.Direction.REVERSE);


        // Wait for the game to start (driver presses PLAY)
        waitForStart();
        runtime.reset();

        // run until the end of the match (driver presses STOP)
        while (opModeIsActive()) {
            double drive;
            double strafe;
            double rotate;

            // Setup a variable for each drive wheel to save power level for telemetry
            double frontLeftPower;
            double frontRightPower;
            double backLeftPower;
            double backRightPower;

            drive = -gamepad1.left_stick_y;
            strafe  =  gamepad1.left_stick_x;
            rotate = gamepad1.right_stick_x;

            frontLeftPower = drive - strafe + rotate;
            backLeftPower = drive + strafe - rotate;
            frontRightPower = drive + strafe + rotate;
            backRightPower = drive - strafe - rotate;


            // Send calculated power to wheels
            three.setPower(backLeftPower*.50);
            one.setPower(frontLeftPower*.50);
            four.setPower(backRightPower*.50);
            two.setPower(frontRightPower*.50);

            //spin carousel on blue side
            if (gamepad2.b) {
                c.setDirection(DcMotor.Direction.FORWARD);
                c.setPower(0.75);
            } else {
                c.setPower(0);
            }
            //spin carousel one red side
            if (gamepad2.x) {
                c.setDirection(DcMotor.Direction.REVERSE);
                c.setPower(0.75);
            }
            // if (gamepad1.y) {
            //     arm.setDirection(DcMotor.Direction.FORWARD);
            //     arm.setPower(0.5);
            // } else {
            //     arm.setPower(0);
            // }
            // if (gamepad1.a) {
            //     arm.setDirection(DcMotor.Direction.REVERSE);
            //     arm.setPower(0.5);
            // }
            //arm to pickup position
            if (gamepad1.dpad_down) {
              arm.setTargetPosition(-50);
              arm.setMode(DcMotor.RunMode.RUN_TO_POSITION);
              arm.setPower(0.5);
            }
            //arm to level one
            if (gamepad1.dpad_left) {
              arm.setTargetPosition(-500);
              arm.setMode(DcMotor.RunMode.RUN_TO_POSITION);
              arm.setPower(0.5);
            }
            //arm to level two
            if (gamepad1.dpad_up) {
              arm.setTargetPosition(-1000);
              arm.setMode(DcMotor.RunMode.RUN_TO_POSITION);
              arm.setPower(0.5);
            }
            //arm to level three
            if (gamepad1.dpad_right) {
              arm.setTargetPosition(-1400);
              arm.setMode(DcMotor.RunMode.RUN_TO_POSITION);
              arm.setPower(0.5);
            }

            //open close
            if (gamepad2.a) {
                claw1.setPosition(0.5);
                claw2.setPosition(0.5);
            }
            //close claw
            if (gamepad2.y) {
                claw1.setPosition(0);
                claw2.setPosition(1);


            }

            // Show the elapsed game time and wheel power.
            telemetry.addData("Status", "Run Time: " + runtime.toString());
            telemetry.addData("Motors", "left (%.2f), right (%.2f)", frontLeftPower, frontRightPower);
            telemetry.update();
        }
    }
}
