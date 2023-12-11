package com.led.customjfx;

import com.led.controller.AnimationController;

import javafx.scene.control.Label;
import javafx.scene.input.InputEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TouchEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;

public class TouchButton extends StackPane {

    public static enum Actions{
        NULL,
        PREVIOUS_ANIM,
        NEXT_ANIM,
        TOGGLE_PLAY,
        SPEED_UP,
        SPEED_DOWN
    }

    private AnimationController ledController;

    private String webColorCodeNormal = "#1F1F1F";
    private String webColorCodeHover = "#2A2D2E";
    private String webColorCodeClick = "#37373D";

    private Rectangle rect = new Rectangle();
    private Label label = new Label();

    private Actions action = Actions.NULL;
    
    public TouchButton(double width, double height, Actions action, AnimationController controller){
        rect.setWidth(width);
        rect.setHeight(height);

        ledController = controller;
        this.action = action;

        rect.setFill(Color.web(webColorCodeNormal));
        rect.setArcWidth(width / 10);
        rect.setArcHeight(height / 10);

        label.setFont(new Font(12));
        label.setTextFill(Color.WHITE);

        SetLabelText();

        getChildren().addAll(rect, label);

        //Add events
        rect.addEventHandler(MouseEvent.MOUSE_ENTERED, e -> HoverHighlight(e));
        rect.addEventHandler(MouseEvent.MOUSE_EXITED, e -> RemoveHighlight(e));
        rect.addEventHandler(MouseEvent.MOUSE_PRESSED, e -> Clicked(e));
        rect.addEventHandler(MouseEvent.MOUSE_RELEASED, e -> HoverHighlight(e));
        rect.addEventHandler(TouchEvent.TOUCH_PRESSED, e -> Clicked(e));
        rect.addEventHandler(TouchEvent.TOUCH_RELEASED, e -> RemoveHighlight(e));
    }

    private void HoverHighlight(InputEvent e){
        e.consume();
        rect.setFill(Color.web(webColorCodeHover));
    }

    private void RemoveHighlight(InputEvent e){
        e.consume();
        rect.setFill(Color.web(webColorCodeNormal));
    }

    private void Clicked(InputEvent e){
        e.consume();
        rect.setFill(Color.web(webColorCodeClick));
        CallTouchControlFunction();
    }

    private void SetLabelText(){
        switch (action) {
            case PREVIOUS_ANIM:
                label.setText("Previous Anim");
                break;
            case TOGGLE_PLAY:
                label.setText("Play | Pause");
                break;
            case NEXT_ANIM:
                label.setText("Next Anim");
                break;
            case SPEED_UP:
                label.setText("Speed Up");
                break;
            case SPEED_DOWN:
                label.setText("Speed Down");
                break;
        
            default:
                break;
        }
    }


    public void CallTouchControlFunction(){

        switch (action) {

            case PREVIOUS_ANIM:
                ledController.NextAnimation(-1);
                break;
            case TOGGLE_PLAY:
                ledController.TogglePauseAnimation();
                break;
            case NEXT_ANIM:
                ledController.NextAnimation(1);
                break;
            case SPEED_UP:
                ledController.AdjustPlaybackSpeed(1);
                break;
            case SPEED_DOWN:
                ledController.AdjustPlaybackSpeed(-1);
                break;
        
            default:
                System.out.println("Event Action is Null.");
                break;
        }

    }

}
