package com.led.animations.helpers;

public class AnimationFrame {
    
    private int frontLeftStatus = 0;
    private int frontRightStatus = 0;
    private int rearLeftStatus = 0;
    private int rearRightStatus = 0;
    private long lengthMS = 0;

    public AnimationFrame(int frontLeftStatus, int frontRightStatus, int rearLeftStatus, int rearRightStatus, long lengthMS){
        this.frontRightStatus = frontRightStatus;
        this.frontLeftStatus = frontLeftStatus;
        this.rearLeftStatus = rearLeftStatus;
        this.rearRightStatus = rearRightStatus;
        this.lengthMS = lengthMS;
    }

    /**
     * Returns fl, fr, rl, rr
     * @return [frontLeftStatus, frontRightStatus, rearLeftStatus, rearRightStatus]
     */
    public int[] GetStatuses(){
        return new int[]{frontLeftStatus, frontRightStatus, rearLeftStatus, rearRightStatus};
    }

    public long GetLengthMS(){
        return lengthMS;
    }

}
