package com.led.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import com.led.MainApp;
import com.led.animations.helpers.Animation;
import com.led.animations.helpers.AnimationFrame;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.animation.Animation.Status;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

public class AnimationController{

    //private String savedAnimationPath = System.getProperty("user.dir") + "\\animations";
    private String savedAnimationPath = "src\\main\\java\\com\\led\\animations\\import";

    private int loopCount = -1;
    private boolean keepSpeedAfterSwap = true;
    
    private int currentAnimationIndex = 0;

    private List<Rectangle> guiBox = new ArrayList<Rectangle>();
    
    private List<Animation> animations = new ArrayList<Animation>();
    List<AnimationFrame> frames = new ArrayList<AnimationFrame>();
    Timeline timer = null;

    public AnimationController(Rectangle[] guiBox){

        for (Rectangle box : guiBox) {
            this.guiBox.add(box);
        }
    }

    public String GetLoadedAnimationName(){
        return animations.get(currentAnimationIndex).GetAnimationName();
    }

    /**
     * Loads in an animation from the current index.
     * @param index - Index of the animation.
     */
    public void LoadAnimation(int index){

        String name = animations.get(index).GetAnimationName();

        currentAnimationIndex = index;
        frames = animations.get(index).GetFrames();

        long curFrame = 0;
        timer = new Timeline();

        System.out.println();
        System.out.println("Loading Animation: " + name);

        for (AnimationFrame frame : frames) {

            int[] statuses = frame.GetStatuses();
            long lengthMS = frame.GetLengthMS();

            timer.getKeyFrames().add(new KeyFrame(Duration.millis(curFrame), event -> 
            HandleLEDStatus(statuses, animations.get(index).GetAnimationName())));

            curFrame += lengthMS;
        };

        //Create a null frame to allow final frame to keep duration (rather than starting next loop instantly)
        timer.getKeyFrames().add(new KeyFrame(Duration.millis(curFrame)));

        timer.setCycleCount(loopCount);

        MainApp.SetLoadedAnimationText(name);
        MainApp.SetPlaybackSpeedText(timer.getRate(), timer.getStatus().toString());

        System.out.println("Animation Loaded!");
        System.out.println();
    }

    /**
     * Starts an animation from the beggining.
     */
    public void PlayAnimationFromStart(){
        timer.playFromStart();
        MainApp.SetPlaybackSpeedText(timer.getRate(), timer.getStatus().toString());
    }

    /**
     * Handles traversing the animations.
     * @param direction - +1 to go to next, -1 to go to previous.
     */
    public void NextAnimation(int direction){

        boolean wasPlaying = false;
        double currentRate = timer.getRate();

        //Check if playing, if so, we will play after load
        if(timer.getStatus().equals(Status.RUNNING)){
            wasPlaying = true;
            timer.stop();
        }

        currentAnimationIndex += direction;
        
        //If over the bounds of stored animations, go to 0, if under 0, go to upper bounds of stored anims.
        if(currentAnimationIndex > (animations.size() - 1)){
            currentAnimationIndex = 0;
        } else if (currentAnimationIndex < 0){
            currentAnimationIndex = animations.size() - 1;
        }

        LoadAnimation(currentAnimationIndex);

        //Set rate back to what it was before swap.
        if(keepSpeedAfterSwap){timer.setRate(currentRate);}

        if(wasPlaying){ PlayAnimationFromStart(); }
    }

    /**
     * Adjusts the playback speed, < 0 lowers speed, 0 defaults back to 1, and > 0 speeds up.
     * @param value - direction of increase, 0 resets back to default.
     */
    public void AdjustPlaybackSpeed(int dir){

        double rate = timer.getRate();
        double change = 0.1;

        //Set dir to either -1, 0, or 1
        if(dir < 0){
            dir = -1;
        } else if (dir > 0){
            dir = 1;
        } else {
            dir = 0;
        }

        if(rate <= 0.105 && dir < 0){
            timer.setRate(0.1);
        } else if(rate >= 10.0 && dir > 0){
            timer.setRate(10.0);
        } else if(dir == 0){
            timer.setRate(1);
        } else {
            timer.setRate(rate + (change * dir));
        }

        MainApp.SetPlaybackSpeedText(timer.getRate(), timer.getStatus().toString());
        System.out.println("Playback Speed: " + timer.getRate());
        
    }

    /**
     * Handles Playing and Pausing the Animation.
     */
    public void TogglePauseAnimation(){
        if(timer.getStatus().equals(Status.RUNNING)){
            timer.pause();
            System.out.println("Animation Paused.");
        } else{
            timer.play();
            System.out.println("Animation Playing.");
        }

        MainApp.SetPlaybackSpeedText(timer.getRate(), timer.getStatus().toString());
    }

    /**
     * Handles the LED and GUI frames.
     * @param statuses - Array of the LED statuses on the current frame.
     * @param animName - Name of the animation for Debug
     */
    private void HandleLEDStatus(int[] statuses, String animName){

        for (int i = 0; i < statuses.length; i++) {

            // String ledName = "NULL";

            // switch (i) {
            //     case 0:
            //         ledName = "Front-Left";
            //         break;
            //     case 1:
            //         ledName = "Front-Right";
            //         break;
            //     case 2:
            //         ledName = "Rear-Left";
            //         break;
            //     case 3:
            //         ledName = "Rear-Right";
            //         break;
            
            //     default:
            //         break;
            // }
            
            //Handles the GUI/Debug/Actual LED changes
            switch (statuses[i]) {
            case 0:
                //System.out.println("[" + ledName +"] - {" + animName + "} - LED OFF");
                guiBox.get(i).setFill(Color.GRAY);
                break;
            case 1:
                //System.out.println("[" + ledName +"] - {" + animName + "} - LED ON");
                guiBox.get(i).setFill(Color.LIGHTGRAY);
                break;
            default:
                break;
            }
        }
    }

    /**
     * Imports all anim files from the import location.
     * @throws IOException
     */
    public void ImportSavedAnimations() throws IOException{

        File dir = new File(savedAnimationPath);
        
        if(dir.exists()){
            System.out.println("Path exists.");
            System.out.println();
        } else {
            System.out.println("Path doesnt exist.");
            System.out.println();
            return;
        }

        File[] directoryListing = dir.listFiles();

        //If there are files, read them, only read files that have .anim extension
        if(directoryListing != null){
            for(File file : directoryListing)
            {

                String animationName = null;
                List<AnimationFrame> tempFrames = new ArrayList<AnimationFrame>();

                //System.out.println(file.getName());

                if(file.getName().toLowerCase().contains(".anim") && file.getName().contains("Template.anim") == false){
                    System.out.println("Reading: " + file.getName());

                    List<String> contents = Files.readAllLines(Paths.get(file.getPath()));

                    for(int lineNumber = 0; lineNumber < contents.size(); lineNumber++){

                        //Parse the Animation Name from the file
                        if(lineNumber == 0 && contents.get(lineNumber).startsWith("Name:")){
                            String[] lineValues = contents.get(lineNumber).split(":");
                            animationName = lineValues[1].strip();
                            System.out.println("Animation Name: " + animationName);
                        } else if(!(lineNumber == 0 || lineNumber == 1 || lineNumber == 2 || 
                            contents.get(lineNumber).strip() == "" || contents.get(lineNumber).strip().startsWith("#"))){
                            
                            //Parse and Strip the values
                            String[] lineValues = contents.get(lineNumber).split(",");
                            int[] values = new int[5];
                            for(int v = 0; v < lineValues.length; v++){
                                values[v] = Integer.parseInt(lineValues[v].strip());
                            }

                            tempFrames.add(new AnimationFrame(values[0], values[1], values[2], values[3], values[4]));

                            //System.out.println("Frame Added!");
                        }
                    }

                    //Make sure that the animationName is not null and that there are frames
                    //If so, create the animation object
                    if(animationName != null && tempFrames.size() > 0){
                        Animation anim = new Animation(animationName);
                        anim.SetFrames(tempFrames);

                        animations.add(anim);

                        System.out.println("'" + animationName + "' imported with " + tempFrames.size() + " keyframes!");
                        System.out.println();
                    }
                    else{
                        System.out.println("The animation file is corrupt or does not fit the proper layout.");
                        System.out.println("The animation has been skipped.");
                        System.out.println();
                    }

                }
            }
        }

    }
}
