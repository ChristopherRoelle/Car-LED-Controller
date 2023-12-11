package com.led;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import java.io.IOException;
import java.text.DecimalFormat;

import com.led.controller.AnimationController;
import com.led.customjfx.TouchButton;
import com.led.customjfx.TouchButton.Actions;


public class MainApp extends Application {

    private static Stage stage;

    private Rectangle[] controllerGUIBoxes = new Rectangle[]{
        new Rectangle(200, 100),
        new Rectangle(200, 100),
        new Rectangle(200, 100),
        new Rectangle(200, 100)
    };
    private AnimationController ledController = new AnimationController(controllerGUIBoxes);

    private BorderPane primaryLayout = new BorderPane();

    private GridPane headerPane = new GridPane();
    private static Label loadedAnimText = new Label("Animation Name");
    private static Label playbackText = new Label("Speed: 1.0");

    private GridPane bodyPane = new GridPane();

    private boolean displayTouchControls = true;
    private HBox controlsPane = new HBox();
    private TouchButton previousBtn = new TouchButton(100, 100, Actions.PREVIOUS_ANIM, ledController);
    private TouchButton playBtn = new TouchButton(100, 100, Actions.TOGGLE_PLAY, ledController);
    private TouchButton nextBtn = new TouchButton(100, 100, Actions.NEXT_ANIM, ledController);

    private VBox speedControls = new VBox();
    private TouchButton speedUpBtn = new TouchButton(100, 47.5, Actions.SPEED_UP, ledController);
    private TouchButton speedDownBtn = new TouchButton(100, 47.5, Actions.SPEED_DOWN, ledController);

    @Override
    public void start(@SuppressWarnings("exports") Stage s) throws IOException {

        //Set the stage
        stage = s;
        stage.setTitle("LED Controller - GUI Debugger");
        //stage.initStyle(StageStyle.UNDECORATED);
        stage.setWidth(490);
        stage.setHeight(500);

        Scene scene = new Scene(primaryLayout, 500, 300);
        stage.setScene(scene);

        //Header
        //headerPane.widthProperty().bind(primaryLayout.widthProperty());
        //Decorate the header
        headerPane.setBackground(new Background(new BackgroundFill(Color.web("#181818"), null, null)));
        headerPane.setBorder(new Border(new BorderStroke(null, null, Color.web("#383C3F"), null,
            BorderStrokeStyle.NONE, BorderStrokeStyle.NONE, BorderStrokeStyle.SOLID, BorderStrokeStyle.NONE,
            CornerRadii.EMPTY, new BorderWidths(1), Insets.EMPTY)));

        //Decorate the loaded text
        loadedAnimText.setFont(new Font(22));
        loadedAnimText.setTextFill(Color.WHITE);
        loadedAnimText.setPadding(new Insets(5, 0, 0, 10));

        //Decorate the Speed text
        playbackText.setFont(new Font(12));
        playbackText.setTextFill(Color.color(1, 1, 1));
        playbackText.setPadding(new Insets(0, 0, 6, 10));

        //Add the text to the header and add the header to the layout
        headerPane.add(loadedAnimText, 1, 0);
        headerPane.add(playbackText, 1, 1);

        //Body
        bodyPane.setBackground(new Background(new BackgroundFill(Color.web("#1F1F1F"), null, null)));

        bodyPane.setHgap(25);
        bodyPane.setVgap(25);
        bodyPane.setPadding(new Insets(25, 25, 25, 25));
        
        //1
        controllerGUIBoxes[0].setFill(Color.valueOf("Gray"));
        bodyPane.add(controllerGUIBoxes[0], 0, 0);

        //2
        controllerGUIBoxes[1].setFill(Color.valueOf("Gray"));
        bodyPane.add(controllerGUIBoxes[1], 1, 0);

        //3
        controllerGUIBoxes[2].setFill(Color.valueOf("Gray"));
        bodyPane.add(controllerGUIBoxes[2], 0, 1);

        //4
        controllerGUIBoxes[3].setFill(Color.valueOf("Gray"));
        bodyPane.add(controllerGUIBoxes[3], 1, 1);

        //Bottom
        controlsPane.setBackground(new Background(new BackgroundFill(Color.web("#181818"), null, null)));
        controlsPane.setBorder(new Border(new BorderStroke(Color.web("#383C3F"), null, null, null,
            BorderStrokeStyle.SOLID, BorderStrokeStyle.NONE, BorderStrokeStyle.NONE, BorderStrokeStyle.NONE,
            CornerRadii.EMPTY, new BorderWidths(1), Insets.EMPTY)));

        controlsPane.setAlignment(Pos.CENTER);
        controlsPane.setPadding(new Insets(5, 10, 5, 10));
        controlsPane.setSpacing(5);

        speedControls.setAlignment(Pos.CENTER);
        speedControls.setSpacing(5);
        speedControls.getChildren().add(speedUpBtn);
        speedControls.getChildren().add(speedDownBtn);


        //Add Buttons
        controlsPane.getChildren().add(previousBtn);
        controlsPane.getChildren().add(playBtn);
        controlsPane.getChildren().add(nextBtn);
        controlsPane.getChildren().add(speedControls);

        //Setup Keyboard input
        stage.addEventHandler(KeyEvent.KEY_PRESSED, event -> {

            //Play/Pause
            if(event.getCode() == KeyCode.SPACE){
                ledController.TogglePauseAnimation();
            }

            //Next Animation
            if(event.getCode() == KeyCode.RIGHT){
                ledController.NextAnimation(1);
            } else if(event.getCode() == KeyCode.LEFT){
                ledController.NextAnimation(-1);
            }

            //Change Playback Speed
            if(event.getCode() == KeyCode.UP){
                ledController.AdjustPlaybackSpeed(1);
            } else if (event.getCode() == KeyCode.DOWN){
                ledController.AdjustPlaybackSpeed(-1);
            } else if (event.getCode() == KeyCode.HOME){
                ledController.AdjustPlaybackSpeed(0);
            }

        });

        primaryLayout.setTop(headerPane);
        primaryLayout.setCenter(bodyPane);
        if(displayTouchControls){ primaryLayout.setBottom(controlsPane);}

        stage.show();

        //Read saved animations in.
        ledController.ImportSavedAnimations();

        //Start at 0 for now.
        ledController.LoadAnimation(0);

        //ledController.PlayAnimationFromStart();
    }

    public static void SetLoadedAnimationText(String name){
        //loadedAnimText.setText("ANIMATION: " + name);
        loadedAnimText.setText(name.toUpperCase());
    }

    public static void SetPlaybackSpeedText(double speed, String status){

        DecimalFormat df = new DecimalFormat("0.0");

        if(status == "RUNNING"){status = "Playing";}
        else if(status == "STOPPED"){status = "Paused";}

        String speedText = String.valueOf(df.format(speed));
        playbackText.setText(("Speed: " + speedText + " [" + status + "]").toUpperCase());
    }

    public static void main(String[] args) {
        launch(args);
    }

}
