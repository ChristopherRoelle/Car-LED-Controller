package com.led.animations.helpers;

import java.util.ArrayList;
import java.util.List;

public class Animation {
    
    private String name = "Base Animation";
    private List<AnimationFrame> animationFrames = new ArrayList<AnimationFrame>();

    public Animation(String newName){
        name = newName;
    }

    public String GetAnimationName(){
        return name;
    }

    public AnimationFrame GetFrameByIndex(int index){
        return animationFrames.get(index);
    }

    public List<AnimationFrame> GetFrames(){
        return animationFrames;
    }

    public void SetFrames(List<AnimationFrame> newFrames){
        animationFrames = newFrames;
    }

}
