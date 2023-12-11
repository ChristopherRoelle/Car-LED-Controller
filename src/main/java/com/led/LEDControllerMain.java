package com.led;

import javafx.application.Platform;

public class LEDControllerMain {
    
    public static void main(String[] args){
        Platform.startup(() ->{});
        MainApp.main(args);
    }

}
